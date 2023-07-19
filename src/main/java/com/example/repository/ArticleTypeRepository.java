package com.example.repository;

import com.example.entity.ArticleTypeEntity;
import com.example.mapper.RegionMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleTypeRepository extends CrudRepository<ArticleTypeEntity, Integer> {
    @Transactional
    @Modifying
    @Query("update ArticleTypeEntity  as r set r.orderNum=:order_num, r.nameUz=:name_uz, r.nameEn=:name_en, r.nameRu=:name_ru, r.prtId=:prId where r.id=:id")
    int update(@Param("id") Integer id, @Param("order_num") Integer orderNum, @Param("name_uz") String nameUz, @Param("name_en") String nameEn, @Param("name_ru") String nameRu,@Param("prId") Integer jwtDTOId);

    @Query(value = "select id, order_num, " +
            "CASE :lang " +
            "   WHEN 'en' THEN name_en " +
            "   WHEN 'ru' THEN name_ru" +
            "   ELSE name_uz" +
            " END as name" +
            " from article_type where visible = true order by order_num", nativeQuery = true)
    List<RegionMapper> findAllByLang(@Param("lang") String lang);
}
