package com.ykj.graduation_design.module.Blog.controller;

import com.ykj.graduation_design.common.RestResult;
import com.ykj.graduation_design.common.entity.SysUser;
import com.ykj.graduation_design.common.utils.UserUtils;
import com.ykj.graduation_design.module.Blog.Dto.ComentsDto;
import com.ykj.graduation_design.module.Blog.entity.Comments;
import com.ykj.graduation_design.module.Blog.services.CommentsService;
import com.ykj.graduation_design.module.login.entity.LoginUser;
import com.ykj.graduation_design.module.login.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * @Author: Ayelsh
 * @Date: 2024/04/10/21:55
 * @Description: 
 */
@RestController
@RequestMapping("/Comments")
public class CommentsController {
    @Autowired
    private CommentsService commentsService;
    @Autowired
    private UserService userService;
    @GetMapping("{arctileId}")
    public void comments(@PathVariable("arctileId") Long arctileId, HttpServletResponse response){
      try{
          RestResult.responseJson(response, new RestResult<>(200, "成功！", commentsService.commentsList(arctileId)));
      }catch (Exception e){
          RestResult.responseJson(response, new RestResult<>(600, "失败", e.getMessage()));
      }
    }
    @PostMapping
    public void newComments(@RequestBody Comments comments,HttpServletResponse response){
        try{
            SysUser sysUser =userService.getById( UserUtils.getCurrentUser().getId());
            comments.setAuthorId(String.valueOf(sysUser.getId()));
            comments.setAuthorNickname(sysUser.getNickName());
            comments.setAuthorAvatar(sysUser.getAvatar());
            comments.setCreatedTime(new Date());
            commentsService.save(comments);
            RestResult.responseJson(response, new RestResult<>(200, "成功！", comments));
        }catch (Exception e){
            RestResult.responseJson(response, new RestResult<>(600, "失败", e.getMessage()));
        }
    }
}
