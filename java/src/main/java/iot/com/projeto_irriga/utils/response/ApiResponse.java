package iot.com.projeto_irriga.utils.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiResponse<T> {
    private HttpStatus status;
    private String message;
    private T datas;
    private boolean sucess;

    public boolean getSucess(){
        return this.sucess;
    }


}
