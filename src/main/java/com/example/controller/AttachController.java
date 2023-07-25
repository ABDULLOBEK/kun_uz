package com.example.controller;

import com.example.dto.AttachDTO;
import com.example.service.AttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/attach")
public class AttachController {
    @Autowired
    private AttachService attachService;

    @PostMapping("/upload")
    public ResponseEntity<AttachDTO> upload(@RequestParam("file") MultipartFile file) {
        AttachDTO fileName = attachService.save(file);
        return ResponseEntity.ok(fileName);
    }

    @GetMapping(value = "/open/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] open(@PathVariable("fileName") String fileName) {
        return this.attachService.loadImage(fileName);
    }

    @GetMapping(value = "/open/{id}/img", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] openImageById(@PathVariable("id") String id) {
        return attachService.loadImageById(id);
    }

    @GetMapping(value = "/open/{id}/general", produces = MediaType.ALL_VALUE)
    public byte[] openByIdGeneral(@PathVariable("id") String id) {
        return attachService.loadByIdGeneral(id);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id){
        ResponseEntity<?> response = attachService.delete(id);

        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok("Image with ID " + id + " has been deleted successfully.");
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete the image with ID " + id + ".");
        }
    }
}


