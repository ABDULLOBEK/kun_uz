package com.example.repository;

import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends CrudRepository<ProfileEntity, Integer>, PagingAndSortingRepository<ProfileEntity, Integer> {
//2
    @Transactional
    @Modifying
    @Query("update ProfileEntity as p set p.name=:name, p.surname=:surname, p.email=:email, p.phone=:phone,p.password=:password, p.status=:status,p.role=:role where p.id=:id")
    int update(@Param("id") Integer id, @Param("name") String name, @Param("surname") String surname, @Param("email") String email,@Param("phone") String phone,@Param("password") String password,@Param("status") String status,@Param("role") String role);
//3
    @Transactional
    @Modifying
    @Query("update ProfileEntity as p set p.name=:name, p.surname=:surname where p.id=:id")
    int updateSimple(@Param("id") Integer id, @Param("name") String name, @Param("surname") String surname);
}
