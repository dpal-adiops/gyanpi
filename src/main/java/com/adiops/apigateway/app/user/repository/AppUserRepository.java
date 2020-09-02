package  com.adiops.apigateway.app.user.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import  com.adiops.apigateway.app.user.entity.AppUserEntity;

/**
 * AppUser Data JPA repository interface.
 * @author Deepak Pal
 *
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUserEntity, Long>{

}
