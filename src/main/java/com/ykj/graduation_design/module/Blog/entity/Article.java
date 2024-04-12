package com.ykj.graduation_design.module.Blog.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;


import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Ayelsh
 */
@Data
@TableName("article")
public class Article implements Serializable{
    @Serial
    private static final long serialVersionUID = 8137202191128471034L;
    /**
     * 主键
     */
    @TableId
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    /**
     * 文章标题
     */
    private String articleTitle;
    /**
     * 文章缩略图
     */
    private String articleThumbnailUrl;
    /**
     * 文章作者id
     */
    private Long articleAuthorId;
    /**
     * 浏览总数
     */
    private Integer articleViewCount;
    /**
     * 预览内容
     */
    private String articlePreviewContent;
    /**
     * 评论总数
     */
    private Integer articleCommentCount;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新时间
     */
    private Date updatedTime;
    /**
     * 文章状态
     */
    private String articleStatus;
    /**
     * 点赞总数
     */
    private Integer articleThumbsUpCount;

}
