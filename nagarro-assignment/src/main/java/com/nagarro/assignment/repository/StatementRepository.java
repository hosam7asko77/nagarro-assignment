package com.nagarro.assignment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nagarro.assignment.entity.StatementEntity;
/**
 * this is interface for Statement Repository which help to deal with the Statment table
 * it extend JpaRepository
 * @author hosam7asko
 *
 */
@Repository
public interface StatementRepository extends JpaRepository<StatementEntity, Integer> {

	public List<StatementEntity> findByAccountId(Integer accountId);
//	@Query("FROM StatmentEntity s WHERE s.amount BETWEEN startAmount AND endAmount")
	public List<StatementEntity> findByAmountBetween(Double startAmount, Double endAmount);
}
