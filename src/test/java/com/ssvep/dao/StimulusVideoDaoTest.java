/**
 * 这个文件包含一个对StimulusVideoDao类进行测试的类，
 * 测试是否可以正常对进行StimulusVideo表进行增删改查操作。
 * 
 * @author 石振山
 * @version 2.1.3
 */
package com.ssvep.dao;

import org.junit.jupiter.api.*;

import com.ssvep.model.StimulusVideos;
import static org.junit.jupiter.api.Assertions.*;

public class StimulusVideoDaoTest {
    private StimulusVideosDao videoDao;
    
    @BeforeEach
    public void setUp() {
        videoDao = new StimulusVideosDao(); 
    }

    @Test
    void testSaveAndGetVideo() {
        StimulusVideos video = new StimulusVideos("Test Type","http://example.com/video.mp4");

        videoDao.save(video);

        StimulusVideos retrievedVideo = videoDao.getVideoById(video.getVideoId());

        assertNotNull(retrievedVideo, "Video should be saved and retrievable");
        assertEquals(video.getTestType(), retrievedVideo.getTestType(), "Test types should match");
        assertEquals(video.getVideoUrl(), retrievedVideo.getVideoUrl(), "Video URLs should match");
    }

    @Test
    void testUpdateVideo() {
        StimulusVideos video = new StimulusVideos("Test Type2","http://example.com/video.mp4");

        videoDao.save(video);

        video.setTestType("Updated Test Type");
        video.setVideoUrl("http://example.com/updated_video.mp4");
        videoDao.update(video);

        StimulusVideos updatedVideo = videoDao.getVideoById(video.getVideoId());

        assertNotNull(updatedVideo, "Updated video should be retrievable");
        assertEquals("Updated Test Type", updatedVideo.getTestType(), "Test type should be updated");
        assertEquals("http://example.com/updated_video.mp4", updatedVideo.getVideoUrl(), "Video URL should be updated");
    }

    @Test
    void testDeleteVideo() {
        StimulusVideos video = new StimulusVideos("Test Type","http://example.com/video.mp4");

        videoDao.save(video);

        videoDao.delete(video.getVideoId());

        StimulusVideos deletedVideo = videoDao.getVideoById(video.getVideoId());
        assertNull(deletedVideo, "Deleted video should not be retrievable");
    }

}
