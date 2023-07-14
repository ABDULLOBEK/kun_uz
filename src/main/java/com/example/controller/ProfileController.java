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
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ProfileDTO profile){
        ProfileDTO response = profileService.create(profile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> put(@RequestBody ProfileDTO profile,
                                 @PathVariable("id") Integer id){
        profileService.update(id, profile);
        return ResponseEntity.ok(true);
    }

    @PutMapping("/simple/{id}")
    public ResponseEntity<?> putSimple(@RequestBody ProfileDTO profile,
                                 @PathVariable("id") Integer id){
        profileService.updateSimple(id, profile);
        return ResponseEntity.ok(true);
    }

    @GetMapping(value = "/pagination/all")
    public ResponseEntity<?> pagination(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(profileService.getAll(page - 1, size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id){
        String response = profileService.delete(id);
        if(response.length()>0){
            return ResponseEntity.ok("Profile Deleted");
        }
        return ResponseEntity.badRequest().body("Profile not found");
    }

    @PostMapping(value = "/filter")
    public ResponseEntity<PageImpl<ProfileDTO>> filter(@RequestBody ProfileFilterDTO filterDTO,@RequestParam(value = "page", defaultValue = "1") int page,
                                                   @RequestParam(value = "size", defaultValue = "10") int size){
        PageImpl<ProfileDTO> response = profileService.filter(filterDTO, page, size);
        return ResponseEntity.ok(response);
    }

}
