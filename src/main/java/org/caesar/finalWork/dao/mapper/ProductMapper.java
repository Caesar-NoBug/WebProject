package org.caesar.finalWork.dao.mapper;

import org.caesar.finalWork.domain.entity.Product;

import java.util.List;

public interface ProductMapper {
    List<Product> selectProduct(Product product) throws Exception;

    List<Product> selectProductPriceInRange(Product product, int min, int max, int page, int size) throws Exception;

    int selectProductCountPriceInRange(Product product, int min, int max) throws Exception;

    //查询指定id的菜品
    Product selectProductById(Integer pid) throws Exception;

    List<Product> selectProductByTid(Integer tid) throws Exception;

    Product selectProductByName(String name) throws Exception;

    //查询所有菜品(分页查询)
    List<Product> selectAllProduct(int page, int size) throws Exception;

    int selectProductCount(Product product) throws Exception;

    int selectProductCount() throws Exception;

    //添加菜品
    boolean insertProduct(Product product) throws Exception;
    //删除菜品
    boolean removeProductByPName(String pname) throws Exception;
    //修改菜品
    boolean updateProductByPName(Product product, String pName) throws Exception;
}
