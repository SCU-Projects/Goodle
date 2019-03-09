package katchup.Sharding;

import katchup.Exception.UnAuthorizedException;
import katchup.Sharding.model.Sharding;
import katchup.Sharding.repository.node0.ShardingNode0Repository;
import katchup.Sharding.repository.node1.ShardingNode1Repository;
import katchup.Sharding.repository.node2.ShardingNode2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ShardingService {

    @Autowired
    ShardingNode0Repository shardingNode0Repository;

    @Autowired
    ShardingNode1Repository shardingNode1Repository;

    @Autowired
    ShardingNode2Repository shardingNode2Repository;

    public Map<Integer, List<String>> getDbIdMeetingIdMap(String userName, List<String> meetingIdsList){
        int dbID = Utilities.getShardedDBLocation(userName).ordinal();
        return retrieveAllFromTable(dbID, meetingIdsList);
    }

    private  Map<Integer, List<String>> retrieveAllFromTable(Integer databaseId, List<String> meetingIdList) {
        //find the parent table of the meetingIDs
        Map<Integer, List<String>> databaseIdmeetingIdMap = new HashMap<>();
        List<Sharding> result = new ArrayList<>();
        switch (databaseId){
            case 0:
                result =  shardingNode0Repository.findAllByMeetingIds(meetingIdList);
                break;
            case 1:
                result = shardingNode1Repository.findAllByMeetingIds(meetingIdList);
                break;
            case 2:
                result = shardingNode2Repository.findAllByMeetingIds(meetingIdList);
                break;
        }
        for(int i = 0; i < 3; i++){
            int dbId = i;
            List<String> dbMeetingIds = result.stream()
                                            .filter(response -> response.getDatabaseId() == dbId)
                                            .map(Sharding::getMeetingId)
                                            .collect(Collectors.toList());
            databaseIdmeetingIdMap.put(i, dbMeetingIds);
        }
        return databaseIdmeetingIdMap;
    }

    private  int retrieveDbIdForMeetingIdFromTable(String userName, String meetingId) {
        int databaseId = Utilities.getShardedDBLocation(userName).ordinal();
        return getMeetingDbLocationForUser(databaseId, meetingId);
    }

    public int getMeetingDbLocationForUser(int databaseId, String meetingId) {
        Sharding result = new Sharding();
        switch (databaseId){
            case 0:
                result =  shardingNode0Repository.findByMeetingId(meetingId);
                break;
            case 1:
                result = shardingNode1Repository.findByMeetingId(meetingId);
                break;
            case 2:
                result = shardingNode2Repository.findByMeetingId(meetingId);
                break;
        }
        if(result == null)
            throw new UnAuthorizedException("Sorry! User is not allowed to access this meeting or Meeting id does not exist");
        return result.getDatabaseId();
    }

    public Sharding createLookUpForMeetingId(Integer targetDatabaseId, Integer databaseId, String meetingId) throws Exception {
        return saveToTable(targetDatabaseId, databaseId, meetingId);
    }

    private Sharding saveToTable(Integer targetDatabaseId, Integer databaseId, String meetingId) throws Exception {
        Sharding record = new Sharding();
        record.setDatabaseId(databaseId);
        record.setMeetingId(meetingId);
        switch (targetDatabaseId){
            case 0:
                return shardingNode0Repository.save(record);
            case 1:
                return shardingNode1Repository.save(record);
            case 2:
                return shardingNode2Repository.save(record);
        }
        throw new Exception("Error saving record to the table");
    }
}
