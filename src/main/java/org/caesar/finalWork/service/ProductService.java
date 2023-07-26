package org.caesar.finalWork.service;

import org.caesar.finalWork.domain.dto.ProductDto;
import org.caesar.finalWork.domain.entity.PType;
import org.caesar.finalWork.domain.entity.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {
    ProductDto selectProduceById(int id) throws Exception;

    Map<String, Object> selectProductDynamic(String pname, String pdesc, String tname, Integer minPrice, Integer maxPrice, int page, int size);

    List<ProductDto> selectAllProduct(int page, int size) throws Exception;

    int selectProductCount() throws Exception;

    List<PType> selectAllPType() throws Exception;

    boolean insertProduct(ProductDto foodDto) throws Exception;

    boolean removeProductByPName(String pName) throws Exception;

    boolean updateProductByPName(ProductDto product, String pName);

    Map<String, List<ProductDto>> selectAllFoodByFType() throws Exception;
}
