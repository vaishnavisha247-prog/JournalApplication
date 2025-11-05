package com.example.journalInformation.journalInfomation.Service;

import com.example.journalInformation.journalInfomation.Entity.JournalEntries;
import com.example.journalInformation.journalInfomation.Entity.User;
import com.example.journalInformation.journalInfomation.Repository.JournalRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class JournalEntriesService {
  @Autowired
  private JournalRepository journalRepository;

  @Autowired
    private UserService userService;
    @Transactional
    public void saveEntry(JournalEntries journalEntry, String userName) {
        try {
            User user = userService.findBYUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntries saved = journalRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveEntry(user);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while saving the entry.", e);
        }

    }

    public List<JournalEntries> getAll() {

        return journalRepository.findAll();
    }

    public Optional<JournalEntries> findById(ObjectId id) {

        return journalRepository.findById(id);
    }

    public boolean deleteById(ObjectId id, String userName) {
        boolean removed = false;
        try {


            User user = userService.findBYUserName(userName);
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if (removed) {


                userService.saveEntry(user);

                journalRepository.deleteById(id);
            }
        } catch (Exception e) {

           log.error("m",e);

            throw new RuntimeException("An error occurred while deleting the entry. ", e);
        }
        return removed;
    }

    public boolean updateEntry(String userName, ObjectId myId, JournalEntries updateEntry) {
        User user = userService.findBYUserName(userName);
        if(user == null)
            return false;
        List<JournalEntries> journalEntriesList = user.getJournalEntries();
        for (JournalEntries entry:journalEntriesList){
           if( entry.getId().equals(myId)){
               entry.setTitle(updateEntry.getTitle());
               entry.setContent(updateEntry.getContent());
               entry.setDate(updateEntry.getDate());
               journalRepository.save(entry);
               userService.saveEntry(user);
               return true;
           }
        }
        return false;
    }
}
