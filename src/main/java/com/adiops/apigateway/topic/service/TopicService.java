package  com.adiops.apigateway.topic.service;

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
 
import com.adiops.apigateway.topic.entity.TopicEntity;
import com.adiops.apigateway.topic.repository.TopicRepository;
import com.adiops.apigateway.topic.resourceobject.TopicRO;

import com.adiops.apigateway.module.entity.ModuleEntity;
import com.adiops.apigateway.module.repository.ModuleRepository;
import com.adiops.apigateway.module.resourceobject.ModuleRO;
import com.adiops.apigateway.question.entity.QuestionEntity;
import com.adiops.apigateway.question.repository.QuestionRepository;
import com.adiops.apigateway.question.resourceobject.QuestionRO;
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
 * This is the implementation class for Topic responsible for all the CRUD operations and other business related operations.
 * @author Deepak Pal
 *
 */
@Service
public class TopicService{

	@Autowired
	TopicRepository mTopicRepository;

	@Autowired
	ModuleRepository mModuleRepository;
	@Autowired
	QuestionRepository mQuestionRepository;
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
	 * fetch list of Topic
	 */
	
	public List<TopicRO> getTopicROs() {
		List<TopicRO> tTopicROs = mTopicRepository.findAll(Sort.by(Sort.Direction.ASC, "keyid")).stream()
				.map(entity -> mModelMapper.map(entity, TopicRO.class)).collect(Collectors.toList());
		return tTopicROs;
	}

	/**
	 *
	 * get Topic by id
	 */
	public TopicRO getTopicById(Long id) throws RestException {
        Optional<TopicEntity> tTopic = mTopicRepository.findById(id);
         
        if(tTopic.isPresent()) {
            return mModelMapper.map(tTopic.get(), TopicRO.class);
        } else {
            throw new RestException("No topic record exist for given id");
        }
    }
	
	 
	/**
	 * create or update 
	 */
	public TopicRO createOrUpdateTopic(TopicRO tTopicRO) throws RestException 
    {
        TopicEntity newEntity ;
         
        if(tTopicRO.getId()!=null)
        {
        	newEntity=	 mTopicRepository.findById(tTopicRO.getId()).orElse(new TopicEntity());
        }
        else
        {
        	newEntity=new TopicEntity();
        }
       
              
      
        	if(tTopicRO.getKeyid() !=null)
        	newEntity.setKeyid(tTopicRO.getKeyid());
        	if(tTopicRO.getName() !=null)
        	newEntity.setName(tTopicRO.getName());
        	if(tTopicRO.getDescription() !=null)
        	newEntity.setDescription(tTopicRO.getDescription());
        	if(tTopicRO.getTitle() !=null)
        	newEntity.setTitle(tTopicRO.getTitle());
        	if(tTopicRO.getAuthorId() !=null)
        	newEntity.setAuthorId(tTopicRO.getAuthorId());
        	if(tTopicRO.getDomainId() !=null)
        	newEntity.setDomainId(tTopicRO.getDomainId());

  	 try {
        	 newEntity = mTopicRepository.save(newEntity);   
		} catch (Exception e) {
			throw new RestException("Could not save result due to unique key exception");
		}
                 
        return  mModelMapper.map(newEntity, TopicRO.class);
    } 
     
	/**
	 * delete by Id
	 */
     public void deleteTopicById(Long id) throws RestException {
       Optional<TopicEntity> tTopic = mTopicRepository.findById(id);
         
        if(tTopic.isPresent()) { 
        
            mTopicRepository.deleteById(id);
        } else {
            throw new RestException("No topic record exist for given id");
        }
    } 
    
	/**
	 * upload file to DB
	 */
	
	public void importCSV(MultipartFile file) throws RestException {
		try {
			List<TopicEntity> tTopics = CSVHelper.csvToPOJOs(file.getInputStream(), TopicEntity.class);
			mTopicRepository.deleteAll();
			mTopicRepository.saveAll(tTopics);
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
			List<TopicEntity> tTopics = CSVHelper.csvToPOJOs(is, TopicEntity.class);
			mTopicRepository.saveAll(tTopics);
		} catch (Exception e) {
			throw new RestException(RestException.ERROR_STATUS_BAD_REQUEST,
					"fail to store csv data: " + e.getMessage());
		}
	}

	/**
	 * get count
	 */
	public long count() {
		return mTopicRepository.count();
	}

	/**
	 *
	 * fetch list of Topic
	 */
	
	public List<TopicRO> findAll(int pageNumber, int rowPerPage) {
		Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("id").ascending());
		List<TopicRO> tTopicROs = mTopicRepository.findAll(sortedByIdAsc).stream()
				.map(entity -> mModelMapper.map(entity, TopicRO.class)).collect(Collectors.toList());
		return tTopicROs;
	}
	
	
	/**
	 *
	 * fetch list of Topic modules
	 */
	
	public List<ModuleRO> findTopicModules(Long id) {
		List<ModuleRO> tModuleROs= new ArrayList<>();   
		Optional<TopicEntity> tTopic = mTopicRepository.findById(id);
		if(tTopic.isPresent()) {
			tTopic.ifPresent(en->{
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
	 * assign a module to topic
	 */
	
	public void addTopicModule(Long id, Long moduleId) {
		Optional<ModuleEntity> tModule = mModuleRepository.findById(moduleId);
		Optional<TopicEntity> tTopic = mTopicRepository.findById(id);
		if(tModule.isPresent() && tTopic.isPresent()) {
			TopicEntity tTopicEntity=tTopic.get();
			ModuleEntity tModuleEntity= tModule.get();
			tTopicEntity.getModules().add(tModuleEntity);
			tModuleEntity.getTopics().add(tTopicEntity);
			mTopicRepository.save(tTopicEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a module to topic
	 */
	
	public void unassignTopicModule(Long id, Long moduleId) {
		Optional<ModuleEntity> tModule = mModuleRepository.findById(moduleId);
		Optional<TopicEntity> tTopic = mTopicRepository.findById(id);
		if(tModule.isPresent() && tTopic.isPresent()) {
			TopicEntity tTopicEntity=tTopic.get();
			tTopicEntity.getModules().remove(tModule.get());
			mTopicRepository.save(tTopicEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Topic modules
	 */
	
	public List<ModuleRO> findUnassignTopicModules(Long id) {
		List<ModuleRO> tModuleROs= new ArrayList<>();   
		 mTopicRepository.findById(id).ifPresent(en->{
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
	 * fetch list of Topic questions
	 */
	
	public List<QuestionRO> findTopicQuestions(Long id) {
		List<QuestionRO> tQuestionROs= new ArrayList<>();   
		Optional<TopicEntity> tTopic = mTopicRepository.findById(id);
		if(tTopic.isPresent()) {
			tTopic.ifPresent(en->{
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
	 * assign a question to topic
	 */
	
	public void addTopicQuestion(Long id, Long questionId) {
		Optional<QuestionEntity> tQuestion = mQuestionRepository.findById(questionId);
		Optional<TopicEntity> tTopic = mTopicRepository.findById(id);
		if(tQuestion.isPresent() && tTopic.isPresent()) {
			TopicEntity tTopicEntity=tTopic.get();
			QuestionEntity tQuestionEntity= tQuestion.get();
			tTopicEntity.getQuestions().add(tQuestionEntity);
			tQuestionEntity.getTopics().add(tTopicEntity);
			mTopicRepository.save(tTopicEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a question to topic
	 */
	
	public void unassignTopicQuestion(Long id, Long questionId) {
		Optional<QuestionEntity> tQuestion = mQuestionRepository.findById(questionId);
		Optional<TopicEntity> tTopic = mTopicRepository.findById(id);
		if(tQuestion.isPresent() && tTopic.isPresent()) {
			TopicEntity tTopicEntity=tTopic.get();
			tTopicEntity.getQuestions().remove(tQuestion.get());
			mTopicRepository.save(tTopicEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Topic questions
	 */
	
	public List<QuestionRO> findUnassignTopicQuestions(Long id) {
		List<QuestionRO> tQuestionROs= new ArrayList<>();   
		 mTopicRepository.findById(id).ifPresent(en->{
			 List<QuestionEntity> tQuestions = mQuestionRepository.findAll();
			 tQuestions.removeAll(en.getQuestions());
			 tQuestions.forEach(re->{					
					tQuestionROs.add(mModelMapper.map(re, QuestionRO.class));
				});
		 });
		
		return tQuestionROs;
	}
	
	
	/**
	 *
	 * fetch list of Topic images
	 */
	
	public List<ImageRO> findTopicImages(Long id) {
		List<ImageRO> tImageROs= new ArrayList<>();   
		Optional<TopicEntity> tTopic = mTopicRepository.findById(id);
		if(tTopic.isPresent()) {
			tTopic.ifPresent(en->{
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
	 * assign a image to topic
	 */
	
	public void addTopicImage(Long id, Long imageId) {
		Optional<ImageEntity> tImage = mImageRepository.findById(imageId);
		Optional<TopicEntity> tTopic = mTopicRepository.findById(id);
		if(tImage.isPresent() && tTopic.isPresent()) {
			TopicEntity tTopicEntity=tTopic.get();
			ImageEntity tImageEntity= tImage.get();
			tTopicEntity.getImages().add(tImageEntity);
			tImageEntity.getTopics().add(tTopicEntity);
			mTopicRepository.save(tTopicEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a image to topic
	 */
	
	public void unassignTopicImage(Long id, Long imageId) {
		Optional<ImageEntity> tImage = mImageRepository.findById(imageId);
		Optional<TopicEntity> tTopic = mTopicRepository.findById(id);
		if(tImage.isPresent() && tTopic.isPresent()) {
			TopicEntity tTopicEntity=tTopic.get();
			tTopicEntity.getImages().remove(tImage.get());
			mTopicRepository.save(tTopicEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Topic images
	 */
	
	public List<ImageRO> findUnassignTopicImages(Long id) {
		List<ImageRO> tImageROs= new ArrayList<>();   
		 mTopicRepository.findById(id).ifPresent(en->{
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
	 * fetch list of Topic videos
	 */
	
	public List<VideoRO> findTopicVideos(Long id) {
		List<VideoRO> tVideoROs= new ArrayList<>();   
		Optional<TopicEntity> tTopic = mTopicRepository.findById(id);
		if(tTopic.isPresent()) {
			tTopic.ifPresent(en->{
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
	 * assign a video to topic
	 */
	
	public void addTopicVideo(Long id, Long videoId) {
		Optional<VideoEntity> tVideo = mVideoRepository.findById(videoId);
		Optional<TopicEntity> tTopic = mTopicRepository.findById(id);
		if(tVideo.isPresent() && tTopic.isPresent()) {
			TopicEntity tTopicEntity=tTopic.get();
			VideoEntity tVideoEntity= tVideo.get();
			tTopicEntity.getVideos().add(tVideoEntity);
			tVideoEntity.getTopics().add(tTopicEntity);
			mTopicRepository.save(tTopicEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a video to topic
	 */
	
	public void unassignTopicVideo(Long id, Long videoId) {
		Optional<VideoEntity> tVideo = mVideoRepository.findById(videoId);
		Optional<TopicEntity> tTopic = mTopicRepository.findById(id);
		if(tVideo.isPresent() && tTopic.isPresent()) {
			TopicEntity tTopicEntity=tTopic.get();
			tTopicEntity.getVideos().remove(tVideo.get());
			mTopicRepository.save(tTopicEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Topic videos
	 */
	
	public List<VideoRO> findUnassignTopicVideos(Long id) {
		List<VideoRO> tVideoROs= new ArrayList<>();   
		 mTopicRepository.findById(id).ifPresent(en->{
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
	 * fetch list of Topic pages
	 */
	
	public List<PageRO> findTopicPages(Long id) {
		List<PageRO> tPageROs= new ArrayList<>();   
		Optional<TopicEntity> tTopic = mTopicRepository.findById(id);
		if(tTopic.isPresent()) {
			tTopic.ifPresent(en->{
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
	 * assign a page to topic
	 */
	
	public void addTopicPage(Long id, Long pageId) {
		Optional<PageEntity> tPage = mPageRepository.findById(pageId);
		Optional<TopicEntity> tTopic = mTopicRepository.findById(id);
		if(tPage.isPresent() && tTopic.isPresent()) {
			TopicEntity tTopicEntity=tTopic.get();
			PageEntity tPageEntity= tPage.get();
			tTopicEntity.getPages().add(tPageEntity);
			tPageEntity.getTopics().add(tTopicEntity);
			mTopicRepository.save(tTopicEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a page to topic
	 */
	
	public void unassignTopicPage(Long id, Long pageId) {
		Optional<PageEntity> tPage = mPageRepository.findById(pageId);
		Optional<TopicEntity> tTopic = mTopicRepository.findById(id);
		if(tPage.isPresent() && tTopic.isPresent()) {
			TopicEntity tTopicEntity=tTopic.get();
			tTopicEntity.getPages().remove(tPage.get());
			mTopicRepository.save(tTopicEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Topic pages
	 */
	
	public List<PageRO> findUnassignTopicPages(Long id) {
		List<PageRO> tPageROs= new ArrayList<>();   
		 mTopicRepository.findById(id).ifPresent(en->{
			 List<PageEntity> tPages = mPageRepository.findAll();
			 tPages.removeAll(en.getPages());
			 tPages.forEach(re->{					
					tPageROs.add(mModelMapper.map(re, PageRO.class));
				});
		 });
		
		return tPageROs;
	}
	
	
	
	
	
	public TopicRO getTopicByKeyId(String key) {
		Optional<?> tTopic= Optional.ofNullable(mTopicRepository.findByKeyid(key));
		 if(tTopic.isPresent()) {
	            return mModelMapper.map(tTopic.get(), TopicRO.class);
	        }
	      return null;  
	}
	
	
	
	
}
