package com.app.katchup.Meeting;

import com.app.katchup.Meeting.model.Meeting;
import com.app.katchup.Meeting.model.Status;
import com.app.katchup.MeetingResponse.model.Inbox;
import com.app.katchup.MeetingResponse.model.MeetingInboxResponse;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MeetingRepository extends CrudRepository<Meeting,String> {
    @Query(value = "{'host' : ?0, 'startDateTime': ?1 , 'endDateTime':?2 , 'venue':?3, 'status':{$ne: 'DELETED'}}")
    Meeting findMeetingByFilter(String host, LocalDateTime startDateTime, LocalDateTime endDateTime, String venue);
}
