package org.caesar.finalWork.service;

import org.caesar.finalWork.domain.entity.User;

public interface UserService {

    User login(String username, String password) throws Exception;
    User register(String username, String password);

}
