package com.nagarro.assignment.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "statement")
@EqualsAndHashCode()
public class StatementEntity {

	@Id
	@Column(name = "ID", nullable = false)
	private Integer statementId;
	@Column(name ="datefield", nullable = false)
	private String dateField;
	@Column(name = "amount", nullable = false)
	private Double amount; 
	@Column(name = "account_id", nullable = false,insertable = false, updatable = false)
	private Integer accountId;

	public StatementEntity() {
	}

	public StatementEntity(Integer statementId, String dateField, Double amount, Integer accountId) {
		this.statementId = statementId;
		this.dateField = dateField;
		this.amount = amount;
		this.accountId = accountId;
	}
}
