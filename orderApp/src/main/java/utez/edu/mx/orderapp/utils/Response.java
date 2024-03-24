package utez.edu.mx.orderapp.utils;

import lombok.Data;

@Data
public class Response<T> {
    private T data;
    private boolean error;
    private int status;
    private String message;
    public Response(T data, boolean error, int status, String message) {
        this.data = data;
        this.error = error;
        this.status = status;
        this.message = message;
    }
    public Response(boolean error, int status, String message) {
        this.error = error;
        this.status = status;
        this.message = message;
    }

    public boolean isSuccess() {
        return !error;
    }
}
