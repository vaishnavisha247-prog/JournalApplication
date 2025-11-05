package com.example.journalInformation.journalInfomation.Repository;

import com.example.journalInformation.journalInfomation.Entity.JournalEntries;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalRepository extends MongoRepository<JournalEntries , ObjectId> {
}
