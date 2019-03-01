package com.app.katchup.Meeting;

import com.app.katchup.Meeting.model.Meeting;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MeetingRepository extends CrudRepository<Meeting, String> {
    @Query(value = "{'host' : ?0, 'startDateTime': ?1 , 'endDateTime':?2 , 'venue':?3}")
    Meeting findMeetingByFilter(String host, LocalDateTime startDateTime, LocalDateTime endDateTime, String venue);
}
