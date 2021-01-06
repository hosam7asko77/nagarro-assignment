package com.nagarro.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserData {

	private String userName;
	private String password;
	private String role;
}
