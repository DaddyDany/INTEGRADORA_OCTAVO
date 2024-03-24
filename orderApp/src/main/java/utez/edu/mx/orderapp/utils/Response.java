package utez.edu.mx.orderapp.utils;

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
