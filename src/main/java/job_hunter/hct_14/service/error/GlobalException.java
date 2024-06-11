package job_hunter.hct_14.service.error;

import job_hunter.hct_14.entity.RestResponse;
import job_hunter.hct_14.service.error.IdInvaldException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = IdInvaldException.class)
    public ResponseEntity<Object> handleIdInvaldException(IdInvaldException idInvaldException) {


        RestResponse<Object> restResponse = new RestResponse<Object>();
        restResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        restResponse.setError(idInvaldException.getMessage());
        restResponse.setMessage("idInvaldException");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }
}

