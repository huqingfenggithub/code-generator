package io.github.jzdayz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Result<T> {

    private int code = 200;

    private String message = "success";

    private T data;

    public static <T> Result<T> success() {
        return new Result<>();
    }

    public static <T> Result<T> success(T data) {
        Result<T> success = Result.<T>success();
        success.setData(data);
        return success;
    }

    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(300);
        result.setMessage(message);
        return result;
    }

}