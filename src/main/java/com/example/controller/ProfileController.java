package com.example.controller;

import com.example.dto.JwtDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.ProfileFilterDTO;
import com.example.enums.ProfileRole;
import com.example.service.ProfileService;
import com.example.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping(value = {"", "/"})
    public ResponseEntity<?> create(@RequestBody ProfileDTO dto, HttpServletRequest request) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.ADMIN, ProfileRole.MODERATOR);
        return ResponseEntity.ok(profileService.create(dto, jwtDTO.getId()));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@RequestBody ProfileDTO profile,
                                    @PathVariable("id") Integer id,
                                    HttpServletRequest request){
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.update(id, profile, jwtDTO.getId()));
    }

    @PutMapping(value = "/detail/{id}")
    public ResponseEntity<Boolean> updateDetail(@RequestBody ProfileDTO dto,
                                                HttpServletRequest request) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, null);
        return ResponseEntity.ok(profileService.updateSimple(jwtDTO.getId(), dto));
    }

    @GetMapping(value = "/get")
    public ResponseEntity<List<ProfileDTO>> getAll( HttpServletRequest request) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Integer id,
                                          HttpServletRequest request){
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.delete(id));
    }

    @PostMapping(value = "/filter")
    public ResponseEntity<PageImpl<ProfileDTO>> filter(@RequestBody ProfileFilterDTO filterDTO,@RequestParam(value = "page", defaultValue = "1") int page,
                                                   @RequestParam(value = "size", defaultValue = "10") int size){
        PageImpl<ProfileDTO> response = profileService.filter(filterDTO, page, size);
        return ResponseEntity.ok(response);
    }

}
