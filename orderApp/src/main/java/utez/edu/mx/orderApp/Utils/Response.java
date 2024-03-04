package utez.edu.mx.orderApp.Utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Response <T>{
    T data;
    boolean error;
    int status;
    String message;
}
