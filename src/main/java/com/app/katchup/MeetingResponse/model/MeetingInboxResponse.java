package com.app.katchup.MeetingResponse.model;

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

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "MeetingInboxResponse")
public class MeetingInboxResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String responseId;
    @NotNull(message = UserConstants.userNameNullMessage)
    @Size(min = UserConstants.userNameMinLength, max = UserConstants.userNameMaxLength)
    private String userName;
    @NotEmpty
    private String meetingId;
    @NotEmpty
    private Decision decision;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

}

