package com.example.codingtask.controllers;


import com.example.codingtask.dtos.MoneyDto;
import com.example.codingtask.services.AwardingPointsService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController()
@RequestMapping("/award-points")
public class AwardingPointsController {

    private final AwardingPointsService awardingPointsService;

    public AwardingPointsController(
            AwardingPointsService awardingPointsService
    ) {
        this.awardingPointsService = awardingPointsService;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody MoneyDto getAwardPointsFromMoneySpent(
            @Valid @RequestBody MoneyDto moneyDto
    ) {
        return awardingPointsService.proceedAwardPoints(moneyDto);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SQLException.class)
    public Map<String, String> handleSQLException(
            SQLException ex
    ) {
        Map<String, String> errorDetail = new HashMap<>();
        errorDetail.put("message", "application encountered error related to database");
        return errorDetail;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Map<String, String> handleInternalExceptions(
            Exception ex
    ) {
        Map<String, String> errorDetail = new HashMap<>();
        errorDetail.put("error message", ex.getMessage());
        errorDetail.put("cause", "application encountered internal error");
        return errorDetail;
    }
}
