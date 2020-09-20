package  com.adiops.apigateway.learning.track.service;

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
 
import com.adiops.apigateway.learning.track.entity.LearningTrackEntity;
import com.adiops.apigateway.learning.track.repository.LearningTrackRepository;
import com.adiops.apigateway.learning.track.resourceobject.LearningTrackRO;

import com.adiops.apigateway.learning.track.path.entity.LearningTrackPathEntity;
import com.adiops.apigateway.learning.track.path.repository.LearningTrackPathRepository;
import com.adiops.apigateway.learning.track.path.resourceobject.LearningTrackPathRO;

import com.adiops.apigateway.learning.track.entity.LearningTrackEntity;
import com.adiops.apigateway.learning.track.repository.LearningTrackRepository;
import com.adiops.apigateway.learning.track.resourceobject.LearningTrackRO;

import com.adiops.apigateway.app.user.entity.AppUserEntity;
import com.adiops.apigateway.app.user.repository.AppUserRepository;
import com.adiops.apigateway.app.user.resourceobject.AppUserRO;


/**
 * This is the implementation class for LearningTrack responsible for all the CRUD operations and other business related operations.
 * @author Deepak Pal
 *
 */
@Service
public class LearningTrackService{

	@Autowired
	LearningTrackRepository mLearningTrackRepository;

	
	@Autowired
	LearningTrackPathRepository mLearningTrackPathRepository;
	
	
	@Autowired
	AppUserRepository mAppUserRepository;
	
	@Autowired
	private ModelMapper mModelMapper;

	/**
	 *
	 * fetch list of LearningTrack
	 */
	
	public List<LearningTrackRO> getLearningTrackROs() {
		List<LearningTrackRO> tLearningTrackROs = mLearningTrackRepository.findAll(Sort.by(Sort.Direction.ASC, "keyid")).stream()
				.map(entity -> mModelMapper.map(entity, LearningTrackRO.class)).collect(Collectors.toList());
		return tLearningTrackROs;
	}

	/**
	 *
	 * get LearningTrack by id
	 */
	public LearningTrackRO getLearningTrackById(Long id) throws RestException {
        Optional<LearningTrackEntity> tLearningTrack = mLearningTrackRepository.findById(id);
         
        if(tLearningTrack.isPresent()) {
            return mModelMapper.map(tLearningTrack.get(), LearningTrackRO.class);
        } else {
            throw new RestException("No learning_track record exist for given id");
        }
    }
	
	 
	/**
	 * create or update 
	 */
	public LearningTrackRO createOrUpdateLearningTrack(LearningTrackRO tLearningTrackRO) throws RestException 
    {
        LearningTrackEntity newEntity ;
         
        if(tLearningTrackRO.getId()!=null)
        {
        	newEntity=	 mLearningTrackRepository.findById(tLearningTrackRO.getId()).orElse(new LearningTrackEntity());
        }
        else
        {
        	newEntity=new LearningTrackEntity();
        }
       
              
      
        	if(tLearningTrackRO.getKeyid() !=null)
        	newEntity.setKeyid(tLearningTrackRO.getKeyid());
        	if(tLearningTrackRO.getName() !=null)
        	newEntity.setName(tLearningTrackRO.getName());
        	if(tLearningTrackRO.getCredit() !=null)
        	newEntity.setCredit(tLearningTrackRO.getCredit());
        	if(tLearningTrackRO.getScore() !=null)
        	newEntity.setScore(tLearningTrackRO.getScore());
        	if(tLearningTrackRO.getMaxScore() !=null)
        	newEntity.setMaxScore(tLearningTrackRO.getMaxScore());
        	if(tLearningTrackRO.getTotalStep() !=null)
        	newEntity.setTotalStep(tLearningTrackRO.getTotalStep());
        	if(tLearningTrackRO.getStatus() !=null)
        	newEntity.setStatus(tLearningTrackRO.getStatus());
        	if(tLearningTrackRO.getRefid() !=null)
        	newEntity.setRefid(tLearningTrackRO.getRefid());

  	 try {
        	 newEntity = mLearningTrackRepository.save(newEntity);   
		} catch (Exception e) {
			throw new RestException("Could not save result due to unique key exception");
		}
                 
        return  mModelMapper.map(newEntity, LearningTrackRO.class);
    } 
     
	/**
	 * delete by Id
	 */
     public void deleteLearningTrackById(Long id) throws RestException {
       Optional<LearningTrackEntity> tLearningTrack = mLearningTrackRepository.findById(id);
         
        if(tLearningTrack.isPresent()) { 
        
            mLearningTrackRepository.deleteById(id);
        } else {
            throw new RestException("No learning_track record exist for given id");
        }
    } 
    
	/**
	 * upload file to DB
	 */
	
	public void importCSV(MultipartFile file) throws RestException {
		try {
			List<LearningTrackEntity> tLearningTracks = CSVHelper.csvToPOJOs(file.getInputStream(), LearningTrackEntity.class);
			tLearningTracks=tLearningTracks.stream().map(entity->{
				LearningTrackEntity tLearningTrackEntity=mLearningTrackRepository.findByKeyid(entity.getKeyid());
				if(tLearningTrackEntity!=null) {
					entity.setId(tLearningTrackEntity.getId());
				}
				return entity;
			}).collect(Collectors.toList());
			mLearningTrackRepository.saveAll(tLearningTracks);
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
			List<LearningTrackEntity> tLearningTracks = CSVHelper.csvToPOJOs(is, LearningTrackEntity.class);
			tLearningTracks=tLearningTracks.stream().map(entity->{
				LearningTrackEntity tLearningTrackEntity=mLearningTrackRepository.findByKeyid(entity.getKeyid());
				if(tLearningTrackEntity!=null) {
					entity.setId(tLearningTrackEntity.getId());
				}
				return entity;
			}).collect(Collectors.toList());
			mLearningTrackRepository.saveAll(tLearningTracks);
		} catch (Exception e) {
			throw new RestException(RestException.ERROR_STATUS_BAD_REQUEST,
					"fail to store csv data: " + e.getMessage());
		}
	}

	/**
	 * get count
	 */
	public long count() {
		return mLearningTrackRepository.count();
	}

	/**
	 *
	 * fetch list of LearningTrack
	 */
	
	public List<LearningTrackRO> findAll(int pageNumber, int rowPerPage) {
		Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("id").ascending());
		List<LearningTrackRO> tLearningTrackROs = mLearningTrackRepository.findAll(sortedByIdAsc).stream()
				.map(entity -> mModelMapper.map(entity, LearningTrackRO.class)).collect(Collectors.toList());
		return tLearningTrackROs;
	}
	
	
	
	
	/**
	 *
	 * fetch list of LearningTrack learning_track_paths
	 */
	
	public List<LearningTrackPathRO> findLearningTrackLearningTrackPaths(Long id) {
		List<LearningTrackPathRO> tLearningTrackPathROs= new ArrayList<>();   
		Optional<LearningTrackEntity> tLearningTrack = mLearningTrackRepository.findById(id);
		if(tLearningTrack.isPresent()) {
			tLearningTrack.ifPresent(en->{
				en.getLearningTrackPaths().forEach(re->{					
					tLearningTrackPathROs.add(mModelMapper.map(re, LearningTrackPathRO.class));
				});
			});
		}	
		Collections.sort(tLearningTrackPathROs, new Comparator<LearningTrackPathRO>() {
			  @Override
			  public int compare(LearningTrackPathRO u1, LearningTrackPathRO u2) {
			    return u1.getKeyid().compareTo(u2.getKeyid());
			  }
			});
						
		return tLearningTrackPathROs;
	}
	
	/**
	 *
	 * assign a learning_track_path to learning_track
	 */
	
	public void addLearningTrackLearningTrackPath(Long id, Long learning_track_pathId) {
		Optional<LearningTrackPathEntity> tLearningTrackPath = mLearningTrackPathRepository.findById(learning_track_pathId);
		Optional<LearningTrackEntity> tLearningTrack = mLearningTrackRepository.findById(id);
		if(tLearningTrackPath.isPresent() && tLearningTrack.isPresent()) {
			LearningTrackEntity tLearningTrackEntity=tLearningTrack.get();
			LearningTrackPathEntity tLearningTrackPathEntity= tLearningTrackPath.get();
			tLearningTrackEntity.getLearningTrackPaths().add(tLearningTrackPathEntity);
			tLearningTrackPathEntity.setLearningTrack(tLearningTrackEntity);
			mLearningTrackRepository.save(tLearningTrackEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a learning_track_path to learning_track
	 */
	
	public void unassignLearningTrackLearningTrackPath(Long id, Long learning_track_pathId) {
		Optional<LearningTrackPathEntity> tLearningTrackPath = mLearningTrackPathRepository.findById(learning_track_pathId);
		Optional<LearningTrackEntity> tLearningTrack = mLearningTrackRepository.findById(id);
		if(tLearningTrackPath.isPresent() && tLearningTrack.isPresent()) {
			LearningTrackEntity tLearningTrackEntity=tLearningTrack.get();
			tLearningTrackEntity.getLearningTrackPaths().remove(tLearningTrackPath.get());
			mLearningTrackRepository.save(tLearningTrackEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of LearningTrack learning_track_paths
	 */
	
	public List<LearningTrackPathRO> findUnassignLearningTrackLearningTrackPaths(Long id) {
		List<LearningTrackPathRO> tLearningTrackPathROs= new ArrayList<>();   
		 mLearningTrackRepository.findById(id).ifPresent(en->{
			 List<LearningTrackPathEntity> tLearningTrackPaths = mLearningTrackPathRepository.findAll();
			 tLearningTrackPaths.removeAll(en.getLearningTrackPaths());
			 tLearningTrackPaths.forEach(re->{					
					tLearningTrackPathROs.add(mModelMapper.map(re, LearningTrackPathRO.class));
				});
		 });
		
		return tLearningTrackPathROs;
	}
	
	
	
	public LearningTrackRO getLearningTrackByKeyId(String key) {
		Optional<?> tLearningTrack= Optional.ofNullable(mLearningTrackRepository.findByKeyid(key));
		 if(tLearningTrack.isPresent()) {
	            return mModelMapper.map(tLearningTrack.get(), LearningTrackRO.class);
	        }
	      return null;  
	}
	
	
	public LearningTrackRO getLearningTrackLearningTrack(Long id) throws RestException {
		 Optional<LearningTrackEntity> tLearningTrack = mLearningTrackRepository.findById(id);         
	        if(tLearningTrack.isPresent()) {
	        	LearningTrackEntity tLearningTrackEntity= tLearningTrack.get().getLearningTrack();
	        	if(tLearningTrackEntity!=null)
	        		return mModelMapper.map(tLearningTrackEntity, LearningTrackRO.class) ;
	        } else {
	            throw new RestException("No learning_track record exist for given id");
	        }	        
	     return null;   
	}
	
	public void addLearningTrackLearningTrack(Long id,Long learningTrackQuestionid){
		 Optional<LearningTrackEntity> tLearningTrack = mLearningTrackRepository.findById(id);         
	        if(tLearningTrack.isPresent()) {
	        	Optional<LearningTrackEntity> tLearningTrackEntityOpt = mLearningTrackRepository.findById(learningTrackQuestionid);
	        	LearningTrackEntity tLearningTrackEntity=tLearningTrack.get();
	        	tLearningTrackEntityOpt.ifPresent(entity->{
	        		tLearningTrackEntity.setLearningTrack(entity);
	        	});
	        		        		
	        }         
	}	
	
	public void unassignLearningTrackLearningTrack(Long id){
		 Optional<LearningTrackEntity> tLearningTrack = mLearningTrackRepository.findById(id);         
	        if(tLearningTrack.isPresent()) {
	        	tLearningTrack.get().setLearningTrack(null);	        		        		
	        }         
	}
	
	
	public AppUserRO getLearningTrackAppUser(Long id) throws RestException {
		 Optional<LearningTrackEntity> tLearningTrack = mLearningTrackRepository.findById(id);         
	        if(tLearningTrack.isPresent()) {
	        	AppUserEntity tAppUserEntity= tLearningTrack.get().getAppUser();
	        	if(tAppUserEntity!=null)
	        		return mModelMapper.map(tAppUserEntity, AppUserRO.class) ;
	        } else {
	            throw new RestException("No learning_track record exist for given id");
	        }	        
	     return null;   
	}
	
	public void addLearningTrackAppUser(Long id,Long learningTrackQuestionid){
		 Optional<LearningTrackEntity> tLearningTrack = mLearningTrackRepository.findById(id);         
	        if(tLearningTrack.isPresent()) {
	        	Optional<AppUserEntity> tAppUserEntityOpt = mAppUserRepository.findById(learningTrackQuestionid);
	        	LearningTrackEntity tLearningTrackEntity=tLearningTrack.get();
	        	tAppUserEntityOpt.ifPresent(entity->{
	        		tLearningTrackEntity.setAppUser(entity);
	        	});
	        		        		
	        }         
	}	
	
	public void unassignLearningTrackAppUser(Long id){
		 Optional<LearningTrackEntity> tLearningTrack = mLearningTrackRepository.findById(id);         
	        if(tLearningTrack.isPresent()) {
	        	tLearningTrack.get().setAppUser(null);	        		        		
	        }         
	}
}
