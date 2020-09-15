package  com.adiops.apigateway.topic.line.group.service;

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
 
import com.adiops.apigateway.topic.line.group.entity.TopicLineGroupEntity;
import com.adiops.apigateway.topic.line.group.repository.TopicLineGroupRepository;
import com.adiops.apigateway.topic.line.group.resourceobject.TopicLineGroupRO;

import com.adiops.apigateway.question.line.item.entity.QuestionLineItemEntity;
import com.adiops.apigateway.question.line.item.repository.QuestionLineItemRepository;
import com.adiops.apigateway.question.line.item.resourceobject.QuestionLineItemRO;


import com.adiops.apigateway.module.line.group.entity.ModuleLineGroupEntity;
import com.adiops.apigateway.module.line.group.repository.ModuleLineGroupRepository;
import com.adiops.apigateway.module.line.group.resourceobject.ModuleLineGroupRO;


/**
 * This is the implementation class for TopicLineGroup responsible for all the CRUD operations and other business related operations.
 * @author Deepak Pal
 *
 */
@Service
public class TopicLineGroupService{

	@Autowired
	TopicLineGroupRepository mTopicLineGroupRepository;

	
	@Autowired
	QuestionLineItemRepository mQuestionLineItemRepository;
	
	
	@Autowired
	ModuleLineGroupRepository mModuleLineGroupRepository;
	
	@Autowired
	private ModelMapper mModelMapper;

	/**
	 *
	 * fetch list of TopicLineGroup
	 */
	
	public List<TopicLineGroupRO> getTopicLineGroupROs() {
		List<TopicLineGroupRO> tTopicLineGroupROs = mTopicLineGroupRepository.findAll(Sort.by(Sort.Direction.ASC, "keyid")).stream()
				.map(entity -> mModelMapper.map(entity, TopicLineGroupRO.class)).collect(Collectors.toList());
		return tTopicLineGroupROs;
	}

	/**
	 *
	 * get TopicLineGroup by id
	 */
	public TopicLineGroupRO getTopicLineGroupById(Long id) throws RestException {
        Optional<TopicLineGroupEntity> tTopicLineGroup = mTopicLineGroupRepository.findById(id);
         
        if(tTopicLineGroup.isPresent()) {
            return mModelMapper.map(tTopicLineGroup.get(), TopicLineGroupRO.class);
        } else {
            throw new RestException("No topic_line_group record exist for given id");
        }
    }
	
	 
	/**
	 * create or update 
	 */
	public TopicLineGroupRO createOrUpdateTopicLineGroup(TopicLineGroupRO tTopicLineGroupRO) throws RestException 
    {
        TopicLineGroupEntity newEntity ;
         
        if(tTopicLineGroupRO.getId()!=null)
        {
        	newEntity=	 mTopicLineGroupRepository.findById(tTopicLineGroupRO.getId()).orElse(new TopicLineGroupEntity());
        }
        else
        {
        	newEntity=new TopicLineGroupEntity();
        }
       
              
      
        	if(tTopicLineGroupRO.getKeyid() !=null)
        	newEntity.setKeyid(tTopicLineGroupRO.getKeyid());
        	if(tTopicLineGroupRO.getStatus() !=null)
        	newEntity.setStatus(tTopicLineGroupRO.getStatus());
        	if(tTopicLineGroupRO.getScore() !=null)
        	newEntity.setScore(tTopicLineGroupRO.getScore());
        	if(tTopicLineGroupRO.getMaxScore() !=null)
        	newEntity.setMaxScore(tTopicLineGroupRO.getMaxScore());
        	if(tTopicLineGroupRO.getTitle() !=null)
        	newEntity.setTitle(tTopicLineGroupRO.getTitle());
        	if(tTopicLineGroupRO.getDescription() !=null)
        	newEntity.setDescription(tTopicLineGroupRO.getDescription());
        	if(tTopicLineGroupRO.getUserKey() !=null)
        	newEntity.setUserKey(tTopicLineGroupRO.getUserKey());
        	if(tTopicLineGroupRO.getCourseKey() !=null)
        	newEntity.setCourseKey(tTopicLineGroupRO.getCourseKey());
        	if(tTopicLineGroupRO.getModuleKey() !=null)
        	newEntity.setModuleKey(tTopicLineGroupRO.getModuleKey());
        	if(tTopicLineGroupRO.getTopicKey() !=null)
        	newEntity.setTopicKey(tTopicLineGroupRO.getTopicKey());
        	if(tTopicLineGroupRO.getStep() !=null)
        	newEntity.setStep(tTopicLineGroupRO.getStep());
        	if(tTopicLineGroupRO.getTotalStep() !=null)
        	newEntity.setTotalStep(tTopicLineGroupRO.getTotalStep());

  	 try {
        	 newEntity = mTopicLineGroupRepository.save(newEntity);   
		} catch (Exception e) {
			throw new RestException("Could not save result due to unique key exception");
		}
                 
        return  mModelMapper.map(newEntity, TopicLineGroupRO.class);
    } 
     
	/**
	 * delete by Id
	 */
     public void deleteTopicLineGroupById(Long id) throws RestException {
       Optional<TopicLineGroupEntity> tTopicLineGroup = mTopicLineGroupRepository.findById(id);
         
        if(tTopicLineGroup.isPresent()) { 
        
            mTopicLineGroupRepository.deleteById(id);
        } else {
            throw new RestException("No topic_line_group record exist for given id");
        }
    } 
    
	/**
	 * upload file to DB
	 */
	
	public void importCSV(MultipartFile file) throws RestException {
		try {
			List<TopicLineGroupEntity> tTopicLineGroups = CSVHelper.csvToPOJOs(file.getInputStream(), TopicLineGroupEntity.class);
			mTopicLineGroupRepository.deleteAll();
			mTopicLineGroupRepository.saveAll(tTopicLineGroups);
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
			List<TopicLineGroupEntity> tTopicLineGroups = CSVHelper.csvToPOJOs(is, TopicLineGroupEntity.class);
			mTopicLineGroupRepository.saveAll(tTopicLineGroups);
		} catch (Exception e) {
			throw new RestException(RestException.ERROR_STATUS_BAD_REQUEST,
					"fail to store csv data: " + e.getMessage());
		}
	}

	/**
	 * get count
	 */
	public long count() {
		return mTopicLineGroupRepository.count();
	}

	/**
	 *
	 * fetch list of TopicLineGroup
	 */
	
	public List<TopicLineGroupRO> findAll(int pageNumber, int rowPerPage) {
		Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("id").ascending());
		List<TopicLineGroupRO> tTopicLineGroupROs = mTopicLineGroupRepository.findAll(sortedByIdAsc).stream()
				.map(entity -> mModelMapper.map(entity, TopicLineGroupRO.class)).collect(Collectors.toList());
		return tTopicLineGroupROs;
	}
	
	
	
	
	/**
	 *
	 * fetch list of TopicLineGroup question_line_items
	 */
	
	public List<QuestionLineItemRO> findTopicLineGroupQuestionLineItems(Long id) {
		List<QuestionLineItemRO> tQuestionLineItemROs= new ArrayList<>();   
		Optional<TopicLineGroupEntity> tTopicLineGroup = mTopicLineGroupRepository.findById(id);
		if(tTopicLineGroup.isPresent()) {
			tTopicLineGroup.ifPresent(en->{
				en.getQuestionLineItems().forEach(re->{					
					tQuestionLineItemROs.add(mModelMapper.map(re, QuestionLineItemRO.class));
				});
			});
		}	
		Collections.sort(tQuestionLineItemROs, new Comparator<QuestionLineItemRO>() {
			  @Override
			  public int compare(QuestionLineItemRO u1, QuestionLineItemRO u2) {
			    return u1.getKeyid().compareTo(u2.getKeyid());
			  }
			});
						
		return tQuestionLineItemROs;
	}
	
	/**
	 *
	 * assign a question_line_item to topic_line_group
	 */
	
	public void addTopicLineGroupQuestionLineItem(Long id, Long question_line_itemId) {
		Optional<QuestionLineItemEntity> tQuestionLineItem = mQuestionLineItemRepository.findById(question_line_itemId);
		Optional<TopicLineGroupEntity> tTopicLineGroup = mTopicLineGroupRepository.findById(id);
		if(tQuestionLineItem.isPresent() && tTopicLineGroup.isPresent()) {
			TopicLineGroupEntity tTopicLineGroupEntity=tTopicLineGroup.get();
			QuestionLineItemEntity tQuestionLineItemEntity= tQuestionLineItem.get();
			tTopicLineGroupEntity.getQuestionLineItems().add(tQuestionLineItemEntity);
			tQuestionLineItemEntity.setTopicLineGroup(tTopicLineGroupEntity);
			mTopicLineGroupRepository.save(tTopicLineGroupEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a question_line_item to topic_line_group
	 */
	
	public void unassignTopicLineGroupQuestionLineItem(Long id, Long question_line_itemId) {
		Optional<QuestionLineItemEntity> tQuestionLineItem = mQuestionLineItemRepository.findById(question_line_itemId);
		Optional<TopicLineGroupEntity> tTopicLineGroup = mTopicLineGroupRepository.findById(id);
		if(tQuestionLineItem.isPresent() && tTopicLineGroup.isPresent()) {
			TopicLineGroupEntity tTopicLineGroupEntity=tTopicLineGroup.get();
			tTopicLineGroupEntity.getQuestionLineItems().remove(tQuestionLineItem.get());
			mTopicLineGroupRepository.save(tTopicLineGroupEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of TopicLineGroup question_line_items
	 */
	
	public List<QuestionLineItemRO> findUnassignTopicLineGroupQuestionLineItems(Long id) {
		List<QuestionLineItemRO> tQuestionLineItemROs= new ArrayList<>();   
		 mTopicLineGroupRepository.findById(id).ifPresent(en->{
			 List<QuestionLineItemEntity> tQuestionLineItems = mQuestionLineItemRepository.findAll();
			 tQuestionLineItems.removeAll(en.getQuestionLineItems());
			 tQuestionLineItems.forEach(re->{					
					tQuestionLineItemROs.add(mModelMapper.map(re, QuestionLineItemRO.class));
				});
		 });
		
		return tQuestionLineItemROs;
	}
	
	
	
	public TopicLineGroupRO getTopicLineGroupByKeyId(String key) {
		Optional<?> tTopicLineGroup= Optional.ofNullable(mTopicLineGroupRepository.findByKeyid(key));
		 if(tTopicLineGroup.isPresent()) {
	            return mModelMapper.map(tTopicLineGroup.get(), TopicLineGroupRO.class);
	        }
	      return null;  
	}
	
	
	
	
	public ModuleLineGroupRO getTopicLineGroupModuleLineGroup(Long id) throws RestException {
		 Optional<TopicLineGroupEntity> tTopicLineGroup = mTopicLineGroupRepository.findById(id);         
	        if(tTopicLineGroup.isPresent()) {
	        	ModuleLineGroupEntity tModuleLineGroupEntity= tTopicLineGroup.get().getModuleLineGroup();
	        	if(tModuleLineGroupEntity!=null)
	        		return mModelMapper.map(tModuleLineGroupEntity, ModuleLineGroupRO.class) ;
	        } else {
	            throw new RestException("No topic_line_group record exist for given id");
	        }	        
	     return null;   
	}
	
	public void addTopicLineGroupModuleLineGroup(Long id,Long learningTrackQuestionid){
		 Optional<TopicLineGroupEntity> tTopicLineGroup = mTopicLineGroupRepository.findById(id);         
	        if(tTopicLineGroup.isPresent()) {
	        	Optional<ModuleLineGroupEntity> tModuleLineGroupEntityOpt = mModuleLineGroupRepository.findById(learningTrackQuestionid);
	        	TopicLineGroupEntity tTopicLineGroupEntity=tTopicLineGroup.get();
	        	tModuleLineGroupEntityOpt.ifPresent(entity->{
	        		tTopicLineGroupEntity.setModuleLineGroup(entity);
	        	});
	        		        		
	        }         
	}	
	
	public void unassignTopicLineGroupModuleLineGroup(Long id){
		 Optional<TopicLineGroupEntity> tTopicLineGroup = mTopicLineGroupRepository.findById(id);         
	        if(tTopicLineGroup.isPresent()) {
	        	tTopicLineGroup.get().setModuleLineGroup(null);	        		        		
	        }         
	}
}
