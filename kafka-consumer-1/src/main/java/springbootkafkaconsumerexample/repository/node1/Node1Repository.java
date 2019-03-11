package springbootkafkaconsumerexample.repository.node1;

import katchup.MeetingResponse.model.MeetingInboxResponse;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Node1Repository extends CrudRepository<MeetingInboxResponse,String> {

}
