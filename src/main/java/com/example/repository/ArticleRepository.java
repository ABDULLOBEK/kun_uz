package com.example.repository;

import com.example.entity.ArticleEntity;
import com.example.enums.ArticleStatus;
import com.example.mapper.ArticleShortInfoIMapper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends CrudRepository<ArticleEntity, String> {


    @Query("from ArticleEntity as a " +
            " inner join a.articleTypeSet as at" +
            " where at.articleTypeId =:articleTypeId  and a.status =:status and a.visible = true " +
            " order by a.publishedDate desc limit :limit")
    List<ArticleShortInfoIMapper> getLast5ArticleByArticleTypeId(@Param("articleTypeId") Integer articleTypeId,
                                                       @Param("status") ArticleStatus status,
                                                       @Param("limit") int limit);

    @Query(value = "select a.id, a.title, a.description, a.image_id as imageId, a.published_date as publishedDate from article as a " +
            " inner join article_types as at on at.article_id = a.id" +
            " where at.article_type_id =:articleTypeId  and a.status ='PUBLISHED' and a.visible = true " +
            " order by a.published_date desc limit :limit", nativeQuery = true)
    List<ArticleShortInfoIMapper> getLast5ArticleByArticleTypeIdNative(@Param("articleTypeId") Integer articleTypeId,
                                                                       @Param("limit") int limit);


    //9. Get Last 4 Article By Types and except given article id.
   /* @Query("from ArticleEntity as a " +
            " inner join a.articleTypeSet as at" +
            " where at.articleTypeId =:articleTypeId and a.id <>:articleId and a.status =:status and a.visible = true " +
            " order by a.publishedDate desc limit 4 ")
    List<ArticleEntity> getLast4ArticleByArticleTypeIdAndExcept(@Param("articleId") String articleId,
                                                                @Param("articleTypeId") Integer articleTypeId,
                                                                @Param("status") ArticleStatus status);*/


}
