package com.ykj.graduation_design;

import com.ykj.graduation_design.common.EnvironmentProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
class GraduationDesignApplicationTests {

    @Test
    void contextLoads() throws URISyntaxException, IOException {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();

        URI uri = new URI(URLEncoder.encode("https://github.com/login/oauth/access_token"));
        // 添加信息
        HttpPost httpPost = new HttpPost(uri);

        // 设置2个post参数，一个是scope、一个是q
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("client_id", EnvironmentProperty.getProperty("client_id")));
        params.add(new BasicNameValuePair("client_secret", EnvironmentProperty.getProperty("client_secret")));
        params.add(new BasicNameValuePair("code", "ec68fd91c01b53a1e413"));
        // 构造一个form表单式的实体
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params);
        // 将请求实体设置到httpPost对象中
        httpPost.setEntity(formEntity);
        CloseableHttpResponse response = null;
        try
        {
            // 执行请求
            response = httpclient.execute(httpPost);
            // 判断返回状态是否为200
            if (response.getCode() == 200)
            {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                System.out.println(content);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally
        {
            if (response != null)
            {
                response.close();
            }
            httpclient.close();
        }

    }

}
