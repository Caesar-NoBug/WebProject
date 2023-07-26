package org.caesar.finalWork.dao.mapper.impl;

import org.caesar.finalWork.dao.ConnectionPool;
import org.caesar.finalWork.dao.mapper.Mapper;
import org.caesar.finalWork.dao.mapper.UserMapper;
import org.caesar.finalWork.dao.task.QueryTask;
import org.caesar.finalWork.domain.entity.Product;
import org.caesar.finalWork.domain.entity.User;

public class UserMapperImpl extends Mapper implements UserMapper {

    private static final UserMapperImpl instance = new UserMapperImpl();

    UserMapperImpl() {
        super(User.class, ConnectionPool.getInstance());
    }

    public static UserMapperImpl getInstance() {
        return instance;
    }

    public boolean register(User user) {

        return true;
    }
    public User login(String usercode, String password) throws Exception{
        String sql = "select * from user where usercode = ? and userpwd = ?";
        return (User) pool.selectOne(new QueryTask(sql, clazz, usercode, password));
    }
}
