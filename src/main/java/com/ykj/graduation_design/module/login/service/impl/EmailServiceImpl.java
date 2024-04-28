package com.ykj.graduation_design.module.login.service.impl;

import com.ykj.graduation_design.common.RestResult;
import com.ykj.graduation_design.common.utils.RedisUtils;
import com.ykj.graduation_design.common.utils.VerCodeGenerateUtil;
import com.ykj.graduation_design.module.login.service.EmailService;
import io.micrometer.common.util.StringUtils;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @Date: 2024/04/24/14:55
 * @Description:
 */
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.from}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;


    @Override
    public void sendCode(HttpServletResponse response,String email) {
        String verCode = VerCodeGenerateUtil.getVerCode();
        log.info("驗證碼："+verCode);
        //存进redis，5分钟到期自动移除
        RedisUtils.set(email,verCode,5 * 60);

        //一下为发送邮件部分
        MimeMessage mimeMessage = null;
        MimeMessageHelper helper = null;
        try {
            //发送复杂的邮件
            mimeMessage = mailSender.createMimeMessage();
            //组装
            helper= new MimeMessageHelper(mimeMessage, true);
            //邮件标题
            helper.setSubject("【网络安全学习社区】 注册账号验证码");

            helper.setText("<html lang=\"en\">\n" +
                    "<head>\n" +
                    "  <meta charset=\"UTF-8\">\n" +
                    "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "  <title>网络安全学习社区来信</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "  <div style=\"font-family: Arial, sans-serif; line-height: 1.6; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ccc; border-radius: 5px; background-color: #f9f9f9;\">\n" +
                    "    <div style=\"text-align: center;\">\n" +
                    "      <h1 style=\"margin: 0;\">欢迎注册本社区账号</h1>\n" +
                    "    </div>\n" +
                    "    <div style=\"margin-top: 20px;\">\n" +
                    "      <p style=\"margin: 0;\">您的验证码为：</p>\n" +
                    "      <p style=\"margin: 0;\">"+verCode+"</p>\n" +
                    "      <p style=\"margin: 0;\">如有任何问题，烦请联系15768206596@163.cm</p>\n" +
                    "      <p style=\"margin: 0;\">Ayelsh Team</p>\n" +
                    "    </div>\n" +
                    "    <div style=\"margin-top: 20px; text-align: center;\">\n" +
                    "      <p style=\"margin: 0;\">This email was sent to [Recipient Email]. If you received this email by mistake, please ignore it.</p>\n" +
                    "    </div>\n" +
                    "  </div>\n" +
                    "</body>\n" +
                    "</html>",true);
            //收件人
            helper.setTo(email);
            helper.setFrom(from);
            try {
                //发送邮件

                mailSender.send(mimeMessage);
            } catch (Exception e) {
                //邮箱是无效的，或者发送失败
                RestResult.responseJson(response, new RestResult<>(600, "验证码发送失败", e.getMessage()));
            }
        } catch (Exception e) {
            //发送失败--服务器繁忙
            RestResult.responseJson(response, new RestResult<>(600, "验证码发送准备失败", e.getMessage()));
        }
        //发送验证码成功
        RestResult.responseJson(response, new RestResult<>(200, "验证码成功发送", null));
    }

    @Override
    public void VerCode(HttpServletResponse response, String email,String code) {
        try {
            String verCode = (String) RedisUtils.get(email);
            if (StringUtils.isBlank(verCode)) {
                RestResult.responseJson(response, new RestResult<>(604, "验证码不存在", null));
            } else if (!code.equals(verCode)) {
                RestResult.responseJson(response, new RestResult<>(600, "验证码错误", null));
            } else {
                RestResult.responseJson(response, new RestResult<>(200, "验证成功", null));
            }
        }catch (Exception e){
            RestResult.responseJson(response, new RestResult<>(600, "程序执行错误", e.getMessage()));
        }
    }
}
