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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ssvep.config.DatabaseConnection;

public abstract class AbstractDao<T> implements TableDao<T> {

    private static final Logger logger = LogManager.getLogger(AbstractDao.class); // 日志记录器

    protected Connection connection;

    public AbstractDao() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(T entity) {
        String sql = getInsertSQL();
        logger.info("准备执行保存操作，SQL: {}", sql); // 记录SQL语句
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setInsertParameters(statement, entity);
            int affectedRows = statement.executeUpdate();
            connection.commit(); // 提交事务
            logger.info("保存操作成功，受影响的行数: {}", affectedRows);

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        setEntityId(entity, generatedKeys.getLong(1));
                        logger.info("生成的主键 ID: {}", generatedKeys.getLong(1));
                    }
                }
            }

        } catch (SQLException e) {
            logger.error("保存操作失败，错误信息: ", e); // 记录异常信息
        }
    }

    @Override
    public void update(T entity) {
        String sql = getUpdateSQL();
        logger.info("准备执行更新操作，SQL: {}", sql);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            setUpdateParameters(statement, entity);
            int affectedRows = statement.executeUpdate();
            connection.commit(); // 提交事务
            logger.info("更新操作成功，受影响的行数: {}", affectedRows);

        } catch (SQLException e) {
            logger.error("更新操作失败，错误信息: ", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM " + getTableName() + " WHERE " + getIdName() + "= ?";
        logger.info("准备执行删除操作，SQL: {}", sql);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            int affectedRows = statement.executeUpdate();
            connection.commit(); // 提交事务
            logger.info("删除操作成功，受影响的行数: {}", affectedRows);

        } catch (SQLException e) {
            logger.error("删除操作失败，错误信息: ", e);
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
        logger.info("准备执行查询操作，SQL: {}", sql);

        try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }

            ResultSet resultSet = statement.executeQuery();
            connection.commit(); // 提交事务

            while (resultSet.next()) {
                results.add(mapRowToEntity(resultSet));
            }
            logger.info("查询操作成功，返回结果数: {}", results.size());

        } catch (SQLException e) {
            logger.error("查询操作失败，错误信息: ", e);
        }

        return results;
    }

    @Override
    public List<T> getAll() {
        logger.info("准备获取所有数据");
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
