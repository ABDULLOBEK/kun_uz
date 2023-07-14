package com.example.service;

import com.example.dto.ProfileDTO;
import com.example.dto.RegionDTO;
import com.example.entity.ProfileEntity;
import com.example.entity.RegionEntity;
import com.example.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class RegionService {
    @Autowired
    private RegionRepository regionRepository;

    public RegionDTO add(RegionDTO dto) {
        RegionEntity entity = toEntity(dto);
        regionRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public Boolean update(Integer id, RegionDTO region){
        int affectedRows = regionRepository.update(id, region.getOrderNum(),region.getNameUz(), region.getNameEn(), region.getNameRu());
        return affectedRows==1;
    }

    public String delete(Integer id) {
         regionRepository.deleteById(id);
        return "Region Deleted";
    }

    public List<RegionDTO> getAll() {
        Iterable<RegionEntity> iterable = regionRepository.findAll();
        List<RegionDTO> dtoList = new LinkedList<>();
        iterable.forEach(entity ->{
            dtoList.add(toDTO(entity));
        });
        return dtoList;
    }

    public List<RegionDTO> getByLang(String language){
        if(language.equals("en")){
            return  getRegionDTOS(regionRepository.getNameEn());
        }
        if(language.equals("uz")){
            return  getRegionDTOS(regionRepository.getNameUZ());
        }
        if(language.equals("ru")){
            return  getRegionDTOS(regionRepository.getNameRu());
        }
        return null;
    }

    private List<RegionDTO> getRegionDTOS(List<RegionEntity> entityList) {
        List<RegionDTO> dtoList = new LinkedList<>();
        if (entityList.isEmpty()) {
            throw new RuntimeException("Student not found");
        }
        for (RegionEntity s:entityList){
            dtoList.add(toDTO(s));
        }
        return dtoList;
    }

    private RegionDTO toDTO(RegionEntity entity) {
        RegionDTO dto = new RegionDTO();
        dto.setOrderNum(entity.getOrderNum());
        dto.setNameUz(entity.getNameUz());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setVisible(entity.getVisible());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }


    private RegionEntity toEntity(RegionDTO dto) {
        RegionEntity entity = new RegionEntity();
        entity.setOrderNum(dto.getOrderNum());
        entity.setNameUz(dto.getNameUz());
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        entity.setVisible(dto.getVisible());
        entity.setCreatedDate(LocalDateTime.now());
        return entity;
    }
}
