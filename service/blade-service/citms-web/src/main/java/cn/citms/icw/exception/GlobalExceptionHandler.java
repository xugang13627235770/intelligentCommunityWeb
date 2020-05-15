package cn.citms.icw.exception;

import org.springblade.core.tool.api.R;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cyh
 */
@ControllerAdvice(basePackages = "cn.citms.icw.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<R<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>(10);
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        R<Map<String, String>> r = R.fail("参数校验错误");
        r.setData(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(r);
    }

}
