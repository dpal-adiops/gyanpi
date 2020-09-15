package  com.adiops.apigateway.module.line.group.service;

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
 
import com.adiops.apigateway.module.line.group.entity.ModuleLineGroupEntity;
import com.adiops.apigateway.module.line.group.repository.ModuleLineGroupRepository;
import com.adiops.apigateway.module.line.group.resourceobject.ModuleLineGroupRO;

import com.adiops.apigateway.topic.line.group.entity.TopicLineGroupEntity;
import com.adiops.apigateway.topic.line.group.repository.TopicLineGroupRepository;
import com.adiops.apigateway.topic.line.group.resourceobject.TopicLineGroupRO;


import com.adiops.apigateway.course.line.group.entity.CourseLineGroupEntity;
import com.adiops.apigateway.course.line.group.repository.CourseLineGroupRepository;
import com.adiops.apigateway.course.line.group.resourceobject.CourseLineGroupRO;


/**
 * This is the implementation class for ModuleLineGroup responsible for all the CRUD operations and other business related operations.
 * @author Deepak Pal
 *
 */
@Service
public class ModuleLineGroupService{

	@Autowired
	ModuleLineGroupRepository mModuleLineGroupRepository;

	
	@Autowired
	TopicLineGroupRepository mTopicLineGroupRepository;
	
	
	@Autowired
	CourseLineGroupRepository mCourseLineGroupRepository;
	
	@Autowired
	private ModelMapper mModelMapper;

	/**
	 *
	 * fetch list of ModuleLineGroup
	 */
	
	public List<ModuleLineGroupRO> getModuleLineGroupROs() {
		List<ModuleLineGroupRO> tModuleLineGroupROs = mModuleLineGroupRepository.findAll(Sort.by(Sort.Direction.ASC, "keyid")).stream()
				.map(entity -> mModelMapper.map(entity, ModuleLineGroupRO.class)).collect(Collectors.toList());
		return tModuleLineGroupROs;
	}

	/**
	 *
	 * get ModuleLineGroup by id
	 */
	public ModuleLineGroupRO getModuleLineGroupById(Long id) throws RestException {
        Optional<ModuleLineGroupEntity> tModuleLineGroup = mModuleLineGroupRepository.findById(id);
         
        if(tModuleLineGroup.isPresent()) {
            return mModelMapper.map(tModuleLineGroup.get(), ModuleLineGroupRO.class);
        } else {
            throw new RestException("No module_line_group record exist for given id");
        }
    }
	
	 
	/**
	 * create or update 
	 */
	public ModuleLineGroupRO createOrUpdateModuleLineGroup(ModuleLineGroupRO tModuleLineGroupRO) throws RestException 
    {
        ModuleLineGroupEntity newEntity ;
         
        if(tModuleLineGroupRO.getId()!=null)
        {
        	newEntity=	 mModuleLineGroupRepository.findById(tModuleLineGroupRO.getId()).orElse(new ModuleLineGroupEntity());
        }
        else
        {
        	newEntity=new ModuleLineGroupEntity();
        }
       
              
      
        	if(tModuleLineGroupRO.getKeyid() !=null)
        	newEntity.setKeyid(tModuleLineGroupRO.getKeyid());
        	if(tModuleLineGroupRO.getStatus() !=null)
        	newEntity.setStatus(tModuleLineGroupRO.getStatus());
        	if(tModuleLineGroupRO.getScore() !=null)
        	newEntity.setScore(tModuleLineGroupRO.getScore());
        	if(tModuleLineGroupRO.getMaxScore() !=null)
        	newEntity.setMaxScore(tModuleLineGroupRO.getMaxScore());
        	if(tModuleLineGroupRO.getTitle() !=null)
        	newEntity.setTitle(tModuleLineGroupRO.getTitle());
        	if(tModuleLineGroupRO.getDescription() !=null)
        	newEntity.setDescription(tModuleLineGroupRO.getDescription());
        	if(tModuleLineGroupRO.getUserKey() !=null)
        	newEntity.setUserKey(tModuleLineGroupRO.getUserKey());
        	if(tModuleLineGroupRO.getCourseKey() !=null)
        	newEntity.setCourseKey(tModuleLineGroupRO.getCourseKey());
        	if(tModuleLineGroupRO.getModuleKey() !=null)
        	newEntity.setModuleKey(tModuleLineGroupRO.getModuleKey());
        	if(tModuleLineGroupRO.getStep() !=null)
        	newEntity.setStep(tModuleLineGroupRO.getStep());
        	if(tModuleLineGroupRO.getTotalStep() !=null)
        	newEntity.setTotalStep(tModuleLineGroupRO.getTotalStep());

  	 try {
        	 newEntity = mModuleLineGroupRepository.save(newEntity);   
		} catch (Exception e) {
			throw new RestException("Could not save result due to unique key exception");
		}
                 
        return  mModelMapper.map(newEntity, ModuleLineGroupRO.class);
    } 
     
	/**
	 * delete by Id
	 */
     public void deleteModuleLineGroupById(Long id) throws RestException {
       Optional<ModuleLineGroupEntity> tModuleLineGroup = mModuleLineGroupRepository.findById(id);
         
        if(tModuleLineGroup.isPresent()) { 
        
            mModuleLineGroupRepository.deleteById(id);
        } else {
            throw new RestException("No module_line_group record exist for given id");
        }
    } 
    
	/**
	 * upload file to DB
	 */
	
	public void importCSV(MultipartFile file) throws RestException {
		try {
			List<ModuleLineGroupEntity> tModuleLineGroups = CSVHelper.csvToPOJOs(file.getInputStream(), ModuleLineGroupEntity.class);
			mModuleLineGroupRepository.deleteAll();
			mModuleLineGroupRepository.saveAll(tModuleLineGroups);
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
			List<ModuleLineGroupEntity> tModuleLineGroups = CSVHelper.csvToPOJOs(is, ModuleLineGroupEntity.class);
			mModuleLineGroupRepository.saveAll(tModuleLineGroups);
		} catch (Exception e) {
			throw new RestException(RestException.ERROR_STATUS_BAD_REQUEST,
					"fail to store csv data: " + e.getMessage());
		}
	}

	/**
	 * get count
	 */
	public long count() {
		return mModuleLineGroupRepository.count();
	}

	/**
	 *
	 * fetch list of ModuleLineGroup
	 */
	
	public List<ModuleLineGroupRO> findAll(int pageNumber, int rowPerPage) {
		Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("id").ascending());
		List<ModuleLineGroupRO> tModuleLineGroupROs = mModuleLineGroupRepository.findAll(sortedByIdAsc).stream()
				.map(entity -> mModelMapper.map(entity, ModuleLineGroupRO.class)).collect(Collectors.toList());
		return tModuleLineGroupROs;
	}
	
	
	
	
	/**
	 *
	 * fetch list of ModuleLineGroup topic_line_groups
	 */
	
	public List<TopicLineGroupRO> findModuleLineGroupTopicLineGroups(Long id) {
		List<TopicLineGroupRO> tTopicLineGroupROs= new ArrayList<>();   
		Optional<ModuleLineGroupEntity> tModuleLineGroup = mModuleLineGroupRepository.findById(id);
		if(tModuleLineGroup.isPresent()) {
			tModuleLineGroup.ifPresent(en->{
				en.getTopicLineGroups().forEach(re->{					
					tTopicLineGroupROs.add(mModelMapper.map(re, TopicLineGroupRO.class));
				});
			});
		}	
		Collections.sort(tTopicLineGroupROs, new Comparator<TopicLineGroupRO>() {
			  @Override
			  public int compare(TopicLineGroupRO u1, TopicLineGroupRO u2) {
			    return u1.getKeyid().compareTo(u2.getKeyid());
			  }
			});
						
		return tTopicLineGroupROs;
	}
	
	/**
	 *
	 * assign a topic_line_group to module_line_group
	 */
	
	public void addModuleLineGroupTopicLineGroup(Long id, Long topic_line_groupId) {
		Optional<TopicLineGroupEntity> tTopicLineGroup = mTopicLineGroupRepository.findById(topic_line_groupId);
		Optional<ModuleLineGroupEntity> tModuleLineGroup = mModuleLineGroupRepository.findById(id);
		if(tTopicLineGroup.isPresent() && tModuleLineGroup.isPresent()) {
			ModuleLineGroupEntity tModuleLineGroupEntity=tModuleLineGroup.get();
			TopicLineGroupEntity tTopicLineGroupEntity= tTopicLineGroup.get();
			tModuleLineGroupEntity.getTopicLineGroups().add(tTopicLineGroupEntity);
			tTopicLineGroupEntity.setModuleLineGroup(tModuleLineGroupEntity);
			mModuleLineGroupRepository.save(tModuleLineGroupEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a topic_line_group to module_line_group
	 */
	
	public void unassignModuleLineGroupTopicLineGroup(Long id, Long topic_line_groupId) {
		Optional<TopicLineGroupEntity> tTopicLineGroup = mTopicLineGroupRepository.findById(topic_line_groupId);
		Optional<ModuleLineGroupEntity> tModuleLineGroup = mModuleLineGroupRepository.findById(id);
		if(tTopicLineGroup.isPresent() && tModuleLineGroup.isPresent()) {
			ModuleLineGroupEntity tModuleLineGroupEntity=tModuleLineGroup.get();
			tModuleLineGroupEntity.getTopicLineGroups().remove(tTopicLineGroup.get());
			mModuleLineGroupRepository.save(tModuleLineGroupEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of ModuleLineGroup topic_line_groups
	 */
	
	public List<TopicLineGroupRO> findUnassignModuleLineGroupTopicLineGroups(Long id) {
		List<TopicLineGroupRO> tTopicLineGroupROs= new ArrayList<>();   
		 mModuleLineGroupRepository.findById(id).ifPresent(en->{
			 List<TopicLineGroupEntity> tTopicLineGroups = mTopicLineGroupRepository.findAll();
			 tTopicLineGroups.removeAll(en.getTopicLineGroups());
			 tTopicLineGroups.forEach(re->{					
					tTopicLineGroupROs.add(mModelMapper.map(re, TopicLineGroupRO.class));
				});
		 });
		
		return tTopicLineGroupROs;
	}
	
	
	
	public ModuleLineGroupRO getModuleLineGroupByKeyId(String key) {
		Optional<?> tModuleLineGroup= Optional.ofNullable(mModuleLineGroupRepository.findByKeyid(key));
		 if(tModuleLineGroup.isPresent()) {
	            return mModelMapper.map(tModuleLineGroup.get(), ModuleLineGroupRO.class);
	        }
	      return null;  
	}
	
	
	
	
	public CourseLineGroupRO getModuleLineGroupCourseLineGroup(Long id) throws RestException {
		 Optional<ModuleLineGroupEntity> tModuleLineGroup = mModuleLineGroupRepository.findById(id);         
	        if(tModuleLineGroup.isPresent()) {
	        	CourseLineGroupEntity tCourseLineGroupEntity= tModuleLineGroup.get().getCourseLineGroup();
	        	if(tCourseLineGroupEntity!=null)
	        		return mModelMapper.map(tCourseLineGroupEntity, CourseLineGroupRO.class) ;
	        } else {
	            throw new RestException("No module_line_group record exist for given id");
	        }	        
	     return null;   
	}
	
	public void addModuleLineGroupCourseLineGroup(Long id,Long learningTrackQuestionid){
		 Optional<ModuleLineGroupEntity> tModuleLineGroup = mModuleLineGroupRepository.findById(id);         
	        if(tModuleLineGroup.isPresent()) {
	        	Optional<CourseLineGroupEntity> tCourseLineGroupEntityOpt = mCourseLineGroupRepository.findById(learningTrackQuestionid);
	        	ModuleLineGroupEntity tModuleLineGroupEntity=tModuleLineGroup.get();
	        	tCourseLineGroupEntityOpt.ifPresent(entity->{
	        		tModuleLineGroupEntity.setCourseLineGroup(entity);
	        	});
	        		        		
	        }         
	}	
	
	public void unassignModuleLineGroupCourseLineGroup(Long id){
		 Optional<ModuleLineGroupEntity> tModuleLineGroup = mModuleLineGroupRepository.findById(id);         
	        if(tModuleLineGroup.isPresent()) {
	        	tModuleLineGroup.get().setCourseLineGroup(null);	        		        		
	        }         
	}
}
