package  com.adiops.apigateway.learning.track.path.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import  com.adiops.apigateway.learning.track.path.entity.LearningTrackPathEntity;

/**
 * LearningTrackPath Data JPA repository interface.
 * @author Deepak Pal
 *
 */
@Repository
public interface LearningTrackPathRepository extends JpaRepository<LearningTrackPathEntity, Long>{

	LearningTrackPathEntity findByKeyid(String key);
}
