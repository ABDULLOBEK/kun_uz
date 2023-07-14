package com.example.service;

import com.example.dto.FilterResultDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.ProfileFilterDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.repository.CustomProfileRepository;
import com.example.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private CustomProfileRepository customRepository;

    public ProfileDTO create(ProfileDTO dto){
        ProfileEntity profile = toEntity(dto);
        profileRepository.save(profile);
        dto.setId(profile.getId());
        return dto;
    }

    public Boolean update(Integer id, ProfileDTO dto){
        int affectedRows = profileRepository.update(id,
                dto.getName(),dto.getSurname(), dto.getEmail(), dto.getPhone(),dto.getPassword(), dto.getStatus().toString(), dto.getRole().toString());
        return affectedRows==1;
    }

    public Boolean updateSimple(Integer id, ProfileDTO dto){
        int affectedRows = profileRepository.updateSimple(id,
                dto.getName(),dto.getSurname());
        return affectedRows==1;
    }

    public PageImpl<ProfileDTO> getAll(int page, int size){
        Pageable pageable = PageRequest.of(page,size, Sort.Direction.DESC, "id");
        Page<ProfileEntity> pageObj = profileRepository.findAll(pageable);
        List<ProfileDTO> studentDTOList = pageObj.stream().map(this::toDTO).collect(Collectors.toList());
        return new PageImpl<>(studentDTOList, pageable, pageObj.getTotalElements());
    }

    public PageImpl<ProfileDTO> filter(ProfileFilterDTO filterDTO, int page, int size) {
        FilterResultDTO<ProfileEntity> result = customRepository.filter(filterDTO, page, size);
        List<ProfileDTO> studentDTOList = result.getContent().stream()
                .map(this::toDTO).collect(Collectors.toList());
        return new PageImpl<>(studentDTOList, PageRequest.of(page, size), result.getTotalCount());
    }

    public String delete(Integer id){
        profileRepository.deleteById(id);
        return  "Profile Deleted";
    }

    private ProfileEntity toEntity(ProfileDTO dto) {
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setPhone(dto.getPhone());
        entity.setStatus(String.valueOf(ProfileStatus.ACTIVE));
        entity.setRole(String.valueOf(dto.getRole()));
        entity.setVisible(true);
        entity.setCreatedDate(LocalDateTime.now());
        return entity;
    }

    private ProfileDTO toDTO(ProfileEntity entity){
        ProfileDTO dto = new ProfileDTO();
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        dto.setPhone(entity.getPhone());
        dto.setStatus(ProfileStatus.valueOf(entity.getStatus()));
        dto.setRole(ProfileRole.valueOf(entity.getRole()));
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }
}
