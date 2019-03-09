package katchup.Meeting.repository.node2;

import katchup.Meeting.model.Meeting;
import katchup.MeetingResponse.model.Decision;
import katchup.MeetingResponse.model.MeetingID;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MeetingNode2Repository extends CrudRepository<Meeting,String> {
    @Query(value = "{'host' : ?0, 'subject' :?1, 'startDateTime': ?2 , 'endDateTime':?3 , 'venue':?4, 'status':{$ne: 'DELETED'}}")
    Meeting findMeetingByFilter(String host, String subject, LocalDateTime startDateTime, LocalDateTime endDateTime, String venue);
}
