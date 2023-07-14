package com.example.service;

import com.example.dto.CategoryDTO;
import com.example.dto.RegionDTO;
import com.example.entity.CategoryEntity;
import com.example.entity.RegionEntity;
import com.example.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    public CategoryDTO add(CategoryDTO dto) {
        CategoryEntity entity = toEntity(dto);
        categoryRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public Boolean update(Integer id, CategoryDTO region){
        int affectedRows = categoryRepository.update(id, region.getOrderNum(),region.getNameUz(), region.getNameEn(), region.getNameRu());
        return affectedRows==1;
    }

    public String delete(Integer id) {
        categoryRepository.deleteById(id);
        return "Region Deleted";
    }

    public List<CategoryDTO> getAll() {
        Iterable<CategoryEntity> iterable = categoryRepository.findAll();
        List<CategoryDTO> dtoList = new LinkedList<>();
        iterable.forEach(entity ->{
            dtoList.add(toDTO(entity));
        });
        return dtoList;
    }

    public List<CategoryDTO> getByLang(String language){
        if(language.equals("en")){
            return  getRegionDTOS(categoryRepository.getNameEn());
        }
        if(language.equals("uz")){
            return  getRegionDTOS(categoryRepository.getNameUZ());
        }
        if(language.equals("ru")){
            return  getRegionDTOS(categoryRepository.getNameRu());
        }
        return null;
    }

    private List<CategoryDTO> getRegionDTOS(List<CategoryEntity> entityList) {
        List<CategoryDTO> dtoList = new LinkedList<>();
        if (entityList.isEmpty()) {
            throw new RuntimeException("Student not found");
        }
        for (CategoryEntity s:entityList){
            dtoList.add(toDTO(s));
        }
        return dtoList;
    }

    private CategoryDTO toDTO(CategoryEntity entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setOrderNum(entity.getOrderNum());
        dto.setNameUz(entity.getNameUz());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setVisible(entity.getVisible());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }


    private CategoryEntity toEntity(CategoryDTO dto) {
        CategoryEntity entity = new CategoryEntity();
        entity.setOrderNum(dto.getOrderNum());
        entity.setNameUz(dto.getNameUz());
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        entity.setVisible(dto.getVisible());
        entity.setCreatedDate(LocalDateTime.now());
        return entity;
    }

}
