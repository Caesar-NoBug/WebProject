package org.caesar.finalWork.dao.mapper.impl;

import org.caesar.finalWork.dao.ConnectionPool;
import org.caesar.finalWork.dao.mapper.PTypeMapper;
import org.caesar.finalWork.dao.mapper.Mapper;
import org.caesar.finalWork.dao.task.ModifyTask;
import org.caesar.finalWork.dao.task.QueryTask;
import org.caesar.finalWork.domain.entity.PType;

import java.util.List;

public class PTypeMapperImpl extends Mapper implements PTypeMapper {

    private static final PTypeMapperImpl instance = new PTypeMapperImpl();

    private PTypeMapperImpl() {
        super(PType.class, ConnectionPool.getInstance());
    }

    public static PTypeMapperImpl getInstance(){
        return instance;
    }

    @Override
    public PType selectFTypeById(int id) throws Exception{
        String sql = "select * from ptype where tid = ?";
        return (PType) pool.selectOne(new QueryTask(sql, clazz, id));
    }

    @Override
    public PType selectFTypeByName(String name) throws Exception{
        String sql = "select * from ptype where tname = ?";
        return (PType) pool.selectOne(new QueryTask(sql, clazz, name));
    }

    @Override
    public List<PType> selectAllFType() throws Exception{
        String sql = "select * from ptype";
        return (List<PType>) pool.selectAll(new QueryTask(sql, clazz));
    }

    @Override
    public boolean insertFType(PType pType){
        String sql = "insert into ptype values(null,?)";;
        return pool.modify(new ModifyTask(sql, pType.getTname()));
    }

}
