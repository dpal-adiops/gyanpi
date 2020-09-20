package  com.adiops.apigateway.course.service;

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
 
import com.adiops.apigateway.course.entity.CourseEntity;
import com.adiops.apigateway.course.repository.CourseRepository;
import com.adiops.apigateway.course.resourceobject.CourseRO;

import com.adiops.apigateway.module.entity.ModuleEntity;
import com.adiops.apigateway.module.repository.ModuleRepository;
import com.adiops.apigateway.module.resourceobject.ModuleRO;
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
 * This is the implementation class for Course responsible for all the CRUD operations and other business related operations.
 * @author Deepak Pal
 *
 */
@Service
public class CourseService{

	@Autowired
	CourseRepository mCourseRepository;

	@Autowired
	ModuleRepository mModuleRepository;
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
	 * fetch list of Course
	 */
	
	public List<CourseRO> getCourseROs() {
		List<CourseRO> tCourseROs = mCourseRepository.findAll(Sort.by(Sort.Direction.ASC, "keyid")).stream()
				.map(entity -> mModelMapper.map(entity, CourseRO.class)).collect(Collectors.toList());
		return tCourseROs;
	}

	/**
	 *
	 * get Course by id
	 */
	public CourseRO getCourseById(Long id) throws RestException {
        Optional<CourseEntity> tCourse = mCourseRepository.findById(id);
         
        if(tCourse.isPresent()) {
            return mModelMapper.map(tCourse.get(), CourseRO.class);
        } else {
            throw new RestException("No course record exist for given id");
        }
    }
	
	 
	/**
	 * create or update 
	 */
	public CourseRO createOrUpdateCourse(CourseRO tCourseRO) throws RestException 
    {
        CourseEntity newEntity ;
         
        if(tCourseRO.getId()!=null)
        {
        	newEntity=	 mCourseRepository.findById(tCourseRO.getId()).orElse(new CourseEntity());
        }
         else if(tCourseRO.getKeyid() !=null)
        {
        	newEntity=Optional.ofNullable(mCourseRepository.findByKeyid(tCourseRO.getKeyid())).orElse(new CourseEntity());
        }
        else
        {
        	newEntity=new CourseEntity();
        }
       
              
      
        	if(tCourseRO.getKeyid() !=null)
        	newEntity.setKeyid(tCourseRO.getKeyid());
        	if(tCourseRO.getName() !=null)
        	newEntity.setName(tCourseRO.getName());
        	if(tCourseRO.getDescription() !=null)
        	newEntity.setDescription(tCourseRO.getDescription());
        	if(tCourseRO.getAuthorId() !=null)
        	newEntity.setAuthorId(tCourseRO.getAuthorId());
        	if(tCourseRO.getDomainId() !=null)
        	newEntity.setDomainId(tCourseRO.getDomainId());

  	 try {
        	 newEntity = mCourseRepository.save(newEntity);   
		} catch (Exception e) {
			throw new RestException("Could not save result due to unique key exception");
		}
                 
        return  mModelMapper.map(newEntity, CourseRO.class);
    } 
     
	/**
	 * delete by Id
	 */
     public void deleteCourseById(Long id) throws RestException {
       Optional<CourseEntity> tCourse = mCourseRepository.findById(id);
         
        if(tCourse.isPresent()) { 
        
            mCourseRepository.deleteById(id);
        } else {
            throw new RestException("No course record exist for given id");
        }
    } 
    
	/**
	 * upload file to DB
	 */
	
	public void importCSV(MultipartFile file) throws RestException {
		try {
			List<CourseEntity> tCourses = CSVHelper.csvToPOJOs(file.getInputStream(), CourseEntity.class);
			tCourses=tCourses.stream().map(entity->{
				CourseEntity tCourseEntity=mCourseRepository.findByKeyid(entity.getKeyid());
				if(tCourseEntity!=null) {
					entity.setId(tCourseEntity.getId());
				}
				return entity;
			}).collect(Collectors.toList());
			mCourseRepository.saveAll(tCourses);
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
			List<CourseEntity> tCourses = CSVHelper.csvToPOJOs(is, CourseEntity.class);
			tCourses=tCourses.stream().map(entity->{
				CourseEntity tCourseEntity=mCourseRepository.findByKeyid(entity.getKeyid());
				if(tCourseEntity!=null) {
					entity.setId(tCourseEntity.getId());
				}
				return entity;
			}).collect(Collectors.toList());
			mCourseRepository.saveAll(tCourses);
		} catch (Exception e) {
			throw new RestException(RestException.ERROR_STATUS_BAD_REQUEST,
					"fail to store csv data: " + e.getMessage());
		}
	}

	/**
	 * get count
	 */
	public long count() {
		return mCourseRepository.count();
	}

	/**
	 *
	 * fetch list of Course
	 */
	
	public List<CourseRO> findAll(int pageNumber, int rowPerPage) {
		Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("id").ascending());
		List<CourseRO> tCourseROs = mCourseRepository.findAll(sortedByIdAsc).stream()
				.map(entity -> mModelMapper.map(entity, CourseRO.class)).collect(Collectors.toList());
		return tCourseROs;
	}
	
	
	/**
	 *
	 * fetch list of Course modules
	 */
	
	public List<ModuleRO> findCourseModules(Long id) {
		List<ModuleRO> tModuleROs= new ArrayList<>();   
		Optional<CourseEntity> tCourse = mCourseRepository.findById(id);
		if(tCourse.isPresent()) {
			tCourse.ifPresent(en->{
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
	 * assign a module to course
	 */
	
	public void addCourseModule(Long id, Long moduleId) {
		Optional<ModuleEntity> tModule = mModuleRepository.findById(moduleId);
		Optional<CourseEntity> tCourse = mCourseRepository.findById(id);
		if(tModule.isPresent() && tCourse.isPresent()) {
			CourseEntity tCourseEntity=tCourse.get();
			ModuleEntity tModuleEntity= tModule.get();
			tCourseEntity.getModules().add(tModuleEntity);
			tModuleEntity.getCourses().add(tCourseEntity);
			mCourseRepository.save(tCourseEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a module to course
	 */
	
	public void unassignCourseModule(Long id, Long moduleId) {
		Optional<ModuleEntity> tModule = mModuleRepository.findById(moduleId);
		Optional<CourseEntity> tCourse = mCourseRepository.findById(id);
		if(tModule.isPresent() && tCourse.isPresent()) {
			CourseEntity tCourseEntity=tCourse.get();
			tCourseEntity.getModules().remove(tModule.get());
			mCourseRepository.save(tCourseEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Course modules
	 */
	
	public List<ModuleRO> findUnassignCourseModules(Long id) {
		List<ModuleRO> tModuleROs= new ArrayList<>();   
		 mCourseRepository.findById(id).ifPresent(en->{
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
	 * fetch list of Course images
	 */
	
	public List<ImageRO> findCourseImages(Long id) {
		List<ImageRO> tImageROs= new ArrayList<>();   
		Optional<CourseEntity> tCourse = mCourseRepository.findById(id);
		if(tCourse.isPresent()) {
			tCourse.ifPresent(en->{
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
	 * assign a image to course
	 */
	
	public void addCourseImage(Long id, Long imageId) {
		Optional<ImageEntity> tImage = mImageRepository.findById(imageId);
		Optional<CourseEntity> tCourse = mCourseRepository.findById(id);
		if(tImage.isPresent() && tCourse.isPresent()) {
			CourseEntity tCourseEntity=tCourse.get();
			ImageEntity tImageEntity= tImage.get();
			tCourseEntity.getImages().add(tImageEntity);
			tImageEntity.getCourses().add(tCourseEntity);
			mCourseRepository.save(tCourseEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a image to course
	 */
	
	public void unassignCourseImage(Long id, Long imageId) {
		Optional<ImageEntity> tImage = mImageRepository.findById(imageId);
		Optional<CourseEntity> tCourse = mCourseRepository.findById(id);
		if(tImage.isPresent() && tCourse.isPresent()) {
			CourseEntity tCourseEntity=tCourse.get();
			tCourseEntity.getImages().remove(tImage.get());
			mCourseRepository.save(tCourseEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Course images
	 */
	
	public List<ImageRO> findUnassignCourseImages(Long id) {
		List<ImageRO> tImageROs= new ArrayList<>();   
		 mCourseRepository.findById(id).ifPresent(en->{
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
	 * fetch list of Course videos
	 */
	
	public List<VideoRO> findCourseVideos(Long id) {
		List<VideoRO> tVideoROs= new ArrayList<>();   
		Optional<CourseEntity> tCourse = mCourseRepository.findById(id);
		if(tCourse.isPresent()) {
			tCourse.ifPresent(en->{
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
	 * assign a video to course
	 */
	
	public void addCourseVideo(Long id, Long videoId) {
		Optional<VideoEntity> tVideo = mVideoRepository.findById(videoId);
		Optional<CourseEntity> tCourse = mCourseRepository.findById(id);
		if(tVideo.isPresent() && tCourse.isPresent()) {
			CourseEntity tCourseEntity=tCourse.get();
			VideoEntity tVideoEntity= tVideo.get();
			tCourseEntity.getVideos().add(tVideoEntity);
			tVideoEntity.getCourses().add(tCourseEntity);
			mCourseRepository.save(tCourseEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a video to course
	 */
	
	public void unassignCourseVideo(Long id, Long videoId) {
		Optional<VideoEntity> tVideo = mVideoRepository.findById(videoId);
		Optional<CourseEntity> tCourse = mCourseRepository.findById(id);
		if(tVideo.isPresent() && tCourse.isPresent()) {
			CourseEntity tCourseEntity=tCourse.get();
			tCourseEntity.getVideos().remove(tVideo.get());
			mCourseRepository.save(tCourseEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Course videos
	 */
	
	public List<VideoRO> findUnassignCourseVideos(Long id) {
		List<VideoRO> tVideoROs= new ArrayList<>();   
		 mCourseRepository.findById(id).ifPresent(en->{
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
	 * fetch list of Course pages
	 */
	
	public List<PageRO> findCoursePages(Long id) {
		List<PageRO> tPageROs= new ArrayList<>();   
		Optional<CourseEntity> tCourse = mCourseRepository.findById(id);
		if(tCourse.isPresent()) {
			tCourse.ifPresent(en->{
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
	 * assign a page to course
	 */
	
	public void addCoursePage(Long id, Long pageId) {
		Optional<PageEntity> tPage = mPageRepository.findById(pageId);
		Optional<CourseEntity> tCourse = mCourseRepository.findById(id);
		if(tPage.isPresent() && tCourse.isPresent()) {
			CourseEntity tCourseEntity=tCourse.get();
			PageEntity tPageEntity= tPage.get();
			tCourseEntity.getPages().add(tPageEntity);
			tPageEntity.getCourses().add(tCourseEntity);
			mCourseRepository.save(tCourseEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a page to course
	 */
	
	public void unassignCoursePage(Long id, Long pageId) {
		Optional<PageEntity> tPage = mPageRepository.findById(pageId);
		Optional<CourseEntity> tCourse = mCourseRepository.findById(id);
		if(tPage.isPresent() && tCourse.isPresent()) {
			CourseEntity tCourseEntity=tCourse.get();
			tCourseEntity.getPages().remove(tPage.get());
			mCourseRepository.save(tCourseEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Course pages
	 */
	
	public List<PageRO> findUnassignCoursePages(Long id) {
		List<PageRO> tPageROs= new ArrayList<>();   
		 mCourseRepository.findById(id).ifPresent(en->{
			 List<PageEntity> tPages = mPageRepository.findAll();
			 tPages.removeAll(en.getPages());
			 tPages.forEach(re->{					
					tPageROs.add(mModelMapper.map(re, PageRO.class));
				});
		 });
		
		return tPageROs;
	}
	
	
	
	
	
	public CourseRO getCourseByKeyId(String key) {
		Optional<?> tCourse= Optional.ofNullable(mCourseRepository.findByKeyid(key));
		 if(tCourse.isPresent()) {
	            return mModelMapper.map(tCourse.get(), CourseRO.class);
	        }
	      return null;  
	}
	
	
	
	
}
