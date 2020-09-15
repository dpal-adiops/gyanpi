package  com.adiops.apigateway.question.line.item.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import  com.adiops.apigateway.question.line.item.entity.QuestionLineItemEntity;

/**
 * QuestionLineItem Data JPA repository interface.
 * @author Deepak Pal
 *
 */
@Repository
public interface QuestionLineItemRepository extends JpaRepository<QuestionLineItemEntity, Long>{

	QuestionLineItemEntity findByKeyid(String key);
}
