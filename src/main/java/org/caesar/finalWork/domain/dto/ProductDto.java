package org.caesar.finalWork.domain.dto;

import lombok.*;
import org.caesar.finalWork.domain.entity.PType;
import org.caesar.finalWork.domain.entity.Product;
import org.caesar.finalWork.domain.vo.JSONable;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

@Data
@NoArgsConstructor
public class ProductDto extends Product implements JSONable {

    private String tname;

    public ProductDto(Product product, PType pType){
        this.pname = product.getPname();
        this.pic = product.getPic();
        this.price = product.getPrice();
        this.pdesc = product.getPdesc();
        this.regtime = product.getRegtime();
        if(pType != null)
            this.tname = pType.getTname();
    }

    public ProductDto(Product product, String tName){
        this.pname = product.getPname();
        this.pic = product.getPic();
        this.price = product.getPrice();
        this.pdesc = product.getPdesc();
        this.regtime = product.getRegtime();
        this.tname = tName;
    }

    @Override
    public String toJSON() {

        //隐藏pid和tid，避免泄露数据库信息
        StringBuilder res = new StringBuilder("{");
        if(pname != null)
            res.append("\"pname\":\"" + pname + "\"");
        if(pic != null)
            res.append(",\"pic\":\"" + pic + "\"");
        if (price != null)
            res.append(",\"price\":\"" + price + "\"");
        if(pdesc != null)
            res.append(",\"pdesc\":\"" + pdesc + "\"");
        if (regtime != null)
            res.append(",\"regtime\":\"" + regtime + "\"");
        if(tname != null)
            res.append(",\"tname\":\"" + tname + "\"");
        res.append("}");

        return res.toString();
    }


    public static ProductDto parseJSON(String json){

        ProductDto productDto = new ProductDto();
        try {
            json = json.replaceAll("[\"{}]+", "");
            StringTokenizer tokenizer = new StringTokenizer(json, ":,");

            while (tokenizer.hasMoreTokens()){
                switch (tokenizer.nextToken()){
                    case "pname":
                        productDto.setPname(tokenizer.nextToken());
                        break;
                    case "pic":
                        productDto.setPic(tokenizer.nextToken());
                        break;
                    case "price":
                        productDto.setPrice(Integer.valueOf(tokenizer.nextToken()));
                        break;
                    case "pdesc":
                        productDto.setPdesc(tokenizer.nextToken());
                        break;
                    case "regtime":
                        productDto.setRegtime(new SimpleDateFormat("yyyy-MM-dd").parse(tokenizer.nextToken()));
                        break;
                    case "tname":
                        productDto.setTname(tokenizer.nextToken());
                        break;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        return productDto;
    }

    @Override
    public String toString() {
        return super.toString()  +
                " tname='" + tname + '\'';
    }
}
