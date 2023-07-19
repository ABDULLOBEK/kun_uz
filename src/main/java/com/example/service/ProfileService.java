package com.example.service;

import com.example.dto.FilterResultDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.ProfileFilterDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.exp.AppBadRequestException;
import com.example.repository.CustomProfileRepository;
import com.example.repository.ProfileRepository;
import com.example.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private CustomProfileRepository customRepository;

    public ProfileDTO create(ProfileDTO dto, Integer prtId){
        isValidProfile(dto);

        Optional<ProfileEntity> profileByEmail = profileRepository.findByEmail(dto.getEmail());
        if (profileByEmail.isPresent()) {
            throw new AppBadRequestException("Email already exists");
        }
        Optional<ProfileEntity> profileByPhone = profileRepository.findByPhone(dto.getPhone());
        if (profileByPhone.isPresent()) {
            throw new AppBadRequestException("Phone already exits");
        }

        ProfileEntity profile = toEntity(dto);
        profile.setPrtId(prtId);
        profileRepository.save(profile);
        dto.setId(profile.getId());
        dto.setCreatedDate(profile.getCreatedDate());
        return dto;
    }

    public Boolean update(Integer id, ProfileDTO dto, Integer prtId){
        isValidProfile(dto); // check
        ProfileEntity entity = get(id); // get
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setPrtId(prtId);
        profileRepository.save(entity);
        return true;
    }

    public Boolean updateSimple(Integer id, ProfileDTO dto){
        isValidProfile(dto); // check
        int affectedRows = profileRepository.updateSimple(id,
                dto.getName(),dto.getSurname());
        return affectedRows==1;
    }

    public List<ProfileDTO> getAll(){
        return profileRepository.findAllByVisibleTrue().stream().map(this::toDTO)
                .collect(Collectors.toList());}

    public PageImpl<ProfileDTO> filter(ProfileFilterDTO filterDTO, int page, int size) {
        FilterResultDTO<ProfileEntity> result = customRepository.filter(filterDTO, page, size);
        List<ProfileDTO> studentDTOList = result.getContent().stream()
                .map(this::toDTO).collect(Collectors.toList());
        return new PageImpl<>(studentDTOList, PageRequest.of(page, size), result.getTotalCount());
    }

    public boolean delete(Integer id) {
        int effectedRows = profileRepository.delete(id);
        return effectedRows == 1;
    }

    public ProfileEntity get(Integer profileId) {
        return profileRepository.findById(profileId).orElseThrow(() -> {
            throw new AppBadRequestException("Profile not found");
        });
    }

    private ProfileEntity toEntity(ProfileDTO dto) {
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(MD5Util.encode(dto.getPassword()));
        entity.setPhone(dto.getPhone());
        entity.setStatus((ProfileStatus.ACTIVE));
        entity.setRole((dto.getRole()));
        return entity;
    }

    private ProfileDTO toDTO(ProfileEntity entity){
        ProfileDTO dto = new ProfileDTO();
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        dto.setPhone(entity.getPhone());
        dto.setStatus(entity.getStatus());
        dto.setRole(entity.getRole());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    void isValidProfile(ProfileDTO dto) {
        if (dto.getName() == null || dto.getName().isBlank() || dto.getName().length() < 3) {
            throw new AppBadRequestException("Name required");
        }
        // ....
        // phone ..
    }


}
