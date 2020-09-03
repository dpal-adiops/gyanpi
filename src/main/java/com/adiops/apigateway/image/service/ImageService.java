package  com.adiops.apigateway.image.service;

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
 
import com.adiops.apigateway.image.entity.ImageEntity;
import com.adiops.apigateway.image.repository.ImageRepository;
import com.adiops.apigateway.image.resourceobject.ImageRO;

import com.adiops.apigateway.course.entity.CourseEntity;
import com.adiops.apigateway.course.repository.CourseRepository;
import com.adiops.apigateway.course.resourceobject.CourseRO;
import com.adiops.apigateway.module.entity.ModuleEntity;
import com.adiops.apigateway.module.repository.ModuleRepository;
import com.adiops.apigateway.module.resourceobject.ModuleRO;
import com.adiops.apigateway.topic.entity.TopicEntity;
import com.adiops.apigateway.topic.repository.TopicRepository;
import com.adiops.apigateway.topic.resourceobject.TopicRO;
import com.adiops.apigateway.question.entity.QuestionEntity;
import com.adiops.apigateway.question.repository.QuestionRepository;
import com.adiops.apigateway.question.resourceobject.QuestionRO;


/**
 * This is the implementation class for Image responsible for all the CRUD operations and other business related operations.
 * @author Deepak Pal
 *
 */
@Service
public class ImageService{

	@Autowired
	ImageRepository mImageRepository;

	@Autowired
	CourseRepository mCourseRepository;
	@Autowired
	ModuleRepository mModuleRepository;
	@Autowired
	TopicRepository mTopicRepository;
	@Autowired
	QuestionRepository mQuestionRepository;
	@Autowired
	private ModelMapper mModelMapper;

	/**
	 *
	 * fetch list of Image
	 */
	
	public List<ImageRO> getImageROs() {
		List<ImageRO> tImageROs = mImageRepository.findAll(Sort.by(Sort.Direction.ASC, "keyid")).stream()
				.map(entity -> mModelMapper.map(entity, ImageRO.class)).collect(Collectors.toList());
		return tImageROs;
	}

	/**
	 *
	 * get Image by id
	 */
	public ImageRO getImageById(Long id) throws RestException {
        Optional<ImageEntity> tImage = mImageRepository.findById(id);
         
        if(tImage.isPresent()) {
            return mModelMapper.map(tImage.get(), ImageRO.class);
        } else {
            throw new RestException("No image record exist for given id");
        }
    }
	
	 
	/**
	 * create or update 
	 */
	public ImageRO createOrUpdateImage(ImageRO tImageRO) throws RestException 
    {
        ImageEntity newEntity ;
         
        if(tImageRO.getId()!=null)
        {
        	newEntity=	 mImageRepository.findById(tImageRO.getId()).orElse(new ImageEntity());
        }
        else
        {
        	newEntity=new ImageEntity();
        }
       
              
      
        	if(tImageRO.getKeyid() !=null)
        	newEntity.setKeyid(tImageRO.getKeyid());
        	if(tImageRO.getName() !=null)
        	newEntity.setName(tImageRO.getName());
        	if(tImageRO.getUrl() !=null)
        	newEntity.setUrl(tImageRO.getUrl());
        	if(tImageRO.getAuthorId() !=null)
        	newEntity.setAuthorId(tImageRO.getAuthorId());
        	if(tImageRO.getDomainId() !=null)
        	newEntity.setDomainId(tImageRO.getDomainId());

  	 try {
        	 newEntity = mImageRepository.save(newEntity);   
		} catch (Exception e) {
			throw new RestException("Could not save result due to unique key exception");
		}
                 
        return  mModelMapper.map(newEntity, ImageRO.class);
    } 
     
	/**
	 * delete by Id
	 */
     public void deleteImageById(Long id) throws RestException {
       Optional<ImageEntity> tImage = mImageRepository.findById(id);
         
        if(tImage.isPresent()) { 
        
            mImageRepository.deleteById(id);
        } else {
            throw new RestException("No image record exist for given id");
        }
    } 
    
	/**
	 * upload file to DB
	 */
	
	public void importCSV(MultipartFile file) throws RestException {
		try {
			List<ImageEntity> tImages = CSVHelper.csvToPOJOs(file.getInputStream(), ImageEntity.class);
			mImageRepository.deleteAll();
			mImageRepository.saveAll(tImages);
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
			List<ImageEntity> tImages = CSVHelper.csvToPOJOs(is, ImageEntity.class);
			mImageRepository.saveAll(tImages);
		} catch (Exception e) {
			throw new RestException(RestException.ERROR_STATUS_BAD_REQUEST,
					"fail to store csv data: " + e.getMessage());
		}
	}

	/**
	 * get count
	 */
	public long count() {
		return mImageRepository.count();
	}

	/**
	 *
	 * fetch list of Image
	 */
	
	public List<ImageRO> findAll(int pageNumber, int rowPerPage) {
		Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("id").ascending());
		List<ImageRO> tImageROs = mImageRepository.findAll(sortedByIdAsc).stream()
				.map(entity -> mModelMapper.map(entity, ImageRO.class)).collect(Collectors.toList());
		return tImageROs;
	}
	
	
	/**
	 *
	 * fetch list of Image courses
	 */
	
	public List<CourseRO> findImageCourses(Long id) {
		List<CourseRO> tCourseROs= new ArrayList<>();   
		Optional<ImageEntity> tImage = mImageRepository.findById(id);
		if(tImage.isPresent()) {
			tImage.ifPresent(en->{
				en.getCourses().forEach(re->{					
					tCourseROs.add(mModelMapper.map(re, CourseRO.class));
				});
			});
		}	
		Collections.sort(tCourseROs, new Comparator<CourseRO>() {
			  @Override
			  public int compare(CourseRO u1, CourseRO u2) {
			    return u1.getKeyid().compareTo(u2.getKeyid());
			  }
			});
						
		return tCourseROs;
	}
	
	/**
	 *
	 * assign a course to image
	 */
	
	public void addImageCourse(Long id, Long courseId) {
		Optional<CourseEntity> tCourse = mCourseRepository.findById(courseId);
		Optional<ImageEntity> tImage = mImageRepository.findById(id);
		if(tCourse.isPresent() && tImage.isPresent()) {
			ImageEntity tImageEntity=tImage.get();
			CourseEntity tCourseEntity= tCourse.get();
			tImageEntity.getCourses().add(tCourseEntity);
			tCourseEntity.getImages().add(tImageEntity);
			mImageRepository.save(tImageEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a course to image
	 */
	
	public void unassignImageCourse(Long id, Long courseId) {
		Optional<CourseEntity> tCourse = mCourseRepository.findById(courseId);
		Optional<ImageEntity> tImage = mImageRepository.findById(id);
		if(tCourse.isPresent() && tImage.isPresent()) {
			ImageEntity tImageEntity=tImage.get();
			tImageEntity.getCourses().remove(tCourse.get());
			mImageRepository.save(tImageEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Image courses
	 */
	
	public List<CourseRO> findUnassignImageCourses(Long id) {
		List<CourseRO> tCourseROs= new ArrayList<>();   
		 mImageRepository.findById(id).ifPresent(en->{
			 List<CourseEntity> tCourses = mCourseRepository.findAll();
			 tCourses.removeAll(en.getCourses());
			 tCourses.forEach(re->{					
					tCourseROs.add(mModelMapper.map(re, CourseRO.class));
				});
		 });
		
		return tCourseROs;
	}
	
	
	/**
	 *
	 * fetch list of Image modules
	 */
	
	public List<ModuleRO> findImageModules(Long id) {
		List<ModuleRO> tModuleROs= new ArrayList<>();   
		Optional<ImageEntity> tImage = mImageRepository.findById(id);
		if(tImage.isPresent()) {
			tImage.ifPresent(en->{
				en.getModules().forEach(re->{					
					tModuleROs.add(mModelMapper.map(re, ModuleRO.class));
				});
			});
		}	
		Collections.sort(tModuleROs, new Comparator<ModuleRO>() {
			  @Override
			  public int compare(ModuleRO u1, ModuleRO u2) {
			    return u1.getKeyid().compareTo(u2.getKeyid());
			  }
			});
						
		return tModuleROs;
	}
	
	/**
	 *
	 * assign a module to image
	 */
	
	public void addImageModule(Long id, Long moduleId) {
		Optional<ModuleEntity> tModule = mModuleRepository.findById(moduleId);
		Optional<ImageEntity> tImage = mImageRepository.findById(id);
		if(tModule.isPresent() && tImage.isPresent()) {
			ImageEntity tImageEntity=tImage.get();
			ModuleEntity tModuleEntity= tModule.get();
			tImageEntity.getModules().add(tModuleEntity);
			tModuleEntity.getImages().add(tImageEntity);
			mImageRepository.save(tImageEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a module to image
	 */
	
	public void unassignImageModule(Long id, Long moduleId) {
		Optional<ModuleEntity> tModule = mModuleRepository.findById(moduleId);
		Optional<ImageEntity> tImage = mImageRepository.findById(id);
		if(tModule.isPresent() && tImage.isPresent()) {
			ImageEntity tImageEntity=tImage.get();
			tImageEntity.getModules().remove(tModule.get());
			mImageRepository.save(tImageEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Image modules
	 */
	
	public List<ModuleRO> findUnassignImageModules(Long id) {
		List<ModuleRO> tModuleROs= new ArrayList<>();   
		 mImageRepository.findById(id).ifPresent(en->{
			 List<ModuleEntity> tModules = mModuleRepository.findAll();
			 tModules.removeAll(en.getModules());
			 tModules.forEach(re->{					
					tModuleROs.add(mModelMapper.map(re, ModuleRO.class));
				});
		 });
		
		return tModuleROs;
	}
	
	
	/**
	 *
	 * fetch list of Image topics
	 */
	
	public List<TopicRO> findImageTopics(Long id) {
		List<TopicRO> tTopicROs= new ArrayList<>();   
		Optional<ImageEntity> tImage = mImageRepository.findById(id);
		if(tImage.isPresent()) {
			tImage.ifPresent(en->{
				en.getTopics().forEach(re->{					
					tTopicROs.add(mModelMapper.map(re, TopicRO.class));
				});
			});
		}	
		Collections.sort(tTopicROs, new Comparator<TopicRO>() {
			  @Override
			  public int compare(TopicRO u1, TopicRO u2) {
			    return u1.getKeyid().compareTo(u2.getKeyid());
			  }
			});
						
		return tTopicROs;
	}
	
	/**
	 *
	 * assign a topic to image
	 */
	
	public void addImageTopic(Long id, Long topicId) {
		Optional<TopicEntity> tTopic = mTopicRepository.findById(topicId);
		Optional<ImageEntity> tImage = mImageRepository.findById(id);
		if(tTopic.isPresent() && tImage.isPresent()) {
			ImageEntity tImageEntity=tImage.get();
			TopicEntity tTopicEntity= tTopic.get();
			tImageEntity.getTopics().add(tTopicEntity);
			tTopicEntity.getImages().add(tImageEntity);
			mImageRepository.save(tImageEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a topic to image
	 */
	
	public void unassignImageTopic(Long id, Long topicId) {
		Optional<TopicEntity> tTopic = mTopicRepository.findById(topicId);
		Optional<ImageEntity> tImage = mImageRepository.findById(id);
		if(tTopic.isPresent() && tImage.isPresent()) {
			ImageEntity tImageEntity=tImage.get();
			tImageEntity.getTopics().remove(tTopic.get());
			mImageRepository.save(tImageEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Image topics
	 */
	
	public List<TopicRO> findUnassignImageTopics(Long id) {
		List<TopicRO> tTopicROs= new ArrayList<>();   
		 mImageRepository.findById(id).ifPresent(en->{
			 List<TopicEntity> tTopics = mTopicRepository.findAll();
			 tTopics.removeAll(en.getTopics());
			 tTopics.forEach(re->{					
					tTopicROs.add(mModelMapper.map(re, TopicRO.class));
				});
		 });
		
		return tTopicROs;
	}
	
	
	/**
	 *
	 * fetch list of Image questions
	 */
	
	public List<QuestionRO> findImageQuestions(Long id) {
		List<QuestionRO> tQuestionROs= new ArrayList<>();   
		Optional<ImageEntity> tImage = mImageRepository.findById(id);
		if(tImage.isPresent()) {
			tImage.ifPresent(en->{
				en.getQuestions().forEach(re->{					
					tQuestionROs.add(mModelMapper.map(re, QuestionRO.class));
				});
			});
		}	
		Collections.sort(tQuestionROs, new Comparator<QuestionRO>() {
			  @Override
			  public int compare(QuestionRO u1, QuestionRO u2) {
			    return u1.getKeyid().compareTo(u2.getKeyid());
			  }
			});
						
		return tQuestionROs;
	}
	
	/**
	 *
	 * assign a question to image
	 */
	
	public void addImageQuestion(Long id, Long questionId) {
		Optional<QuestionEntity> tQuestion = mQuestionRepository.findById(questionId);
		Optional<ImageEntity> tImage = mImageRepository.findById(id);
		if(tQuestion.isPresent() && tImage.isPresent()) {
			ImageEntity tImageEntity=tImage.get();
			QuestionEntity tQuestionEntity= tQuestion.get();
			tImageEntity.getQuestions().add(tQuestionEntity);
			tQuestionEntity.getImages().add(tImageEntity);
			mImageRepository.save(tImageEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a question to image
	 */
	
	public void unassignImageQuestion(Long id, Long questionId) {
		Optional<QuestionEntity> tQuestion = mQuestionRepository.findById(questionId);
		Optional<ImageEntity> tImage = mImageRepository.findById(id);
		if(tQuestion.isPresent() && tImage.isPresent()) {
			ImageEntity tImageEntity=tImage.get();
			tImageEntity.getQuestions().remove(tQuestion.get());
			mImageRepository.save(tImageEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Image questions
	 */
	
	public List<QuestionRO> findUnassignImageQuestions(Long id) {
		List<QuestionRO> tQuestionROs= new ArrayList<>();   
		 mImageRepository.findById(id).ifPresent(en->{
			 List<QuestionEntity> tQuestions = mQuestionRepository.findAll();
			 tQuestions.removeAll(en.getQuestions());
			 tQuestions.forEach(re->{					
					tQuestionROs.add(mModelMapper.map(re, QuestionRO.class));
				});
		 });
		
		return tQuestionROs;
	}
	
	
}
