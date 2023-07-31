package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.EmailHistoryDTO;
import com.example.service.EmailHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/emailHistory")
public class EmailHistoryController {
    @Autowired
    private EmailHistoryService emailHistoryService;

    @GetMapping(value = "/email")
    public ResponseEntity<List<EmailHistoryDTO>> getByEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(emailHistoryService.getByEmail(email));
    }

    @GetMapping(value = "/date")
    public ResponseEntity<List<EmailHistoryDTO>> getByDate(@RequestParam("date")
                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                            LocalDate date) {
        return ResponseEntity.ok(emailHistoryService.getByDate(date));
    }

    @GetMapping(value = "/pagination")
    public ResponseEntity<?> pagination(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "size", defaultValue = "10") int size) {

        return ResponseEntity.ok(emailHistoryService.pagination(page - 1, size));
    }
}
