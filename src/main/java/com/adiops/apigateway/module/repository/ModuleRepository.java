package  com.adiops.apigateway.module.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import  com.adiops.apigateway.module.entity.ModuleEntity;

/**
 * Module Data JPA repository interface.
 * @author Deepak Pal
 *
 */
@Repository
public interface ModuleRepository extends JpaRepository<ModuleEntity, Long>{

	ModuleEntity findByKeyid(String key);
}
