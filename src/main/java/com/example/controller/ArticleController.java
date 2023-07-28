package com.example.controller;

import com.example.dto.ArticleDTO;
import com.example.dto.JwtDTO;
import com.example.dto.ProfileDTO;
import com.example.enums.ProfileRole;
import com.example.service.ArticleService;
import com.example.service.ProfileService;
import com.example.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping(value = {"", "/"})
    public ResponseEntity<?> create(@RequestBody ArticleDTO dto, HttpServletRequest request) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.create(dto, jwtDTO.getId()));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable("id") String id,
                                    @RequestBody ArticleDTO dto, HttpServletRequest request) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.update(id, dto, jwtDTO.getId()));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id, HttpServletRequest request){
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.delete(id));
    }

    @PutMapping(value = "/status/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable("id") String id,
                                    @RequestBody ArticleDTO dto, HttpServletRequest request) {
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.changeStatus(id, dto, jwtDTO.getId()));
    }

    @GetMapping(value = "/get5")
    public ResponseEntity<?> getLast5(@RequestParam("articleTypeId") Integer typeId){
        return ResponseEntity.ok(articleService.last5(typeId));
    }

    @GetMapping(value = "/get3")
    public ResponseEntity<?> getLast3(@RequestParam("articleTypeId") Integer typeId){
        return ResponseEntity.ok(articleService.last3(typeId));
    }

}
