package com.ykj.graduation_design.module.Blog.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


import java.io.Serializable;
import java.util.Date;

/**
 * @author ronger
 */
@Data
@TableName("Article")
public class Article implements Serializable, Cloneable {
    /**
     * 主键
     */
    @TableId
    private Long idArticle;
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
     * 文章类型
     */
    private String articleType;
    /**
     * 文章标签
     */
    private String articleTags;
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
     * 0:非优选1：优选;
     */
    private String articlePerfect;
    /**
     * 文章永久链接
     */
    private String articlePermalink;
    /**
     * 站内链接
     */
    private String articleLink;
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
    /**
     * 赞赏总数
     */
    private Integer articleSponsorCount;
}
