package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {
    TimeEntryRepository timeEntryRepo;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepo=timeEntryRepository;
    }

    @PostMapping
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry timeEntry=timeEntryRepo.create(timeEntryToCreate);
        return new ResponseEntity<>(timeEntry,HttpStatus.CREATED);
    }

    @GetMapping("{nonExistentTimeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable Long nonExistentTimeEntryId) {
        if(timeEntryRepo.get(nonExistentTimeEntryId) !=null) {
            TimeEntry timeEntry = timeEntryRepo.get(nonExistentTimeEntryId);
            return new ResponseEntity<>(timeEntry, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        return new ResponseEntity<>(timeEntryRepo.list(),HttpStatus.OK);
    }

    @PutMapping("{timeEntryId}")
    public ResponseEntity<TimeEntry> update(@PathVariable Long timeEntryId,@RequestBody TimeEntry expected) {
        if(timeEntryRepo.get(timeEntryId) != null) {
            TimeEntry timeEntry=timeEntryRepo.update(timeEntryId, expected);
            return new ResponseEntity<>(timeEntry,HttpStatus.OK);
        }
        else
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{timeEntryId}")
    public ResponseEntity<TimeEntry> delete(@PathVariable Long timeEntryId) {
            timeEntryRepo.delete(timeEntryId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
