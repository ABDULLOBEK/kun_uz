package com.example.service;

import com.example.dto.RegionDTO;
import com.example.entity.RegionEntity;
import com.example.enums.Language;
import com.example.exp.AppBadRequestException;
import com.example.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class RegionService {
    @Autowired
    private RegionRepository regionRepository;

    public RegionDTO add(RegionDTO dto) {
        RegionEntity entity = toEntity(dto);
//        entity.setPrtId(prtId);
        regionRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public Boolean update(Integer id, RegionDTO region, Integer jwtDTOId){
        int affectedRows = regionRepository.update(id, region.getOrderNum(),region.getNameUz(), region.getNameEn(), region.getNameRu(),jwtDTOId);
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

    public List<RegionDTO> getByLang(Language lang) {
        List<RegionDTO> dtoList = new LinkedList<>();
        regionRepository.findAllByLang(lang.name()).forEach(mapper -> {
            RegionDTO dto = new RegionDTO();
            dto.setId(mapper.getId());
            dto.setOrderNum(mapper.getOrderNumber());
            dto.setName(mapper.getName());
            dtoList.add(dto);
        });
        return dtoList;
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
        return entity;
    }
    public RegionDTO getById(Integer id, Language language) {
        return toDTO(get(id));
    }

    public RegionEntity get(Integer id) {
        return regionRepository.findById(id).orElseThrow(() -> {
            throw new AppBadRequestException("Region not found");
        });
    }
}
