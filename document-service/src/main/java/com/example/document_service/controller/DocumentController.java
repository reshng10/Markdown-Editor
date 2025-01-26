package com.example.document_service.controller;


import com.example.document_service.dto.DocumentDto;
import com.example.document_service.exception.UserNotAllowedException;
import com.example.document_service.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/docs")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;


    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> createDoc(@RequestBody DocumentDto documentDto){
        documentService.createDocument(documentDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> updateDoc(String userId, @RequestBody DocumentDto documentDto) throws UserNotAllowedException {
        documentService.updateDocument(userId, documentDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('USER','ADMIN', 'ANONYMOUS')")
    public List<DocumentDto> fetchRecentDocuments(){
        return documentService.fetchTopRecentDocs(); 
    }

    @DeleteMapping("/delete/{documentDtoId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public void deleteDocument(String userId, @PathVariable String documentDtoId) throws UserNotAllowedException {
        documentService.deleteDocument(documentDtoId,userId);
    }

}
