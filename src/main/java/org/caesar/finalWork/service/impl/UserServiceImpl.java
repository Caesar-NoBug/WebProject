package org.caesar.finalWork.service.impl;

import org.caesar.finalWork.dao.mapper.impl.PTypeMapperImpl;
import org.caesar.finalWork.dao.mapper.impl.ProductMapperImpl;
import org.caesar.finalWork.dao.mapper.impl.UserMapperImpl;
import org.caesar.finalWork.domain.entity.User;
import org.caesar.finalWork.service.UserService;

import java.util.Objects;

public class UserServiceImpl implements UserService {

    private static final UserServiceImpl instance = new UserServiceImpl();
    private UserMapperImpl userMapper = UserMapperImpl.getInstance();

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        return instance;
    }


    @Override
    public User login(String username, String password) throws Exception {
        return userMapper.login(username, password);
    }

    @Override
    public User register(String username, String password) {
        return null;
    }
}
