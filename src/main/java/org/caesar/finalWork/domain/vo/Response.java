package org.caesar.finalWork.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.caesar.finalWork.constant.Code;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class Response {

    private Code code;
    private Map<String, Object> datas;
    private String msg;

    public static Response OK(Map<String, Object> datas){
        return new Response(Code.OK, datas, "OK");
    }

    public static Response OK(Object data){
        HashMap<String, Object> map = new HashMap<>();
        map.put("data", data);
        return new Response(Code.OK, map, "OK");
    }

    public static Response OK(String msg){
        return new Response(Code.OK, null, msg);
    }

    public static Response ERROR(Map<String, Object> datas, String msg){
        return new Response(Code.RESPONSE_ERROR, datas, msg);
    }

    public static Response ERROR(String msg){
        return new Response(Code.RESPONSE_ERROR, null, msg);
    }

    public static Response ERROR(Map<String, Object> datas){
        return new Response(Code.RESPONSE_ERROR, datas, "error happened");
    }

    public String toJSONString(){

        StringBuilder res = new StringBuilder("{\"code\":\"" + code.getValue() +  "\",\"data\":{");
        if(datas == null || datas.size() == 0){
            res.append("},\"msg\":\"" + msg + "\"}");
            return res.toString();
        }
        datas.forEach((k, v) -> {
            //数组
            if(v instanceof List){
                res.append("\"" + k + "\":[");
                ((List) v).forEach(e -> {
                    if(e instanceof JSONable)
                        res.append(((JSONable) e).toJSON() + ",");
                    else
                        res.append("\"" + e + "\",");
                });
                res.deleteCharAt(res.length() - 1);
                res.append("],");
                return;
            }
            //自定义对象
            if(v instanceof JSONable)
                res.append("\"" + k + "\":" + ((JSONable) v).toJSON() + ",");
            //基本类型
            else
                res.append("\"" + k + "\":\"" + v + "\",");
        });
        res.deleteCharAt(res.length() - 1);

        res.append("},\"msg\":\"" + msg + "\"}");

        return res.toString();
    }

    public void doResponse(HttpServletResponse resp){
        try {
            resp.getWriter().write(toJSONString());
            System.out.println(toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
