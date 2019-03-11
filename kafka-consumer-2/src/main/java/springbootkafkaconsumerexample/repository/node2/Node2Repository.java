package springbootkafkaconsumerexample.repository.node2;

import katchup.MeetingResponse.model.MeetingInboxResponse;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Node2Repository extends CrudRepository<MeetingInboxResponse,String> {
    @Query(value = "{'meetingId' : ?0}")
    List<MeetingInboxResponse> findAllbyMeetingID(String meetingId);
}
