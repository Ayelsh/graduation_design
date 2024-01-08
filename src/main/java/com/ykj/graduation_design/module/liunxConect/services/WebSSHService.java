package com.ykj.graduation_design.module.liunxConect.services;

import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @Date: 2024/01/05
 * @Description: webSSH的业务逻辑
 */
  public interface WebSSHService {
      /**
       * @Description: 初始化ssh连接
       * @Param:
       * @return:
       */
      public void initConnection(WebSocketSession session);

      /**
       * @Description: 处理客户段发的数据
       * @Param:
       * @return:
       */
      public void recvHandle(String buffer, WebSocketSession session);

      /**
       * @Description: 数据写回前端 for websocket
       * @Param:
       * @return:
       */
      public void sendMessage(WebSocketSession session, byte[] buffer) throws IOException;

      /**
       * @Description: 关闭连接
       * @Param: 
       * @return:
       */
      public void close(WebSocketSession session);
  }