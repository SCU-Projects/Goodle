package com.app.katchup.Meeting.repository.primary;

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
    @Query(value = "{'host' : ?0, 'subject' :?1, 'startDateTime': ?2 , 'endDateTime':?3 , 'venue':?4, 'status':{$ne: 'DELETED'}}")
    Meeting findMeetingByFilter(String host, String subject, LocalDateTime startDateTime, LocalDateTime endDateTime, String venue);
}