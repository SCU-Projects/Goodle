package com.app.katchup.Meeting.model;
import com.app.katchup.MeetingResponse.model.Decision;
import com.app.katchup.Users.UserConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Meeting")
 public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String meetingId;
    
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  String password;
    
    @NotNull(message = MeetingConstants.dateTimeNullMessage)
    @NotEmpty
    private LocalDateTime startDateTime;
    
    @NotNull(message = MeetingConstants.dateTimeNullMessage)
    @NotEmpty
    private LocalDateTime endDateTime;
    
    @NotNull(message = MeetingConstants.inviteListNullMessage)
    List<String> inviteList;
    
    @NotNull(message = MeetingConstants.venueNullMessage)
    @NotEmpty
    String venue;
    
    @NotNull(message = MeetingConstants.responseNullMessage)
    @NotEmpty
    private Decision response;
  
   private boolean isPollAllowed;
    
    @NotNull(message = UserConstants.userNameNullMessage)
    @Size(min = UserConstants.userNameMinLength, max = UserConstants.userNameMaxLength)
    private String host;
       
    int seats;
    
    @NotEmpty
    private Status status;
}
