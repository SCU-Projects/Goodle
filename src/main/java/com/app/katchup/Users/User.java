package com.app.katchup.Users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
	@NonNull
	private String userName;
	private String fullName;
	private String phoneNumber;
	private String email;
	private String address;
}

