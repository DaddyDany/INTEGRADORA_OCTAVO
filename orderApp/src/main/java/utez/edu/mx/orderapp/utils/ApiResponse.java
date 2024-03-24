package utez.edu.mx.orderapp.utils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Setter
@Getter
public class ApiResponse {
    private Object data;
    private HttpStatus status;
    private boolean error = false;
    private String message;

    public ApiResponse(Object data, String message, HttpStatus status, boolean error) {
        this.data = data;
        this.message = message;
        this.status = status;
        this.error = error;
    }
}
