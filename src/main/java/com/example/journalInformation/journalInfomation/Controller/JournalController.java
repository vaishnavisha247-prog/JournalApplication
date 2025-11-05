package com.example.journalInformation.journalInfomation.Controller;

import com.example.journalInformation.journalInfomation.Entity.JournalEntries;
import com.example.journalInformation.journalInfomation.Entity.User;
import com.example.journalInformation.journalInfomation.Service.JournalEntriesService;
import com.example.journalInformation.journalInfomation.Service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal_entries")
public class JournalController {

    @Autowired
    private UserService userService;
    @Autowired
    private JournalEntriesService journalEntriesService;


    @GetMapping("/all")
    public ResponseEntity<?> getAllJournalEntries() {
        List<JournalEntries> entries = journalEntriesService.getAll();

        if (entries == null || entries.isEmpty()) {
            return new ResponseEntity<>("No journal entries found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(entries, HttpStatus.OK);
    }


    @GetMapping("/{userName}")
    public ResponseEntity<?> getAllJournalEntriesService(@PathVariable String userName) {
        User user = userService.findBYUserName(userName);
        if(user == null){
            return new ResponseEntity<>("User not found",HttpStatus.NO_CONTENT);
        }
        List<JournalEntries> all = user.getJournalEntries();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

        @PostMapping("/userName")
        public ResponseEntity<JournalEntries> createEntry(@RequestBody JournalEntries myEntry,@PathVariable String userName) {

            try {
                journalEntriesService.saveEntry(myEntry,userName);
                return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        @GetMapping("/id/{myId}")
        public ResponseEntity<JournalEntries> getJournalEntryById(@PathVariable ObjectId myId) {
            Optional<JournalEntries> journalEntry =journalEntriesService.findById(myId);
            return journalEntry.map(entry -> new ResponseEntity<>(entry, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));


        }


        @DeleteMapping("/id/{userName}/{myId}")
        public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId ,@PathVariable String userName ) {
            journalEntriesService.deleteById(myId,userName);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }

        @PutMapping("/{userName}/{myId}")
        public ResponseEntity<?> updateJournalEntry(@PathVariable String userName,
                                                    @PathVariable ObjectId myId,
                                                    @RequestBody JournalEntries updateEntry
                                                    ){

        try{
            boolean isUpdate = journalEntriesService.updateEntry(userName,myId,updateEntry);
            if(isUpdate){
                return new ResponseEntity<>("Journal Entry updated Successfully ",HttpStatus.OK);
            }else {
                return new ResponseEntity<>("Entry or User not found ",HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            return  new ResponseEntity<>("Error: " + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        }


    }

