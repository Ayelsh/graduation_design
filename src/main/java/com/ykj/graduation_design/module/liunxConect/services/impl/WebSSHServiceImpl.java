package com.ykj.graduation_design.module.liunxConect.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.ykj.graduation_design.config.ConstantPool;
import com.ykj.graduation_design.module.liunxConect.pojo.SSHConnectInfo;
import com.ykj.graduation_design.module.liunxConect.pojo.WebSSHData;
import com.ykj.graduation_design.module.liunxConect.services.WebSSHService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class WebSSHServiceImpl implements WebSSHService {

    /**
     * 存放ssh连接信息的map
     */
    private static final Map<String, Object> sshMap = new ConcurrentHashMap<>();

    /**
     * 线程池
     */
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * @Description: 初始化连接
     * @Param: [session]
     * @return: void
     */
    @Override
    public void initConnection(WebSocketSession session) {
        JSch jSch = new JSch();
        SSHConnectInfo sshConnectInfo = new SSHConnectInfo();
        sshConnectInfo.setjSch(jSch);
        sshConnectInfo.setWebSocketSession(session);
        String username = String.valueOf(session.getAttributes().get(ConstantPool.USERNAME));
        //将这个ssh连接信息放入map中
        sshMap.put(username, sshConnectInfo);

    }


    /**
     * @Description: 处理客户端发送的数据
     * @Param: [buffer, session]
     * @return: void
     */
    @Override
    public void recvHandle(String buffer, WebSocketSession session) {

        //初始话连接数据
        log.info("输入:{}", buffer);
        ObjectMapper objectMapper = new ObjectMapper();
        WebSSHData webSSHData = null;

        try {
            webSSHData = objectMapper.readValue(buffer, WebSSHData.class);
        } catch (IOException e) {
            log.error("Json转换异常");
            log.error("异常信息:{}", e.getMessage());
            return;
        }

        log.info("webSSHData:{}", webSSHData.toString());

        String username = String.valueOf(session.getAttributes().get(ConstantPool.USERNAME));

        /*
            连接
         */
        if (ConstantPool.WEBSSH_OPERATE_CONNECT.equals(webSSHData.getOperate())) {

            log.info("操作：连接");
            //找到刚才存储的ssh连接对象
            SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(username);
            WebSSHData finalWebSSHData = webSSHData;
            //启动线程异步处理，执行线程--连接
            executorService.execute(() -> {
                try {

                    connectToSSH(sshConnectInfo, finalWebSSHData, session);
                    //保存覆盖新的channel
                    sshMap.put(username, sshConnectInfo);

                } catch (JSchException | IOException e) {
                    log.error("webssh连接异常");
                    log.error("异常信息:{}", e.getMessage());
                    close(session);
                }
            });
        }
        /*
            操作
         */
        else if (ConstantPool.WEBSSH_OPERATE_COMMAND.equals(webSSHData.getOperate())) {


            String command = webSSHData.getCommand();
            log.info("命令:{}", command);

            SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(username);
            Channel channel = sshConnectInfo.getChannel();
            executorService.execute(() -> {
                try {

                    transToSSH(channel, command);

                } catch (IOException e) {
                    log.error("webssh连接异常");
                    log.error("异常信息:{}", e.getMessage());
                    close(session);
                }
            });

        } else {
            log.error("不支持的操作");
            close(session);
        }
    }

    @Override
    public void sendMessage(WebSocketSession session, byte[] buffer) throws IOException {
        session.sendMessage(new BinaryMessage(buffer));
    }

    @Override
    public void close(WebSocketSession session) {
        String userId = String.valueOf(session.getAttributes().get(ConstantPool.USERNAME));
        SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(userId);
        if (sshConnectInfo != null) {
            //断开连接
            if (sshConnectInfo.getChannel() != null) sshConnectInfo.getChannel().disconnect();
            //map中移除
            sshMap.remove(userId);
        }
    }

//    /**
//     * @Description: 使用jsch连接终端
//     * @Param: [cloudSSH, webSSHData, webSocketSession]
//     * @return: void
//     */
//    private void connectToSSH(SSHConnectInfo sshConnectInfo, WebSSHData webSSHData, WebSocketSession webSocketSession) throws JSchException, IOException {
//        //获取jsch的会话
//        Session session = null;
//        Properties config = new Properties();
//        config.put("StrictHostKeyChecking", "no");
//        session = sshConnectInfo.getjSch().getSession(webSSHData.getUsername(), webSSHData.getHost(), webSSHData.getPort());
//        session.setConfig(config);
//        session.setPassword(webSSHData.getPassword());
//
//        //连接  超时时间30s
//        session.connect(30000);
//
//        //开启shell通道
//        Channel channel = session.openChannel("shell");
//
//        //通道连接 超时时间3s
//        channel.connect(3000);
//
//        //设置channel
//        sshConnectInfo.setChannel(channel);
//
//        transToSSH(channel, "\r");
//        session.disconnect();
//
//    }

//    /**
//     * @Description: 将消息转发到终端
//     * @Param: [channel, data]
//     * @return: void
//     */
//    private void transToSSH(Channel channel, String command, WebSocketSession webSocketSession) throws IOException {
//
//        assert channel != null;
//        OutputStream outputStream = channel.getOutputStream();
//        outputStream.write(command.getBytes());
//        outputStream.flush();
//        //读取终端返回的信息流
//        InputStream inputStream = channel.getInputStream();
//        try {
//            //循环读取
//            byte[] buffer = new byte[1024];
//            int i = 0;
//            //如果没有数据来，线程会一直阻塞在这个地方等待数据。
//            while ((i = inputStream.read(buffer)) != -1) {
//                sendMessage(webSocketSession, Arrays.copyOfRange(buffer, 0, i));
//            }
//        } finally {
//            channel.disconnect();
//            if (inputStream != null) {
//                inputStream.close();
//            }
//            outputStream.close();
//            log.info("已关闭");
//        }
//
//
//    }


//    @Override
//    public void recvHandle(String buffer, WebSocketSession session) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        WebSSHData webSSHData = null;
//        try {
//            webSSHData = objectMapper.readValue(buffer, WebSSHData.class);
//        } catch (IOException e) {
//            logger.error("Json转换异常");
//            logger.error("异常信息:{}", e.getMessage());
//            return;
//        }
//        String userId = String.valueOf(session.getAttributes().get(ConstantPool.USER_UUID_KEY));
//        if (ConstantPool.WEBSSH_OPERATE_CONNECT.equals(webSSHData.getOperate())) {
//            //找到刚才存储的ssh连接对象
//            SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(userId);
//            //启动线程异步处理
//            WebSSHData finalWebSSHData = webSSHData;
//            executorService.execute(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        connectToSSH(sshConnectInfo, finalWebSSHData, session);
//                    } catch (JSchException | IOException e) {
//                        logger.error("webssh连接异常");
//                        logger.error("异常信息:{}", e.getMessage());
//                        close(session);
//                    }
//                }
//            });
//        } else if (ConstantPool.WEBSSH_OPERATE_COMMAND.equals(webSSHData.getOperate())) {
//            String command = webSSHData.getCommand();
//            SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(userId);
//            if (sshConnectInfo != null) {
//                try {
//                    transToSSH(sshConnectInfo.getChannel(), command);
//                } catch (IOException e) {
//                    logger.error("webssh连接异常");
//                    logger.error("异常信息:{}", e.getMessage());
//                    close(session);
//                }
//            }
//        } else {
//            logger.error("不支持的操作");
//            close(session);
//        }
//    }


    /**
     * @Description: 使用jsch连接终端
     * @Param: [cloudSSH, webSSHData, webSocketSession]
     * @return: void
     * @Author: NoCortY
     * @Date: 2020/3/7
     */
    private void connectToSSH(SSHConnectInfo sshConnectInfo, WebSSHData webSSHData, WebSocketSession webSocketSession) throws JSchException, IOException {
        Session session = null;
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        //获取jsch的会话
        session = sshConnectInfo.getjSch().getSession(webSSHData.getUsername(), webSSHData.getHost(), webSSHData.getPort());
        session.setConfig(config);
        session.setPassword(webSSHData.getPassword());
        //连接  超时时间30s
        session.connect(30000);
        Channel channel = session.openChannel("shell");
        //通道连接 超时时间3s
        channel.connect(3000);
        //设置channel
        sshConnectInfo.setChannel(channel);
        //转发消息
        transToSSH(channel, "\r");
        //读取终端返回的信息流
        InputStream inputStream = channel.getInputStream();
        try {
            //循环读取
            byte[] buffer = new byte[1024];
            int i = 0;
            //如果没有数据来，线程会一直阻塞在这个地方等待数据。
            while ((i = inputStream.read(buffer)) != -1) {
                sendMessage(webSocketSession, Arrays.copyOfRange(buffer, 0, i));
            }

        } finally {
            //断开连接后关闭会话
            session.disconnect();
            channel.disconnect();
            if (inputStream != null) {
                inputStream.close();
            }
        }

    }

    private void transToSSH(Channel channel, String command) throws IOException {
        assert channel != null;
        OutputStream outputStream = channel.getOutputStream();
        outputStream.write(command.getBytes());
        outputStream.flush();

    }

}
