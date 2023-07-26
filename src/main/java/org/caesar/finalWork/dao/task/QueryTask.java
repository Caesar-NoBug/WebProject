package org.caesar.finalWork.dao.task;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class QueryTask extends Task {
    //resultType:返回数据类型：0-指定类型对象或对象集合 1-聚合函数结果（int）
    private String resultType = RESULT_TYPE_OBJECT;
    public static final String RESULT_TYPE_OBJECT = "object";
    public static final String RESULT_TYPE_INT = "int";
    public static final String RESULT_TYPE_STRING = "string";
    public static final String RESULT_TYPE_DOUBLE = "double";

    public QueryTask(String sql, Class clazz, Object... params) {
        this.sql = sql;
        this.clazz = clazz;
        this.params = new ArrayList<>();
        this.params.addAll(Arrays.asList(params));
    }

    @Override
    protected Object run(PreparedStatement ps) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Object result = null;
        ResultSet rs = ps.executeQuery();

        if(RESULT_TYPE_OBJECT.equals(resultType)) {

            List<Object> list = new ArrayList<>();
            while (rs.next()) {
                Object instance = info.getConstructor().newInstance();
                for (int i = 0; i < info.getSize(); i++) {
                    Object attribute = info.getClasses()[i].cast(rs.getObject(info.getNames()[i]));
                    info.getMethods()[i].invoke(instance, attribute);
                }
                list.add(instance);
            }
            result = list;
        }
        else if (RESULT_TYPE_INT.equals(resultType)){

            if (rs.next()) {
                result = rs.getInt(1);
            }
            else
                result = -1;
        }

        if (!Objects.isNull(ps))
            ps.close();
        if (!Objects.isNull(rs))
            rs.close();

        return result;

    }

}