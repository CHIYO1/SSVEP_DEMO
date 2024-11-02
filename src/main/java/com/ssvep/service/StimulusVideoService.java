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

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class StimulusVideoService {
     private StimulusVideosDao videosDao;

    public StimulusVideoService() {
        videosDao = new StimulusVideosDao();
    }

    public StimulusVideoDto getVideoById(Long id) {
        StimulusVideos video = videosDao.getVideoById(id);
        return convertToDto(video);
    }

    public List<StimulusVideoDto> getAllVideos() {
        List<StimulusVideos> videos = videosDao.getAll();

        return videos.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public void createVideo(StimulusVideoDto videoDto) throws SQLException {
        StimulusVideos video = convertToEntity(videoDto);

        videosDao.save(video);
    }

    public void updateVideo(StimulusVideoDto videoDto) throws SQLException {
        StimulusVideos video = convertToEntity(videoDto);

        videosDao.update(video);
    }

    public void deleteVideo(Long id) throws SQLException {
        videosDao.delete(id);
    }

    private StimulusVideoDto convertToDto(StimulusVideos video) {
        if (video == null) {
            return null;
        }
        return new StimulusVideoDto(video.getVideoId(), video.getTestType(), video.getVideoUrl());
    }

    private StimulusVideos convertToEntity(StimulusVideoDto videoDto) {
        StimulusVideos video = new StimulusVideos();
        video.setVideoId(videoDto.getVideoId());
        video.setTestType(videoDto.getTestType());
        video.setVideoUrl(videoDto.getVideoUrl());
        return video;
    }

}
