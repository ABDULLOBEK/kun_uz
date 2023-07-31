package com.example.service;

import com.example.dto.EmailHistoryDTO;
import com.example.entity.EmailHistoryEntity;
import com.example.repository.EmailHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailHistoryService {

    @Autowired
    private EmailHistoryRepository emailHistoryRepository;

    public List<EmailHistoryDTO> getByEmail(String email) {
        List<EmailHistoryEntity> entityList = emailHistoryRepository.findByEmail(email);
        List<EmailHistoryDTO> dtoList = new LinkedList<>();
        entityList.forEach(entity ->{
            dtoList.add(toDTO(entity));
        });
        return dtoList;
     }

    public List<EmailHistoryDTO> getByDate(LocalDate date) {
        LocalDateTime from = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime to = LocalDateTime.of(date, LocalTime.MAX);
        List<EmailHistoryEntity> entityList = emailHistoryRepository.findByCreatedDateBetween(from,to);
        List<EmailHistoryDTO> dtoList = new LinkedList<>();
        entityList.forEach(entity ->{
            dtoList.add(toDTO(entity));
        });
        return dtoList;
    }

    private EmailHistoryDTO toDTO(EmailHistoryEntity entity) {
        EmailHistoryDTO dto = new EmailHistoryDTO();
        dto.setEmail(entity.getEmail());
        dto.setMessage(entity.getMessage());
        dto.setCreated_data(entity.getCreatedDate());
        return dto;
    }

    public PageImpl<EmailHistoryDTO> pagination(int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.Direction.DESC, "id");
        Page<EmailHistoryEntity> pageObj = emailHistoryRepository.findAll(pageable);
        List<EmailHistoryDTO> studentDTOList = pageObj.stream().map(this::toDTO).collect(Collectors.toList());
        return new PageImpl<>(studentDTOList, pageable, pageObj.getTotalElements());
    }

}
