package io.github.jzdayz.controller;

import io.github.jzdayz.Result;
import io.github.jzdayz.ex.TableNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ExController {

    @ExceptionHandler(TableNotFoundException.class)
    public Result<?> tableNotFoundException() {
        return Result.error("表不存在！");
    }

    @ExceptionHandler(Exception.class)
    public Result<?> ex(Exception e) {
        log.error(e.getMessage(), e);
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(value = {BindException.class})
    @ResponseBody
    public Result<?> bindException(BindException ex) {
        Result<Object> error = Result.error("");
        Map<String, String> data = new HashMap<>();
        for (FieldError fieldError : ex.getFieldErrors()) {
            data.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        error.setData(data);
        return error;
    }

}
