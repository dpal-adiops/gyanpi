package  com.adiops.apigateway.app.role.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.adiops.apigateway.common.helper.CSVHelper;
import com.adiops.apigateway.common.response.RestException;
 
import com.adiops.apigateway.app.role.entity.AppRoleEntity;
import com.adiops.apigateway.app.role.repository.AppRoleRepository;
import com.adiops.apigateway.app.role.resourceobject.AppRoleRO;

import com.adiops.apigateway.app.user.entity.AppUserEntity;
import com.adiops.apigateway.app.user.repository.AppUserRepository;
import com.adiops.apigateway.app.user.resourceobject.AppUserRO;




/**
 * This is the implementation class for AppRole responsible for all the CRUD operations and other business related operations.
 * @author Deepak Pal
 *
 */
@Service
public class AppRoleService{

	@Autowired
	AppRoleRepository mAppRoleRepository;

	@Autowired
	AppUserRepository mAppUserRepository;
	
	
	
	
	@Autowired
	private ModelMapper mModelMapper;

	/**
	 *
	 * fetch list of AppRole
	 */
	
	public List<AppRoleRO> getAppRoleROs() {
		List<AppRoleRO> tAppRoleROs = mAppRoleRepository.findAll(Sort.by(Sort.Direction.ASC, "keyid")).stream()
				.map(entity -> mModelMapper.map(entity, AppRoleRO.class)).collect(Collectors.toList());
		return tAppRoleROs;
	}

	/**
	 *
	 * get AppRole by id
	 */
	public AppRoleRO getAppRoleById(Long id) throws RestException {
        Optional<AppRoleEntity> tAppRole = mAppRoleRepository.findById(id);
         
        if(tAppRole.isPresent()) {
            return mModelMapper.map(tAppRole.get(), AppRoleRO.class);
        } else {
            throw new RestException("No app_role record exist for given id");
        }
    }
	
	 
	/**
	 * create or update 
	 */
	public AppRoleRO createOrUpdateAppRole(AppRoleRO tAppRoleRO) throws RestException 
    {
        AppRoleEntity newEntity ;
         
        if(tAppRoleRO.getId()!=null)
        {
        	newEntity=	 mAppRoleRepository.findById(tAppRoleRO.getId()).orElse(new AppRoleEntity());
        }
        else
        {
        	newEntity=new AppRoleEntity();
        }
       
              
      
        	if(tAppRoleRO.getKeyid() !=null)
        	newEntity.setKeyid(tAppRoleRO.getKeyid());
        	if(tAppRoleRO.getName() !=null)
        	newEntity.setName(tAppRoleRO.getName());
        	if(tAppRoleRO.getDescription() !=null)
        	newEntity.setDescription(tAppRoleRO.getDescription());
        	if(tAppRoleRO.getPermission() !=null)
        	newEntity.setPermission(tAppRoleRO.getPermission());

  	 try {
        	 newEntity = mAppRoleRepository.save(newEntity);   
		} catch (Exception e) {
			throw new RestException("Could not save result due to unique key exception");
		}
                 
        return  mModelMapper.map(newEntity, AppRoleRO.class);
    } 
     
	/**
	 * delete by Id
	 */
     public void deleteAppRoleById(Long id) throws RestException {
       Optional<AppRoleEntity> tAppRole = mAppRoleRepository.findById(id);
         
        if(tAppRole.isPresent()) { 
        
            mAppRoleRepository.deleteById(id);
        } else {
            throw new RestException("No app_role record exist for given id");
        }
    } 
    
	/**
	 * upload file to DB
	 */
	
	public void importCSV(MultipartFile file) throws RestException {
		try {
			List<AppRoleEntity> tAppRoles = CSVHelper.csvToPOJOs(file.getInputStream(), AppRoleEntity.class);
			mAppRoleRepository.deleteAll();
			mAppRoleRepository.saveAll(tAppRoles);
		} catch (IOException e) {
			throw new RestException(RestException.ERROR_STATUS_BAD_REQUEST,
					"fail to store csv data: " + e.getMessage());
		}
	}

	/**
	 * upload stream to DB
	 */
	public void importCSV(InputStream is) throws RestException {
		try {
			List<AppRoleEntity> tAppRoles = CSVHelper.csvToPOJOs(is, AppRoleEntity.class);
			mAppRoleRepository.saveAll(tAppRoles);
		} catch (Exception e) {
			throw new RestException(RestException.ERROR_STATUS_BAD_REQUEST,
					"fail to store csv data: " + e.getMessage());
		}
	}

	/**
	 * get count
	 */
	public long count() {
		return mAppRoleRepository.count();
	}

	/**
	 *
	 * fetch list of AppRole
	 */
	
	public List<AppRoleRO> findAll(int pageNumber, int rowPerPage) {
		Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("id").ascending());
		List<AppRoleRO> tAppRoleROs = mAppRoleRepository.findAll(sortedByIdAsc).stream()
				.map(entity -> mModelMapper.map(entity, AppRoleRO.class)).collect(Collectors.toList());
		return tAppRoleROs;
	}
	
	
	/**
	 *
	 * fetch list of AppRole app_users
	 */
	
	public List<AppUserRO> findAppRoleAppUsers(Long id) {
		List<AppUserRO> tAppUserROs= new ArrayList<>();   
		Optional<AppRoleEntity> tAppRole = mAppRoleRepository.findById(id);
		if(tAppRole.isPresent()) {
			tAppRole.ifPresent(en->{
				en.getAppUsers().forEach(re->{					
					tAppUserROs.add(mModelMapper.map(re, AppUserRO.class));
				});
			});
		}	
		Collections.sort(tAppUserROs, new Comparator<AppUserRO>() {
			  @Override
			  public int compare(AppUserRO u1, AppUserRO u2) {
			    return u1.getKeyid().compareTo(u2.getKeyid());
			  }
			});
						
		return tAppUserROs;
	}
	
	/**
	 *
	 * assign a app_user to app_role
	 */
	
	public void addAppRoleAppUser(Long id, Long app_userId) {
		Optional<AppUserEntity> tAppUser = mAppUserRepository.findById(app_userId);
		Optional<AppRoleEntity> tAppRole = mAppRoleRepository.findById(id);
		if(tAppUser.isPresent() && tAppRole.isPresent()) {
			AppRoleEntity tAppRoleEntity=tAppRole.get();
			AppUserEntity tAppUserEntity= tAppUser.get();
			tAppRoleEntity.getAppUsers().add(tAppUserEntity);
			tAppUserEntity.getAppRoles().add(tAppRoleEntity);
			mAppRoleRepository.save(tAppRoleEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a app_user to app_role
	 */
	
	public void unassignAppRoleAppUser(Long id, Long app_userId) {
		Optional<AppUserEntity> tAppUser = mAppUserRepository.findById(app_userId);
		Optional<AppRoleEntity> tAppRole = mAppRoleRepository.findById(id);
		if(tAppUser.isPresent() && tAppRole.isPresent()) {
			AppRoleEntity tAppRoleEntity=tAppRole.get();
			tAppRoleEntity.getAppUsers().remove(tAppUser.get());
			mAppRoleRepository.save(tAppRoleEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of AppRole app_users
	 */
	
	public List<AppUserRO> findUnassignAppRoleAppUsers(Long id) {
		List<AppUserRO> tAppUserROs= new ArrayList<>();   
		 mAppRoleRepository.findById(id).ifPresent(en->{
			 List<AppUserEntity> tAppUsers = mAppUserRepository.findAll();
			 tAppUsers.removeAll(en.getAppUsers());
			 tAppUsers.forEach(re->{					
					tAppUserROs.add(mModelMapper.map(re, AppUserRO.class));
				});
		 });
		
		return tAppUserROs;
	}
	
	
	
	
	
	public AppRoleRO getAppRoleByKeyId(String key) {
		Optional<?> tAppRole= Optional.ofNullable(mAppRoleRepository.findByKeyid(key));
		 if(tAppRole.isPresent()) {
	            return mModelMapper.map(tAppRole.get(), AppRoleRO.class);
	        }
	      return null;  
	}
	
	
	
	
}
