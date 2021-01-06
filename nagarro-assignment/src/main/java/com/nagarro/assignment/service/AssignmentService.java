package com.nagarro.assignment.service;
/**
 *
 * @author hosam7asko
 *
 */

import java.text.ParseException;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.nagarro.assignment.config.exception.AssignmentNotFoundApiException;
import com.nagarro.assignment.model.AccountModel;
import com.nagarro.assignment.model.StatementModel;

public interface AssignmentService extends UserDetailsService {
	/**
	 * This method using to get data based on specific start and end date
	 * @param accountId
	 * @param startDate
	 * @param endDate
	 * @return AccountModel
	 * @throws AssignmentNotFoundApiException
	 * @throws Exception
	 */
	 AccountModel getDataByDateRange(Integer accountId,String startDate , String endDate)throws AssignmentNotFoundApiException, Exception;
	/**
	 * This method using to get data based on specific range of amount 
	 * @param accountId
	 * @param startAmount
	 * @param endAmount
	 * @return AccountModel
	 * @throws AssignmentNotFoundApiException
	 * @throws Exception 
	 */
	 AccountModel getDataByAmountRange(Integer accountId, Double startAmount, Double endAmount)throws AssignmentNotFoundApiException, Exception;
	/**
	 * This method using to get last three month data for specific  account
	 * @param accountId
	 * @return AccountModel
	 * @throws AssignmentNotFoundApiException
	 * @throws Exception 
	 * @throws ParseException 
	 */
	 AccountModel getLastThreeMonth(Integer accountId)throws AssignmentNotFoundApiException, Exception;
}
