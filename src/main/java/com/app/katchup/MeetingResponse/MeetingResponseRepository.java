package com.app.katchup.MeetingResponse;
import com.app.katchup.MeetingResponse.model.Inbox;
import com.app.katchup.MeetingResponse.model.MeetingInboxResponse;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingResponseRepository extends CrudRepository<MeetingInboxResponse, String>{
    @Query(value = "{'userName' : ?0}", fields = "{'meetingId':1}")
    List<Inbox> findInboxByUserName(String userName);

    @Query(value = "{'userName' : ?0, 'meetingId' : ?1 }")
    MeetingInboxResponse findbyUserNameAndMeetingID(String userName, String meetingId);
}

