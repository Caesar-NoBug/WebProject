package org.caesar.finalWork.controller;

import org.caesar.finalWork.constant.StringConsist;
import org.caesar.finalWork.domain.dto.ProductDto;
import org.caesar.finalWork.domain.entity.PType;
import org.caesar.finalWork.domain.vo.Response;
import org.caesar.finalWork.service.impl.ProductServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(urlPatterns = "/product")
@ServletSecurity(httpMethodConstraints = {
        @HttpMethodConstraint(value = "GET"),
        @HttpMethodConstraint(value = "POST"),
        @HttpMethodConstraint(value = "PUT"),
        @HttpMethodConstraint(value = "DELETE"),
        @HttpMethodConstraint(value = "OPTIONS")
        })
public class ProductServlet extends HttpServlet {

    private ProductServiceImpl productService = ProductServiceImpl.getInstance();

    //查询商品
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String type = req.getParameter("type");
        if(StringConsist.QUERY_TYPE_INIT.equals(type)){
            int total = productService.selectProductCount();
            List<String> types = new ArrayList<>();
            for (PType pType : productService.selectAllPType()) {
                types.add(pType.getTname());
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("total", total);
            map.put("types", types);
            Response.OK(map).doResponse(resp);
            return;
        }
        else if(StringConsist.QUERY_TYPE_ALL.equals(type)){

            int page = Integer.parseInt(req.getParameter("page"));
            int size = Integer.parseInt(req.getParameter("size"));

            List<ProductDto> products = productService.selectAllProduct(page, size);
            if(products == null)
                Response.ERROR("查询失败").doResponse(resp);
            else{
                HashMap<String, Object> map = new HashMap<>();
                map.put("products", products);
                Response.OK(map).doResponse(resp);
            }
            return;
        }
        else if(StringConsist.QUERY_TYPE_query.equals(type)){
            String pname = req.getParameter("pname");
            String pdesc = req.getParameter("pdesc");
            String tname = req.getParameter("tname");
            int page = Integer.parseInt(req.getParameter("page"));
            int size = Integer.parseInt(req.getParameter("size"));
            Integer minPrice = null;
            Integer maxPrice = null;
            if(req.getParameter("minPrice") != null)
                minPrice = Integer.parseInt(req.getParameter("minPrice"));
            if (req.getParameter("maxPrice") != null)
                maxPrice = Integer.parseInt(req.getParameter("maxPrice"));

            Map<String, Object> map = new HashMap<>();
            map = productService.selectProductDynamic(pname, pdesc, tname, minPrice, maxPrice, page, size);
            if(map == null)
                Response.ERROR("查询失败").doResponse(resp);
            else{
                Response.OK(map).doResponse(resp);
            }

        }

    }

    //新增商品
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String json = req.getReader().readLine();
        ProductDto product = null;

        product = ProductDto.parseJSON(json);

        if(Objects.isNull(product)) {
            Response.ERROR("参数错误").doResponse(resp);
            return;
        }

        if(!productService.insertProduct(product))
            Response.ERROR("添加失败").doResponse(resp);
        else
            Response.OK("添加成功").doResponse(resp);
    }

    //修改商品
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pName = req.getParameter("pName");
        String json = req.getReader().readLine();
        ProductDto product = ProductDto.parseJSON(json);
        if(productService.updateProductByPName(product, pName))
            Response.OK("修改成功").doResponse(resp);
        else
            Response.ERROR("修改失败").doResponse(resp);
    }

    //删除商品
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String pName = req.getParameter("pName");
        if(productService.removeProductByPName(pName))
            Response.OK("删除成功").doResponse(resp);
        else
            Response.ERROR("删除失败").doResponse(resp);
    }

}
