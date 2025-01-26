package com.example.document_service.repository;

import com.example.document_service.document.DocumentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends MongoRepository<DocumentModel, String> {
      Page<DocumentModel> findByAvailable(boolean b, PageRequest updatedAt);
}