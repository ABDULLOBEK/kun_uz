package com.example.service;

import com.example.entity.ArticleTypesEntity;
import com.example.repository.ArticleTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleTypesService {
    @Autowired
    private ArticleTypesRepository articleTypesRepository;

    public void create(String articleId, List<Integer> typeIdList) {
        typeIdList.forEach(typesId -> {
            create(articleId, typesId);
        });
    }

    public void create(String articleId, Integer typesId) {
        ArticleTypesEntity entity = new ArticleTypesEntity();
        entity.setArticleId(articleId);
        entity.setArticleTypeId(typesId);
        articleTypesRepository.save(entity);
    }


    public void merge(String articleId, List<Integer> newList) {
        // newList = [1,2,7,8]
        if (newList == null) {
            articleTypesRepository.deleteByArticleId(articleId);
            return;
        }
        //[1,2,3,4,5]
        List<Integer> oldList = articleTypesRepository.getAllArticleTypeIdList(articleId);
        for (Integer typeId : newList) {
            if (!oldList.contains(typeId)) {
                create(articleId, typeId); // create
            }
        }
        for (Integer typeId : oldList) {
            if (!newList.contains(typeId)) {
                articleTypesRepository.deleteByArticleIdAndTypeId(articleId, typeId); // delete
            }
        }
    }

}
