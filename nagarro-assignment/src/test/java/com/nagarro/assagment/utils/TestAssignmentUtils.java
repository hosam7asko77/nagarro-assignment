package com.nagarro.assagment.utils;


import com.nagarro.assignment.entity.AccountEntity;
import com.nagarro.assignment.entity.StatementEntity;
import com.nagarro.assignment.model.AccountModel;
import com.nagarro.assignment.model.StatementModel;
import com.nagarro.assignment.model.UserData;
import com.nagarro.assignment.model.request.AmountRangeRequestModel;
import com.nagarro.assignment.share.AssignmentUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestAssignmentUtils {


    private static AccountEntity entity;
    private static AccountModel model;
    private static AmountRangeRequestModel restModel ;
    private static List<StatementEntity> statementEntities;
    private static List<StatementModel> statementModels;

    static {
        entity = new AccountEntity();
        model = new AccountModel();
        statementModels = new ArrayList<>(Arrays.asList(new StatementModel(1,new Date(),224.0),
                new StatementModel(1,new Date(),124.0)));
        statementEntities = new ArrayList<>(Arrays.asList(new StatementEntity(1,"19.02.2012",224.3,1),
                new StatementEntity(1,"19.4.2012",124.3,1)));
        entity.setAccountId(1);
        entity.setAccountType("saving");
        entity.setAccountNumber(1345L);
        entity.setStatementEntities(statementEntities);
        model.setAccountId(1);
        model.setAccountType("saving");
        model.setAccountNumber("123456");
        model.setStatementModels(statementModels);
        restModel = new AmountRangeRequestModel();
        restModel.setStartAmount(100.0);
        restModel.setEndAmount(500.0);
    }


    @InjectMocks
    AssignmentUtils utils;
    @Mock
    Environment env;

//    @Test
//    public void testGenerateString() {
//        String generateString = utils.generateString(13);
//        assertNotNull(generateString);
//    }
//
//    @Test
//    public void testHasTokenExpired() {
//        when(env.getProperty("token.secret")).thenReturn("AtmoZHNusX7LlV3PR7T7xpcH5XU8Tnrd8miuTkZMVt7eIRvZ2ErFAxyuWjw8");
//        boolean expired = utils.hasTokenExpired("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkc2ZmZiIsImV4cCI6MTYwNDkxNjI5NX0.u5iwy1DwLQMxB1X9kjZ3R1tZ9Pl5Qbh4bZimpss3RA0cuigKnzWo-8hNFFs-6j5ITfgJxDqh0QtqSaMDyAG6Dw");
//        assertFalse(expired);
//    }
//
//    @Test
//    public void testGenerateToken() {
//        when(env.getProperty("token.expiaratime_hour")).thenReturn("3600000");
//        when(env.getProperty("token.secret")).thenReturn("AtmoZHNusX7LlV3PR7T7xpcH5XU8Tnrd8miuTkZMVt7eIRvZ2ErFAxyuWjw8");
//        String token = utils.generateToken("dsfff");
//        assertNotNull(token);
//    }
//
    @Test
    public void convertEntitiesToModels() {
        List<StatementModel> list = utils.convertEntitiesToModels(statementEntities);
        assertTrue(list.size() > 0);
    }
    @Test
    public void statementDataRangeOfDate() {
        List<StatementModel> list = utils.statementDataRange(statementEntities,"19.01.2012","19.03.2012");
        assertTrue(list.size() > 0);
    }
    @Test
    public void statementDataRangeAmount() {
        List<StatementModel> list = utils.statementDataRange(statementEntities, 100.0,500.5);
        assertTrue(list.size() > 0);
    }

    @Test
    public void getLastThreeMonthData() {
        List<StatementModel> list = utils.getLastThreeMonthData(statementModels);
        assertTrue(list.size() > 0);
    }
    @Test
    public void validateRange() {
        String range = utils.validateRange(1, "11.10.2012", "10.11.2012");
        assertNull(range);
    }
    @Test
    public void validateRangeNotMatchNegative() {
        String range = utils.validateRange(1, "11.1.2012", "10-11-2012");
        assertNotNull(range);
    }
    @Test
    public void validateRangeNegative() {
        String range = utils.validateRange(1, null, null);
        assertNotNull(range);
    }
    @Test
    public void validateRangeAmount() {
        String range = utils.validateRange(1, restModel);
        assertNull(range);
    }
    @Test
    public void validateRangeNotMatchAmountNegative() {
        AmountRangeRequestModel model = new AmountRangeRequestModel();
        model.setStartAmount(600.0);
        model.setEndAmount(100.0);
        String range = utils.validateRange(1,model );
        assertNotNull(range);
    }
    @Test
    public void validateRangeAmountNegative() {
        AmountRangeRequestModel model = new AmountRangeRequestModel();
        model.setStartAmount(null);
        model.setEndAmount(100.0);
        String range = utils.validateRange(1,model);
        assertNotNull(range);
    }

    @Test
    public void getUserdata(){
        List<UserData> dataList=new ArrayList<>(Arrays.asList(new UserData("hosam","dgdhg","ADMIN"),
                new UserData("hasko","user","USER")));
        UserData userdata = utils.getUserdata(dataList, "hosam");
        assertNotNull(userdata);
    }
    @Test
    public void getUserdataNegative(){
        List<UserData> dataList=new ArrayList<>(Arrays.asList(new UserData("hosam","dgdhg","ADMIN"),
                new UserData("hasko","user","USER")));
        UserData userdata = utils.getUserdata(dataList, "mohamed");
        assertNull(userdata);
    }


//
//    @Test
//    public void testConvertDtoRestModel() {
//        List<CandidateDto> dtoList = new ArrayList<>(Arrays.asList(dto));
//        List<CandidateRestModel> restModels = utils.convertDtoRestModel(dtoList);
//        System.out.println(restModels);
//        assertTrue(restModels.size() > 0);
//    }
}
