package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {
    TimeEntryRepository timeEntryRepo;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository timeEntryRepository,
                               MeterRegistry meterRegistry) {
        timeEntryRepo=timeEntryRepository;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @PostMapping
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry timeEntry=timeEntryRepo.create(timeEntryToCreate);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepo.list().size());
        return new ResponseEntity<>(timeEntry,HttpStatus.CREATED);
    }

    @GetMapping("{nonExistentTimeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable Long nonExistentTimeEntryId) {
        if(timeEntryRepo.get(nonExistentTimeEntryId) !=null) {
            TimeEntry timeEntry = timeEntryRepo.get(nonExistentTimeEntryId);
            actionCounter.increment();
            return new ResponseEntity<>(timeEntry, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        actionCounter.increment();
        return new ResponseEntity<>(timeEntryRepo.list(),HttpStatus.OK);
    }

    @PutMapping("{timeEntryId}")
    public ResponseEntity<TimeEntry> update(@PathVariable Long timeEntryId,@RequestBody TimeEntry expected) {
        if(timeEntryRepo.get(timeEntryId) != null) {
            TimeEntry timeEntry=timeEntryRepo.update(timeEntryId, expected);
            actionCounter.increment();
            return new ResponseEntity<>(timeEntry,HttpStatus.OK);
        }
        else
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{timeEntryId}")
    public ResponseEntity<TimeEntry> delete(@PathVariable Long timeEntryId) {
            timeEntryRepo.delete(timeEntryId);
            actionCounter.increment();
            timeEntrySummary.record(timeEntryRepo.list().size());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
