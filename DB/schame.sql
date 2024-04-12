USE graduation_design;
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`          BIGINT(20)  NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_name`   VARCHAR(64) NOT NULL DEFAULT 'NULL' COMMENT '用户名',
    `nick_name`   VARCHAR(64) NOT NULL DEFAULT 'NULL' COMMENT '昵称',
    `password`    VARCHAR(64) NOT NULL DEFAULT 'NULL' COMMENT '密码',
    `status`      CHAR(1)              DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
    `email`       VARCHAR(64)          DEFAULT NULL COMMENT '邮箱',
    `phonenumber` VARCHAR(32)          DEFAULT NULL COMMENT '手机号',
    `sex`         CHAR(1)              DEFAULT NULL COMMENT '用户性别（0男，1女，2未知）',
    `avatar`      VARCHAR(128)         DEFAULT NULL COMMENT '头像',
    `user_type`   CHAR(1)     NOT NULL DEFAULT '1' COMMENT '用户类型（0管理员，1普通用户）',
    `create_by`   BIGINT(20)           DEFAULT 'admin' COMMENT '创建人的用户id',
    `create_time` DATETIME             DEFAULT NULL COMMENT '创建时间',
    `update_by`   BIGINT(20)           DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME             DEFAULT NULL COMMENT '更新时间',
    `del_flag`    INT(11)              DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
    `github_data` VARCHAR(255)         DEFAULT NULL COMMENT 'Github_Json',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户表';

USE graduation_design;
DROP TABLE IF EXISTS `article`;
create table article
(
    id                      bigint auto_increment comment '主键'
        primary key,
    article_title           varchar(128)     null comment '文章标题',
    article_thumbnail_url   varchar(128)     null comment '文章缩略图',
    article_author_id       bigint           null comment '文章作者id',
    article_type            char default '0' null comment '文章类型',
    article_tags            varchar(128)     null comment '文章标签',
    article_view_count      int  default 1   null comment '浏览总数',
    article_preview_content varchar(256)     null comment '预览内容',
    article_comment_count   int  default 0   null comment '评论总数',
    created_time            datetime         null comment '创建时间',
    updated_time            datetime         null comment '更新时间',
    article_status          char default '0' null comment '文章状态',
    article_thumbs_up_count int  default 0   null comment '点赞总数'
) comment '文章表 ' collate = utf8mb4_unicode_ci;

create table comments
(
    id              bigint auto_increment comment '主键'
        primary key,
    content         varchar(255) null comment '评论内容',
    author_nickName varchar(128) null comment '评论者昵称',
    author_id       varchar(128) null comment '评论者id',
    author_avatar   varchar(128) null comment '评论者头像链接',
    comment_id       bigint comment '父评论id',
    article_id      bigint comment '文章id',
    repaly_id  bigint comment '回复的评论id',
    created_time    datetime     null comment '创建时间',
    updated_time    datetime     null comment '更新时间'
) comment '评论表 ' collate = utf8mb4_unicode_ci;



