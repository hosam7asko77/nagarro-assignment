package com.nagarro.assagment.service;


import com.nagarro.assignment.config.exception.AssignmentNotFoundApiException;
import com.nagarro.assignment.entity.AccountEntity;
import com.nagarro.assignment.entity.StatementEntity;
import com.nagarro.assignment.model.AccountModel;
import com.nagarro.assignment.model.StatementModel;
import com.nagarro.assignment.model.UserData;
import com.nagarro.assignment.repository.AccountRepository;
import com.nagarro.assignment.repository.StatementRepository;
import com.nagarro.assignment.service.AssignmentService;
import com.nagarro.assignment.service.AssignmentServiceImpl;
import com.nagarro.assignment.share.AssignmentUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest()
@RunWith(MockitoJUnitRunner.class)
public class TestAssignmentService {

    private static AccountEntity entity;
    private static AccountModel model;
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
    }

    @InjectMocks
    private AssignmentServiceImpl service;
    @Mock
    private AssignmentUtils utils;
    @Mock
    private AccountRepository repository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Test
    public void getLastThreeMonth() throws Exception {
        when(repository.findById(anyInt())).thenReturn(Optional.of(entity));
        when(utils.convertEntitiesToModels(anyList())).thenReturn(statementModels);
        when(utils.getLastThreeMonthData(anyList())).thenReturn(statementModels);
        when(utils.encryptAccountNumber(anyString())).thenReturn("sdfghjdjkdhjdjkj");
        AccountModel month = service.getLastThreeMonth(1);
        assertNotNull(month);
    }

    @Test
    public void getLastThreeMonthNegative() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());
        AssignmentNotFoundApiException apiException = assertThrows(AssignmentNotFoundApiException.class, () -> {
            service.getLastThreeMonth(1);
        });
        boolean exist = apiException.getMessage().contains("Account Not Found");
        assertTrue(exist);
    }
    @Test
    public void getLastThreeMonthNotExistNegative() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(entity));
        when(utils.convertEntitiesToModels(anyList())).thenReturn(statementModels);
        List<StatementModel> statementModels=new ArrayList<>();
        when(utils.getLastThreeMonthData(anyList())).thenReturn(statementModels);
        AssignmentNotFoundApiException apiException = assertThrows(AssignmentNotFoundApiException.class, () -> {
            service.getLastThreeMonth(1);
        });
        boolean exist = apiException.getMessage().contains("Now Transaction found for last three month");
        assertTrue(exist);
    }

    @Test
    public void getDataByDateRange() throws Exception {
        when(repository.findById(anyInt())).thenReturn(Optional.of(entity));
        when(utils.statementDataRange(anyList(),anyString(),anyString())).thenReturn(statementModels);
        when(utils.encryptAccountNumber(anyString())).thenReturn("sdfghjdjkdhjdjkj");
        AccountModel range = service.getDataByDateRange(1, "10,10,2010", "10,11,2012");
        assertNotNull(range);
    }

    @Test
    public void getDataByDateRangedNegative() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());
        AssignmentNotFoundApiException exception = assertThrows(AssignmentNotFoundApiException.class, () -> {
            service.getDataByDateRange(1, "10,10,2010", "10,11,2012");
        });
        boolean res = exception.getMessage().contains("Account Not Found");
        assertTrue(res);
    }
    @Test
    public void getDataByDateRangedNotExistNegative() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(entity));
        List<StatementModel> statementModels=new ArrayList<>();
        when(utils.statementDataRange(anyList(),anyString(),anyString())).thenReturn(statementModels);
        AssignmentNotFoundApiException exception = assertThrows(AssignmentNotFoundApiException.class, () -> {
            service.getDataByDateRange(1, "10,10,2010", "10,11,2012");
        });
        boolean res = exception.getMessage().contains("No data found for given range of date");
        assertTrue(res);
    }

    @Test
    public void getDataByAmountRange() throws Exception {
        when(repository.findById(anyInt())).thenReturn(Optional.of(entity));
        when(utils.statementDataRange(anyList(),anyDouble(),anyDouble())).thenReturn(statementModels);
        when(utils.encryptAccountNumber(anyString())).thenReturn("sdfghjdjkdhjdjkj");
        AccountModel range = service.getDataByAmountRange(1, 200.0,500.0);
        assertNotNull(range);
    }

    @Test
    public void getDataByAmountRangeNegative() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());
        AssignmentNotFoundApiException exception = assertThrows(AssignmentNotFoundApiException.class, () -> {
            service.getDataByAmountRange(1, 200.0,500.0);
        });
        boolean res = exception.getMessage().contains("Account Not Found");
        assertTrue(res);
    }
    @Test
    public void getDataByAmountRangeNotExistNegative() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(entity));
        List<StatementModel> statementModels=new ArrayList<>();
        when(utils.statementDataRange(anyList(),anyDouble(),anyDouble())).thenReturn(statementModels);
        AssignmentNotFoundApiException exception = assertThrows(AssignmentNotFoundApiException.class, () -> {
            service.getDataByAmountRange(1, 200.0,500.0);
        });
        boolean res = exception.getMessage().contains("No data found for given range of Amount");
        assertTrue(res);
    }


    @Test
    public void testLoadUserByUsername() {
        UserData userData1 = new UserData("testadmin", "adminpassword", "ADMIN");
        when(utils.getUserdata(anyList(),anyString())).thenReturn(userData1);
        when(passwordEncoder.encode(anyString())).thenReturn("dhgddfggfd");
        UserDetails details = service.loadUserByUsername("adminpassword");
        assertNotNull(details);
    }

    @Test
    public void testLoadUserByUsernameNegative() {
        String username = "adminpassword";
        when(utils.getUserdata(anyList(),anyString())).thenReturn(null);
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername(username);
        });
        boolean contains = exception.getMessage().contains("This user " + username + " not found");
        assertTrue(contains);
    }
//
//    @Test
//    public void testGetCandidateByEmail() {
//        when(repository.findByEmail(anyString())).thenReturn(entity);
//        CandidateDto dto = service.getCandidateByEmail("hsam7asko1993@gmail.com");
//        assertNotNull(dto);
//    }
//
//    @Test
//    public void testGetCandidateByEmailNegative() {
//        when(repository.findByEmail(anyString())).thenReturn(null);
//        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
//            service.getCandidateByEmail("hosam7asko1993@gmail.com");
//        });
//        boolean contains = exception.getMessage().contains("no candidate found with this");
//        assertTrue(contains);
//    }
//
//    @Test
//    public void testUpdateCandidate() throws CandidateApiException {
//        when(repository.findByPublicId(Mockito.anyString())).thenReturn(entity);
//        when(encoder.encode(anyString())).thenReturn("dgfsgsghdhgdghdgh");
//        when(repository.save(any(CandidateEntity.class))).thenReturn(entity);
//        CandidateDto candidateDto = service.updateCandidate(dto);
//        assertNotNull(candidateDto);
//    }
//
//    @Test
//    public void testUpdateCandidateNegativeNotExist() {
//        when(repository.findByPublicId(Mockito.anyString())).thenReturn(null);
//        assertThrows(CandidateApiException.class, () -> {
//            service.updateCandidate(dto);
//        });
//    }
//
//    @Test
//    public void testUpdateCandidateNegative() {
//        when(repository.findByPublicId(Mockito.anyString())).thenReturn(entity);
//        when(encoder.encode(anyString())).thenReturn("dgfsgsghdhgdghdgh");
//        when(repository.save(any(CandidateEntity.class))).thenReturn(null);
//        assertThrows(CandidateApiException.class, () -> {
//            service.updateCandidate(dto);
//        });
//    }
//
//    @Test
//    public void testApprovalCandidate() {
//        when(repository.findByPublicId(anyString())).thenReturn(entity);
//        when(repository.save(any(CandidateEntity.class))).thenReturn(entity);
//        boolean approval = service.approvalCandidate("sskdahj1hhd");
//        assertTrue(approval);
//    }
//
//    @Test
//    public void testApprovalCandidateNegative() {
//        when(repository.findByPublicId(anyString())).thenReturn(entity);
//        when(repository.save(any(CandidateEntity.class))).thenReturn(null);
//        boolean approval = service.approvalCandidate("sskdahj1hhd");
//        assertFalse(approval);
//    }
//
//    @Test
//    public void testVerifyEmail() {
//        when(repository.findByVerificatonToken(anyString())).thenReturn(entity);
//        when(utils.hasTokenExpired(anyString())).thenReturn(false);
//        when(repository.save(any(CandidateEntity.class))).thenReturn(entity);
//        boolean verifyEmail = service.verifyEmail("fghdsdhfg23244rfd");
//        assertTrue(verifyEmail);
//    }
//
//    @Test
//    public void testVerifyEmailNegative() {
//        when(repository.findByVerificatonToken(anyString())).thenReturn(entity);
//        when(utils.hasTokenExpired(anyString())).thenReturn(true);
//        boolean verifyEmail = service.verifyEmail("fghdsdhfg23244rfd");
//        assertFalse(verifyEmail);
//    }
//
//    @Test
//    public void testEmailExist() {
//        when(repository.findByEmail(anyString())).thenReturn(entity);
//        boolean exist = service.emailExist("hosam7asko@gmail.com");
//        assertTrue(exist);
//    }
//
//    @Test
//    public void testEmailExistNegative() {
//        when(repository.findByEmail(anyString())).thenReturn(null);
//        boolean exist = service.emailExist("hosam7asko@gmail.com");
//        assertFalse(exist);
//    }
//
//    @Test
//    public void testUpdatePassword() throws CandidateApiException {
//        UpdatePasswordDto passwordDto = new UpdatePasswordDto();
//        passwordDto.setPassword("sdfghjsdfgh");
//        passwordDto.setPublicId("sdfghjkt4545");
//        when(encoder.encode(anyString())).thenReturn("dfghjkl34567gfds");
//        when(repository.updatePassword(anyString(), anyString())).thenReturn(1);
//        boolean isUpdate = service.updatePassword(passwordDto);
//        assertTrue(isUpdate);
//    }
//
//    @Test
//    public void testUpdatePasswordNegative() throws CandidateApiException {
//        UpdatePasswordDto passwordDto = new UpdatePasswordDto();
//        passwordDto.setPassword("sdfghjsdfgh");
//        passwordDto.setPublicId("sdfghjkt4545");
//        when(encoder.encode(anyString())).thenReturn("dfghjkl34567gfds");
//        when(repository.updatePassword(anyString(), anyString())).thenReturn(0);
//        CandidateApiException exception = assertThrows(CandidateApiException.class, () -> {
//            service.updatePassword(passwordDto);
//        });
//        boolean notFound = exception.getMessage().contains("not found");
//        assertTrue(notFound);
//    }
//
//    @Test
//    public void testGetAllCandidates() {
//        List<CandidateEntity> entities = new ArrayList<>(Arrays.asList(entity));
//        when(repository.findAll()).thenReturn(entities);
//        List<CandidateDto> dtos = new ArrayList<>(Arrays.asList(dto));
//        when(utils.convertDtoToEntity(anyList())).thenReturn(dtos);
//        List<CandidateDto> dtoList = service.getAllCandidates();
//        assertTrue(dtoList.size() > 0);
//    }
//
//    @Test
//    public void testAdhaarExist() {
//        when(repository.findByAdhaarNo(anyLong())).thenReturn(entity);
//        boolean exist = service.adhaarExist(123456789L);
//        assertTrue(exist);
//    }
//
//    @Test
//    public void testAdhaarExistNegative() {
//        when(repository.findByAdhaarNo(anyLong())).thenReturn(null);
//        boolean exist = service.adhaarExist(123456789L);
//        assertFalse(exist);
//    }

}
