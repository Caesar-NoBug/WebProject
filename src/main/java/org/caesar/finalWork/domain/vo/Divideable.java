package org.caesar.finalWork.domain.vo;

import java.util.List;

// Divideable接口，用于将对象转换为Object数组,以支持动态sql
public interface Divideable {
    List toParamList();
}
