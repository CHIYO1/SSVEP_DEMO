/**
 * 这个类负责处理检测记录的业务逻辑，并调用 TestRecordDao 来执行数据访问操作。
 * 
 * @author 石振山
 * @version 1.2.1
 */
package com.ssvep.service;

import com.ssvep.dao.TestRecordsDao;
import com.ssvep.dto.TestRecordDto;
import com.ssvep.model.TestRecords;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class TestRecordService {
    private TestRecordsDao recordsDao;

    public TestRecordService() {
        recordsDao = new TestRecordsDao();
    }

    public TestRecordDto getRecordById(Long id) {
        TestRecords record = recordsDao.getRecordById(id);
        return convertToDto(record);
    }

    public List<TestRecordDto> getRecordsByUser(Long userid) {
        List<TestRecords> records = recordsDao.getRecordsByUser(userid);

        return records.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<TestRecordDto> getAllRecords() {
        List<TestRecords> records = recordsDao.getAll();

        return records.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public void createRecord(TestRecordDto recordDto) throws SQLException {
        TestRecords record = convertToEntity(recordDto);

        recordsDao.save(record);
    }

    public void updateRecord(TestRecordDto recordDto) throws SQLException {
        TestRecords record = convertToEntity(recordDto);

        recordsDao.update(record);
    }

    public void deleteRecord(Long id) throws SQLException {
        recordsDao.delete(id);
    }

    private TestRecordDto convertToDto(TestRecords record) {
        if (record == null) {
            return null;
        }
        return new TestRecordDto(record.getRecordId(), record.getUserId(), record.getTestType(), record.getTestDate(), record.getTestResults(), record.getRelatedInfo(), record.getstimulusvideoId());
    }

    private TestRecords convertToEntity(TestRecordDto recordDto) {
        TestRecords record = new TestRecords();
        record.setRecordId(recordDto.getRecordId());
        record.setUserId(recordDto.getUserId());
        record.setTestType(recordDto.getTestType());
        record.setTestDate(recordDto.getTestDate());
        record.setTestResults(recordDto.getTestResults());
        record.setRelatedInfo(recordDto.getRelatedInfo());
        record.setstimulusvideoId(recordDto.getStimulusVideoId());

        return record;
    }

}
