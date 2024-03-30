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
    `create_by`   BIGINT(20)           DEFAULT NULL COMMENT '创建人的用户id',
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
    article_permalink       varchar(128)     null comment '文章永久链接',
    article_link            varchar(32)      null comment '站内链接',
    created_time            datetime         null comment '创建时间',
    updated_time            datetime         null comment '更新时间',
    article_perfect         char default '0' null comment '0:非优选1：优选',
    article_status          char default '0' null comment '文章状态',
    article_thumbs_up_count int  default 0   null comment '点赞总数',
    article_sponsor_count   int  default 0   null comment '赞赏总数'
) comment '文章表 ' collate = utf8mb4_unicode_ci;

INSERT INTO article (id, article_title, article_thumbnail_url, article_author_id, article_type,
                                   article_tags, article_view_count, article_preview_content, article_comment_count,
                                   article_permalink, article_link, created_time, updated_time, article_perfect,
                                   article_status, article_thumbs_up_count, article_sponsor_count)
VALUES (null, '给新人的一封信', null, 1, '0', '公告,新手信', 3275,
        '您好，欢迎来到 RYMCU 社区，RYMCU 是一个嵌入式知识学习交流平台。RY 取自”容易”的首字母，寓意为让电子设计变得 so easy。新手的疑问初学者都有很多疑问，在这里对这些疑问进行一一解答。我英语不好，可以学习编程吗？对于初学者来说，英语不是主要的障碍，国内有着充足的中文教程。但在接下来的学习过程中，需要阅读大量的英文文档，所以还是需要有一些英语基础和理解学习能力，配合翻译工具（如百度',
        0, 'http://localhost:3000/article/1',
        '/article/1', '2020-01-03 01:27:25', '2022-09-26 15:33:03', '0', '0', 7,
        3);

create table content
(
    id_article           bigint   not null comment '主键',
    article_content      text    null comment '文章内容原文',
    article_content_html text     null comment '文章内容Html',
    created_time         datetime null comment '创建时间',
    updated_time         datetime null comment '更新时间'
) comment ' ' collate = utf8mb4_unicode_ci;

create index forest_article_content_id_article_index
    on forest_article_content (id_article);


