package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTimeEntryRepository implements TimeEntryRepository{

    private HashMap<Long, TimeEntry> timeEntries = new HashMap<>();
    Long id=(long)timeEntries.size();

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        timeEntry.setId(++id);
        timeEntries.put(timeEntry.getId(), timeEntry);
        return timeEntry;
    }

    @Override
    public TimeEntry get(Long id) {
        return timeEntries.get(id);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(timeEntries.values());
    }

    @Override
    public TimeEntry update(Long id, TimeEntry timeEntry) {
        if(timeEntries.get(id) != null) {
            timeEntries.replace(id, timeEntry);
            timeEntry.setId(id);
            return timeEntry;
        }
        else
            return null;
    }

    @Override
    public void delete(Long id) {
       timeEntries.remove(id);
    }

    public TimeEntry find(long timeEntryId) {
        return timeEntries.get(timeEntryId);
    }
}
