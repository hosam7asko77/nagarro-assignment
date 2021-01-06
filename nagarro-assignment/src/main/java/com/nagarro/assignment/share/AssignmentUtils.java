package com.nagarro.assignment.share;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.nagarro.assignment.model.UserData;
import com.nagarro.assignment.service.AssignmentServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.nagarro.assignment.config.exception.AssignmentNotFoundApiException;
import com.nagarro.assignment.entity.AccountEntity;
import com.nagarro.assignment.entity.StatementEntity;
import com.nagarro.assignment.model.AccountModel;
import com.nagarro.assignment.model.StatementModel;
import com.nagarro.assignment.model.request.AmountRangeRequestModel;

/**
 * This class content all methods which help in assignment
 *
 * @author hosam7asko
 */
@Component
public class AssignmentUtils {
    /**
     * Enable logging for the class
     */
    private static final Logger log = LoggerFactory.getLogger(AssignmentServiceImpl.class);
    /**
     * inject environment to this class
     */
    @Autowired
    private Environment env;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    /**
     * this method using to convert StatementEntity to StatementModel
     *
     * @param entities
     * @return
     * @throws AssignmentNotFoundApiException
     */
    public List<StatementModel> convertEntitiesToModels(List<StatementEntity> entities) throws AssignmentNotFoundApiException {
        log.info("AssignmentUtils convertEntitiesToModels() method start");
        List<StatementModel> returnValue = new ArrayList<>();
        for (StatementEntity entity : entities) {
            StatementModel model = new StatementModel();
            BeanUtils.copyProperties(entity, model);
            try {
                model.setDateField(dateFormat.parse(entity.getDateField()));
            } catch (ParseException e) {
                log.error("AssignmentUtils convertEntitiesToModels() method exception");
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            returnValue.add(model);
        }
        log.info("AssignmentUtils convertEntitiesToModels() method end");
        return returnValue;
    }

    /**
     * @param entities
     * @param startDate
     * @param endDate
     * @return
     * @throws AssignmentNotFoundApiException
     */
    public List<StatementModel> statementDataRange(List<StatementEntity> entities, String startDate, String endDate)
            throws AssignmentNotFoundApiException {
        log.info("AssignmentUtils statementDataRange() method start");
        List<StatementModel> returnValue = new ArrayList<>();
        try {
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);
            for (StatementEntity entity : entities) {
                StatementModel model = new StatementModel();
                Date checkDate = dateFormat.parse(entity.getDateField());
                if (checkDate.compareTo(start) > 0 && checkDate.compareTo(end) < 0) {
                    BeanUtils.copyProperties(entity, model);
                    model.setDateField(checkDate);
                    returnValue.add(model);
                }
            }

        } catch (ParseException e) {
            log.error("AssignmentUtils statementDataRange() method exception");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.info("AssignmentUtils statementDataRange() method end");
        return returnValue;
    }

    public List<StatementModel> statementDataRange(List<StatementEntity> entities, Double startAmount, Double endAmount)
            throws AssignmentNotFoundApiException {
        log.info("AssignmentUtils statementDataRange() method start");
        List<StatementModel> returnValue = new ArrayList<>();
        try {
            for (StatementEntity entity : entities) {
                StatementModel model = new StatementModel();
                if (entity.getAmount() >= startAmount && entity.getAmount() <= endAmount) {
                    BeanUtils.copyProperties(entity, model);
                    model.setDateField(dateFormat.parse(entity.getDateField()));
                    returnValue.add(model);
                }
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.info("AssignmentUtils statementDataRange() method end");
        return returnValue;
    }

    /**
     * @param models
     * @return
     */
    public List<StatementModel> getLastThreeMonthData(List<StatementModel> models) {
        log.info("AssignmentUtils getLastThreeMonthData() method start");
        Date maxDate = models.stream().map(StatementModel::getDateField).max(Date::compareTo).get();
        Calendar c = Calendar.getInstance();
        c.setTime(maxDate);
        c.add(Calendar.MONTH, -3);
        Date date = c.getTime();
        List<StatementModel> returnValue = new ArrayList<>();
        for (StatementModel statementModel : models) {
            if (date.compareTo(statementModel.getDateField()) < 0) {
                returnValue.add(statementModel);
            }
        }
        log.info("AssignmentUtils getLastThreeMonthData() method end");
        return returnValue;
    }

    /**
     *
     * @param accountId
     * @param startDate
     * @param endDate
     * @return
     */
    public String validateRange(Integer accountId, String startDate, String endDate) {
        log.info("AssignmentUtils validateRange() method start");
        String message = null;
        if (accountId == 0 || startDate == null || endDate == null) {
            message = "required field are missing or account id is zero";
        } else if (!match(startDate) || !match(endDate)) {
            message = "please following this date pattern DD.MM.YYYY";
        }
        log.info("AssignmentUtils validateRange() method end");
        return message;
    }

    /**
     *
     * @param accountId
     * @param model
     * @return
     */
    public String validateRange(Integer accountId, AmountRangeRequestModel model) {
        log.info("AssignmentUtils validateRange() method start");
        String message = null;
        if (accountId == 0 || model.getStartAmount() == null || model.getEndAmount() == null) {
            message = "required field are missing or account number is zero";
        } else if (model.getStartAmount() >= model.getEndAmount()) {
            message = "please the start amount can not be more than end amount or equal";
        }
        log.info("AssignmentUtils validateRange() method end");
        return message;
    }

    /**
     *
     * @param pattern
     * @return
     */
    private boolean match(String pattern) {
        return Pattern.matches("^\\d{2}.\\d{2}.\\d{4}$", pattern);
    }

    /**
     *
     * @param account
     * @return
     * @throws Exception
     */
    public String encryptAccountNumber(String account) throws Exception {
        log.info("AssignmentUtils encryptAccountNumber() method start");
        IvParameterSpec iv = new IvParameterSpec(env.getProperty("initVector").getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec(env.getProperty("key").getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(account.getBytes());
        log.info("AssignmentUtils encryptAccountNumber() method end");
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public UserData getUserdata(List<UserData> usersData, String username) {
        log.info("AssignmentUtils encryptAccountNumber() method call");
        return usersData.stream()
                .filter(x -> username.equals(x.getUserName()))
                .findAny()
                .orElse(null);
    }
}
