package com.example.service;

import com.example.dto.ArticleDTO;
import com.example.entity.ArticleEntity;
import com.example.enums.ArticleStatus;
import com.example.enums.Language;
import com.example.exp.AppBadRequestException;
import com.example.mapper.ArticleShortInfoIMapper;
import com.example.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private RegionService regionService;
    @Autowired
    private ArticleTypesService articleTypesService;

    @Autowired
    private AttachService attachService;

    public ArticleDTO create(ArticleDTO dto, Integer moderatorId) {
        // check
        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setDescription(dto.getDescription());
        entity.setImageId(dto.getImageId());
        entity.setRegionId(dto.getRegionId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setModeratorId(moderatorId);
        entity.setStatus(ArticleStatus.NOT_PUBLISHED);
        articleRepository.save(entity); // save
        articleTypesService.create(entity.getId(), dto.getArticleType()); // save type list
        // response
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public ArticleDTO update(String id, ArticleDTO dto, Integer moderatorId) {
        // check
        ArticleEntity entity = get(id);
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setDescription(dto.getDescription());
        entity.setImageId(dto.getImageId());
        entity.setRegionId(dto.getRegionId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setModeratorId(moderatorId);
        entity.setStatus(ArticleStatus.NOT_PUBLISHED);
        articleRepository.save(entity); // save

        articleTypesService.merge(entity.getId(), dto.getArticleType()); // save type list
        // response
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public Boolean delete(String id){
        articleRepository.deleteById(id);
        return true;
    }

    public ArticleStatus changeStatus(String id, ArticleDTO dto, Integer moderatorId){
        ArticleEntity entity = get(id);
        if(dto.getStatus().equals(ArticleStatus.NOT_PUBLISHED)){
        entity.setStatus(ArticleStatus.PUBLISHED);
        }else {
            entity.setStatus(ArticleStatus.NOT_PUBLISHED);
        }
        articleRepository.save(entity);
        return entity.getStatus();
    }

    public List<ArticleDTO> last5(Integer articleTypeId) {
        List<ArticleShortInfoIMapper> list = articleRepository.getLast5ArticleByArticleTypeIdNative(articleTypeId, 5);
        List<ArticleDTO> dtoList = new LinkedList<>();

        list.forEach(mapper -> {
            ArticleDTO dto = new ArticleDTO();
            dto.setId(mapper.getId());
            dto.setTitle(mapper.getTitle());
            dto.setDescription(mapper.getDescription());
            dto.setImage(attachService.getAttachWithUrl(mapper.getImageId()));
            dtoList.add(dto);
        });
        return dtoList;
    }

    public List<ArticleDTO> last3(Integer articleTypeId) {
        List<ArticleShortInfoIMapper> list = articleRepository.getLast5ArticleByArticleTypeId(articleTypeId, ArticleStatus.PUBLISHED,3);
        List<ArticleDTO> dtoList = new LinkedList<>();

        list.forEach(mapper -> {
            ArticleDTO dto = new ArticleDTO();
            dto.setId(mapper.getId());
            dto.setTitle(mapper.getTitle());
            dto.setDescription(mapper.getDescription());
            dto.setImage(attachService.getAttachWithUrl(mapper.getImageId()));
            dtoList.add(dto);
        });
        return dtoList;
    }


    public ArticleDTO getById(String articleId, Language language) {
        ArticleEntity entity = get(articleId);
        ArticleDTO dto = new ArticleDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setContent(entity.getContent());
         dto.setImageId(entity.getImageId());
        dto.setRegion(regionService.getById(entity.getRegionId(), language));
        //
        return dto;
    }

    public ArticleEntity get(String id) {
        return articleRepository.findById(id).orElseThrow(() -> {
            throw new AppBadRequestException("Article not found");
        });
    }

}
