package com.example.repository;

import com.example.dto.FilterResultDTO;
import com.example.dto.ProfileFilterDTO;
import com.example.entity.ProfileEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomProfileRepository {
    @Autowired
    private EntityManager entityManager;
//    7
    public FilterResultDTO<ProfileEntity> filter(ProfileFilterDTO filterDTO, int page, int size){
        StringBuilder stringBuilder = new StringBuilder();

        Map<String , Object> params = new HashMap<>();
        if (filterDTO.getName() != null) {
            stringBuilder.append(" and lower(p.name) =:name");
            params.put("name", filterDTO.getName().toLowerCase());
        }
        if (filterDTO.getSurname() != null) {
            stringBuilder.append(" and p.surname like :surname");
            params.put("surname", "%" + filterDTO.getSurname() + "%");
        }
        if (filterDTO.getPhone() != null) {
            stringBuilder.append(" and p.phone =:phone");
            params.put("phone", filterDTO.getPhone());
        }
        if(filterDTO.getRole()!=null){
            stringBuilder.append(" and p.role =:role");
            params.put("role",filterDTO.getRole());
        }
        if (filterDTO.getCreatedDateFrom() != null && filterDTO.getCreatedDateTo() != null) {
            // 10.07.2023 00:00:00
            // 17.07.2023 23:59:59
            stringBuilder.append(" and p.createdDate between :dateFrom and :dateTo ");
            params.put("dateFrom", LocalDateTime.of(filterDTO.getCreatedDateFrom().toLocalDate(), LocalTime.MIN));
            params.put("dateTo", LocalDateTime.of(filterDTO.getCreatedDateTo().toLocalDate(), LocalTime.MAX));
        } else if (filterDTO.getCreatedDateFrom() != null) {
            stringBuilder.append(" and p.createdDate >= :dateFrom");
            params.put("dateFrom", LocalDateTime.of(filterDTO.getCreatedDateFrom().toLocalDate(), LocalTime.MIN));
        } else if (filterDTO.getCreatedDateTo() != null) {
            stringBuilder.append(" and p.createdDate <= :dateTo");
            params.put("dateFrom", LocalDateTime.of(filterDTO.getCreatedDateTo().toLocalDate(), LocalTime.MAX));
        }

        StringBuilder selectBuilder = new StringBuilder("SELECT p FROM ProfileEntity AS p WHERE p.visible = true");
        selectBuilder.append(stringBuilder);

        StringBuilder countBuilder = new StringBuilder("SELECT COUNT(p) FROM ProfileEntity AS p WHERE p.visible = true");
        countBuilder.append(stringBuilder);

        Query selectQuery = entityManager.createQuery(selectBuilder.toString());
        selectQuery.setMaxResults(size); // limit
        selectQuery.setFirstResult(size * page); // offset

        Query countQuery = entityManager.createQuery(countBuilder.toString());
        // params
        for (Map.Entry<String, Object> param : params.entrySet()) {
            selectQuery.setParameter(param.getKey(), param.getValue());
            countQuery.setParameter(param.getKey(), param.getValue());
        }

        List<ProfileEntity> entityList = selectQuery.getResultList();
        Long totalCount = (Long) countQuery.getSingleResult();

        return new FilterResultDTO<>(entityList, totalCount);
    }

}
