package su.yuk1chan.warehousehistory.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import su.yuk1chan.warehousehistory.dto.ErrorResponse;

import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse illegalArgumentExceptionHandle(IllegalArgumentException iae) {
        return new ErrorResponse(iae.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentNotValidExceptionHandle(MethodArgumentNotValidException manve) {
        return new ErrorResponse(manve.getBindingResult().getAllErrors().getFirst().getDefaultMessage());
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse sqlException() {
        return new ErrorResponse("Ошибка базы данных");
    }
}
