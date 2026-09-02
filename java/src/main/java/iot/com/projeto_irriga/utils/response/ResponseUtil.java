package iot.com.projeto_irriga.utils.response;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtil {
    public <T> ApiResponse<T> sucess(T datas, String message, HttpStatus status) {
        ApiResponse<T> apiResponse = new ApiResponse<>();

        apiResponse.setDatas(datas);
        apiResponse.setMessage(message);
        apiResponse.setStatus(status);
        apiResponse.setSucess(true);

        return apiResponse;
    }

    public <T> ApiResponse<T> error(T datas, String message, HttpStatus status){
        ApiResponse <T> apiResponse = new ApiResponse<>();

        apiResponse.setSucess(false);
        apiResponse.setDatas(datas);
        apiResponse.setMessage(message);
        apiResponse.setStatus(status);

        return  apiResponse;
    }
}
