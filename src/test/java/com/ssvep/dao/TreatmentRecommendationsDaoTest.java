/**
 * 这个文件包含一个对TreatmentRecommendationsDao类进行测试的类，
 * 测试是否可以正常对进行TreatmentRecommendations表进行增删改查操作。
 * 
 * @author 石振山
 * @version 1.2.3
 */
package com.ssvep.dao;

import org.junit.jupiter.api.*;
import com.ssvep.model.TreatmentRecommendations;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;

public class TreatmentRecommendationsDaoTest {
     private TreatmentRecommendationsDao recommendationsDao;

    @BeforeEach
    public void setUp() {
        recommendationsDao = new TreatmentRecommendationsDao();
    }

    @Test
    public void testSaveRecommendation() {
        TreatmentRecommendations recommendation = new TreatmentRecommendations(44L, null);
        recommendationsDao.save(recommendation);

        TreatmentRecommendations retrievedRecommendation = recommendationsDao.getRecommendationById(recommendation.getRecommendationId());

        assertNotNull(retrievedRecommendation, "Recommendation should be saved and retrievable");
        assertEquals(recommendation.getUserId(), retrievedRecommendation.getUserId(), "User IDs should match");
        assertEquals(recommendation.getAdvice(), retrievedRecommendation.getAdvice(), "Advices should match");
    }

    @Test
    public void testGetRecommendationByUser() {
        TreatmentRecommendations recommendation1 = new TreatmentRecommendations(45L, null);
        recommendationsDao.save(recommendation1);

        TreatmentRecommendations recommendation2 = new TreatmentRecommendations(45L, null);
        recommendationsDao.save(recommendation2);

        List<TreatmentRecommendations> recommendations = recommendationsDao.getRecommendationByUser(45L);
        assertEquals(2, recommendations.size(), "There should be 2 recommendations for user 45");
    }

    @Test
    public void testUpdateRecommendation() {
        TreatmentRecommendations recommendation = new TreatmentRecommendations(44L, null);
        recommendationsDao.save(recommendation);

        recommendation.setAdvice(new HashMap<String,Object>() {{
            put("updated", "newValue");
        }});
        recommendationsDao.update(recommendation);

        TreatmentRecommendations updatedRecommendation = recommendationsDao.getRecommendationById(recommendation.getRecommendationId());
        assertEquals("newValue", updatedRecommendation.getAdvice().get("updated"), "Advice should be updated");
    }

    @Test
    public void testDeleteRecommendation() {
        TreatmentRecommendations recommendation = new TreatmentRecommendations(44L, null);
        recommendationsDao.save(recommendation);

        recommendationsDao.delete(recommendation.getRecommendationId());

        TreatmentRecommendations deletedRecommendation = recommendationsDao.getRecommendationById(recommendation.getRecommendationId());
        assertNull(deletedRecommendation, "Recommendation should be deleted and not retrievable");
    }

}
