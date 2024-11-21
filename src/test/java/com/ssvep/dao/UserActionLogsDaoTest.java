/**
 * 这个文件包含一个对UserActionLogsDao类进行测试的类，
 * 测试是否可以正常对进行UserActionLogs表进行增删改查操作。
 * 
 * @author 石振山
 * @version 1.1.3
 */
package com.ssvep.dao;

import org.junit.jupiter.api.*;

import com.ssvep.model.UserActionLogs;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

public class UserActionLogsDaoTest {
    private UserActionLogsDao logDao;

    @BeforeEach
    public void setup(){
        logDao=new UserActionLogsDao();
    }

    @Test
    public void testSaveLog() {
        UserActionLogs log = new UserActionLogs(44L,"LOGIN",LocalDateTime.now());
        
        logDao.save(log);

        UserActionLogs retrievedLog = logDao.getLogById(log.getLogId());

        assertNotNull(retrievedLog, "Log should be saved and retrievable");
        assertEquals(log.getUserId(), retrievedLog.getUserId(), "User IDs should match");
        assertEquals(log.getActionType(), retrievedLog.getActionType(), "Action types should match");
    }

    @Test
    public void testGetAllLogs() {
        UserActionLogs log1 = new UserActionLogs(44L,"LOGIN",LocalDateTime.now());
        logDao.save(log1);

        UserActionLogs log2 = new UserActionLogs(44L,"LOGIN",LocalDateTime.now());
        logDao.save(log2);
        
        List<UserActionLogs> logs = logDao.getAll();
        assertTrue(logs.size() >= 2, "There should be at least 2 logs in the database");
    }

    @Test
    public void testUpdateLog() {
        UserActionLogs log = new UserActionLogs(44L,"LOGIN",LocalDateTime.now());
        logDao.save(log);

        log.setActionType("UPDATE");
        logDao.update(log);

        UserActionLogs updatedLog = logDao.getLogById(log.getLogId());
        assertEquals("UPDATE", updatedLog.getActionType(), "Action type should be updated");
    }

    @Test
    public void testDeleteLog() {
        UserActionLogs log = new UserActionLogs(44L,"LOGIN",LocalDateTime.now());
        logDao.save(log);

        logDao.delete(log.getLogId());

        UserActionLogs deletedLog = logDao.getLogById(log.getLogId());
        assertNull(deletedLog, "Log should be deleted and not retrievable");
    }

    @Test
    public void testGetLogsByUser() {
        UserActionLogs log1 = new UserActionLogs(45L,"LOGIN",LocalDateTime.now());
        logDao.save(log1);

        UserActionLogs log2 = new UserActionLogs(45L,"LOGIN",LocalDateTime.now());
        logDao.save(log2);

        List<UserActionLogs> logs = logDao.getLogsByUser(45L);
        assertNotNull(logs, "Logs should not be null");
        assertEquals(2, logs.size(), "There should be 2 logs for user with ID 45");
    }

}
