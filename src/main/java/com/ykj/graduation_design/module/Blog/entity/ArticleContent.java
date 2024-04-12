package com.ykj.graduation_design.module.Blog.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author ronger
 */
@Data
@Document(collection = "text")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ArticleContent {

    @MongoId
    private ObjectId id;

    @Field("id_article")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long idArticle;

    @Field("article_content")
    private String articleContent;

    @Field("article_content_html")
    private String articleContentHtml;

    @DateTimeFormat
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Field("created_time")
    private Date createdTime;

    @DateTimeFormat
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Field("updated_time")
    private Date updatedTime;

}
