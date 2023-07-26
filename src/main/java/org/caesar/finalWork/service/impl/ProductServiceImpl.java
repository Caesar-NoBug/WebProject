package org.caesar.finalWork.service.impl;

import org.caesar.finalWork.dao.mapper.impl.PTypeMapperImpl;
import org.caesar.finalWork.dao.mapper.impl.ProductMapperImpl;
import org.caesar.finalWork.domain.dto.ProductDto;
import org.caesar.finalWork.domain.entity.PType;
import org.caesar.finalWork.domain.entity.Product;
import org.caesar.finalWork.service.ProductService;

import java.util.*;
import java.util.stream.Collectors;

public class ProductServiceImpl implements ProductService {

    private static final ProductServiceImpl instance = new ProductServiceImpl();
    private ProductMapperImpl productMapper = ProductMapperImpl.getInstance();
    private PTypeMapperImpl pTypeMapper = PTypeMapperImpl.getInstance();

    private ProductServiceImpl() {
    }

    public static ProductServiceImpl getInstance() {
        return instance;
    }

    @Override
    public ProductDto selectProduceById(int id) throws Exception {
        Product product = productMapper.selectProductById(id);
        PType pType = pTypeMapper.selectFTypeById(product.getTid());
        return new ProductDto(product, pType);
    }

    @Override
    public Map<String, Object> selectProductDynamic(String pname, String pdesc, String tname, Integer minPrice, Integer maxPrice, int page, int size) {

        Map<String, Object> map = new HashMap<>();

        try {

            Product product = new Product();
            product.setPname(pname);
            product.setPdesc(pdesc);
            if(tname != null){
                product.setTid(pTypeMapper.selectFTypeByName(tname).getTid());
            }

            //如果价格区间不为空，则进行价格区间查询
            if(minPrice != null || maxPrice != null ){

                if(minPrice == null)
                    minPrice = Integer.MIN_VALUE;

                if(maxPrice == null)
                    maxPrice = Integer.MAX_VALUE;

                if(minPrice > maxPrice)
                    return null;

                 map.put("products", loadProductWithType(productMapper.selectProductPriceInRange(product, minPrice, maxPrice, page, size)));
                 map.put("total", productMapper.selectProductCountPriceInRange(product, minPrice, maxPrice));
            }
            else{
                //否则进行普通查询
                map.put("products", loadProductWithType(productMapper.selectProduct(product)));
                map.put("total", productMapper.selectProductCount(product));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return map;
    }


    @Override
    public List<ProductDto> selectAllProduct(int page, int size) {

        try {
            return loadProductWithType(productMapper.selectAllProduct(page, size));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int selectProductCount() {
        int count = -1;
        try {
            count = productMapper.selectProductCount();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return count;
    }

    @Override
    public List<PType> selectAllPType() {
        List<PType> types = null;
        try {
            types = pTypeMapper.selectAllFType();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return types;
    }

    @Override
    public boolean insertProduct(ProductDto productDto) {

        try {
            //若无指定菜品类型，则直接添加菜品
            if (productDto.getTname() == null || "".equals(productDto.getTname()))
                return productMapper.insertProduct(productDto);

            PType pType = pTypeMapper.selectFTypeByName(productDto.getTname());
            //非法菜品类型
            if (pType == null)
                return false;

            //为菜品绑定菜品类型
            int tid = pType.getTid();
            productDto.setTid(tid);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return productMapper.insertProduct(productDto);
    }

    @Override
    public boolean removeProductByPName(String pName){
        return productMapper.removeProductByPName(pName);
    }

    @Override
    public boolean updateProductByPName(ProductDto product, String pName){
        try {
            if(product.getTname() != null){
                PType pType = pTypeMapper.selectFTypeByName(product.getTname());
                if(pType == null)
                    return false;
                product.setTid(pType.getTid());
            }
            else
                product.setTid(null);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return productMapper.updateProductByPName(product, pName);
    }

    @Override
    public Map<String, List<ProductDto>> selectAllFoodByFType() throws Exception {
        List<PType> types = pTypeMapper.selectAllFType();
        HashMap<String, List<ProductDto>> map = new HashMap<>();
        for (PType type : types) {
            map.put(type.getTname(), productMapper.selectProductByTid(type.getTid())
                    .stream().map(food -> new ProductDto(food, type))
                    .collect(Collectors.toList()));
        }

        return map;
    }

    private List<ProductDto> loadProductWithType(List<Product> products) throws Exception{
        List<ProductDto> result = new ArrayList<>();
        List<PType> types = pTypeMapper.selectAllFType();
        Map<Integer, PType> map = new HashMap<>();

        for (PType type : types)
            map.put(type.getTid(), type);

        for (Product product : products)
            result.add(new ProductDto(product, map.get(product.getTid())));

        return result;
    }
}
