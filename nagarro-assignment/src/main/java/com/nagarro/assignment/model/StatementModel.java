package com.nagarro.assignment.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class StatementModel {
	private Integer statementId;
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
	private Date dateField;
	private Double amount;

	public StatementModel() {
	}

	public StatementModel(Integer statementId, Date dateField, Double amount) {
		this.statementId = statementId;
		this.dateField = dateField;
		this.amount = amount;
	}
}
