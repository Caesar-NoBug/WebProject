package org.caesar.finalWork.dao.mapper;

import org.caesar.finalWork.dao.ConnectionPool;

public abstract class Mapper {

    //缓存Class对象以提高性能
    protected Class clazz;
    protected ConnectionPool pool = ConnectionPool.getInstance();

    protected Mapper() {
    }

    protected Mapper(Class clazz, ConnectionPool pool) {
        this.clazz = clazz;
        this.pool = pool;
    }

}
