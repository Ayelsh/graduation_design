package com.ykj.graduation_design.module.Blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ykj.graduation_design.module.Blog.Dto.ComentsDto;
import com.ykj.graduation_design.module.Blog.entity.Comments;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * @Author: Ayelsh
 * @Date: 2024/04/10/21:43
 * @Description: 
 */
@Mapper
public interface CommentsMapper extends BaseMapper<Comments> {
    @Select("SELECT * FROM comments WHERE article_id = ${id} and comment_id IS NULL ")
    List<ComentsDto> getParentList(@Param("id") Long articleId);


    @Select("SELECT * FROM comments WHERE comment_id = ${id}")
    List<Comments> getChildrenList(@Param("id") Long commentId);


}
