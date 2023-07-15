package com.example.controller;

import com.example.dto.ArticleTypeDTO;
import com.example.dto.RegionDTO;
import com.example.enums.Language;
import com.example.service.ArticleTypeService;
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

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody ArticleTypeDTO articleType){
        ArticleTypeDTO response = articleTypeService.add(articleType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody ArticleTypeDTO articleType,
                                    @PathVariable("id") Integer id){
        return ResponseEntity.ok(articleTypeService.update(id, articleType));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id){
        String  response = articleTypeService.delete(id);
        if(response.length()>0){
            return ResponseEntity.ok("Student Deleted");
        }
        return ResponseEntity.badRequest().body("Student not found");
    }

    @GetMapping("/all")
    public List<ArticleTypeDTO> all(){
        return articleTypeService.getAll();
    }

    @GetMapping("/lang")
    public ResponseEntity<?> getByLang(@RequestParam("lang") Language lang){
        return ResponseEntity.ok(articleTypeService.getByLang(lang));
    }













}
