package  com.adiops.apigateway.course.line.group.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import  com.adiops.apigateway.course.line.group.entity.CourseLineGroupEntity;

/**
 * CourseLineGroup Data JPA repository interface.
 * @author Deepak Pal
 *
 */
@Repository
public interface CourseLineGroupRepository extends JpaRepository<CourseLineGroupEntity, Long>{

	CourseLineGroupEntity findByKeyid(String key);
}
