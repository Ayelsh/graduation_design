package com.ykj.graduation_design.module.Blog.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 评论表 
* @author Ayelsh.ye
 * @TableName comments
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("comments")
public class Comments implements Serializable {

    @Serial
    private static final long serialVersionUID = 3371009478961451163L;
    /**
    * 主键
    */
    @TableId
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    /**
    * 评论内容
    */
    private String content;
    /**
    * 评论者昵称
    */
    private String authorNickname;
    /**
    * 评论者id
    */
    private String authorId;
    /**
    * 评论者头像链接
    */
    private String authorAvatar;
    /**
    * 父评论id
    */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long commentId;
    /**
    * 文章id
    */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long articleId;
    /**
    * 回复的评论id
    */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long repalyId;
    /**
     * 回复的评论user
     */
    private String repalyName;
    /**
    * 创建时间
    */
    private Date createdTime;
    /**
    * 更新时间
    */
    private Date updatedTime;

}
