package katchup.Meeting.repository.primary;

import katchup.Meeting.model.Meeting;
import katchup.Meeting.model.Status;
import katchup.MeetingResponse.model.Inbox;
import katchup.MeetingResponse.model.MeetingInboxResponse;
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
