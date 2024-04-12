package com.ykj.graduation_design.module.Blog.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykj.graduation_design.module.Blog.Dto.ComentsDto;
import com.ykj.graduation_design.module.Blog.entity.Comments;
import com.ykj.graduation_design.module.Blog.mapper.CommentsMapper;
import com.ykj.graduation_design.module.Blog.services.CommentsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * @Author: Ayelsh
 * @Date: 2024/04/10/21:46
 * @Description: 
 */
@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments> implements CommentsService {
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<ComentsDto> commentsList(Long id) {

        return baseMapper.getParentList(id).stream().map(comments -> {
            comments.setChildren(baseMapper.getChildrenList(comments.getId()));
            return comments;
        }).toList();
    }
}
