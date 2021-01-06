package com.nagarro.assignment.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "account")
@EqualsAndHashCode()
public class AccountEntity {

	@Id
	@Column(name = "ID" , nullable = false)
	private Integer accountId;
	@Column(name = "account_type" , nullable = false)
	@JsonProperty("account_type")
	private String accountType;
	@JsonProperty("account_number")
	@Column(name = "account_number" , nullable = false)
	private Long accountNumber;
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id", nullable = false)
	private List<StatementEntity> statementEntities;
	
}
