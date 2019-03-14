package katchup.Users;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	@NotNull(message = UserConstants.userNameNullMessage)
	@Size(min = UserConstants.userNameMinLength, max = UserConstants.userNameMaxLength)
	private String userName;
	private String fullName;
	private String phoneNumber;
	private String email;
	private String address;
	@NotNull(message = UserConstants.passwordNullMessage)
	@Size(min = UserConstants.passwordMinLength, max = UserConstants.passwordMaxLength)
	private String password;
}

