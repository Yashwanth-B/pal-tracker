package test.pivotal.pal.tracker;

import io.pivotal.pal.tracker.InMemoryTimeEntryRepository;
import io.pivotal.pal.tracker.TimeEntry;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryTimeEntryRepositoryTest {
    @Test
    public void create() throws Exception {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        long projectId = 123;
        long userId = 456;
        TimeEntry createdTimeEntry = repo.create(new TimeEntry(projectId, userId, LocalDate.parse("2017-01-08"), 8));

        long timeEntryId = 1;
        TimeEntry expected = new TimeEntry(timeEntryId, projectId, userId, LocalDate.parse("2017-01-08"), 8);
        assertThat(createdTimeEntry.getId()).isEqualTo(expected.getId());

        TimeEntry readEntry = repo.find(createdTimeEntry.getId());
        assertThat(readEntry.getId()).isEqualTo(expected.getId());
    }

    @Test
    public void find() throws Exception {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        long projectId = 123;
        long userId = 456;
        repo.create(new TimeEntry(projectId, userId, LocalDate.parse("2017-01-08"), 8));

        long timeEntryId = 1;
        TimeEntry expected = new TimeEntry(timeEntryId, projectId, userId, LocalDate.parse("2017-01-08"), 8);
        TimeEntry readEntry = repo.find(timeEntryId);
        assertThat(readEntry.getId()).isEqualTo(expected.getId());
    }

    @Test
    public void find_MissingEntry() {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        long timeEntryId = 1;

        TimeEntry readEntry = repo.find(timeEntryId);
        assertThat(readEntry).isNull();
    }


    @Test
    public void update() throws Exception {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();
        TimeEntry created = repo.create(new TimeEntry(123, 456, LocalDate.parse("2017-01-08"), 8));

        TimeEntry updatedEntry = repo.update(
                created.getId(),
                new TimeEntry(321, 654, LocalDate.parse("2017-01-09"), 5));

        TimeEntry expected = new TimeEntry(created.getId(), 321, 654, LocalDate.parse("2017-01-09"), 5);
        assertThat(updatedEntry.getProjectId()).isEqualTo(expected.getProjectId());
    }

    @Test
    public void update_MissingEntry() {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        TimeEntry updatedEntry = repo.update((long) 1,new TimeEntry(321L, 654L, LocalDate.parse("2017-01-09"), 5));

        assertThat(updatedEntry).isNull();
    }

    @Test
    public void delete() throws Exception {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        long projectId = 123;
        long userId = 456;
        TimeEntry created = repo.create(new TimeEntry(projectId, userId, LocalDate.parse("2017-01-08"), 8));

        repo.delete(created.getId());
        assertThat(repo.list()).isEmpty();
    }

    @Test
    public void deleteKeepsTrackOfLatestIdProperly() {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        long projectId = 123L;
        long userId = 456L;
        TimeEntry created = repo.create(new TimeEntry(projectId, userId, LocalDate.parse("2017-01-08"), 8));

        assertThat(created.getId()).isEqualTo(1);

        repo.delete(created.getId());

        TimeEntry createdSecond = repo.create(new TimeEntry(projectId, userId, LocalDate.parse("2017-01-08"), 8));
        System.out.print(createdSecond.getId()+" id");
        assertThat(createdSecond.getId()).isEqualTo(2);
    }
}
