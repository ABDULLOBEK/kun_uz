package com.example.controller;

import com.example.dto.JwtDTO;
import com.example.dto.RegionDTO;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.service.RegionService;
import com.example.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/region")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @PostMapping("/admin/create")
    public ResponseEntity<?> create(@RequestBody RegionDTO region,  HttpServletRequest request){
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        RegionDTO response = regionService.add(region,jwtDTO.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> update(@RequestBody RegionDTO region,
                                 @PathVariable("id") Integer id,
                                    HttpServletRequest request){
            JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.update(id, region,jwtDTO.getId()));
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id,
                                    HttpServletRequest request){
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        String  response = regionService.delete(id);
        if(response.length()>0){
            return ResponseEntity.ok("Student Deleted");
        }
        return ResponseEntity.badRequest().body("Student not found");
    }

    @GetMapping("/admin/all")
    public List<RegionDTO> all(){
        return regionService.getAll();
    }

    @GetMapping("/lang")
    public ResponseEntity<?> getByLang(@RequestParam("lang") Language lang,  HttpServletRequest request){

        return ResponseEntity.ok(regionService.getByLang(lang));
    }
}
