package com.nagarro.assignment.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

public class AccountModel {

	private Integer accountId;
	private String accountType;
	private String accountNumber;
	private List<StatementModel> statementModels;

	public AccountModel() {
	}

	public AccountModel(Integer accountId, String accountType, String accountNumber, List<StatementModel> statementModels) {
		this.accountId = accountId;
		this.accountType = accountType;
		this.accountNumber = accountNumber;
		this.statementModels = statementModels;
	}
}
