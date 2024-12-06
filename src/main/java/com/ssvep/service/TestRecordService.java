package com.ssvep.service;

import com.ssvep.dao.TestRecordsDao;
import com.ssvep.dto.TestRecordDto;
import com.ssvep.model.TestRecords;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class TestRecordService {
    private static final Logger logger = LogManager.getLogger(TestRecordService.class);
    private TestRecordsDao recordsDao;

    public TestRecordService() {
        recordsDao = new TestRecordsDao();
    }

    // 根据记录ID获取测试记录
    public TestRecordDto getRecordById(Long id) {
        TestRecords record = recordsDao.getRecordById(id);
        if (record != null) {
            logger.info("成功获取测试记录，记录ID：{}", id);
        } else {
            logger.warn("未找到测试记录，记录ID：{}", id);
        }
        return convertToDto(record);
    }

    // 根据用户ID获取测试记录
    public List<TestRecordDto> getRecordsByUser(Long userid) {
        List<TestRecords> records = recordsDao.getRecordsByUser(userid);
        logger.info("成功获取用户ID {} 的测试记录，共{}条", userid, records.size());
        return records.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // 获取所有测试记录
    public List<TestRecordDto> getAllRecords() {
        List<TestRecords> records = recordsDao.getAll();
        logger.info("成功获取所有测试记录，共{}条", records.size());
        return records.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // 创建测试记录
    public void createRecord(TestRecordDto recordDto) throws SQLException {
        TestRecords record = convertToEntity(recordDto);
        recordsDao.save(record);
        logger.info("成功创建测试记录，记录ID：{}", record.getRecordId());
    }

    // 更新测试记录
    public void updateRecord(TestRecordDto recordDto) throws SQLException {
        TestRecords record = convertToEntity(recordDto);
        recordsDao.update(record);
        logger.info("成功更新测试记录，记录ID：{}", recordDto.getRecordId());
    }

    // 删除测试记录
    public void deleteRecord(Long id) throws SQLException {
        recordsDao.delete(id);
        logger.info("成功删除测试记录，记录ID：{}", id);
    }

    // DTO到实体的转换
    private TestRecordDto convertToDto(TestRecords record) {
        if (record == null) {
            return null;
        }
        return new TestRecordDto(record.getRecordId(), record.getUserId(), record.getTestType(), record.getTestDate(), record.getTestResults(), record.getRelatedInfo(), record.getstimulusvideoId());
    }

    // 实体到DTO的转换
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
