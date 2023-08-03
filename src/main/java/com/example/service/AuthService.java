package com.example.service;

import com.example.dto.*;
import com.example.entity.EmailHistoryEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.exp.AppBadRequestException;
import com.example.repository.EmailHistoryRepository;
import com.example.repository.ProfileRepository;
import com.example.util.JWTUtil;
import com.example.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private MailSenderService mailSenderService;
    @Autowired
    private EmailHistoryRepository emailHistoryRepository;
    @Autowired
    private SmsSenderService smsSenderService;


    public ApiResponseDTO login(AuthDTO dto) {
        // check
        Optional<ProfileEntity> optional = profileRepository.findByPhone(dto.getPhone());
        if (optional.isEmpty()) {
            return new ApiResponseDTO(false, "Login or Password not found");
        }
        ProfileEntity profileEntity = optional.get();
        if (!profileEntity.getPassword().equals(MD5Util.encode(dto.getPassword()))) {
            return new ApiResponseDTO(false, "Login or Password not found");
        }
        if (!profileEntity.getStatus().equals(ProfileStatus.ACTIVE) || !profileEntity.getVisible()) {
            return new ApiResponseDTO(false, "Your status not active. Please contact with support.");
        }

        ProfileDTO response = new ProfileDTO();
        response.setId(profileEntity.getId());
        response.setName(profileEntity.getName());
        response.setSurname(profileEntity.getSurname());
        response.setRole(profileEntity.getRole());
        response.setPhone(profileEntity.getPhone());
        response.setJwt(JWTUtil.encode(profileEntity.getId(), profileEntity.getRole()));
        return new ApiResponseDTO(true, response);
    }

    public ApiResponseDTO registration(RegistrationDTO dto) {
        // check
        Optional<ProfileEntity> exists = profileRepository.findByEmail(dto.getEmail());
        if (exists.isPresent()) {
            if (exists.get().getStatus().equals(ProfileStatus.REGISTRATION)) {
                profileRepository.delete(exists.get()); // delete
            } else {
                return new ApiResponseDTO(false, "Email already exists.");
            }
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
//        entity.setPhone(dto.getPhone());
        entity.setPassword(MD5Util.encode(dto.getPassword()));
        entity.setRole(ProfileRole.USER);
        entity.setStatus(ProfileStatus.REGISTRATION);
        profileRepository.save(entity);
//        smsSenderService.sendRegistrationSms(dto.getPhone());
        String message =  mailSenderService.sendEmailVerification(dto.getEmail(),entity.getName(), entity.getId());// send registration verification link
        EmailHistoryEntity historyEntity = new EmailHistoryEntity();
        historyEntity.setEmail(dto.getEmail());
        historyEntity.setMessage(message);
        emailHistoryRepository.save(historyEntity);
        return new ApiResponseDTO(true, "The verification link was send to email.");


    }

    public ApiResponseDTO emailVerification(String jwt) {
        JwtDTO jwtDTO = JWTUtil.decodeEmailJwt(jwt);

        Optional<ProfileEntity> exists = profileRepository.findById(jwtDTO.getId());
        if (exists.isEmpty()) {
            throw new AppBadRequestException("Profile not found");
        }

        ProfileEntity entity = exists.get();
        if (!entity.getStatus().equals(ProfileStatus.REGISTRATION)) {
            throw new AppBadRequestException("Wrong status");
        }
        entity.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(entity); // update
        return new ApiResponseDTO(true, "Registration completed");
    }
}
