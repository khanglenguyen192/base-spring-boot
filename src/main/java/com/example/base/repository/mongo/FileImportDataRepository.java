package com.example.base.repository.mongo;

import com.example.base.entities.mongo.FileImportData;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileImportDataRepository extends MongoRepository<FileImportData, ObjectId> {
    public FileImportData findByFilePath(String filePath);
}
