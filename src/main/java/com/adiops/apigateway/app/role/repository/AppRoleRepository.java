package  com.adiops.apigateway.app.role.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import  com.adiops.apigateway.app.role.entity.AppRoleEntity;

/**
 * AppRole Data JPA repository interface.
 * @author Deepak Pal
 *
 */
@Repository
public interface AppRoleRepository extends JpaRepository<AppRoleEntity, Long>{

}
