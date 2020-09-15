package  com.adiops.apigateway.question.line.item.service;

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
 
import com.adiops.apigateway.question.line.item.entity.QuestionLineItemEntity;
import com.adiops.apigateway.question.line.item.repository.QuestionLineItemRepository;
import com.adiops.apigateway.question.line.item.resourceobject.QuestionLineItemRO;



import com.adiops.apigateway.topic.line.group.entity.TopicLineGroupEntity;
import com.adiops.apigateway.topic.line.group.repository.TopicLineGroupRepository;
import com.adiops.apigateway.topic.line.group.resourceobject.TopicLineGroupRO;


/**
 * This is the implementation class for QuestionLineItem responsible for all the CRUD operations and other business related operations.
 * @author Deepak Pal
 *
 */
@Service
public class QuestionLineItemService{

	@Autowired
	QuestionLineItemRepository mQuestionLineItemRepository;

	
	
	
	@Autowired
	TopicLineGroupRepository mTopicLineGroupRepository;
	
	@Autowired
	private ModelMapper mModelMapper;

	/**
	 *
	 * fetch list of QuestionLineItem
	 */
	
	public List<QuestionLineItemRO> getQuestionLineItemROs() {
		List<QuestionLineItemRO> tQuestionLineItemROs = mQuestionLineItemRepository.findAll(Sort.by(Sort.Direction.ASC, "keyid")).stream()
				.map(entity -> mModelMapper.map(entity, QuestionLineItemRO.class)).collect(Collectors.toList());
		return tQuestionLineItemROs;
	}

	/**
	 *
	 * get QuestionLineItem by id
	 */
	public QuestionLineItemRO getQuestionLineItemById(Long id) throws RestException {
        Optional<QuestionLineItemEntity> tQuestionLineItem = mQuestionLineItemRepository.findById(id);
         
        if(tQuestionLineItem.isPresent()) {
            return mModelMapper.map(tQuestionLineItem.get(), QuestionLineItemRO.class);
        } else {
            throw new RestException("No question_line_item record exist for given id");
        }
    }
	
	 
	/**
	 * create or update 
	 */
	public QuestionLineItemRO createOrUpdateQuestionLineItem(QuestionLineItemRO tQuestionLineItemRO) throws RestException 
    {
        QuestionLineItemEntity newEntity ;
         
        if(tQuestionLineItemRO.getId()!=null)
        {
        	newEntity=	 mQuestionLineItemRepository.findById(tQuestionLineItemRO.getId()).orElse(new QuestionLineItemEntity());
        }
        else
        {
        	newEntity=new QuestionLineItemEntity();
        }
       
              
      
        	if(tQuestionLineItemRO.getKeyid() !=null)
        	newEntity.setKeyid(tQuestionLineItemRO.getKeyid());
        	if(tQuestionLineItemRO.getStatus() !=null)
        	newEntity.setStatus(tQuestionLineItemRO.getStatus());
        	if(tQuestionLineItemRO.getScore() !=null)
        	newEntity.setScore(tQuestionLineItemRO.getScore());
        	if(tQuestionLineItemRO.getMaxScore() !=null)
        	newEntity.setMaxScore(tQuestionLineItemRO.getMaxScore());
        	if(tQuestionLineItemRO.getTitle() !=null)
        	newEntity.setTitle(tQuestionLineItemRO.getTitle());
        	if(tQuestionLineItemRO.getDescription() !=null)
        	newEntity.setDescription(tQuestionLineItemRO.getDescription());
        	if(tQuestionLineItemRO.getUserKey() !=null)
        	newEntity.setUserKey(tQuestionLineItemRO.getUserKey());
        	if(tQuestionLineItemRO.getCourseKey() !=null)
        	newEntity.setCourseKey(tQuestionLineItemRO.getCourseKey());
        	if(tQuestionLineItemRO.getModuleKey() !=null)
        	newEntity.setModuleKey(tQuestionLineItemRO.getModuleKey());
        	if(tQuestionLineItemRO.getTopicKey() !=null)
        	newEntity.setTopicKey(tQuestionLineItemRO.getTopicKey());
        	if(tQuestionLineItemRO.getQuestionKey() !=null)
        	newEntity.setQuestionKey(tQuestionLineItemRO.getQuestionKey());
        	if(tQuestionLineItemRO.getAnswer() !=null)
        	newEntity.setAnswer(tQuestionLineItemRO.getAnswer());
        	if(tQuestionLineItemRO.getCorrectAnswer() !=null)
        	newEntity.setCorrectAnswer(tQuestionLineItemRO.getCorrectAnswer());
        	if(tQuestionLineItemRO.getQuestionType() !=null)
        	newEntity.setQuestionType(tQuestionLineItemRO.getQuestionType());

  	 try {
        	 newEntity = mQuestionLineItemRepository.save(newEntity);   
		} catch (Exception e) {
			throw new RestException("Could not save result due to unique key exception");
		}
                 
        return  mModelMapper.map(newEntity, QuestionLineItemRO.class);
    } 
     
	/**
	 * delete by Id
	 */
     public void deleteQuestionLineItemById(Long id) throws RestException {
       Optional<QuestionLineItemEntity> tQuestionLineItem = mQuestionLineItemRepository.findById(id);
         
        if(tQuestionLineItem.isPresent()) { 
        
            mQuestionLineItemRepository.deleteById(id);
        } else {
            throw new RestException("No question_line_item record exist for given id");
        }
    } 
    
	/**
	 * upload file to DB
	 */
	
	public void importCSV(MultipartFile file) throws RestException {
		try {
			List<QuestionLineItemEntity> tQuestionLineItems = CSVHelper.csvToPOJOs(file.getInputStream(), QuestionLineItemEntity.class);
			mQuestionLineItemRepository.deleteAll();
			mQuestionLineItemRepository.saveAll(tQuestionLineItems);
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
			List<QuestionLineItemEntity> tQuestionLineItems = CSVHelper.csvToPOJOs(is, QuestionLineItemEntity.class);
			mQuestionLineItemRepository.saveAll(tQuestionLineItems);
		} catch (Exception e) {
			throw new RestException(RestException.ERROR_STATUS_BAD_REQUEST,
					"fail to store csv data: " + e.getMessage());
		}
	}

	/**
	 * get count
	 */
	public long count() {
		return mQuestionLineItemRepository.count();
	}

	/**
	 *
	 * fetch list of QuestionLineItem
	 */
	
	public List<QuestionLineItemRO> findAll(int pageNumber, int rowPerPage) {
		Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("id").ascending());
		List<QuestionLineItemRO> tQuestionLineItemROs = mQuestionLineItemRepository.findAll(sortedByIdAsc).stream()
				.map(entity -> mModelMapper.map(entity, QuestionLineItemRO.class)).collect(Collectors.toList());
		return tQuestionLineItemROs;
	}
	
	
	
	
	
	public QuestionLineItemRO getQuestionLineItemByKeyId(String key) {
		Optional<?> tQuestionLineItem= Optional.ofNullable(mQuestionLineItemRepository.findByKeyid(key));
		 if(tQuestionLineItem.isPresent()) {
	            return mModelMapper.map(tQuestionLineItem.get(), QuestionLineItemRO.class);
	        }
	      return null;  
	}
	
	
	
	
	public TopicLineGroupRO getQuestionLineItemTopicLineGroup(Long id) throws RestException {
		 Optional<QuestionLineItemEntity> tQuestionLineItem = mQuestionLineItemRepository.findById(id);         
	        if(tQuestionLineItem.isPresent()) {
	        	TopicLineGroupEntity tTopicLineGroupEntity= tQuestionLineItem.get().getTopicLineGroup();
	        	if(tTopicLineGroupEntity!=null)
	        		return mModelMapper.map(tTopicLineGroupEntity, TopicLineGroupRO.class) ;
	        } else {
	            throw new RestException("No question_line_item record exist for given id");
	        }	        
	     return null;   
	}
	
	public void addQuestionLineItemTopicLineGroup(Long id,Long learningTrackQuestionid){
		 Optional<QuestionLineItemEntity> tQuestionLineItem = mQuestionLineItemRepository.findById(id);         
	        if(tQuestionLineItem.isPresent()) {
	        	Optional<TopicLineGroupEntity> tTopicLineGroupEntityOpt = mTopicLineGroupRepository.findById(learningTrackQuestionid);
	        	QuestionLineItemEntity tQuestionLineItemEntity=tQuestionLineItem.get();
	        	tTopicLineGroupEntityOpt.ifPresent(entity->{
	        		tQuestionLineItemEntity.setTopicLineGroup(entity);
	        	});
	        		        		
	        }         
	}	
	
	public void unassignQuestionLineItemTopicLineGroup(Long id){
		 Optional<QuestionLineItemEntity> tQuestionLineItem = mQuestionLineItemRepository.findById(id);         
	        if(tQuestionLineItem.isPresent()) {
	        	tQuestionLineItem.get().setTopicLineGroup(null);	        		        		
	        }         
	}
}
