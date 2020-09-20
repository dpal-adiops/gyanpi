package  com.adiops.apigateway.learning.path.service;

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
 
import com.adiops.apigateway.learning.path.entity.LearningPathEntity;
import com.adiops.apigateway.learning.path.repository.LearningPathRepository;
import com.adiops.apigateway.learning.path.resourceobject.LearningPathRO;

import com.adiops.apigateway.course.line.group.entity.CourseLineGroupEntity;
import com.adiops.apigateway.course.line.group.repository.CourseLineGroupRepository;
import com.adiops.apigateway.course.line.group.resourceobject.CourseLineGroupRO;




/**
 * This is the implementation class for LearningPath responsible for all the CRUD operations and other business related operations.
 * @author Deepak Pal
 *
 */
@Service
public class LearningPathService{

	@Autowired
	LearningPathRepository mLearningPathRepository;

	
	@Autowired
	CourseLineGroupRepository mCourseLineGroupRepository;
	
	
	
	@Autowired
	private ModelMapper mModelMapper;

	/**
	 *
	 * fetch list of LearningPath
	 */
	
	public List<LearningPathRO> getLearningPathROs() {
		List<LearningPathRO> tLearningPathROs = mLearningPathRepository.findAll(Sort.by(Sort.Direction.ASC, "keyid")).stream()
				.map(entity -> mModelMapper.map(entity, LearningPathRO.class)).collect(Collectors.toList());
		return tLearningPathROs;
	}

	/**
	 *
	 * get LearningPath by id
	 */
	public LearningPathRO getLearningPathById(Long id) throws RestException {
        Optional<LearningPathEntity> tLearningPath = mLearningPathRepository.findById(id);
         
        if(tLearningPath.isPresent()) {
            return mModelMapper.map(tLearningPath.get(), LearningPathRO.class);
        } else {
            throw new RestException("No learning_path record exist for given id");
        }
    }
	
	 
	/**
	 * create or update 
	 */
	public LearningPathRO createOrUpdateLearningPath(LearningPathRO tLearningPathRO) throws RestException 
    {
        LearningPathEntity newEntity ;
         
        if(tLearningPathRO.getId()!=null)
        {
        	newEntity=	 mLearningPathRepository.findById(tLearningPathRO.getId()).orElse(new LearningPathEntity());
        }
         else if(tLearningPathRO.getKeyid() !=null)
        {
        	newEntity=Optional.ofNullable(mLearningPathRepository.findByKeyid(tLearningPathRO.getKeyid())).orElse(new LearningPathEntity());
        }
        else
        {
        	newEntity=new LearningPathEntity();
        }
       
              
      
        	if(tLearningPathRO.getKeyid() !=null)
        	newEntity.setKeyid(tLearningPathRO.getKeyid());
        	if(tLearningPathRO.getStatus() !=null)
        	newEntity.setStatus(tLearningPathRO.getStatus());
        	if(tLearningPathRO.getScore() !=null)
        	newEntity.setScore(tLearningPathRO.getScore());
        	if(tLearningPathRO.getMaxScore() !=null)
        	newEntity.setMaxScore(tLearningPathRO.getMaxScore());
        	if(tLearningPathRO.getUserKey() !=null)
        	newEntity.setUserKey(tLearningPathRO.getUserKey());
        	if(tLearningPathRO.getStep() !=null)
        	newEntity.setStep(tLearningPathRO.getStep());
        	if(tLearningPathRO.getTotalStep() !=null)
        	newEntity.setTotalStep(tLearningPathRO.getTotalStep());

  	 try {
        	 newEntity = mLearningPathRepository.save(newEntity);   
		} catch (Exception e) {
			throw new RestException("Could not save result due to unique key exception");
		}
                 
        return  mModelMapper.map(newEntity, LearningPathRO.class);
    } 
     
	/**
	 * delete by Id
	 */
     public void deleteLearningPathById(Long id) throws RestException {
       Optional<LearningPathEntity> tLearningPath = mLearningPathRepository.findById(id);
         
        if(tLearningPath.isPresent()) { 
        
            mLearningPathRepository.deleteById(id);
        } else {
            throw new RestException("No learning_path record exist for given id");
        }
    } 
    
	/**
	 * upload file to DB
	 */
	
	public void importCSV(MultipartFile file) throws RestException {
		try {
			List<LearningPathEntity> tLearningPaths = CSVHelper.csvToPOJOs(file.getInputStream(), LearningPathEntity.class);
			tLearningPaths=tLearningPaths.stream().map(entity->{
				LearningPathEntity tLearningPathEntity=mLearningPathRepository.findByKeyid(entity.getKeyid());
				if(tLearningPathEntity!=null) {
					entity.setId(tLearningPathEntity.getId());
				}
				return entity;
			}).collect(Collectors.toList());
			mLearningPathRepository.saveAll(tLearningPaths);
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
			List<LearningPathEntity> tLearningPaths = CSVHelper.csvToPOJOs(is, LearningPathEntity.class);
			tLearningPaths=tLearningPaths.stream().map(entity->{
				LearningPathEntity tLearningPathEntity=mLearningPathRepository.findByKeyid(entity.getKeyid());
				if(tLearningPathEntity!=null) {
					entity.setId(tLearningPathEntity.getId());
				}
				return entity;
			}).collect(Collectors.toList());
			mLearningPathRepository.saveAll(tLearningPaths);
		} catch (Exception e) {
			throw new RestException(RestException.ERROR_STATUS_BAD_REQUEST,
					"fail to store csv data: " + e.getMessage());
		}
	}

	/**
	 * get count
	 */
	public long count() {
		return mLearningPathRepository.count();
	}

	/**
	 *
	 * fetch list of LearningPath
	 */
	
	public List<LearningPathRO> findAll(int pageNumber, int rowPerPage) {
		Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("id").ascending());
		List<LearningPathRO> tLearningPathROs = mLearningPathRepository.findAll(sortedByIdAsc).stream()
				.map(entity -> mModelMapper.map(entity, LearningPathRO.class)).collect(Collectors.toList());
		return tLearningPathROs;
	}
	
	
	
	
	/**
	 *
	 * fetch list of LearningPath course_line_groups
	 */
	
	public List<CourseLineGroupRO> findLearningPathCourseLineGroups(Long id) {
		List<CourseLineGroupRO> tCourseLineGroupROs= new ArrayList<>();   
		Optional<LearningPathEntity> tLearningPath = mLearningPathRepository.findById(id);
		if(tLearningPath.isPresent()) {
			tLearningPath.ifPresent(en->{
				en.getCourseLineGroups().forEach(re->{					
					tCourseLineGroupROs.add(mModelMapper.map(re, CourseLineGroupRO.class));
				});
			});
		}	
		Collections.sort(tCourseLineGroupROs, new Comparator<CourseLineGroupRO>() {
			  @Override
			  public int compare(CourseLineGroupRO u1, CourseLineGroupRO u2) {
			    return u1.getKeyid().compareTo(u2.getKeyid());
			  }
			});
						
		return tCourseLineGroupROs;
	}
	
	/**
	 *
	 * assign a course_line_group to learning_path
	 */
	
	public void addLearningPathCourseLineGroup(Long id, Long course_line_groupId) {
		Optional<CourseLineGroupEntity> tCourseLineGroup = mCourseLineGroupRepository.findById(course_line_groupId);
		Optional<LearningPathEntity> tLearningPath = mLearningPathRepository.findById(id);
		if(tCourseLineGroup.isPresent() && tLearningPath.isPresent()) {
			LearningPathEntity tLearningPathEntity=tLearningPath.get();
			CourseLineGroupEntity tCourseLineGroupEntity= tCourseLineGroup.get();
			tLearningPathEntity.getCourseLineGroups().add(tCourseLineGroupEntity);
			tCourseLineGroupEntity.setLearningPath(tLearningPathEntity);
			mLearningPathRepository.save(tLearningPathEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a course_line_group to learning_path
	 */
	
	public void unassignLearningPathCourseLineGroup(Long id, Long course_line_groupId) {
		Optional<CourseLineGroupEntity> tCourseLineGroup = mCourseLineGroupRepository.findById(course_line_groupId);
		Optional<LearningPathEntity> tLearningPath = mLearningPathRepository.findById(id);
		if(tCourseLineGroup.isPresent() && tLearningPath.isPresent()) {
			LearningPathEntity tLearningPathEntity=tLearningPath.get();
			tLearningPathEntity.getCourseLineGroups().remove(tCourseLineGroup.get());
			mLearningPathRepository.save(tLearningPathEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of LearningPath course_line_groups
	 */
	
	public List<CourseLineGroupRO> findUnassignLearningPathCourseLineGroups(Long id) {
		List<CourseLineGroupRO> tCourseLineGroupROs= new ArrayList<>();   
		 mLearningPathRepository.findById(id).ifPresent(en->{
			 List<CourseLineGroupEntity> tCourseLineGroups = mCourseLineGroupRepository.findAll();
			 tCourseLineGroups.removeAll(en.getCourseLineGroups());
			 tCourseLineGroups.forEach(re->{					
					tCourseLineGroupROs.add(mModelMapper.map(re, CourseLineGroupRO.class));
				});
		 });
		
		return tCourseLineGroupROs;
	}
	
	
	
	public LearningPathRO getLearningPathByKeyId(String key) {
		Optional<?> tLearningPath= Optional.ofNullable(mLearningPathRepository.findByKeyid(key));
		 if(tLearningPath.isPresent()) {
	            return mModelMapper.map(tLearningPath.get(), LearningPathRO.class);
	        }
	      return null;  
	}
	
	
	
	
}
