package job_hunter.hct_14.util;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import job_hunter.hct_14.entity.RestResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class FormatResponse implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        HttpServletResponse httpResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = httpResponse.getStatus();

        RestResponse<Object> restResponse = new RestResponse<Object>();
        restResponse.setStatus(status);

        if (status >= 400) {
            return body;
//            restResponse.setError("call failed roi ngu vl");
//            restResponse.setMessage(body);
//            restResponse.setStatus(status);

//            "message": "",
//            "error": "",
//            "statusCode": "",
        }else {
            restResponse.setData(body);
            restResponse.setMessage("call api cucces");

        }



        return restResponse;
    }
}
