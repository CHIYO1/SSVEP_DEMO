/**
 * 这个文件包含一个对TestRecordsDao类进行测试的类，
 * 测试是否可以正常对进行TestRecords表进行增删改查操作。
 * 
 * @author 石振山
 * @version 3.1.3
 */
package com.ssvep.dao;

import org.junit.jupiter.api.*;

import com.ssvep.model.TestRecords;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class TestRecordsDaoTest {
    private TestRecordsDao testRecordsDao;

    @BeforeEach
    public void setUp() {
        testRecordsDao = new TestRecordsDao();
    }

    @Test
    void testSaveAndGetRecord() {
        LocalDate date = LocalDate.now();
        TestRecords record = new TestRecords(44L, "type", date, null, "teststring", 7L);
        testRecordsDao.save(record);

        TestRecords retrievedRecord = testRecordsDao.getRecordById(record.getRecordId());

        assertNotNull(retrievedRecord, "Record should be saved and retrievable");
        assertEquals(record.getTestType(), retrievedRecord.getTestType(), "Test types should match");
        assertEquals(record.getTestResults(), retrievedRecord.getTestResults(), "Test results should match");
        assertEquals(record.getstimulusvideoId(), retrievedRecord.getstimulusvideoId(),
                "Video IDs should match");
    }

    @Test
    void testUpdateRecord() {
        LocalDate date = LocalDate.now();
        TestRecords record = new TestRecords(44L, "type", date, null, "teststring", 7L);
        testRecordsDao.save(record);

        record.setTestType("Updated Test Type");
        Map<String, Object> map = new HashMap<>();
        map.put("key1", Integer.valueOf(1));
        record.setTestResults(map);
        record.setRelatedInfo("Updated related info");
        testRecordsDao.update(record);

        TestRecords updatedRecord = testRecordsDao.getRecordById(record.getRecordId());

        assertNotNull(updatedRecord, "Updated record should be retrievable");
        assertEquals("Updated Test Type", updatedRecord.getTestType(), "Test type should be updated");
        assertEquals("Updated related info", updatedRecord.getRelatedInfo(), "Related info should be updated");
    }

    @Test
    void testDeleteRecord() {
        LocalDate date = LocalDate.now();
        TestRecords record = new TestRecords(44L, "type", date, null, "teststring", 7L);
        testRecordsDao.save(record);

        testRecordsDao.delete(record.getRecordId());

        TestRecords deletedRecord = testRecordsDao.getRecordById(record.getRecordId());
        assertNull(deletedRecord, "Deleted record should not be retrievable");
    }
}
