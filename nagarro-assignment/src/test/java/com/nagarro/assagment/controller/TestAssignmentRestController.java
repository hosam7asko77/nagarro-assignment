package com.nagarro.assagment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.nagarro.assignment.AssignmentApplication;
import com.nagarro.assignment.controller.AssignmentRestApiController;
import com.nagarro.assignment.entity.AccountEntity;
import com.nagarro.assignment.entity.StatementEntity;
import com.nagarro.assignment.model.AccountModel;
import com.nagarro.assignment.model.StatementModel;
import com.nagarro.assignment.model.request.AmountRangeRequestModel;
import com.nagarro.assignment.service.AssignmentService;
import com.nagarro.assignment.share.AssignmentUtils;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.relational.core.sql.Assignment;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes= AssignmentApplication.class)
@WebMvcTest(controllers = AssignmentRestApiController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TestAssignmentRestController {
    @Autowired
    WebApplicationContext webApplicationContext;

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    private static AccountEntity entity;
    private static AccountModel model;
    private static AmountRangeRequestModel restModel ;
    private static List<StatementEntity> statementEntities;
    private static List<StatementModel> statementModels;

    static {
        entity = new AccountEntity();
        model = new AccountModel();
        statementModels = new ArrayList<>(Arrays.asList(new StatementModel(1,new Date(),1234.0),
                new StatementModel(1,new Date(),1234.0)));
        statementEntities = new ArrayList<>(Arrays.asList(new StatementEntity(1,"19.02.2012",1234.3,1),
                new StatementEntity(1,"19.02.2012",1234.3,1)));
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


    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AssignmentService service;
    @MockBean
    private AssignmentUtils utils;
    @MockBean
    private BCryptPasswordEncoder encoder;
    @Test
    public void getStatement() throws Exception {
        when(service.getLastThreeMonth(anyInt())).thenReturn(model);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/assigment/account/" + 1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        int code = result.getResponse().getStatus();
        assertEquals(200, code);
    }
    @Test
    public void getStatementNegative() throws Exception {
        when(service.getLastThreeMonth(anyInt())).thenReturn(null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/assigment/account/ss")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        int code = result.getResponse().getStatus();
        assertNotEquals(200, code);
    }
    @Test
    public void getStatementDateByRange() throws Exception {
        String startDate="15.01.2012";
        String endDate = "15.11.2012";
        when(service.getDataByDateRange(anyInt(),anyString(),anyString())).thenReturn(model);
        when(utils.validateRange(anyInt(),anyString(),anyString())).thenReturn(null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/assigment/range/" + 1 + "?startDate="+startDate+"&endDate="+endDate)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(200, status);
    }
    @Test
    public void getStatementByRangeDateNegativeNotAccept() throws Exception {
        String startDate="15.01.2012";
        String endDate = "15.11.2012";
        when(service.getDataByDateRange(anyInt(),anyString(),anyString())).thenReturn(model);
        when(utils.validateRange(anyInt(),anyString(),anyString())).thenReturn("message");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/assigment/range/" + 1 + "?startDate="+startDate+"&endDate=")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(406, status);
    }
    @Test
    public void getStatementByRangeDateNegativeUrlNotFound() throws Exception {
        String startDate="15.01.2012";
        String endDate = "15.11.2012";
        when(service.getDataByDateRange(anyInt(),anyString(),anyString())).thenReturn(model);
        when(utils.validateRange(anyInt(),anyString(),anyString())).thenReturn(null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/assigment/range/" + 1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(400, status);
    }
    @Test
    public void getStatementByRangeOfAmount() throws Exception {
        String reqBody = mapToJson(restModel);
        when(service.getDataByAmountRange(anyInt(),anyDouble(),anyDouble())).thenReturn(model);
        when(utils.validateRange(anyInt(),any(AmountRangeRequestModel.class))).thenReturn(null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/assigment/range/" + 1)
                .content(reqBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(200, status);
    }
    @Test
    public void getStatementByRangeOfAmountNegativeNotAccept() throws Exception {
        String reqBody = mapToJson(restModel);
        when(service.getDataByAmountRange(anyInt(),anyDouble(),anyDouble())).thenReturn(model);
        when(utils.validateRange(anyInt(),any(AmountRangeRequestModel.class))).thenReturn("message");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/assigment/range/" + 1)
                .content(reqBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(406, status);
    }
    @Test
    public void getStatementByRangeOfAmountNegativeUrlNotFound() throws Exception {
        when(service.getDataByAmountRange(anyInt(),anyDouble(),anyDouble())).thenReturn(model);
        when(utils.validateRange(anyInt(),any(AmountRangeRequestModel.class))).thenReturn(null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/assigment/range/" + 1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        int status = result.getResponse().getStatus();
        assertEquals(400, status);
    }

}
