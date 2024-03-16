package utez.edu.mx.orderApp.Utils;
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
    private boolean error = false; // Predeterminado a false, asumiendo la mayoría de respuestas no son errores
    private String message;

    // Constructor para inicializar todos los campos relevantes
    public ApiResponse(Object data, String message, HttpStatus status, boolean error) {
        this.data = data;
        this.message = message;
        this.status = status;
        this.error = error;
    }
}
