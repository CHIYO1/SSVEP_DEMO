/**
 * 这个文件包含一个公共接口，包含对数据库的相关操作。
 * 
 * @author 石振山
 * @version 1.1.0
 */
package com.ssvep.dao;

import java.util.List;
import java.util.Map;

public interface TableDao<T> {
    void save(T entity);
    List<T> query(Map<String, Object> criteria);
    void update(T entity);
    void delete(Long id);
    List<T> getAll();
}
