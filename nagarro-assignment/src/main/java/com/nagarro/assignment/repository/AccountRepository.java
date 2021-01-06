package com.nagarro.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagarro.assignment.entity.AccountEntity;
import org.springframework.stereotype.Repository;
/**
 * this is interface for Account Repository which help to deal with the Account table
 * it extend JpaRepository
 * @author hosam7asko
 *
 */
@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {

	
}
