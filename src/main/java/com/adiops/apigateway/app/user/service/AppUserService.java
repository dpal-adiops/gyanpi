package  com.adiops.apigateway.app.user.service;

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
 
import com.adiops.apigateway.app.user.entity.AppUserEntity;
import com.adiops.apigateway.app.user.repository.AppUserRepository;
import com.adiops.apigateway.app.user.resourceobject.AppUserRO;

import com.adiops.apigateway.app.role.entity.AppRoleEntity;
import com.adiops.apigateway.app.role.repository.AppRoleRepository;
import com.adiops.apigateway.app.role.resourceobject.AppRoleRO;


/**
 * This is the implementation class for AppUser responsible for all the CRUD operations and other business related operations.
 * @author Deepak Pal
 *
 */
@Service
public class AppUserService{

	@Autowired
	AppUserRepository mAppUserRepository;

	@Autowired
	AppRoleRepository mAppRoleRepository;
	@Autowired
	private ModelMapper mModelMapper;

	/**
	 *
	 * fetch list of AppUser
	 */
	
	public List<AppUserRO> getAppUserROs() {
		List<AppUserRO> tAppUserROs = mAppUserRepository.findAll(Sort.by(Sort.Direction.ASC, "keyid")).stream()
				.map(entity -> mModelMapper.map(entity, AppUserRO.class)).collect(Collectors.toList());
		return tAppUserROs;
	}

	/**
	 *
	 * get AppUser by id
	 */
	public AppUserRO getAppUserById(Long id) throws RestException {
        Optional<AppUserEntity> tAppUser = mAppUserRepository.findById(id);
         
        if(tAppUser.isPresent()) {
            return mModelMapper.map(tAppUser.get(), AppUserRO.class);
        } else {
            throw new RestException("No app_user record exist for given id");
        }
    }
	
	 
	/**
	 * create or update 
	 */
	public AppUserRO createOrUpdateAppUser(AppUserRO tAppUserRO) throws RestException 
    {
        AppUserEntity newEntity ;
         
        if(tAppUserRO.getId()!=null)
        {
        	newEntity=	 mAppUserRepository.findById(tAppUserRO.getId()).orElse(new AppUserEntity());
        }
        else
        {
        	newEntity=new AppUserEntity();
        }
       
              
      
        	if(tAppUserRO.getKeyid() !=null)
        	newEntity.setKeyid(tAppUserRO.getKeyid());
        	if(tAppUserRO.getUserName() !=null)
        	newEntity.setUserName(tAppUserRO.getUserName());
        	if(tAppUserRO.getEmail() !=null)
        	newEntity.setEmail(tAppUserRO.getEmail());
        	if(tAppUserRO.getFirstName() !=null)
        	newEntity.setFirstName(tAppUserRO.getFirstName());
        	if(tAppUserRO.getLastName() !=null)
        	newEntity.setLastName(tAppUserRO.getLastName());
        	if(tAppUserRO.getEncryptedPassword() !=null)
        	newEntity.setEncryptedPassword(tAppUserRO.getEncryptedPassword());
        	if(tAppUserRO.getEnabled() !=null)
        	newEntity.setEnabled(tAppUserRO.getEnabled());

  	 try {
        	 newEntity = mAppUserRepository.save(newEntity);   
		} catch (Exception e) {
			throw new RestException("Could not save result due to unique key exception");
		}
                 
        return  mModelMapper.map(newEntity, AppUserRO.class);
    } 
     
	/**
	 * delete by Id
	 */
     public void deleteAppUserById(Long id) throws RestException {
       Optional<AppUserEntity> tAppUser = mAppUserRepository.findById(id);
         
        if(tAppUser.isPresent()) { 
        
            mAppUserRepository.deleteById(id);
        } else {
            throw new RestException("No app_user record exist for given id");
        }
    } 
    
	/**
	 * upload file to DB
	 */
	
	public void importCSV(MultipartFile file) throws RestException {
		try {
			List<AppUserEntity> tAppUsers = CSVHelper.csvToPOJOs(file.getInputStream(), AppUserEntity.class);
			mAppUserRepository.deleteAll();
			mAppUserRepository.saveAll(tAppUsers);
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
			List<AppUserEntity> tAppUsers = CSVHelper.csvToPOJOs(is, AppUserEntity.class);
			mAppUserRepository.saveAll(tAppUsers);
		} catch (Exception e) {
			throw new RestException(RestException.ERROR_STATUS_BAD_REQUEST,
					"fail to store csv data: " + e.getMessage());
		}
	}

	/**
	 * get count
	 */
	public long count() {
		return mAppUserRepository.count();
	}

	/**
	 *
	 * fetch list of AppUser
	 */
	
	public List<AppUserRO> findAll(int pageNumber, int rowPerPage) {
		Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("id").ascending());
		List<AppUserRO> tAppUserROs = mAppUserRepository.findAll(sortedByIdAsc).stream()
				.map(entity -> mModelMapper.map(entity, AppUserRO.class)).collect(Collectors.toList());
		return tAppUserROs;
	}
	
	
	/**
	 *
	 * fetch list of AppUser app_roles
	 */
	
	public List<AppRoleRO> findAppUserAppRoles(Long id) {
		List<AppRoleRO> tAppRoleROs= new ArrayList<>();   
		Optional<AppUserEntity> tAppUser = mAppUserRepository.findById(id);
		if(tAppUser.isPresent()) {
			tAppUser.ifPresent(en->{
				en.getAppRoles().forEach(re->{					
					tAppRoleROs.add(mModelMapper.map(re, AppRoleRO.class));
				});
			});
		}	
		Collections.sort(tAppRoleROs, new Comparator<AppRoleRO>() {
			  @Override
			  public int compare(AppRoleRO u1, AppRoleRO u2) {
			    return u1.getKeyid().compareTo(u2.getKeyid());
			  }
			});
						
		return tAppRoleROs;
	}
	
	/**
	 *
	 * assign a app_role to app_user
	 */
	
	public void addAppUserAppRole(Long id, Long app_roleId) {
		Optional<AppRoleEntity> tAppRole = mAppRoleRepository.findById(app_roleId);
		Optional<AppUserEntity> tAppUser = mAppUserRepository.findById(id);
		if(tAppRole.isPresent() && tAppUser.isPresent()) {
			AppUserEntity tAppUserEntity=tAppUser.get();
			AppRoleEntity tAppRoleEntity= tAppRole.get();
			tAppUserEntity.getAppRoles().add(tAppRoleEntity);
			tAppRoleEntity.getAppUsers().add(tAppUserEntity);
			mAppUserRepository.save(tAppUserEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a app_role to app_user
	 */
	
	public void unassignAppUserAppRole(Long id, Long app_roleId) {
		Optional<AppRoleEntity> tAppRole = mAppRoleRepository.findById(app_roleId);
		Optional<AppUserEntity> tAppUser = mAppUserRepository.findById(id);
		if(tAppRole.isPresent() && tAppUser.isPresent()) {
			AppUserEntity tAppUserEntity=tAppUser.get();
			tAppUserEntity.getAppRoles().remove(tAppRole.get());
			mAppUserRepository.save(tAppUserEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of AppUser app_roles
	 */
	
	public List<AppRoleRO> findUnassignAppUserAppRoles(Long id) {
		List<AppRoleRO> tAppRoleROs= new ArrayList<>();   
		 mAppUserRepository.findById(id).ifPresent(en->{
			 List<AppRoleEntity> tAppRoles = mAppRoleRepository.findAll();
			 tAppRoles.removeAll(en.getAppRoles());
			 tAppRoles.forEach(re->{					
					tAppRoleROs.add(mModelMapper.map(re, AppRoleRO.class));
				});
		 });
		
		return tAppRoleROs;
	}
	
	
}
