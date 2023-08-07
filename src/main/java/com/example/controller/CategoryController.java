package com.example.controller;

import com.example.dto.CategoryDTO;
import com.example.dto.JwtDTO;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.service.CategoryService;
import com.example.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody CategoryDTO category, HttpServletRequest request){
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.ROLE_ADMIN);
        CategoryDTO response = categoryService.add(category,jwtDTO.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@RequestBody CategoryDTO category,
                                 @PathVariable("id") Integer id,
                                 HttpServletRequest request){
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.ROLE_ADMIN);
        categoryService.update(id, category,jwtDTO.getId());
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id, HttpServletRequest request){
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.ROLE_ADMIN);
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
    public ResponseEntity<?> getByLang(@RequestParam("lang") Language lang, HttpServletRequest request){
        JwtDTO jwtDTO = SecurityUtil.hasRole(request, ProfileRole.ROLE_ADMIN);
        return ResponseEntity.ok(categoryService.getByLang(lang));
    }
}
