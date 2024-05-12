CREATE DATABASE IF NOT EXISTS graduation_design;
USE graduation_design;
DROP TABLE IF EXISTS `sys_user`;
create table sys_user
(
    id           bigint auto_increment comment '主键'
        primary key,
    user_name    varchar(64) UNIQUE default 'NULL' not null comment '用户名',
    nick_name    varchar(64) default 'NULL' not null comment '昵称',
    password     varchar(64) default 'NULL' not null comment '密码',
    status       char        default '0'    null comment '账号状态（0正常 1停用）',
    email        varchar(64) UNIQUE         null comment '邮箱' ,
    phone_number varchar(32)                null comment '手机号',
    sex          char                       null comment '用户性别（0男，1女，2未知）',
    avatar       varchar(128)               null comment '头像',
    user_type    char        default '1'    not null comment '用户类型（0管理员，1普通用户）',
    create_by    bigint                     null comment '创建人的用户id',
    create_time  datetime                   null comment '创建时间',
    update_by    bigint                     null comment '更新人',
    update_time  datetime                   null comment '更新时间',
    del_flag     int         default 0      null comment '删除标志（0代表未删除，1代表已删除）',
    roles        varchar(255)               null comment '角色'
)
    comment '用户表';
alter table sys_user add unique(user_name);
CREATE INDEX idx_username
    ON sys_user (user_name);

USE graduation_design;
DROP TABLE IF EXISTS `article`;
create table article
(
    id                      bigint auto_increment comment '主键'
        primary key,
    article_title           varchar(128)     null comment '文章标题',
    article_thumbnail_url   varchar(128)     null comment '文章缩略图',
    article_author_id       bigint           null comment '文章作者id',
    article_view_count      int  default 1   null comment '浏览总数',
    article_preview_content varchar(256)     null comment '预览内容',
    article_comment_count   int  default 0   null comment '评论总数',
    created_time            datetime         null comment '创建时间',
    updated_time            datetime         null comment '更新时间',
    article_status          char default '0' null comment '文章状态',
    article_thumbs_up_count int  default 0   null comment '点赞总数'
)
    comment '文章表 ' collate = utf8mb4_unicode_ci;

CREATE INDEX idx_author_id
    ON article (article_author_id);

DROP TABLE IF EXISTS `comments`;
create table comments
(
    id              bigint auto_increment comment '主键'
        primary key,
    content         varchar(255) null comment '评论内容',
    author_nickName varchar(128) null comment '评论者昵称',
    author_id       varchar(128) null comment '评论者id',
    author_avatar   varchar(128) null comment '评论者头像链接',
    comment_id      bigint       null comment '父评论id',
    article_id      bigint       null comment '文章id',
    repaly_id       bigint       null comment '回复的评论id',
    created_time    datetime     null comment '创建时间',
    updated_time    datetime     null comment '更新时间',
    repaly_name     varchar(10)  null comment '回复的user'
)
    comment '评论表 ' collate = utf8mb4_unicode_ci;
CREATE INDEX idx_comment_id
    ON comments (comment_id);
CREATE INDEX idx_comment_id_and_article_id
    ON comments (article_id,comment_id);

DROP TABLE IF EXISTS `button`;
create table button
(
    id           bigint auto_increment comment '主键'
        primary key,
    command      varchar(255) null comment '按键对应命令',
    description  varchar(128) null comment '按键描述',
    created_time datetime     null comment '创建时间',
    updated_time datetime     null comment '更新时间',
    command_type tinyint(1)   null comment '命令类型'
)
    comment '按键表' collate = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `resources`;
create table resources
(
    id           bigint auto_increment comment '主键'
        primary key,
    fileName     varchar(255) null comment '资源文件名',
    author_id       varchar(128) null comment '上传者id',
    description  varchar(255) null comment '资源描述',
    created_time datetime     null comment '创建时间',
    updated_time datetime     null comment '更新时间',
    file_url      varchar(255) null comment '资源文件URL'
)
    comment '按键表' collate = utf8mb4_unicode_ci;

update article set article_author_id = 1783786073882198016 where article_author_id IS NULL