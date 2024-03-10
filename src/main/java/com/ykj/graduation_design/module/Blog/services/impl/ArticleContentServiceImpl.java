package com.ykj.graduation_design.module.Blog.services.impl;

import com.ykj.graduation_design.module.Blog.entity.ArticleContent;
import com.ykj.graduation_design.module.Blog.services.ArticleContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @Date: 2024/01/28/15:57
 * @Description:
 */
@Service
@Slf4j
public class ArticleContentServiceImpl implements ArticleContentService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ArticleContentServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<ArticleContent> allContentQuery() {
        List<ArticleContent> articleContentList = new ArrayList<>();
        articleContentList = mongoTemplate.findAll(ArticleContent.class,"text");
        return articleContentList;
    }


    @Override
    public void addContent(ArticleContent articleContent) {
        mongoTemplate.insert(articleContent);

    }

    @Override
    public ArticleContent queryByArticleId(Long id) {
        Query query  = new Query();
        Criteria criteria = new Criteria();
        criteria.and("id_article").is(id);
        query.addCriteria(criteria);
        return mongoTemplate.findOne(query,ArticleContent.class);
    }
}
