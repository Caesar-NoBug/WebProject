package org.caesar.finalWork;



import org.caesar.finalWork.dao.mapper.impl.ProductMapperImpl;
import org.caesar.finalWork.domain.dto.ProductDto;
import org.caesar.finalWork.domain.entity.PType;
import org.caesar.finalWork.domain.entity.Product;
import org.caesar.finalWork.service.impl.ProductServiceImpl;

import java.util.List;


public class Test {

    public static void main(String[] args) throws Exception {
        ProductMapperImpl productMapper = ProductMapperImpl.getInstance();
        ProductServiceImpl productService = ProductServiceImpl.getInstance();
        Product product = new Product(null, null,"元气森林", "", 5, "...", null);
        ProductDto productDto = new ProductDto(product, (PType) null);
        /*System.out.println(productMapper.selectProductCount(product));
        System.out.println(productMapper.selectProductCount());*/
        System.out.println(productService.updateProductByPName(productDto, "一件商品"));
        /*System.out.println(productMapper.selectProductByName("绿色的帽子"));
        System.out.println(productMapper.selectProductByTid(1));
        System.out.println(productMapper.selectProductById(13));
        System.out.println(productMapper.selectAllProduct(1, 10));
        System.out.println(productMapper.selectProduct(product));*/

    }
}
