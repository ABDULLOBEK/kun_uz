package com.example.repository;

import com.example.entity.EmailHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmailHistoryRepository extends CrudRepository<EmailHistoryEntity, Integer>, PagingAndSortingRepository<EmailHistoryEntity, Integer> {

    @Query("SELECT s FROM EmailHistoryEntity s WHERE s.createdDate > :startDateTime AND s.createdDate <= :endDateTime")
    List<EmailHistoryEntity> findByCreatedDateBetween(@Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime);

    List<EmailHistoryEntity> findByEmail(String email);


}
