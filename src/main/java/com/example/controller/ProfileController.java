package com.example.controller;

import com.example.dto.ProfileDTO;
import com.example.dto.ProfileFilterDTO;
import com.example.service.ProfileService;
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

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody ProfileDTO profile){
        ProfileDTO response = profileService.create(profile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("//{id}")
    public ResponseEntity<?> update(@RequestBody ProfileDTO profile,
                                 @PathVariable("id") Integer id){
        return ResponseEntity.ok(profileService.update(id, profile));
    }

    @PutMapping("/simple/{id}")
    public ResponseEntity<Boolean> updateSimple(@RequestBody ProfileDTO profile,
                                 @PathVariable("id") Integer id){
        return ResponseEntity.ok(profileService.updateSimple(id, profile));
    }

    @GetMapping(value = "")
    public ResponseEntity<List<ProfileDTO>> getAll() {
        return ResponseEntity.ok(profileService.getAll());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Integer id){
        return ResponseEntity.ok(profileService.delete(id));
    }

    @PostMapping(value = "/filter")
    public ResponseEntity<PageImpl<ProfileDTO>> filter(@RequestBody ProfileFilterDTO filterDTO,@RequestParam(value = "page", defaultValue = "1") int page,
                                                   @RequestParam(value = "size", defaultValue = "10") int size){
        PageImpl<ProfileDTO> response = profileService.filter(filterDTO, page, size);
        return ResponseEntity.ok(response);
    }

}
