package com.ykj.graduation_design.module.Blog.services;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykj.graduation_design.module.Blog.Dto.ComentsDto;
import com.ykj.graduation_design.module.Blog.entity.Comments;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * @Author: Ayelsh
 * @Date: 2024/04/10/21:45
 * @Description: 
 */
public interface CommentsService extends IService<Comments> {

    List<ComentsDto> commentsList(Long id);
}
