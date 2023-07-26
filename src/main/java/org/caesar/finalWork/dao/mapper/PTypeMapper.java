package org.caesar.finalWork.dao.mapper;

import org.caesar.finalWork.domain.entity.PType;

import java.util.List;

public interface PTypeMapper {

    PType selectFTypeById(int id) throws Exception;

    PType selectFTypeByName(String name) throws Exception;

    List<PType> selectAllFType() throws Exception;

    boolean insertFType(PType pType) throws Exception;
}
