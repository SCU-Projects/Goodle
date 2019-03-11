package springbootkafkaconsumerexample.repository.node2;

import katchup.MeetingResponse.model.MeetingInboxResponse;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Node2Repository extends CrudRepository<MeetingInboxResponse,String> {

}
