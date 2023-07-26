package org.caesar.finalWork.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.caesar.finalWork.domain.vo.Divideable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Divideable {
    private Integer pid;
    private Integer tid;
    protected String pname;
    protected String pic;
    protected Integer price;
    protected String pdesc;
    protected Date regtime;

    @Override
    public List toParamList(){
        ArrayList<Object> params = new ArrayList<>();
        params.add(pid);
        params.add(tid);
        params.add(pname);
        params.add(pic);
        params.add(price);
        params.add(pdesc);
        params.add(regtime);
        return params;
    }

}
