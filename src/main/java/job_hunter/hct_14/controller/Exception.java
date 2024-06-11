package job_hunter.hct_14.controller;

import job_hunter.hct_14.service.error.IdInvaldException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class Exception {
    @ExceptionHandler(value = IdInvaldException.class)
    public ResponseEntity<String> handleIdInvaldException(IdInvaldException idInvaldException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(idInvaldException.getMessage());
    }
}
