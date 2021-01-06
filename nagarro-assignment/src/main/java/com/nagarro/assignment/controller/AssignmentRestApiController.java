package com.nagarro.assignment.controller;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.assignment.config.exception.AssignmentDateMissMatchException;
import com.nagarro.assignment.model.AccountModel;
import com.nagarro.assignment.model.request.AmountRangeRequestModel;
import com.nagarro.assignment.service.AssignmentService;
import com.nagarro.assignment.share.AssignmentUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import static com.nagarro.assignment.share.Constant.*;

/**
 * @author hosam7asko
 */
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
@RestController
@Validated
@RequestMapping(value = ASSIGNMENT_REST_URL)
@Api(value = ASSIGNMENT_REST_URL, tags = "Assignment Rest Api")
@ApiResponses(value = {@ApiResponse(code = 200, message = "Sucecss|OK"),
        @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
        @ApiResponse(code = 404, message = "account not found!!!"),
        @ApiResponse(code = 400, message = "bad request!!!")})
public class AssignmentRestApiController {
    /**
     * Enable logging for the class
     */
    private static final Logger log = LoggerFactory.getLogger(AssignmentRestApiController.class);

    private AssignmentService service;
    private AssignmentUtils utils;
    /**
     * inject AssignmentService AssignmentUtils to the class using constructor
     */
    @Autowired
    public AssignmentRestApiController(AssignmentService service, AssignmentUtils utils) {
        this.service = service;
        this.utils = utils;
    }

    /**
     * this method using to send last three month info to the user
     * it is return not found exception if it is not exist
     * it will receive Id as pathVariable
     * the url should http://localhost:port/assigment/account/pathVariable
     * @param accountId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "three months back statement", response = AccountModel.class, tags = GET_LAST_THREE_MONTH_INFO, consumes = "accountId", produces = "application/json, application/xml", notes = "This method receive acountId as path param "
            + "and return details with three month back account  statements for that account id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "account not found!!!"),
            @ApiResponse(code = 400, message = "bad request!!!")

    })

    @GetMapping(value = GET_LAST_THREE_MONTH_INFO, produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    public AccountModel getLastThreeMonthStatement(@PathVariable Integer accountId) throws Exception {
        log.info("AssignmentRestApiController class getLastThreeMonthStatement() method start");
        AccountModel threeMonth = service.getLastThreeMonth(accountId);
        log.info("AssignmentRestApiController class getLastThreeMonthStatement() method end");
        return threeMonth;

    }

    /**
     * this method using to send  account details  and range of statements info to the user
     * it will receive Id as pathVariable and request body which contains range of amount
     * with start amount and end amount
     * it is return not found exception if Id is not exist
     * and return mismatch error if the range is not given by user
     * the url should http://localhost:port/assigment/pathVariable/range
     * @param accountId
     * @param amountRage
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "The amount range", response = AccountModel.class, tags = GET_RANGE_OF_AMOUNT_INFO, consumes = "application/json, application/xml", produces = "application/json, application/xml", notes = "This method receive acountId as path param and AmountRangeRequestModel as requestBody"
            + "and return account details with amount range statements for that account id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "account not found!!!"),
            @ApiResponse(code = 406, message = "data not acceptable!!!")

    })
    @PostMapping(value = GET_RANGE_OF_AMOUNT_INFO, consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XHTML_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    public AccountModel getStatementByRangeOfAmount(@PathVariable() @Min(1) Integer accountId,
                                                @Valid @RequestBody AmountRangeRequestModel amountRage) throws Exception {
        log.info("AssignmentRestApiController class getStatementByRangeOfAmount()  method start");
        String message = utils.validateRange(accountId, amountRage);
        if (message != null) {
            log.error("AssignmentRestApiController class getStatementByRangeOfAmount() method exception occurring");
            throw new AssignmentDateMissMatchException(message);
        }
        log.info("AssignmentRestApiController class getStatementByRangeOfAmount() method end");
        return service.getDataByAmountRange(accountId, amountRage.getStartAmount(),
                amountRage.getEndAmount());
    }

    /**
     * this method using to send  account details  and range of statements info to the user
     * it will receive Id as pathVariable and requestParams for date
     * which it will be the start date and end date
     * it is return not found exception if Id is not exist
     * and return mismatch error if the range is provide given by user
     * the url should http://localhost:port/assigment/pathVariable/range
     * @param accountId
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "The date range", response = AccountModel.class, tags = GET_RANGE_OF_AMOUNT_INFO, consumes = "accountId", produces = "application/json, application/xml", notes = "This method receive acountId as path param and start date, end date as query param"
            + "and return account details with date range statements for that account id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "account not found!!!"),
            @ApiResponse(code = 406, message = "data not acceptable!!!")})

    @GetMapping(value = GET_RANGE_OF_DATE_INFO, produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XHTML_XML_VALUE})

    public AccountModel getStatementByRangeOfDate(@PathVariable Integer accountId,
                                                @RequestParam(name = "startDate") @Valid String startDate,
                                                @RequestParam(name = "endDate") @Valid String endDate)
            throws Exception {
        log.info("AssignmentRestApiController class getStatementByRangeOfDate()method start");
        String message = utils.validateRange(accountId, startDate, endDate);
        if (message != null) {
            log.error("AssignmentRestApiController class getStatementByRangeOfDate() exception occurring");
            throw new AssignmentDateMissMatchException(message);
        }
        log.info("AssignmentRestApiController class getStatementByRangeOfDate()method end");
        return service.getDataByDateRange(accountId, startDate, endDate);
    }
}
