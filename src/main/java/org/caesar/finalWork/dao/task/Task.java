package org.caesar.finalWork.dao.task;



import lombok.Data;
import lombok.NoArgsConstructor;
import org.caesar.finalWork.dao.ConnectionPool;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Data
@NoArgsConstructor
public abstract class Task {

    /**
     * sql:查询语句
     * connectionPool:查询使用的连接池
     * connection:查询使用的连接池
     * params:查询使用的参数
     * clazz:返回值类型
     * info:返回值类型相关信息
     * page:分页查询的页码
     * size:分页查询的每页大小
     */
    protected String sql;
    private ConnectionPool connectionPool;
    private Connection connection;
    protected List<Object> params;
    protected Class clazz;
    protected ObjectInfo info;
    private Integer page;
    private Integer size;
    //是否为动态查询
    private boolean isDynamic = false;

    protected abstract Object run(PreparedStatement ps) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;

    //静态查询（不对sql和参数做额外处理）
    public Object execute() throws Exception {

        if(isDynamic){
            return executeDynamic();
        }

        ResultSet rs = null;
        Object result = null;

        if(page != null && size != null){
            sql += " limit " + size + " offset " + size * (page - 1);
        }

        PreparedStatement ps = connection.prepareStatement(sql);

        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }

        result = run(ps);

        notifyPool();
        return result;
    }

    //实现动态组合查询和模糊查询（若参数类型为字符串则自动转成模糊查询）
    public Object executeDynamic() throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ResultSet rs = null;
        Object result = null;
        StringBuilder sql = new StringBuilder(this.sql);

        if(!(params.get(0) instanceof List)){
            System.out.println("非法参数类型：参数不是数组");
            return null;
        }

        params = (List<Object>) params.get(0);

        //判断是否附带了额外的查询条件
        boolean avalible = false;
        for (Object param : params) {
            if(param != null) {
                avalible = true;
                break;
            }
        }

        if (avalible) {

            //附带了额外的查询条件
            if(!sql.toString().endsWith("where "))
                sql.append(" and ");

            //拼接查询条件
            for (int i = 0; i < params.size(); i++) {
                if (params.get(i) != null) {

                    if(params.get(i) instanceof String)
                        sql.append(info.getNames()[i] + " like ? and ");
                    else
                        sql.append(info.getNames()[i] + " = ? and ");
                }
            }

            sql.delete(sql.length() - 5, sql.length() - 1);
        }
        else {
            //没有附带额外的查询条件则删除where
            if(sql.toString().endsWith("where "))
                sql.delete(sql.length() - 6, sql.length() - 1);
        }

        //分页查询
        if(page != null && size != null){
            sql.append(" limit " + size + " offset " + size * (page - 1));
        }

        PreparedStatement ps = connection.prepareStatement(sql.toString());

        //设置查询参数
        int cnt = 1;
        for (int i = 0; i < params.size(); i++) {
            if (params.get(i) != null) {
                if(params.get(i) instanceof String)
                    ps.setObject(cnt ++, "%" +((String) params.get(i)) + "%");
                else
                    ps.setObject(cnt++, params.get(i));
            }
        }

        result = run(ps);

        notifyPool();
        return result;
    }

    //Observer pattern(Subject)
    protected void notifyPool() throws SQLException {
        connectionPool.updateState(connection);
    }
}
