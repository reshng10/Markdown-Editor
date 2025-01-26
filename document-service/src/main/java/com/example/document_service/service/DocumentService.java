package com.example.document_service.service;

import com.example.document_service.document.DocumentModel;
import com.example.document_service.dto.DocumentDto;
import com.example.document_service.exception.UserNotAllowedException;
import com.example.document_service.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;


    public void createDocument(DocumentDto documentDto){
        DocumentModel documentModel = DocumentModel.builder()
                .title(documentDto.getTitle())
                .content(documentDto.getContent())
                .userId(documentDto.getUserId())
                .available(documentDto.getAvailable())
                .build();
        documentRepository.save(documentModel);
    }

    public void updateDocument(String userId, DocumentDto documentDto) throws UserNotAllowedException {
       Optional<DocumentModel> documentModel = documentRepository.findById(documentDto.getDocumentDtoId());

       if (documentModel.isPresent()){
           if(documentModel.get().getUserId().equals(userId)){
              documentModel.get().setTitle(documentDto.getTitle());
              documentModel.get().setContent(documentDto.getContent());
              documentModel.get().setAvailable(documentDto.getAvailable());

              documentRepository.save(documentModel.get());
           }
           else {
               throw new UserNotAllowedException("You are not allowed to modify this document");
           }
       }
    }


    public List<DocumentDto> fetchAllDocs(){
      List<DocumentModel> documentModels =  documentRepository.findAll();

        return documentModels
              .stream()
              .map(documentModel ->
                      DocumentDto.builder()
                              .userId(documentModel.getUserId())
                              .title(documentModel.getTitle())
                              .content(documentModel.getContent())
                              .available(documentModel.getAvailable())
                              .build())
                .toList();
    }

    public List<DocumentDto> fetchTopRecentDocs(){

    Page<DocumentModel> recentDocuments = documentRepository
            .findByAvailable(true,
                    PageRequest.of(0,10, Sort.by(Sort.Direction.DESC,"updatedAt")));


return recentDocuments
        .stream()
        .map(documentModel -> DocumentDto.builder()
                .documentDtoId(documentModel.getId())
                .title(documentModel.getTitle())
                .available(documentModel.getAvailable())
                .content(documentModel.getContent())
                .userId(documentModel.getUserId())
                .build())
        .toList();
    }

    public void deleteDocument(String documentDtoId, String userId) throws UserNotAllowedException {
        Optional<DocumentModel> optional = documentRepository.findById(documentDtoId);
        if (optional.isPresent()) {
            DocumentModel existing = optional.get();
            if (existing.getUserId().equals(userId)) {
                documentRepository.deleteById(documentDtoId);
            } else {
                throw new UserNotAllowedException("you do not have permission to delete this document!");
            }
        }
    }
}
