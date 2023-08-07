package com.example.controller;

import com.example.dto.ArticleTypeDTO;
import com.example.dto.JwtDTO;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.service.ArticleTypeService;
import com.example.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articleType")
public class ArticleTypeController {
    @Autowired
    private ArticleTypeService articleTypeService;

    @PostMapping("/admin")
    public ResponseEntity<?> create(@RequestBody ArticleTypeDTO articleType){
//        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.ADMIN);
        ArticleTypeDTO response = articleTypeService.add(articleType,1);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> update(@RequestBody ArticleTypeDTO articleType,
                                    @PathVariable("id") Integer id,
                                    HttpServletRequest request){
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.ROLE_ADMIN);
        return ResponseEntity.ok(articleTypeService.update(id, articleType,jwtDTO.getId()));
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id,
                                    HttpServletRequest request){
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.ROLE_ADMIN);
        String  response = articleTypeService.delete(id);
        if(response.length()>0){
            return ResponseEntity.ok("Student Deleted");
        }
        return ResponseEntity.badRequest().body("Student not found");
    }

    @GetMapping("/admin/all")
    public List<ArticleTypeDTO> all(){
        return articleTypeService.getAll();
    }

    @GetMapping("/lang")
    public ResponseEntity<?> getByLang(@RequestParam("lang") Language lang,
                                       HttpServletRequest request){
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.ROLE_ADMIN);
        return ResponseEntity.ok(articleTypeService.getByLang(lang));
    }













}
