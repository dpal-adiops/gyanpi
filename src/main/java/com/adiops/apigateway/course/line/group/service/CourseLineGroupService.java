package  com.adiops.apigateway.course.line.group.service;

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
 
import com.adiops.apigateway.course.line.group.entity.CourseLineGroupEntity;
import com.adiops.apigateway.course.line.group.repository.CourseLineGroupRepository;
import com.adiops.apigateway.course.line.group.resourceobject.CourseLineGroupRO;

import com.adiops.apigateway.module.line.group.entity.ModuleLineGroupEntity;
import com.adiops.apigateway.module.line.group.repository.ModuleLineGroupRepository;
import com.adiops.apigateway.module.line.group.resourceobject.ModuleLineGroupRO;


import com.adiops.apigateway.learning.path.entity.LearningPathEntity;
import com.adiops.apigateway.learning.path.repository.LearningPathRepository;
import com.adiops.apigateway.learning.path.resourceobject.LearningPathRO;


/**
 * This is the implementation class for CourseLineGroup responsible for all the CRUD operations and other business related operations.
 * @author Deepak Pal
 *
 */
@Service
public class CourseLineGroupService{

	@Autowired
	CourseLineGroupRepository mCourseLineGroupRepository;

	
	@Autowired
	ModuleLineGroupRepository mModuleLineGroupRepository;
	
	
	@Autowired
	LearningPathRepository mLearningPathRepository;
	
	@Autowired
	private ModelMapper mModelMapper;

	/**
	 *
	 * fetch list of CourseLineGroup
	 */
	
	public List<CourseLineGroupRO> getCourseLineGroupROs() {
		List<CourseLineGroupRO> tCourseLineGroupROs = mCourseLineGroupRepository.findAll(Sort.by(Sort.Direction.ASC, "keyid")).stream()
				.map(entity -> mModelMapper.map(entity, CourseLineGroupRO.class)).collect(Collectors.toList());
		return tCourseLineGroupROs;
	}

	/**
	 *
	 * get CourseLineGroup by id
	 */
	public CourseLineGroupRO getCourseLineGroupById(Long id) throws RestException {
        Optional<CourseLineGroupEntity> tCourseLineGroup = mCourseLineGroupRepository.findById(id);
         
        if(tCourseLineGroup.isPresent()) {
            return mModelMapper.map(tCourseLineGroup.get(), CourseLineGroupRO.class);
        } else {
            throw new RestException("No course_line_group record exist for given id");
        }
    }
	
	 
	/**
	 * create or update 
	 */
	public CourseLineGroupRO createOrUpdateCourseLineGroup(CourseLineGroupRO tCourseLineGroupRO) throws RestException 
    {
        CourseLineGroupEntity newEntity ;
         
        if(tCourseLineGroupRO.getId()!=null)
        {
        	newEntity=	 mCourseLineGroupRepository.findById(tCourseLineGroupRO.getId()).orElse(new CourseLineGroupEntity());
        }
         else if(tCourseLineGroupRO.getKeyid() !=null)
        {
        	newEntity=Optional.ofNullable(mCourseLineGroupRepository.findByKeyid(tCourseLineGroupRO.getKeyid())).orElse(new CourseLineGroupEntity());
        }
        else
        {
        	newEntity=new CourseLineGroupEntity();
        }
       
              
      
        	if(tCourseLineGroupRO.getKeyid() !=null)
        	newEntity.setKeyid(tCourseLineGroupRO.getKeyid());
        	if(tCourseLineGroupRO.getStatus() !=null)
        	newEntity.setStatus(tCourseLineGroupRO.getStatus());
        	if(tCourseLineGroupRO.getScore() !=null)
        	newEntity.setScore(tCourseLineGroupRO.getScore());
        	if(tCourseLineGroupRO.getMaxScore() !=null)
        	newEntity.setMaxScore(tCourseLineGroupRO.getMaxScore());
        	if(tCourseLineGroupRO.getTitle() !=null)
        	newEntity.setTitle(tCourseLineGroupRO.getTitle());
        	if(tCourseLineGroupRO.getDescription() !=null)
        	newEntity.setDescription(tCourseLineGroupRO.getDescription());
        	if(tCourseLineGroupRO.getUserKey() !=null)
        	newEntity.setUserKey(tCourseLineGroupRO.getUserKey());
        	if(tCourseLineGroupRO.getCourseKey() !=null)
        	newEntity.setCourseKey(tCourseLineGroupRO.getCourseKey());
        	if(tCourseLineGroupRO.getStep() !=null)
        	newEntity.setStep(tCourseLineGroupRO.getStep());
        	if(tCourseLineGroupRO.getTotalStep() !=null)
        	newEntity.setTotalStep(tCourseLineGroupRO.getTotalStep());

  	 try {
        	 newEntity = mCourseLineGroupRepository.save(newEntity);   
		} catch (Exception e) {
			throw new RestException("Could not save result due to unique key exception");
		}
                 
        return  mModelMapper.map(newEntity, CourseLineGroupRO.class);
    } 
     
	/**
	 * delete by Id
	 */
     public void deleteCourseLineGroupById(Long id) throws RestException {
       Optional<CourseLineGroupEntity> tCourseLineGroup = mCourseLineGroupRepository.findById(id);
         
        if(tCourseLineGroup.isPresent()) { 
        
            mCourseLineGroupRepository.deleteById(id);
        } else {
            throw new RestException("No course_line_group record exist for given id");
        }
    } 
    
	/**
	 * upload file to DB
	 */
	
	public void importCSV(MultipartFile file) throws RestException {
		try {
			List<CourseLineGroupEntity> tCourseLineGroups = CSVHelper.csvToPOJOs(file.getInputStream(), CourseLineGroupEntity.class);
			tCourseLineGroups=tCourseLineGroups.stream().map(entity->{
				CourseLineGroupEntity tCourseLineGroupEntity=mCourseLineGroupRepository.findByKeyid(entity.getKeyid());
				if(tCourseLineGroupEntity!=null) {
					entity.setId(tCourseLineGroupEntity.getId());
				}
				return entity;
			}).collect(Collectors.toList());
			mCourseLineGroupRepository.saveAll(tCourseLineGroups);
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
			List<CourseLineGroupEntity> tCourseLineGroups = CSVHelper.csvToPOJOs(is, CourseLineGroupEntity.class);
			tCourseLineGroups=tCourseLineGroups.stream().map(entity->{
				CourseLineGroupEntity tCourseLineGroupEntity=mCourseLineGroupRepository.findByKeyid(entity.getKeyid());
				if(tCourseLineGroupEntity!=null) {
					entity.setId(tCourseLineGroupEntity.getId());
				}
				return entity;
			}).collect(Collectors.toList());
			mCourseLineGroupRepository.saveAll(tCourseLineGroups);
		} catch (Exception e) {
			throw new RestException(RestException.ERROR_STATUS_BAD_REQUEST,
					"fail to store csv data: " + e.getMessage());
		}
	}

	/**
	 * get count
	 */
	public long count() {
		return mCourseLineGroupRepository.count();
	}

	/**
	 *
	 * fetch list of CourseLineGroup
	 */
	
	public List<CourseLineGroupRO> findAll(int pageNumber, int rowPerPage) {
		Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("id").ascending());
		List<CourseLineGroupRO> tCourseLineGroupROs = mCourseLineGroupRepository.findAll(sortedByIdAsc).stream()
				.map(entity -> mModelMapper.map(entity, CourseLineGroupRO.class)).collect(Collectors.toList());
		return tCourseLineGroupROs;
	}
	
	
	
	
	/**
	 *
	 * fetch list of CourseLineGroup module_line_groups
	 */
	
	public List<ModuleLineGroupRO> findCourseLineGroupModuleLineGroups(Long id) {
		List<ModuleLineGroupRO> tModuleLineGroupROs= new ArrayList<>();   
		Optional<CourseLineGroupEntity> tCourseLineGroup = mCourseLineGroupRepository.findById(id);
		if(tCourseLineGroup.isPresent()) {
			tCourseLineGroup.ifPresent(en->{
				en.getModuleLineGroups().forEach(re->{					
					tModuleLineGroupROs.add(mModelMapper.map(re, ModuleLineGroupRO.class));
				});
			});
		}	
		Collections.sort(tModuleLineGroupROs, new Comparator<ModuleLineGroupRO>() {
			  @Override
			  public int compare(ModuleLineGroupRO u1, ModuleLineGroupRO u2) {
			    return u1.getKeyid().compareTo(u2.getKeyid());
			  }
			});
						
		return tModuleLineGroupROs;
	}
	
	/**
	 *
	 * assign a module_line_group to course_line_group
	 */
	
	public void addCourseLineGroupModuleLineGroup(Long id, Long module_line_groupId) {
		Optional<ModuleLineGroupEntity> tModuleLineGroup = mModuleLineGroupRepository.findById(module_line_groupId);
		Optional<CourseLineGroupEntity> tCourseLineGroup = mCourseLineGroupRepository.findById(id);
		if(tModuleLineGroup.isPresent() && tCourseLineGroup.isPresent()) {
			CourseLineGroupEntity tCourseLineGroupEntity=tCourseLineGroup.get();
			ModuleLineGroupEntity tModuleLineGroupEntity= tModuleLineGroup.get();
			tCourseLineGroupEntity.getModuleLineGroups().add(tModuleLineGroupEntity);
			tModuleLineGroupEntity.setCourseLineGroup(tCourseLineGroupEntity);
			mCourseLineGroupRepository.save(tCourseLineGroupEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a module_line_group to course_line_group
	 */
	
	public void unassignCourseLineGroupModuleLineGroup(Long id, Long module_line_groupId) {
		Optional<ModuleLineGroupEntity> tModuleLineGroup = mModuleLineGroupRepository.findById(module_line_groupId);
		Optional<CourseLineGroupEntity> tCourseLineGroup = mCourseLineGroupRepository.findById(id);
		if(tModuleLineGroup.isPresent() && tCourseLineGroup.isPresent()) {
			CourseLineGroupEntity tCourseLineGroupEntity=tCourseLineGroup.get();
			tCourseLineGroupEntity.getModuleLineGroups().remove(tModuleLineGroup.get());
			mCourseLineGroupRepository.save(tCourseLineGroupEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of CourseLineGroup module_line_groups
	 */
	
	public List<ModuleLineGroupRO> findUnassignCourseLineGroupModuleLineGroups(Long id) {
		List<ModuleLineGroupRO> tModuleLineGroupROs= new ArrayList<>();   
		 mCourseLineGroupRepository.findById(id).ifPresent(en->{
			 List<ModuleLineGroupEntity> tModuleLineGroups = mModuleLineGroupRepository.findAll();
			 tModuleLineGroups.removeAll(en.getModuleLineGroups());
			 tModuleLineGroups.forEach(re->{					
					tModuleLineGroupROs.add(mModelMapper.map(re, ModuleLineGroupRO.class));
				});
		 });
		
		return tModuleLineGroupROs;
	}
	
	
	
	public CourseLineGroupRO getCourseLineGroupByKeyId(String key) {
		Optional<?> tCourseLineGroup= Optional.ofNullable(mCourseLineGroupRepository.findByKeyid(key));
		 if(tCourseLineGroup.isPresent()) {
	            return mModelMapper.map(tCourseLineGroup.get(), CourseLineGroupRO.class);
	        }
	      return null;  
	}
	
	
	
	
	public LearningPathRO getCourseLineGroupLearningPath(Long id) throws RestException {
		 Optional<CourseLineGroupEntity> tCourseLineGroup = mCourseLineGroupRepository.findById(id);         
	        if(tCourseLineGroup.isPresent()) {
	        	LearningPathEntity tLearningPathEntity= tCourseLineGroup.get().getLearningPath();
	        	if(tLearningPathEntity!=null)
	        		return mModelMapper.map(tLearningPathEntity, LearningPathRO.class) ;
	        } else {
	            throw new RestException("No course_line_group record exist for given id");
	        }	        
	     return null;   
	}
	
	public void addCourseLineGroupLearningPath(Long id,Long learningTrackQuestionid){
		 Optional<CourseLineGroupEntity> tCourseLineGroup = mCourseLineGroupRepository.findById(id);         
	        if(tCourseLineGroup.isPresent()) {
	        	Optional<LearningPathEntity> tLearningPathEntityOpt = mLearningPathRepository.findById(learningTrackQuestionid);
	        	CourseLineGroupEntity tCourseLineGroupEntity=tCourseLineGroup.get();
	        	tLearningPathEntityOpt.ifPresent(entity->{
	        		tCourseLineGroupEntity.setLearningPath(entity);
	        	});
	        		        		
	        }         
	}	
	
	public void unassignCourseLineGroupLearningPath(Long id){
		 Optional<CourseLineGroupEntity> tCourseLineGroup = mCourseLineGroupRepository.findById(id);         
	        if(tCourseLineGroup.isPresent()) {
	        	tCourseLineGroup.get().setLearningPath(null);	        		        		
	        }         
	}
}
