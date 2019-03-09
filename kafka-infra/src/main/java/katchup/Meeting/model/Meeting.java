package katchup.Meeting.model;
import katchup.Meeting.MeetingConstants;
import katchup.Users.UserConstants;
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
import java.util.ArrayList;
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

    private  String password;

    @NotNull(message = MeetingConstants.subjectNullMessage)
    @NotEmpty
    private String subject;

    @NotNull(message = MeetingConstants.dateTimeNullMessage)
    @NotEmpty
    private LocalDateTime startDateTime;

    @NotNull(message = MeetingConstants.dateTimeNullMessage)
    @NotEmpty
    private LocalDateTime endDateTime;

    @NotNull(message = MeetingConstants.inviteListNullMessage)
    private List<String> inviteList;

    private List<String> extParticipantList = new ArrayList<>();

    @NotNull(message = MeetingConstants.venueNullMessage)
    @NotEmpty
    private String venue;

    private boolean pollAllowed;

    private boolean goWithMajorityAllowed;

    private boolean externalParticipantsAllowed;

    @NotNull(message = UserConstants.userNameNullMessage)
    @Size(min = UserConstants.userNameMinLength, max = UserConstants.userNameMaxLength)
    private String host;

    private int seats = -1;

    private Status status = Status.OPEN;
}