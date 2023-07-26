package org.caesar.finalWork.dao.mapper.impl;

import org.caesar.finalWork.dao.ConnectionPool;
import org.caesar.finalWork.dao.mapper.ProductMapper;
import org.caesar.finalWork.dao.mapper.Mapper;
import org.caesar.finalWork.dao.task.ModifyTask;
import org.caesar.finalWork.dao.task.QueryTask;
import org.caesar.finalWork.domain.entity.Product;

import java.sql.Date;
import java.util.List;

public class ProductMapperImpl extends Mapper implements ProductMapper {

    private static final ProductMapperImpl instance = new ProductMapperImpl();

    ProductMapperImpl() {
        super(Product.class, ConnectionPool.getInstance());
    }

    public static ProductMapperImpl getInstance() {
        return instance;
    }

    /**
     * @param product 菜品对象
     * @return 返回满足product中非空条件的菜品集合
     * @throws Exception
     */
    @Override
    public List<Product> selectProduct(Product product) throws Exception {
        Object params = product.toParamList();
        QueryTask task = new QueryTask("select * from product where ", clazz, params);
        task.setDynamic(true);
        return (List<Product>) pool.selectAll(task);
    }

    @Override
    public List<Product> selectProductPriceInRange(Product product, int min, int max, int page, int size) throws Exception{
        Object params = product.toParamList();
        String sql = "select * from product where price >= " + min + " and price <= " + max + " ";
        QueryTask task = new QueryTask(sql, clazz, params);
        task.setDynamic(true);
        task.setPage(page);
        task.setSize(size);
        return (List<Product>) pool.selectAll(task);
    }

    @Override
    public int selectProductCountPriceInRange(Product product, int min, int max) throws Exception{
        Object params = product.toParamList();
        String sql = "select count(*) from product where price >= " + min + " and price <= " + max + " ";
        QueryTask task = new QueryTask(sql, clazz, params);
        task.setResultType(QueryTask.RESULT_TYPE_INT);
        task.setDynamic(true);
        return (int) pool.selectObject(task);
    }

    @Override
    public Product selectProductById(Integer pid) throws Exception {
        String sql = "select * from product where pid = ?";
        return (Product) pool.selectOne(new QueryTask(sql, clazz, pid));
    }

    @Override
    public List<Product> selectProductByTid(Integer tid) throws Exception {
        String sql = "select * from product where tid = ?";
        return (List<Product>) pool.selectAll(new QueryTask(sql, clazz, tid));
    }

    @Override
    //根据菜品名查询
    public Product selectProductByName(String name) throws Exception {
        String sql = "select * from product where pname = ?";
        return (Product) pool.selectOne(new QueryTask(sql, clazz, name));
    }

    @Override
    public List<Product> selectAllProduct(int page, int size) throws Exception {
        String sql = "select * from product";
        QueryTask task = new QueryTask(sql, clazz);
        task.setPage(page);
        task.setSize(size);
        return (List<Product>) pool.selectAll(task);
    }

    @Override
    public int selectProductCount(Product product) throws Exception{
        Object params = product.toParamList();
        QueryTask task = new QueryTask("select count(*) from product where ", clazz, params);
        task.setDynamic(true);
        task.setResultType(QueryTask.RESULT_TYPE_INT);
        return (int) pool.selectObject(task);
    }

    @Override
    public int selectProductCount() throws Exception {
        String sql = "select count(*) from product";
        QueryTask task = new QueryTask(sql, clazz);
        task.setResultType(QueryTask.RESULT_TYPE_INT);
        return (int) pool.selectObject(task);
    }

    @Override
    public boolean insertProduct(Product product){

        if(product.getRegtime() == null)
            product.setRegtime(new Date(System.currentTimeMillis()));
        if(product.getTid() != null) {
            String sql = "insert into product values(?,?,?,?,?,?,?)";
            return pool.modify(new ModifyTask(sql, product.getPid(), product.getTid(), product.getPname(), product.getPic(), product.getPrice(), product.getPdesc(), product.getRegtime()));
        }

        String sql = "insert into product values(?, null,?,?,?,?,?)";
        return pool.modify(new ModifyTask(sql, product.getPid(), product.getPname(), product.getPic(), product.getPrice(), product.getPdesc(), product.getRegtime()));
    }

    @Override
    public boolean removeProductByPName(String pName){
        String sql = "delete from product where pname = ?";
        return pool.modify(new ModifyTask(sql, pName));
    }

    @Override
    public boolean updateProductByPName(Product product, String pName){
        if(product.getPic() == null) {
            String sql = "update product set tid = ?, pname = ?, price = ?, pdesc = ? where pname = ?";
            return pool.modify(new ModifyTask(sql,product.getTid(), product.getPname(), product.getPrice(), product.getPdesc(), pName));
        }
        else{
            String sql = "update product set tid = ?, pname = ?, pic = ?, price = ?, pdesc = ? where pname = ?";
            return pool.modify(new ModifyTask(sql, product.getTid(), product.getPname(), product.getPic(), product.getPrice(), product.getPdesc(), pName));
        }
    }

}
