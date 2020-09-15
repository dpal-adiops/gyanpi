package  com.adiops.apigateway.video.service;

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
 
import com.adiops.apigateway.video.entity.VideoEntity;
import com.adiops.apigateway.video.repository.VideoRepository;
import com.adiops.apigateway.video.resourceobject.VideoRO;

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
 * This is the implementation class for Video responsible for all the CRUD operations and other business related operations.
 * @author Deepak Pal
 *
 */
@Service
public class VideoService{

	@Autowired
	VideoRepository mVideoRepository;

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
	 * fetch list of Video
	 */
	
	public List<VideoRO> getVideoROs() {
		List<VideoRO> tVideoROs = mVideoRepository.findAll(Sort.by(Sort.Direction.ASC, "keyid")).stream()
				.map(entity -> mModelMapper.map(entity, VideoRO.class)).collect(Collectors.toList());
		return tVideoROs;
	}

	/**
	 *
	 * get Video by id
	 */
	public VideoRO getVideoById(Long id) throws RestException {
        Optional<VideoEntity> tVideo = mVideoRepository.findById(id);
         
        if(tVideo.isPresent()) {
            return mModelMapper.map(tVideo.get(), VideoRO.class);
        } else {
            throw new RestException("No video record exist for given id");
        }
    }
	
	 
	/**
	 * create or update 
	 */
	public VideoRO createOrUpdateVideo(VideoRO tVideoRO) throws RestException 
    {
        VideoEntity newEntity ;
         
        if(tVideoRO.getId()!=null)
        {
        	newEntity=	 mVideoRepository.findById(tVideoRO.getId()).orElse(new VideoEntity());
        }
        else
        {
        	newEntity=new VideoEntity();
        }
       
              
      
        	if(tVideoRO.getKeyid() !=null)
        	newEntity.setKeyid(tVideoRO.getKeyid());
        	if(tVideoRO.getName() !=null)
        	newEntity.setName(tVideoRO.getName());
        	if(tVideoRO.getUrl() !=null)
        	newEntity.setUrl(tVideoRO.getUrl());
        	if(tVideoRO.getAuthorId() !=null)
        	newEntity.setAuthorId(tVideoRO.getAuthorId());
        	if(tVideoRO.getDomainId() !=null)
        	newEntity.setDomainId(tVideoRO.getDomainId());

  	 try {
        	 newEntity = mVideoRepository.save(newEntity);   
		} catch (Exception e) {
			throw new RestException("Could not save result due to unique key exception");
		}
                 
        return  mModelMapper.map(newEntity, VideoRO.class);
    } 
     
	/**
	 * delete by Id
	 */
     public void deleteVideoById(Long id) throws RestException {
       Optional<VideoEntity> tVideo = mVideoRepository.findById(id);
         
        if(tVideo.isPresent()) { 
        
            mVideoRepository.deleteById(id);
        } else {
            throw new RestException("No video record exist for given id");
        }
    } 
    
	/**
	 * upload file to DB
	 */
	
	public void importCSV(MultipartFile file) throws RestException {
		try {
			List<VideoEntity> tVideos = CSVHelper.csvToPOJOs(file.getInputStream(), VideoEntity.class);
			mVideoRepository.deleteAll();
			mVideoRepository.saveAll(tVideos);
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
			List<VideoEntity> tVideos = CSVHelper.csvToPOJOs(is, VideoEntity.class);
			mVideoRepository.saveAll(tVideos);
		} catch (Exception e) {
			throw new RestException(RestException.ERROR_STATUS_BAD_REQUEST,
					"fail to store csv data: " + e.getMessage());
		}
	}

	/**
	 * get count
	 */
	public long count() {
		return mVideoRepository.count();
	}

	/**
	 *
	 * fetch list of Video
	 */
	
	public List<VideoRO> findAll(int pageNumber, int rowPerPage) {
		Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("id").ascending());
		List<VideoRO> tVideoROs = mVideoRepository.findAll(sortedByIdAsc).stream()
				.map(entity -> mModelMapper.map(entity, VideoRO.class)).collect(Collectors.toList());
		return tVideoROs;
	}
	
	
	/**
	 *
	 * fetch list of Video courses
	 */
	
	public List<CourseRO> findVideoCourses(Long id) {
		List<CourseRO> tCourseROs= new ArrayList<>();   
		Optional<VideoEntity> tVideo = mVideoRepository.findById(id);
		if(tVideo.isPresent()) {
			tVideo.ifPresent(en->{
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
	 * assign a course to video
	 */
	
	public void addVideoCourse(Long id, Long courseId) {
		Optional<CourseEntity> tCourse = mCourseRepository.findById(courseId);
		Optional<VideoEntity> tVideo = mVideoRepository.findById(id);
		if(tCourse.isPresent() && tVideo.isPresent()) {
			VideoEntity tVideoEntity=tVideo.get();
			CourseEntity tCourseEntity= tCourse.get();
			tVideoEntity.getCourses().add(tCourseEntity);
			tCourseEntity.getVideos().add(tVideoEntity);
			mVideoRepository.save(tVideoEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a course to video
	 */
	
	public void unassignVideoCourse(Long id, Long courseId) {
		Optional<CourseEntity> tCourse = mCourseRepository.findById(courseId);
		Optional<VideoEntity> tVideo = mVideoRepository.findById(id);
		if(tCourse.isPresent() && tVideo.isPresent()) {
			VideoEntity tVideoEntity=tVideo.get();
			tVideoEntity.getCourses().remove(tCourse.get());
			mVideoRepository.save(tVideoEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Video courses
	 */
	
	public List<CourseRO> findUnassignVideoCourses(Long id) {
		List<CourseRO> tCourseROs= new ArrayList<>();   
		 mVideoRepository.findById(id).ifPresent(en->{
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
	 * fetch list of Video modules
	 */
	
	public List<ModuleRO> findVideoModules(Long id) {
		List<ModuleRO> tModuleROs= new ArrayList<>();   
		Optional<VideoEntity> tVideo = mVideoRepository.findById(id);
		if(tVideo.isPresent()) {
			tVideo.ifPresent(en->{
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
	 * assign a module to video
	 */
	
	public void addVideoModule(Long id, Long moduleId) {
		Optional<ModuleEntity> tModule = mModuleRepository.findById(moduleId);
		Optional<VideoEntity> tVideo = mVideoRepository.findById(id);
		if(tModule.isPresent() && tVideo.isPresent()) {
			VideoEntity tVideoEntity=tVideo.get();
			ModuleEntity tModuleEntity= tModule.get();
			tVideoEntity.getModules().add(tModuleEntity);
			tModuleEntity.getVideos().add(tVideoEntity);
			mVideoRepository.save(tVideoEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a module to video
	 */
	
	public void unassignVideoModule(Long id, Long moduleId) {
		Optional<ModuleEntity> tModule = mModuleRepository.findById(moduleId);
		Optional<VideoEntity> tVideo = mVideoRepository.findById(id);
		if(tModule.isPresent() && tVideo.isPresent()) {
			VideoEntity tVideoEntity=tVideo.get();
			tVideoEntity.getModules().remove(tModule.get());
			mVideoRepository.save(tVideoEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Video modules
	 */
	
	public List<ModuleRO> findUnassignVideoModules(Long id) {
		List<ModuleRO> tModuleROs= new ArrayList<>();   
		 mVideoRepository.findById(id).ifPresent(en->{
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
	 * fetch list of Video topics
	 */
	
	public List<TopicRO> findVideoTopics(Long id) {
		List<TopicRO> tTopicROs= new ArrayList<>();   
		Optional<VideoEntity> tVideo = mVideoRepository.findById(id);
		if(tVideo.isPresent()) {
			tVideo.ifPresent(en->{
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
	 * assign a topic to video
	 */
	
	public void addVideoTopic(Long id, Long topicId) {
		Optional<TopicEntity> tTopic = mTopicRepository.findById(topicId);
		Optional<VideoEntity> tVideo = mVideoRepository.findById(id);
		if(tTopic.isPresent() && tVideo.isPresent()) {
			VideoEntity tVideoEntity=tVideo.get();
			TopicEntity tTopicEntity= tTopic.get();
			tVideoEntity.getTopics().add(tTopicEntity);
			tTopicEntity.getVideos().add(tVideoEntity);
			mVideoRepository.save(tVideoEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a topic to video
	 */
	
	public void unassignVideoTopic(Long id, Long topicId) {
		Optional<TopicEntity> tTopic = mTopicRepository.findById(topicId);
		Optional<VideoEntity> tVideo = mVideoRepository.findById(id);
		if(tTopic.isPresent() && tVideo.isPresent()) {
			VideoEntity tVideoEntity=tVideo.get();
			tVideoEntity.getTopics().remove(tTopic.get());
			mVideoRepository.save(tVideoEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Video topics
	 */
	
	public List<TopicRO> findUnassignVideoTopics(Long id) {
		List<TopicRO> tTopicROs= new ArrayList<>();   
		 mVideoRepository.findById(id).ifPresent(en->{
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
	 * fetch list of Video questions
	 */
	
	public List<QuestionRO> findVideoQuestions(Long id) {
		List<QuestionRO> tQuestionROs= new ArrayList<>();   
		Optional<VideoEntity> tVideo = mVideoRepository.findById(id);
		if(tVideo.isPresent()) {
			tVideo.ifPresent(en->{
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
	 * assign a question to video
	 */
	
	public void addVideoQuestion(Long id, Long questionId) {
		Optional<QuestionEntity> tQuestion = mQuestionRepository.findById(questionId);
		Optional<VideoEntity> tVideo = mVideoRepository.findById(id);
		if(tQuestion.isPresent() && tVideo.isPresent()) {
			VideoEntity tVideoEntity=tVideo.get();
			QuestionEntity tQuestionEntity= tQuestion.get();
			tVideoEntity.getQuestions().add(tQuestionEntity);
			tQuestionEntity.getVideos().add(tVideoEntity);
			mVideoRepository.save(tVideoEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a question to video
	 */
	
	public void unassignVideoQuestion(Long id, Long questionId) {
		Optional<QuestionEntity> tQuestion = mQuestionRepository.findById(questionId);
		Optional<VideoEntity> tVideo = mVideoRepository.findById(id);
		if(tQuestion.isPresent() && tVideo.isPresent()) {
			VideoEntity tVideoEntity=tVideo.get();
			tVideoEntity.getQuestions().remove(tQuestion.get());
			mVideoRepository.save(tVideoEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Video questions
	 */
	
	public List<QuestionRO> findUnassignVideoQuestions(Long id) {
		List<QuestionRO> tQuestionROs= new ArrayList<>();   
		 mVideoRepository.findById(id).ifPresent(en->{
			 List<QuestionEntity> tQuestions = mQuestionRepository.findAll();
			 tQuestions.removeAll(en.getQuestions());
			 tQuestions.forEach(re->{					
					tQuestionROs.add(mModelMapper.map(re, QuestionRO.class));
				});
		 });
		
		return tQuestionROs;
	}
	
	
	
	
	
	public VideoRO getVideoByKeyId(String key) {
		Optional<?> tVideo= Optional.ofNullable(mVideoRepository.findByKeyid(key));
		 if(tVideo.isPresent()) {
	            return mModelMapper.map(tVideo.get(), VideoRO.class);
	        }
	      return null;  
	}
	
	
	
	
}
