package  com.adiops.apigateway.learning.track.question.service;

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
 
import com.adiops.apigateway.learning.track.question.entity.LearningTrackQuestionEntity;
import com.adiops.apigateway.learning.track.question.repository.LearningTrackQuestionRepository;
import com.adiops.apigateway.learning.track.question.resourceobject.LearningTrackQuestionRO;


import com.adiops.apigateway.learning.track.path.entity.LearningTrackPathEntity;
import com.adiops.apigateway.learning.track.path.repository.LearningTrackPathRepository;
import com.adiops.apigateway.learning.track.path.resourceobject.LearningTrackPathRO;



/**
 * This is the implementation class for LearningTrackQuestion responsible for all the CRUD operations and other business related operations.
 * @author Deepak Pal
 *
 */
@Service
public class LearningTrackQuestionService{

	@Autowired
	LearningTrackQuestionRepository mLearningTrackQuestionRepository;

	
	
	@Autowired
	LearningTrackPathRepository mLearningTrackPathRepository;
	
	
	@Autowired
	private ModelMapper mModelMapper;

	/**
	 *
	 * fetch list of LearningTrackQuestion
	 */
	
	public List<LearningTrackQuestionRO> getLearningTrackQuestionROs() {
		List<LearningTrackQuestionRO> tLearningTrackQuestionROs = mLearningTrackQuestionRepository.findAll(Sort.by(Sort.Direction.ASC, "keyid")).stream()
				.map(entity -> mModelMapper.map(entity, LearningTrackQuestionRO.class)).collect(Collectors.toList());
		return tLearningTrackQuestionROs;
	}

	/**
	 *
	 * get LearningTrackQuestion by id
	 */
	public LearningTrackQuestionRO getLearningTrackQuestionById(Long id) throws RestException {
        Optional<LearningTrackQuestionEntity> tLearningTrackQuestion = mLearningTrackQuestionRepository.findById(id);
         
        if(tLearningTrackQuestion.isPresent()) {
            return mModelMapper.map(tLearningTrackQuestion.get(), LearningTrackQuestionRO.class);
        } else {
            throw new RestException("No learning_track_question record exist for given id");
        }
    }
	
	 
	/**
	 * create or update 
	 */
	public LearningTrackQuestionRO createOrUpdateLearningTrackQuestion(LearningTrackQuestionRO tLearningTrackQuestionRO) throws RestException 
    {
        LearningTrackQuestionEntity newEntity ;
         
        if(tLearningTrackQuestionRO.getId()!=null)
        {
        	newEntity=	 mLearningTrackQuestionRepository.findById(tLearningTrackQuestionRO.getId()).orElse(new LearningTrackQuestionEntity());
        }
        else
        {
        	newEntity=new LearningTrackQuestionEntity();
        }
       
              
      
        	if(tLearningTrackQuestionRO.getKeyid() !=null)
        	newEntity.setKeyid(tLearningTrackQuestionRO.getKeyid());
        	if(tLearningTrackQuestionRO.getTitle() !=null)
        	newEntity.setTitle(tLearningTrackQuestionRO.getTitle());
        	if(tLearningTrackQuestionRO.getQuestion() !=null)
        	newEntity.setQuestion(tLearningTrackQuestionRO.getQuestion());
        	if(tLearningTrackQuestionRO.getAnswer() !=null)
        	newEntity.setAnswer(tLearningTrackQuestionRO.getAnswer());
        	if(tLearningTrackQuestionRO.getCorrectAnswer() !=null)
        	newEntity.setCorrectAnswer(tLearningTrackQuestionRO.getCorrectAnswer());
        	if(tLearningTrackQuestionRO.getScore() !=null)
        	newEntity.setScore(tLearningTrackQuestionRO.getScore());
        	if(tLearningTrackQuestionRO.getMaxScore() !=null)
        	newEntity.setMaxScore(tLearningTrackQuestionRO.getMaxScore());
        	if(tLearningTrackQuestionRO.getCourseKey() !=null)
        	newEntity.setCourseKey(tLearningTrackQuestionRO.getCourseKey());
        	if(tLearningTrackQuestionRO.getModuleKey() !=null)
        	newEntity.setModuleKey(tLearningTrackQuestionRO.getModuleKey());
        	if(tLearningTrackQuestionRO.getTopicKey() !=null)
        	newEntity.setTopicKey(tLearningTrackQuestionRO.getTopicKey());
        	if(tLearningTrackQuestionRO.getQuestionKey() !=null)
        	newEntity.setQuestionKey(tLearningTrackQuestionRO.getQuestionKey());
        	if(tLearningTrackQuestionRO.getUserKey() !=null)
        	newEntity.setUserKey(tLearningTrackQuestionRO.getUserKey());
        	if(tLearningTrackQuestionRO.getStatus() !=null)
        	newEntity.setStatus(tLearningTrackQuestionRO.getStatus());

  	 try {
        	 newEntity = mLearningTrackQuestionRepository.save(newEntity);   
		} catch (Exception e) {
			throw new RestException("Could not save result due to unique key exception");
		}
                 
        return  mModelMapper.map(newEntity, LearningTrackQuestionRO.class);
    } 
     
	/**
	 * delete by Id
	 */
     public void deleteLearningTrackQuestionById(Long id) throws RestException {
       Optional<LearningTrackQuestionEntity> tLearningTrackQuestion = mLearningTrackQuestionRepository.findById(id);
         
        if(tLearningTrackQuestion.isPresent()) { 
        
            mLearningTrackQuestionRepository.deleteById(id);
        } else {
            throw new RestException("No learning_track_question record exist for given id");
        }
    } 
    
	/**
	 * upload file to DB
	 */
	
	public void importCSV(MultipartFile file) throws RestException {
		try {
			List<LearningTrackQuestionEntity> tLearningTrackQuestions = CSVHelper.csvToPOJOs(file.getInputStream(), LearningTrackQuestionEntity.class);
			tLearningTrackQuestions=tLearningTrackQuestions.stream().map(entity->{
				LearningTrackQuestionEntity tLearningTrackQuestionEntity=mLearningTrackQuestionRepository.findByKeyid(entity.getKeyid());
				if(tLearningTrackQuestionEntity!=null) {
					entity.setId(tLearningTrackQuestionEntity.getId());
				}
				return entity;
			}).collect(Collectors.toList());
			mLearningTrackQuestionRepository.saveAll(tLearningTrackQuestions);
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
			List<LearningTrackQuestionEntity> tLearningTrackQuestions = CSVHelper.csvToPOJOs(is, LearningTrackQuestionEntity.class);
			tLearningTrackQuestions=tLearningTrackQuestions.stream().map(entity->{
				LearningTrackQuestionEntity tLearningTrackQuestionEntity=mLearningTrackQuestionRepository.findByKeyid(entity.getKeyid());
				if(tLearningTrackQuestionEntity!=null) {
					entity.setId(tLearningTrackQuestionEntity.getId());
				}
				return entity;
			}).collect(Collectors.toList());
			mLearningTrackQuestionRepository.saveAll(tLearningTrackQuestions);
		} catch (Exception e) {
			throw new RestException(RestException.ERROR_STATUS_BAD_REQUEST,
					"fail to store csv data: " + e.getMessage());
		}
	}

	/**
	 * get count
	 */
	public long count() {
		return mLearningTrackQuestionRepository.count();
	}

	/**
	 *
	 * fetch list of LearningTrackQuestion
	 */
	
	public List<LearningTrackQuestionRO> findAll(int pageNumber, int rowPerPage) {
		Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("id").ascending());
		List<LearningTrackQuestionRO> tLearningTrackQuestionROs = mLearningTrackQuestionRepository.findAll(sortedByIdAsc).stream()
				.map(entity -> mModelMapper.map(entity, LearningTrackQuestionRO.class)).collect(Collectors.toList());
		return tLearningTrackQuestionROs;
	}
	
	
	
	
	
	public LearningTrackQuestionRO getLearningTrackQuestionByKeyId(String key) {
		Optional<?> tLearningTrackQuestion= Optional.ofNullable(mLearningTrackQuestionRepository.findByKeyid(key));
		 if(tLearningTrackQuestion.isPresent()) {
	            return mModelMapper.map(tLearningTrackQuestion.get(), LearningTrackQuestionRO.class);
	        }
	      return null;  
	}
	
	
	public LearningTrackPathRO getLearningTrackQuestionLearningTrackPath(Long id) throws RestException {
		 Optional<LearningTrackQuestionEntity> tLearningTrackQuestion = mLearningTrackQuestionRepository.findById(id);         
	        if(tLearningTrackQuestion.isPresent()) {
	        	LearningTrackPathEntity tLearningTrackPathEntity= tLearningTrackQuestion.get().getLearningTrackPath();
	        	if(tLearningTrackPathEntity!=null)
	        		return mModelMapper.map(tLearningTrackPathEntity, LearningTrackPathRO.class) ;
	        } else {
	            throw new RestException("No learning_track_question record exist for given id");
	        }	        
	     return null;   
	}
	
	public void addLearningTrackQuestionLearningTrackPath(Long id,Long learningTrackQuestionid){
		 Optional<LearningTrackQuestionEntity> tLearningTrackQuestion = mLearningTrackQuestionRepository.findById(id);         
	        if(tLearningTrackQuestion.isPresent()) {
	        	Optional<LearningTrackPathEntity> tLearningTrackPathEntityOpt = mLearningTrackPathRepository.findById(learningTrackQuestionid);
	        	LearningTrackQuestionEntity tLearningTrackQuestionEntity=tLearningTrackQuestion.get();
	        	tLearningTrackPathEntityOpt.ifPresent(entity->{
	        		tLearningTrackQuestionEntity.setLearningTrackPath(entity);
	        	});
	        		        		
	        }         
	}	
	
	public void unassignLearningTrackQuestionLearningTrackPath(Long id){
		 Optional<LearningTrackQuestionEntity> tLearningTrackQuestion = mLearningTrackQuestionRepository.findById(id);         
	        if(tLearningTrackQuestion.isPresent()) {
	        	tLearningTrackQuestion.get().setLearningTrackPath(null);	        		        		
	        }         
	}
	
	
}
