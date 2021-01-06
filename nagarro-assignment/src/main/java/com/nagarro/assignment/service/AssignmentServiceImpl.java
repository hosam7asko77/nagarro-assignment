package com.nagarro.assignment.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.nagarro.assignment.controller.AssignmentRestApiController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nagarro.assignment.config.exception.AssignmentNotFoundApiException;
import com.nagarro.assignment.entity.AccountEntity;
import com.nagarro.assignment.model.AccountModel;
import com.nagarro.assignment.model.StatementModel;
import com.nagarro.assignment.model.UserData;
import com.nagarro.assignment.repository.AccountRepository;
import com.nagarro.assignment.share.AssignmentUtils;

/**
 * @author hosam7asko
 */
@Service
public class AssignmentServiceImpl implements AssignmentService {

    private AssignmentUtils utils;
    private AccountRepository accountRepo;
    private BCryptPasswordEncoder passwordEncoder;
    /**
     * Enable logging for the class
     */
    private static final Logger log = LoggerFactory.getLogger(AssignmentServiceImpl.class);

    /**
     * inject AccountRepository, AssignmentUtils and BCryptPasswordEncoder to the class using constructor
     */
    @Autowired
    public AssignmentServiceImpl(AssignmentUtils utils, AccountRepository accountRepo, BCryptPasswordEncoder passwordEncoder) {
        this.utils = utils;
        this.accountRepo = accountRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * method to get data from data base using range ond account id
     *
     * @param accountId
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    @Override
    public AccountModel getDataByDateRange(Integer accountId, String startDate, String endDate)
            throws Exception {
        log.info("AssignmentServiceImpl class getDataByDateRange() method start");
        Optional<AccountEntity> entity = accountRepo.findById(accountId);
        if (!entity.isPresent()) {
            log.info("AssignmentServiceImpl class getDataByDateRange() method not found");
            throw new AssignmentNotFoundApiException("Account Not Found");
        } else {
            List<StatementModel> range = utils.statementDataRange(entity.get().getStatementEntities(), startDate, endDate);
            if (range.size() <= 0) {
                log.info("AssignmentServiceImpl class getDataByDateRange() method range not exist ");
                throw new AssignmentNotFoundApiException("No data found for given range of date");
            }
            AccountModel model = new AccountModel();
            BeanUtils.copyProperties(entity.get(), model);
            String encryptAccountNumber = utils.encryptAccountNumber(entity.get().getAccountId().toString());
            model.setAccountNumber(encryptAccountNumber);
            model.setStatementModels(range);
            log.info("AssignmentServiceImpl class getDataByDateRange() method end");
            return model;
        }

    }

    /**
     * @param accountId
     * @param startAmount
     * @param endAmount
     * @return
     * @throws Exception
     */
    @Override
    public AccountModel getDataByAmountRange(Integer accountId, Double startAmount, Double endAmount)
            throws Exception {
        log.info("AssignmentServiceImpl class getDataByAmountRange() method start");
        Optional<AccountEntity> entity = accountRepo.findById(accountId);
        if (!entity.isPresent()) {
            log.info("AssignmentServiceImpl class getDataByAmountRange() method not exist");
            throw new AssignmentNotFoundApiException("Account Not Found");
        } else {
            List<StatementModel> range = utils.statementDataRange(entity.get().getStatementEntities(), startAmount, endAmount);
            if (range.size() <= 0) {
                log.info("AssignmentServiceImpl class getDataByAmountRange() method range is not exist");
                throw new AssignmentNotFoundApiException("No data found for given range of Amount");
            }

            AccountModel model = new AccountModel();
            BeanUtils.copyProperties(entity.get(), model);
            String encryptAccountNumber = utils.encryptAccountNumber(entity.get().getAccountId().toString());
            model.setAccountNumber(encryptAccountNumber);
            model.setStatementModels(range);
            log.info("AssignmentServiceImpl class getDataByAmountRange() method end");
            return model;
        }
    }

    /**
     * this method using to retrieve last three month for the data base
     * @param accountId
     * @return
     * @throws Exception
     */
    @Override
    public AccountModel getLastThreeMonth(Integer accountId) throws Exception {
        log.info("AssignmentServiceImpl class getLastThreeMonth() method start");
        Optional<AccountEntity> entity = accountRepo.findById(accountId);
        if (!entity.isPresent()) {
            log.info("AssignmentServiceImpl class getLastThreeMonth() method account not exist");
            throw new AssignmentNotFoundApiException("Account Not Found");
        } else {
            List<StatementModel> list = utils.convertEntitiesToModels(entity.get().getStatementEntities());
            List<StatementModel> threeMonthData = utils.getLastThreeMonthData(list);
            if (threeMonthData.size() <= 0) {
                log.info("AssignmentServiceImpl class getLastThreeMonth() method no statement");
                throw new AssignmentNotFoundApiException("Now Transaction found for last three month");
            }
            AccountModel model = new AccountModel();
            BeanUtils.copyProperties(entity.get(), model);
            String encryptAccountNumber = utils.encryptAccountNumber(entity.get().getAccountId().toString());
            model.setAccountNumber(encryptAccountNumber);
            model.setStatementModels(threeMonthData);
            log.info("AssignmentServiceImpl class getLastThreeMonth() method end");
            return model;
        }


    }

    /**
     * this method using to get get user data that using to login
     * @param username
     * @return
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("AssignmentServiceImpl class loadUserByUsername() method start");
        List<UserData> usersData = new ArrayList<>(Arrays.asList(new UserData("testadmin", "adminpassword", "ADMIN"),
                new UserData("testUser", "userpassword", "USER")));
        UserData userData = utils.getUserdata(usersData, username);
        if (userData == null) {
            log.info("AssignmentServiceImpl class loadUserByUsername() method user not found");
            throw new UsernameNotFoundException("This user " + username + " not found");
        } else {
            String password = passwordEncoder.encode(userData.getPassword());
            UserBuilder builder = User.withUsername(userData.getUserName()).password(password)
                    .roles(userData.getRole());
            log.info("AssignmentServiceImpl class loadUserByUsername() method end");
            return builder.build();
        }
    }


}
