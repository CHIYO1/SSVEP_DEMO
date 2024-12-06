/**
 * 这个类负责处理刺激视频的业务逻辑，并调用 StimulusVideoDao 来执行数据访问操作。
 * 
 * @author 石振山
 * @version 1.2.1
 */
package com.ssvep.service;

import com.ssvep.dao.StimulusVideosDao;
import com.ssvep.dto.StimulusVideoDto;
import com.ssvep.model.StimulusVideos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class StimulusVideoService {
    private static final Logger logger = LogManager.getLogger(StimulusVideoService.class);
    private StimulusVideosDao videosDao;

    public StimulusVideoService() {
        videosDao = new StimulusVideosDao();
    }

    // 根据视频ID获取视频
    public StimulusVideoDto getVideoById(Long id) {
        StimulusVideos video = videosDao.getVideoById(id);
        if (video != null) {
            logger.info("成功获取视频，视频ID：{}", id);
        } else {
            logger.warn("未找到视频，视频ID：{}", id);
        }
        return convertToDto(video);
    }

    // 获取所有视频
    public List<StimulusVideoDto> getAllVideos() {
        List<StimulusVideos> videos = videosDao.getAll();
        logger.info("成功获取{}条视频记录", videos.size());
        return videos.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // 创建视频
    public void createVideo(StimulusVideoDto videoDto) throws SQLException {
        StimulusVideos video = convertToEntity(videoDto);
        videosDao.save(video);
        logger.info("成功创建视频，视频ID：{}", video.getVideoId());
    }

    // 更新视频
    public void updateVideo(StimulusVideoDto videoDto) throws SQLException {
        StimulusVideos video = convertToEntity(videoDto);
        videosDao.update(video);
        logger.info("成功更新视频，视频ID：{}", videoDto.getVideoId());
    }

    // 删除视频
    public void deleteVideo(Long id) throws SQLException {
        videosDao.delete(id);
        logger.info("成功删除视频，视频ID：{}", id);
    }

    // DTO到实体的转换
    private StimulusVideoDto convertToDto(StimulusVideos video) {
        if (video == null) {
            return null;
        }
        return new StimulusVideoDto(video.getVideoId(), video.getTestType(), video.getVideoUrl());
    }

    // 实体到DTO的转换
    private StimulusVideos convertToEntity(StimulusVideoDto videoDto) {
        StimulusVideos video = new StimulusVideos();
        video.setVideoId(videoDto.getVideoId());
        video.setTestType(videoDto.getTestType());
        video.setVideoUrl(videoDto.getVideoUrl());
        return video;
    }
}

