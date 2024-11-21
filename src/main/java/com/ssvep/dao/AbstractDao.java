/**
 * 这个文件包含一个数据库操作的抽象基类。
 * 
 * @author 石振山
 * @version 1.3.2
 */
package com.ssvep.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssvep.config.DatabaseConnection;

public abstract class AbstractDao<T> implements TableDao<T> {
    protected Connection connection;

    public AbstractDao() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(T entity) {
        String sql = getInsertSQL();
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setInsertParameters(statement, entity);
            int affectedRows = statement.executeUpdate();
            connection.commit(); // 提交事务

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        setEntityId(entity, generatedKeys.getLong(1));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(T entity) {
        String sql = getUpdateSQL();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            setUpdateParameters(statement, entity);
            statement.executeUpdate();
            connection.commit(); // 提交事务

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM " + getTableName() + " WHERE " + getIdName() + "= ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();
            connection.commit(); // 提交事务

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<T> query(Map<String, Object> criteria) {
        List<T> results = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM " + getTableName() + " WHERE ");
        List<Object> params = new ArrayList<>();

        for (String key : criteria.keySet()) {
            sql.append(key).append(" = ? AND ");
            params.add(criteria.get(key));
        }

        sql.setLength(sql.length() - 5); // 去掉最后的 " AND "

        try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }

            ResultSet resultSet = statement.executeQuery();
            connection.commit(); // 提交事务

            while (resultSet.next()) {
                results.add(mapRowToEntity(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    @Override
    public List<T> getAll() {
        Map<String, Object> criteria = new HashMap<>();

        return query(criteria);
    }

    protected abstract String getTableName();

    protected abstract String getIdName();

    protected abstract String getInsertSQL();

    protected abstract String getUpdateSQL();

    protected abstract void setInsertParameters(PreparedStatement statement, T entity) throws SQLException;

    protected abstract void setUpdateParameters(PreparedStatement statement, T entity) throws SQLException;

    protected abstract void setEntityId(T entity, Long id);

    protected abstract T mapRowToEntity(ResultSet resultSet) throws SQLException;

}
