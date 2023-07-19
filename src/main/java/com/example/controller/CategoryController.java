package com.example.controller;

import com.example.dto.CategoryDTO;
import com.example.dto.JwtDTO;
import com.example.dto.RegionDTO;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.service.CategoryService;
import com.example.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody CategoryDTO category, @RequestHeader("Authorization") String authToken){
        JwtDTO jwtDTO = SecurityUtil.hasRole(authToken, ProfileRole.ADMIN);
        CategoryDTO response = categoryService.add(category,jwtDTO.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@RequestBody CategoryDTO category,
                                 @PathVariable("id") Integer id,
                                 @RequestHeader("Authorization") String authToken){
        JwtDTO jwtDTO = SecurityUtil.hasRole(authToken, ProfileRole.ADMIN);
        categoryService.update(id, category,jwtDTO.getId());
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id,@RequestHeader("Authorization") String authToken){
        JwtDTO jwtDTO = SecurityUtil.hasRole(authToken, ProfileRole.ADMIN);
        String  response = categoryService.delete(id);
        if(response.length()>0){
            return ResponseEntity.ok("Student Deleted");
        }
        return ResponseEntity.badRequest().body("Student not found");
    }

    @GetMapping("/all")
    public List<CategoryDTO> all(){
        return categoryService.getAll();
    }

    @GetMapping("/lang")
    public ResponseEntity<?> getByLang(@RequestParam("lang") Language lang,@RequestHeader("Authorization") String authToken){
        JwtDTO jwtDTO = SecurityUtil.hasRole(authToken, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.getByLang(lang));
    }
}
