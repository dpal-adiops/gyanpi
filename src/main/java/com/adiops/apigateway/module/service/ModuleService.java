package  com.adiops.apigateway.module.service;

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
 
import com.adiops.apigateway.module.entity.ModuleEntity;
import com.adiops.apigateway.module.repository.ModuleRepository;
import com.adiops.apigateway.module.resourceobject.ModuleRO;

import com.adiops.apigateway.course.entity.CourseEntity;
import com.adiops.apigateway.course.repository.CourseRepository;
import com.adiops.apigateway.course.resourceobject.CourseRO;
import com.adiops.apigateway.topic.entity.TopicEntity;
import com.adiops.apigateway.topic.repository.TopicRepository;
import com.adiops.apigateway.topic.resourceobject.TopicRO;
import com.adiops.apigateway.image.entity.ImageEntity;
import com.adiops.apigateway.image.repository.ImageRepository;
import com.adiops.apigateway.image.resourceobject.ImageRO;
import com.adiops.apigateway.video.entity.VideoEntity;
import com.adiops.apigateway.video.repository.VideoRepository;
import com.adiops.apigateway.video.resourceobject.VideoRO;
import com.adiops.apigateway.page.entity.PageEntity;
import com.adiops.apigateway.page.repository.PageRepository;
import com.adiops.apigateway.page.resourceobject.PageRO;




/**
 * This is the implementation class for Module responsible for all the CRUD operations and other business related operations.
 * @author Deepak Pal
 *
 */
@Service
public class ModuleService{

	@Autowired
	ModuleRepository mModuleRepository;

	@Autowired
	CourseRepository mCourseRepository;
	@Autowired
	TopicRepository mTopicRepository;
	@Autowired
	ImageRepository mImageRepository;
	@Autowired
	VideoRepository mVideoRepository;
	@Autowired
	PageRepository mPageRepository;
	
	
	
	
	@Autowired
	private ModelMapper mModelMapper;

	/**
	 *
	 * fetch list of Module
	 */
	
	public List<ModuleRO> getModuleROs() {
		List<ModuleRO> tModuleROs = mModuleRepository.findAll(Sort.by(Sort.Direction.ASC, "keyid")).stream()
				.map(entity -> mModelMapper.map(entity, ModuleRO.class)).collect(Collectors.toList());
		return tModuleROs;
	}

	/**
	 *
	 * get Module by id
	 */
	public ModuleRO getModuleById(Long id) throws RestException {
        Optional<ModuleEntity> tModule = mModuleRepository.findById(id);
         
        if(tModule.isPresent()) {
            return mModelMapper.map(tModule.get(), ModuleRO.class);
        } else {
            throw new RestException("No module record exist for given id");
        }
    }
	
	 
	/**
	 * create or update 
	 */
	public ModuleRO createOrUpdateModule(ModuleRO tModuleRO) throws RestException 
    {
        ModuleEntity newEntity ;
         
        if(tModuleRO.getId()!=null)
        {
        	newEntity=	 mModuleRepository.findById(tModuleRO.getId()).orElse(new ModuleEntity());
        }
        else
        {
        	newEntity=new ModuleEntity();
        }
       
              
      
        	if(tModuleRO.getKeyid() !=null)
        	newEntity.setKeyid(tModuleRO.getKeyid());
        	if(tModuleRO.getName() !=null)
        	newEntity.setName(tModuleRO.getName());
        	if(tModuleRO.getDescription() !=null)
        	newEntity.setDescription(tModuleRO.getDescription());
        	if(tModuleRO.getAuthorId() !=null)
        	newEntity.setAuthorId(tModuleRO.getAuthorId());
        	if(tModuleRO.getDomainId() !=null)
        	newEntity.setDomainId(tModuleRO.getDomainId());

  	 try {
        	 newEntity = mModuleRepository.save(newEntity);   
		} catch (Exception e) {
			throw new RestException("Could not save result due to unique key exception");
		}
                 
        return  mModelMapper.map(newEntity, ModuleRO.class);
    } 
     
	/**
	 * delete by Id
	 */
     public void deleteModuleById(Long id) throws RestException {
       Optional<ModuleEntity> tModule = mModuleRepository.findById(id);
         
        if(tModule.isPresent()) { 
        
            mModuleRepository.deleteById(id);
        } else {
            throw new RestException("No module record exist for given id");
        }
    } 
    
	/**
	 * upload file to DB
	 */
	
	public void importCSV(MultipartFile file) throws RestException {
		try {
			List<ModuleEntity> tModules = CSVHelper.csvToPOJOs(file.getInputStream(), ModuleEntity.class);
			mModuleRepository.deleteAll();
			mModuleRepository.saveAll(tModules);
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
			List<ModuleEntity> tModules = CSVHelper.csvToPOJOs(is, ModuleEntity.class);
			mModuleRepository.saveAll(tModules);
		} catch (Exception e) {
			throw new RestException(RestException.ERROR_STATUS_BAD_REQUEST,
					"fail to store csv data: " + e.getMessage());
		}
	}

	/**
	 * get count
	 */
	public long count() {
		return mModuleRepository.count();
	}

	/**
	 *
	 * fetch list of Module
	 */
	
	public List<ModuleRO> findAll(int pageNumber, int rowPerPage) {
		Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("id").ascending());
		List<ModuleRO> tModuleROs = mModuleRepository.findAll(sortedByIdAsc).stream()
				.map(entity -> mModelMapper.map(entity, ModuleRO.class)).collect(Collectors.toList());
		return tModuleROs;
	}
	
	
	/**
	 *
	 * fetch list of Module courses
	 */
	
	public List<CourseRO> findModuleCourses(Long id) {
		List<CourseRO> tCourseROs= new ArrayList<>();   
		Optional<ModuleEntity> tModule = mModuleRepository.findById(id);
		if(tModule.isPresent()) {
			tModule.ifPresent(en->{
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
	 * assign a course to module
	 */
	
	public void addModuleCourse(Long id, Long courseId) {
		Optional<CourseEntity> tCourse = mCourseRepository.findById(courseId);
		Optional<ModuleEntity> tModule = mModuleRepository.findById(id);
		if(tCourse.isPresent() && tModule.isPresent()) {
			ModuleEntity tModuleEntity=tModule.get();
			CourseEntity tCourseEntity= tCourse.get();
			tModuleEntity.getCourses().add(tCourseEntity);
			tCourseEntity.getModules().add(tModuleEntity);
			mModuleRepository.save(tModuleEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a course to module
	 */
	
	public void unassignModuleCourse(Long id, Long courseId) {
		Optional<CourseEntity> tCourse = mCourseRepository.findById(courseId);
		Optional<ModuleEntity> tModule = mModuleRepository.findById(id);
		if(tCourse.isPresent() && tModule.isPresent()) {
			ModuleEntity tModuleEntity=tModule.get();
			tModuleEntity.getCourses().remove(tCourse.get());
			mModuleRepository.save(tModuleEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Module courses
	 */
	
	public List<CourseRO> findUnassignModuleCourses(Long id) {
		List<CourseRO> tCourseROs= new ArrayList<>();   
		 mModuleRepository.findById(id).ifPresent(en->{
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
	 * fetch list of Module topics
	 */
	
	public List<TopicRO> findModuleTopics(Long id) {
		List<TopicRO> tTopicROs= new ArrayList<>();   
		Optional<ModuleEntity> tModule = mModuleRepository.findById(id);
		if(tModule.isPresent()) {
			tModule.ifPresent(en->{
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
	 * assign a topic to module
	 */
	
	public void addModuleTopic(Long id, Long topicId) {
		Optional<TopicEntity> tTopic = mTopicRepository.findById(topicId);
		Optional<ModuleEntity> tModule = mModuleRepository.findById(id);
		if(tTopic.isPresent() && tModule.isPresent()) {
			ModuleEntity tModuleEntity=tModule.get();
			TopicEntity tTopicEntity= tTopic.get();
			tModuleEntity.getTopics().add(tTopicEntity);
			tTopicEntity.getModules().add(tModuleEntity);
			mModuleRepository.save(tModuleEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a topic to module
	 */
	
	public void unassignModuleTopic(Long id, Long topicId) {
		Optional<TopicEntity> tTopic = mTopicRepository.findById(topicId);
		Optional<ModuleEntity> tModule = mModuleRepository.findById(id);
		if(tTopic.isPresent() && tModule.isPresent()) {
			ModuleEntity tModuleEntity=tModule.get();
			tModuleEntity.getTopics().remove(tTopic.get());
			mModuleRepository.save(tModuleEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Module topics
	 */
	
	public List<TopicRO> findUnassignModuleTopics(Long id) {
		List<TopicRO> tTopicROs= new ArrayList<>();   
		 mModuleRepository.findById(id).ifPresent(en->{
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
	 * fetch list of Module images
	 */
	
	public List<ImageRO> findModuleImages(Long id) {
		List<ImageRO> tImageROs= new ArrayList<>();   
		Optional<ModuleEntity> tModule = mModuleRepository.findById(id);
		if(tModule.isPresent()) {
			tModule.ifPresent(en->{
				en.getImages().forEach(re->{					
					tImageROs.add(mModelMapper.map(re, ImageRO.class));
				});
			});
		}	
		Collections.sort(tImageROs, new Comparator<ImageRO>() {
			  @Override
			  public int compare(ImageRO u1, ImageRO u2) {
			    return u1.getKeyid().compareTo(u2.getKeyid());
			  }
			});
						
		return tImageROs;
	}
	
	/**
	 *
	 * assign a image to module
	 */
	
	public void addModuleImage(Long id, Long imageId) {
		Optional<ImageEntity> tImage = mImageRepository.findById(imageId);
		Optional<ModuleEntity> tModule = mModuleRepository.findById(id);
		if(tImage.isPresent() && tModule.isPresent()) {
			ModuleEntity tModuleEntity=tModule.get();
			ImageEntity tImageEntity= tImage.get();
			tModuleEntity.getImages().add(tImageEntity);
			tImageEntity.getModules().add(tModuleEntity);
			mModuleRepository.save(tModuleEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a image to module
	 */
	
	public void unassignModuleImage(Long id, Long imageId) {
		Optional<ImageEntity> tImage = mImageRepository.findById(imageId);
		Optional<ModuleEntity> tModule = mModuleRepository.findById(id);
		if(tImage.isPresent() && tModule.isPresent()) {
			ModuleEntity tModuleEntity=tModule.get();
			tModuleEntity.getImages().remove(tImage.get());
			mModuleRepository.save(tModuleEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Module images
	 */
	
	public List<ImageRO> findUnassignModuleImages(Long id) {
		List<ImageRO> tImageROs= new ArrayList<>();   
		 mModuleRepository.findById(id).ifPresent(en->{
			 List<ImageEntity> tImages = mImageRepository.findAll();
			 tImages.removeAll(en.getImages());
			 tImages.forEach(re->{					
					tImageROs.add(mModelMapper.map(re, ImageRO.class));
				});
		 });
		
		return tImageROs;
	}
	
	
	/**
	 *
	 * fetch list of Module videos
	 */
	
	public List<VideoRO> findModuleVideos(Long id) {
		List<VideoRO> tVideoROs= new ArrayList<>();   
		Optional<ModuleEntity> tModule = mModuleRepository.findById(id);
		if(tModule.isPresent()) {
			tModule.ifPresent(en->{
				en.getVideos().forEach(re->{					
					tVideoROs.add(mModelMapper.map(re, VideoRO.class));
				});
			});
		}	
		Collections.sort(tVideoROs, new Comparator<VideoRO>() {
			  @Override
			  public int compare(VideoRO u1, VideoRO u2) {
			    return u1.getKeyid().compareTo(u2.getKeyid());
			  }
			});
						
		return tVideoROs;
	}
	
	/**
	 *
	 * assign a video to module
	 */
	
	public void addModuleVideo(Long id, Long videoId) {
		Optional<VideoEntity> tVideo = mVideoRepository.findById(videoId);
		Optional<ModuleEntity> tModule = mModuleRepository.findById(id);
		if(tVideo.isPresent() && tModule.isPresent()) {
			ModuleEntity tModuleEntity=tModule.get();
			VideoEntity tVideoEntity= tVideo.get();
			tModuleEntity.getVideos().add(tVideoEntity);
			tVideoEntity.getModules().add(tModuleEntity);
			mModuleRepository.save(tModuleEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a video to module
	 */
	
	public void unassignModuleVideo(Long id, Long videoId) {
		Optional<VideoEntity> tVideo = mVideoRepository.findById(videoId);
		Optional<ModuleEntity> tModule = mModuleRepository.findById(id);
		if(tVideo.isPresent() && tModule.isPresent()) {
			ModuleEntity tModuleEntity=tModule.get();
			tModuleEntity.getVideos().remove(tVideo.get());
			mModuleRepository.save(tModuleEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Module videos
	 */
	
	public List<VideoRO> findUnassignModuleVideos(Long id) {
		List<VideoRO> tVideoROs= new ArrayList<>();   
		 mModuleRepository.findById(id).ifPresent(en->{
			 List<VideoEntity> tVideos = mVideoRepository.findAll();
			 tVideos.removeAll(en.getVideos());
			 tVideos.forEach(re->{					
					tVideoROs.add(mModelMapper.map(re, VideoRO.class));
				});
		 });
		
		return tVideoROs;
	}
	
	
	/**
	 *
	 * fetch list of Module pages
	 */
	
	public List<PageRO> findModulePages(Long id) {
		List<PageRO> tPageROs= new ArrayList<>();   
		Optional<ModuleEntity> tModule = mModuleRepository.findById(id);
		if(tModule.isPresent()) {
			tModule.ifPresent(en->{
				en.getPages().forEach(re->{					
					tPageROs.add(mModelMapper.map(re, PageRO.class));
				});
			});
		}	
		Collections.sort(tPageROs, new Comparator<PageRO>() {
			  @Override
			  public int compare(PageRO u1, PageRO u2) {
			    return u1.getKeyid().compareTo(u2.getKeyid());
			  }
			});
						
		return tPageROs;
	}
	
	/**
	 *
	 * assign a page to module
	 */
	
	public void addModulePage(Long id, Long pageId) {
		Optional<PageEntity> tPage = mPageRepository.findById(pageId);
		Optional<ModuleEntity> tModule = mModuleRepository.findById(id);
		if(tPage.isPresent() && tModule.isPresent()) {
			ModuleEntity tModuleEntity=tModule.get();
			PageEntity tPageEntity= tPage.get();
			tModuleEntity.getPages().add(tPageEntity);
			tPageEntity.getModules().add(tModuleEntity);
			mModuleRepository.save(tModuleEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a page to module
	 */
	
	public void unassignModulePage(Long id, Long pageId) {
		Optional<PageEntity> tPage = mPageRepository.findById(pageId);
		Optional<ModuleEntity> tModule = mModuleRepository.findById(id);
		if(tPage.isPresent() && tModule.isPresent()) {
			ModuleEntity tModuleEntity=tModule.get();
			tModuleEntity.getPages().remove(tPage.get());
			mModuleRepository.save(tModuleEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Module pages
	 */
	
	public List<PageRO> findUnassignModulePages(Long id) {
		List<PageRO> tPageROs= new ArrayList<>();   
		 mModuleRepository.findById(id).ifPresent(en->{
			 List<PageEntity> tPages = mPageRepository.findAll();
			 tPages.removeAll(en.getPages());
			 tPages.forEach(re->{					
					tPageROs.add(mModelMapper.map(re, PageRO.class));
				});
		 });
		
		return tPageROs;
	}
	
	
	
	
	
	public ModuleRO getModuleByKeyId(String key) {
		Optional<?> tModule= Optional.ofNullable(mModuleRepository.findByKeyid(key));
		 if(tModule.isPresent()) {
	            return mModelMapper.map(tModule.get(), ModuleRO.class);
	        }
	      return null;  
	}
	
	
	
	
}
