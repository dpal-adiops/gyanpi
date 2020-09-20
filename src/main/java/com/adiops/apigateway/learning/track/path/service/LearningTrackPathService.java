package  com.adiops.apigateway.learning.track.path.service;

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
 
import com.adiops.apigateway.learning.track.path.entity.LearningTrackPathEntity;
import com.adiops.apigateway.learning.track.path.repository.LearningTrackPathRepository;
import com.adiops.apigateway.learning.track.path.resourceobject.LearningTrackPathRO;


import com.adiops.apigateway.learning.track.question.entity.LearningTrackQuestionEntity;
import com.adiops.apigateway.learning.track.question.repository.LearningTrackQuestionRepository;
import com.adiops.apigateway.learning.track.question.resourceobject.LearningTrackQuestionRO;
import com.adiops.apigateway.learning.track.path.entity.LearningTrackPathEntity;
import com.adiops.apigateway.learning.track.path.repository.LearningTrackPathRepository;
import com.adiops.apigateway.learning.track.path.resourceobject.LearningTrackPathRO;

import com.adiops.apigateway.learning.track.entity.LearningTrackEntity;
import com.adiops.apigateway.learning.track.repository.LearningTrackRepository;
import com.adiops.apigateway.learning.track.resourceobject.LearningTrackRO;


/**
 * This is the implementation class for LearningTrackPath responsible for all the CRUD operations and other business related operations.
 * @author Deepak Pal
 *
 */
@Service
public class LearningTrackPathService{

	@Autowired
	LearningTrackPathRepository mLearningTrackPathRepository;

	
	
	@Autowired
	LearningTrackQuestionRepository mLearningTrackQuestionRepository;
	
	@Autowired
	LearningTrackRepository mLearningTrackRepository;
	
	@Autowired
	private ModelMapper mModelMapper;

	/**
	 *
	 * fetch list of LearningTrackPath
	 */
	
	public List<LearningTrackPathRO> getLearningTrackPathROs() {
		List<LearningTrackPathRO> tLearningTrackPathROs = mLearningTrackPathRepository.findAll(Sort.by(Sort.Direction.ASC, "keyid")).stream()
				.map(entity -> mModelMapper.map(entity, LearningTrackPathRO.class)).collect(Collectors.toList());
		return tLearningTrackPathROs;
	}

	/**
	 *
	 * get LearningTrackPath by id
	 */
	public LearningTrackPathRO getLearningTrackPathById(Long id) throws RestException {
        Optional<LearningTrackPathEntity> tLearningTrackPath = mLearningTrackPathRepository.findById(id);
         
        if(tLearningTrackPath.isPresent()) {
            return mModelMapper.map(tLearningTrackPath.get(), LearningTrackPathRO.class);
        } else {
            throw new RestException("No learning_track_path record exist for given id");
        }
    }
	
	 
	/**
	 * create or update 
	 */
	public LearningTrackPathRO createOrUpdateLearningTrackPath(LearningTrackPathRO tLearningTrackPathRO) throws RestException 
    {
        LearningTrackPathEntity newEntity ;
         
        if(tLearningTrackPathRO.getId()!=null)
        {
        	newEntity=	 mLearningTrackPathRepository.findById(tLearningTrackPathRO.getId()).orElse(new LearningTrackPathEntity());
        }
        else
        {
        	newEntity=new LearningTrackPathEntity();
        }
       
              
      
        	if(tLearningTrackPathRO.getKeyid() !=null)
        	newEntity.setKeyid(tLearningTrackPathRO.getKeyid());
        	if(tLearningTrackPathRO.getStep() !=null)
        	newEntity.setStep(tLearningTrackPathRO.getStep());
        	if(tLearningTrackPathRO.getStatus() !=null)
        	newEntity.setStatus(tLearningTrackPathRO.getStatus());
        	if(tLearningTrackPathRO.getRefid() !=null)
        	newEntity.setRefid(tLearningTrackPathRO.getRefid());

  	 try {
        	 newEntity = mLearningTrackPathRepository.save(newEntity);   
		} catch (Exception e) {
			throw new RestException("Could not save result due to unique key exception");
		}
                 
        return  mModelMapper.map(newEntity, LearningTrackPathRO.class);
    } 
     
	/**
	 * delete by Id
	 */
     public void deleteLearningTrackPathById(Long id) throws RestException {
       Optional<LearningTrackPathEntity> tLearningTrackPath = mLearningTrackPathRepository.findById(id);
         
        if(tLearningTrackPath.isPresent()) { 
        
            mLearningTrackPathRepository.deleteById(id);
        } else {
            throw new RestException("No learning_track_path record exist for given id");
        }
    } 
    
	/**
	 * upload file to DB
	 */
	
	public void importCSV(MultipartFile file) throws RestException {
		try {
			List<LearningTrackPathEntity> tLearningTrackPaths = CSVHelper.csvToPOJOs(file.getInputStream(), LearningTrackPathEntity.class);
			tLearningTrackPaths=tLearningTrackPaths.stream().map(entity->{
				LearningTrackPathEntity tLearningTrackPathEntity=mLearningTrackPathRepository.findByKeyid(entity.getKeyid());
				if(tLearningTrackPathEntity!=null) {
					entity.setId(tLearningTrackPathEntity.getId());
				}
				return entity;
			}).collect(Collectors.toList());
			mLearningTrackPathRepository.saveAll(tLearningTrackPaths);
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
			List<LearningTrackPathEntity> tLearningTrackPaths = CSVHelper.csvToPOJOs(is, LearningTrackPathEntity.class);
			tLearningTrackPaths=tLearningTrackPaths.stream().map(entity->{
				LearningTrackPathEntity tLearningTrackPathEntity=mLearningTrackPathRepository.findByKeyid(entity.getKeyid());
				if(tLearningTrackPathEntity!=null) {
					entity.setId(tLearningTrackPathEntity.getId());
				}
				return entity;
			}).collect(Collectors.toList());
			mLearningTrackPathRepository.saveAll(tLearningTrackPaths);
		} catch (Exception e) {
			throw new RestException(RestException.ERROR_STATUS_BAD_REQUEST,
					"fail to store csv data: " + e.getMessage());
		}
	}

	/**
	 * get count
	 */
	public long count() {
		return mLearningTrackPathRepository.count();
	}

	/**
	 *
	 * fetch list of LearningTrackPath
	 */
	
	public List<LearningTrackPathRO> findAll(int pageNumber, int rowPerPage) {
		Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("id").ascending());
		List<LearningTrackPathRO> tLearningTrackPathROs = mLearningTrackPathRepository.findAll(sortedByIdAsc).stream()
				.map(entity -> mModelMapper.map(entity, LearningTrackPathRO.class)).collect(Collectors.toList());
		return tLearningTrackPathROs;
	}
	
	
	
	
	
	public LearningTrackPathRO getLearningTrackPathByKeyId(String key) {
		Optional<?> tLearningTrackPath= Optional.ofNullable(mLearningTrackPathRepository.findByKeyid(key));
		 if(tLearningTrackPath.isPresent()) {
	            return mModelMapper.map(tLearningTrackPath.get(), LearningTrackPathRO.class);
	        }
	      return null;  
	}
	
	
	public LearningTrackQuestionRO getLearningTrackPathLearningTrackQuestion(Long id) throws RestException {
		 Optional<LearningTrackPathEntity> tLearningTrackPath = mLearningTrackPathRepository.findById(id);         
	        if(tLearningTrackPath.isPresent()) {
	        	LearningTrackQuestionEntity tLearningTrackQuestionEntity= tLearningTrackPath.get().getLearningTrackQuestion();
	        	if(tLearningTrackQuestionEntity!=null)
	        		return mModelMapper.map(tLearningTrackQuestionEntity, LearningTrackQuestionRO.class) ;
	        } else {
	            throw new RestException("No learning_track_path record exist for given id");
	        }	        
	     return null;   
	}
	
	public void addLearningTrackPathLearningTrackQuestion(Long id,Long learningTrackQuestionid){
		 Optional<LearningTrackPathEntity> tLearningTrackPath = mLearningTrackPathRepository.findById(id);         
	        if(tLearningTrackPath.isPresent()) {
	        	Optional<LearningTrackQuestionEntity> tLearningTrackQuestionEntityOpt = mLearningTrackQuestionRepository.findById(learningTrackQuestionid);
	        	LearningTrackPathEntity tLearningTrackPathEntity=tLearningTrackPath.get();
	        	tLearningTrackQuestionEntityOpt.ifPresent(entity->{
	        		tLearningTrackPathEntity.setLearningTrackQuestion(entity);
	        	});
	        		        		
	        }         
	}	
	
	public void unassignLearningTrackPathLearningTrackQuestion(Long id){
		 Optional<LearningTrackPathEntity> tLearningTrackPath = mLearningTrackPathRepository.findById(id);         
	        if(tLearningTrackPath.isPresent()) {
	        	tLearningTrackPath.get().setLearningTrackQuestion(null);	        		        		
	        }         
	}
	public LearningTrackPathRO getLearningTrackPathLearningTrackPath(Long id) throws RestException {
		 Optional<LearningTrackPathEntity> tLearningTrackPath = mLearningTrackPathRepository.findById(id);         
	        if(tLearningTrackPath.isPresent()) {
	        	LearningTrackPathEntity tLearningTrackPathEntity= tLearningTrackPath.get().getLearningTrackPath();
	        	if(tLearningTrackPathEntity!=null)
	        		return mModelMapper.map(tLearningTrackPathEntity, LearningTrackPathRO.class) ;
	        } else {
	            throw new RestException("No learning_track_path record exist for given id");
	        }	        
	     return null;   
	}
	
	public void addLearningTrackPathLearningTrackPath(Long id,Long learningTrackQuestionid){
		 Optional<LearningTrackPathEntity> tLearningTrackPath = mLearningTrackPathRepository.findById(id);         
	        if(tLearningTrackPath.isPresent()) {
	        	Optional<LearningTrackPathEntity> tLearningTrackPathEntityOpt = mLearningTrackPathRepository.findById(learningTrackQuestionid);
	        	LearningTrackPathEntity tLearningTrackPathEntity=tLearningTrackPath.get();
	        	tLearningTrackPathEntityOpt.ifPresent(entity->{
	        		tLearningTrackPathEntity.setLearningTrackPath(entity);
	        	});
	        		        		
	        }         
	}	
	
	public void unassignLearningTrackPathLearningTrackPath(Long id){
		 Optional<LearningTrackPathEntity> tLearningTrackPath = mLearningTrackPathRepository.findById(id);         
	        if(tLearningTrackPath.isPresent()) {
	        	tLearningTrackPath.get().setLearningTrackPath(null);	        		        		
	        }         
	}
	
	
	public LearningTrackRO getLearningTrackPathLearningTrack(Long id) throws RestException {
		 Optional<LearningTrackPathEntity> tLearningTrackPath = mLearningTrackPathRepository.findById(id);         
	        if(tLearningTrackPath.isPresent()) {
	        	LearningTrackEntity tLearningTrackEntity= tLearningTrackPath.get().getLearningTrack();
	        	if(tLearningTrackEntity!=null)
	        		return mModelMapper.map(tLearningTrackEntity, LearningTrackRO.class) ;
	        } else {
	            throw new RestException("No learning_track_path record exist for given id");
	        }	        
	     return null;   
	}
	
	public void addLearningTrackPathLearningTrack(Long id,Long learningTrackQuestionid){
		 Optional<LearningTrackPathEntity> tLearningTrackPath = mLearningTrackPathRepository.findById(id);         
	        if(tLearningTrackPath.isPresent()) {
	        	Optional<LearningTrackEntity> tLearningTrackEntityOpt = mLearningTrackRepository.findById(learningTrackQuestionid);
	        	LearningTrackPathEntity tLearningTrackPathEntity=tLearningTrackPath.get();
	        	tLearningTrackEntityOpt.ifPresent(entity->{
	        		tLearningTrackPathEntity.setLearningTrack(entity);
	        	});
	        		        		
	        }         
	}	
	
	public void unassignLearningTrackPathLearningTrack(Long id){
		 Optional<LearningTrackPathEntity> tLearningTrackPath = mLearningTrackPathRepository.findById(id);         
	        if(tLearningTrackPath.isPresent()) {
	        	tLearningTrackPath.get().setLearningTrack(null);	        		        		
	        }         
	}
}
