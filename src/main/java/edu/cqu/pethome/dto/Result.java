package edu.cqu.pethome.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private Boolean success;
    private String errorMsg;
    private Object data;
    public static Result ok(){
        return new Result(true,null,null);
    }
    public static Result ok(Object data){
        return new Result(true,null,data);
    }
    public static Result ok(List<Object> data){
        return new Result(true,null,data);
    }
    public static Result err(String message){
        return new Result(false,message,null);
    }
}
