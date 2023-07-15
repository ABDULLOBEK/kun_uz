package com.example.service;

import com.example.dto.ArticleTypeDTO;
import com.example.dto.RegionDTO;
import com.example.entity.ArticleTypeEntity;
import com.example.entity.RegionEntity;
import com.example.enums.Language;
import com.example.repository.ArticleTypeRepository;
import com.example.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ArticleTypeService {
    @Autowired
    private ArticleTypeRepository articleTypeRepository;

    public ArticleTypeDTO add(ArticleTypeDTO dto) {
        ArticleTypeEntity entity = toEntity(dto);
        articleTypeRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public Boolean update(Integer id, ArticleTypeDTO region){
        int affectedRows = articleTypeRepository.update(id, region.getOrderNum(),region.getNameUz(), region.getNameEn(), region.getNameRu());
        return affectedRows==1;
    }

    public String delete(Integer id) {
        articleTypeRepository.deleteById(id);
        return "Region Deleted";
    }

    public List<ArticleTypeDTO> getAll() {
        Iterable<ArticleTypeEntity> iterable = articleTypeRepository.findAll();
        List<ArticleTypeDTO> dtoList = new LinkedList<>();
        iterable.forEach(entity ->{
            dtoList.add(toDTO(entity));
        });
        return dtoList;
    }

    public List<ArticleTypeDTO> getByLang(Language lang) {
        List<ArticleTypeDTO> dtoList = new LinkedList<>();
        articleTypeRepository.findAllByLang(lang.name()).forEach(mapper -> {
            ArticleTypeDTO dto = new ArticleTypeDTO();
            dto.setId(mapper.getId());
            dto.setOrderNum(mapper.getOrderNumber());
            dto.setName(mapper.getName());
            dtoList.add(dto);
        });
        return dtoList;
    }


    private List<ArticleTypeDTO> getRegionDTOS(List<ArticleTypeEntity> entityList) {
        List<ArticleTypeDTO> dtoList = new LinkedList<>();
        if (entityList.isEmpty()) {
            throw new RuntimeException("Student not found");
        }
        for (ArticleTypeEntity s:entityList){
            dtoList.add(toDTO(s));
        }
        return dtoList;
    }

    private ArticleTypeDTO toDTO(ArticleTypeEntity entity) {
        ArticleTypeDTO dto = new ArticleTypeDTO();
        dto.setOrderNum(entity.getOrderNum());
        dto.setNameUz(entity.getNameUz());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setVisible(entity.getVisible());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }


    private ArticleTypeEntity toEntity(ArticleTypeDTO dto) {
        ArticleTypeEntity entity = new ArticleTypeEntity();
        entity.setOrderNum(dto.getOrderNum());
        entity.setNameUz(dto.getNameUz());
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        return entity;
    }
}
