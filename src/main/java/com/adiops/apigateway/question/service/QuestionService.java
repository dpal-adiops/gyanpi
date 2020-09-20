package  com.adiops.apigateway.question.service;

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
 
import com.adiops.apigateway.question.entity.QuestionEntity;
import com.adiops.apigateway.question.repository.QuestionRepository;
import com.adiops.apigateway.question.resourceobject.QuestionRO;

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
 * This is the implementation class for Question responsible for all the CRUD operations and other business related operations.
 * @author Deepak Pal
 *
 */
@Service
public class QuestionService{

	@Autowired
	QuestionRepository mQuestionRepository;

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
	 * fetch list of Question
	 */
	
	public List<QuestionRO> getQuestionROs() {
		List<QuestionRO> tQuestionROs = mQuestionRepository.findAll(Sort.by(Sort.Direction.ASC, "keyid")).stream()
				.map(entity -> mModelMapper.map(entity, QuestionRO.class)).collect(Collectors.toList());
		return tQuestionROs;
	}

	/**
	 *
	 * get Question by id
	 */
	public QuestionRO getQuestionById(Long id) throws RestException {
        Optional<QuestionEntity> tQuestion = mQuestionRepository.findById(id);
         
        if(tQuestion.isPresent()) {
            return mModelMapper.map(tQuestion.get(), QuestionRO.class);
        } else {
            throw new RestException("No question record exist for given id");
        }
    }
	
	 
	/**
	 * create or update 
	 */
	public QuestionRO createOrUpdateQuestion(QuestionRO tQuestionRO) throws RestException 
    {
        QuestionEntity newEntity ;
         
        if(tQuestionRO.getId()!=null)
        {
        	newEntity=	 mQuestionRepository.findById(tQuestionRO.getId()).orElse(new QuestionEntity());
        }
         else if(tQuestionRO.getKeyid() !=null)
        {
        	newEntity=Optional.ofNullable(mQuestionRepository.findByKeyid(tQuestionRO.getKeyid())).orElse(new QuestionEntity());
        }
        else
        {
        	newEntity=new QuestionEntity();
        }
       
              
      
        	if(tQuestionRO.getKeyid() !=null)
        	newEntity.setKeyid(tQuestionRO.getKeyid());
        	if(tQuestionRO.getTitle() !=null)
        	newEntity.setTitle(tQuestionRO.getTitle());
        	if(tQuestionRO.getDescription() !=null)
        	newEntity.setDescription(tQuestionRO.getDescription());
        	if(tQuestionRO.getAnswer() !=null)
        	newEntity.setAnswer(tQuestionRO.getAnswer());
        	if(tQuestionRO.getAuthorId() !=null)
        	newEntity.setAuthorId(tQuestionRO.getAuthorId());
        	if(tQuestionRO.getDomainId() !=null)
        	newEntity.setDomainId(tQuestionRO.getDomainId());

  	 try {
        	 newEntity = mQuestionRepository.save(newEntity);   
		} catch (Exception e) {
			throw new RestException("Could not save result due to unique key exception");
		}
                 
        return  mModelMapper.map(newEntity, QuestionRO.class);
    } 
     
	/**
	 * delete by Id
	 */
     public void deleteQuestionById(Long id) throws RestException {
       Optional<QuestionEntity> tQuestion = mQuestionRepository.findById(id);
         
        if(tQuestion.isPresent()) { 
        
            mQuestionRepository.deleteById(id);
        } else {
            throw new RestException("No question record exist for given id");
        }
    } 
    
	/**
	 * upload file to DB
	 */
	
	public void importCSV(MultipartFile file) throws RestException {
		try {
			List<QuestionEntity> tQuestions = CSVHelper.csvToPOJOs(file.getInputStream(), QuestionEntity.class);
			tQuestions=tQuestions.stream().map(entity->{
				QuestionEntity tQuestionEntity=mQuestionRepository.findByKeyid(entity.getKeyid());
				if(tQuestionEntity!=null) {
					entity.setId(tQuestionEntity.getId());
				}
				return entity;
			}).collect(Collectors.toList());
			mQuestionRepository.saveAll(tQuestions);
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
			List<QuestionEntity> tQuestions = CSVHelper.csvToPOJOs(is, QuestionEntity.class);
			tQuestions=tQuestions.stream().map(entity->{
				QuestionEntity tQuestionEntity=mQuestionRepository.findByKeyid(entity.getKeyid());
				if(tQuestionEntity!=null) {
					entity.setId(tQuestionEntity.getId());
				}
				return entity;
			}).collect(Collectors.toList());
			mQuestionRepository.saveAll(tQuestions);
		} catch (Exception e) {
			throw new RestException(RestException.ERROR_STATUS_BAD_REQUEST,
					"fail to store csv data: " + e.getMessage());
		}
	}

	/**
	 * get count
	 */
	public long count() {
		return mQuestionRepository.count();
	}

	/**
	 *
	 * fetch list of Question
	 */
	
	public List<QuestionRO> findAll(int pageNumber, int rowPerPage) {
		Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("id").ascending());
		List<QuestionRO> tQuestionROs = mQuestionRepository.findAll(sortedByIdAsc).stream()
				.map(entity -> mModelMapper.map(entity, QuestionRO.class)).collect(Collectors.toList());
		return tQuestionROs;
	}
	
	
	/**
	 *
	 * fetch list of Question topics
	 */
	
	public List<TopicRO> findQuestionTopics(Long id) {
		List<TopicRO> tTopicROs= new ArrayList<>();   
		Optional<QuestionEntity> tQuestion = mQuestionRepository.findById(id);
		if(tQuestion.isPresent()) {
			tQuestion.ifPresent(en->{
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
	 * assign a topic to question
	 */
	
	public void addQuestionTopic(Long id, Long topicId) {
		Optional<TopicEntity> tTopic = mTopicRepository.findById(topicId);
		Optional<QuestionEntity> tQuestion = mQuestionRepository.findById(id);
		if(tTopic.isPresent() && tQuestion.isPresent()) {
			QuestionEntity tQuestionEntity=tQuestion.get();
			TopicEntity tTopicEntity= tTopic.get();
			tQuestionEntity.getTopics().add(tTopicEntity);
			tTopicEntity.getQuestions().add(tQuestionEntity);
			mQuestionRepository.save(tQuestionEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a topic to question
	 */
	
	public void unassignQuestionTopic(Long id, Long topicId) {
		Optional<TopicEntity> tTopic = mTopicRepository.findById(topicId);
		Optional<QuestionEntity> tQuestion = mQuestionRepository.findById(id);
		if(tTopic.isPresent() && tQuestion.isPresent()) {
			QuestionEntity tQuestionEntity=tQuestion.get();
			tQuestionEntity.getTopics().remove(tTopic.get());
			mQuestionRepository.save(tQuestionEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Question topics
	 */
	
	public List<TopicRO> findUnassignQuestionTopics(Long id) {
		List<TopicRO> tTopicROs= new ArrayList<>();   
		 mQuestionRepository.findById(id).ifPresent(en->{
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
	 * fetch list of Question images
	 */
	
	public List<ImageRO> findQuestionImages(Long id) {
		List<ImageRO> tImageROs= new ArrayList<>();   
		Optional<QuestionEntity> tQuestion = mQuestionRepository.findById(id);
		if(tQuestion.isPresent()) {
			tQuestion.ifPresent(en->{
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
	 * assign a image to question
	 */
	
	public void addQuestionImage(Long id, Long imageId) {
		Optional<ImageEntity> tImage = mImageRepository.findById(imageId);
		Optional<QuestionEntity> tQuestion = mQuestionRepository.findById(id);
		if(tImage.isPresent() && tQuestion.isPresent()) {
			QuestionEntity tQuestionEntity=tQuestion.get();
			ImageEntity tImageEntity= tImage.get();
			tQuestionEntity.getImages().add(tImageEntity);
			tImageEntity.getQuestions().add(tQuestionEntity);
			mQuestionRepository.save(tQuestionEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a image to question
	 */
	
	public void unassignQuestionImage(Long id, Long imageId) {
		Optional<ImageEntity> tImage = mImageRepository.findById(imageId);
		Optional<QuestionEntity> tQuestion = mQuestionRepository.findById(id);
		if(tImage.isPresent() && tQuestion.isPresent()) {
			QuestionEntity tQuestionEntity=tQuestion.get();
			tQuestionEntity.getImages().remove(tImage.get());
			mQuestionRepository.save(tQuestionEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Question images
	 */
	
	public List<ImageRO> findUnassignQuestionImages(Long id) {
		List<ImageRO> tImageROs= new ArrayList<>();   
		 mQuestionRepository.findById(id).ifPresent(en->{
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
	 * fetch list of Question videos
	 */
	
	public List<VideoRO> findQuestionVideos(Long id) {
		List<VideoRO> tVideoROs= new ArrayList<>();   
		Optional<QuestionEntity> tQuestion = mQuestionRepository.findById(id);
		if(tQuestion.isPresent()) {
			tQuestion.ifPresent(en->{
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
	 * assign a video to question
	 */
	
	public void addQuestionVideo(Long id, Long videoId) {
		Optional<VideoEntity> tVideo = mVideoRepository.findById(videoId);
		Optional<QuestionEntity> tQuestion = mQuestionRepository.findById(id);
		if(tVideo.isPresent() && tQuestion.isPresent()) {
			QuestionEntity tQuestionEntity=tQuestion.get();
			VideoEntity tVideoEntity= tVideo.get();
			tQuestionEntity.getVideos().add(tVideoEntity);
			tVideoEntity.getQuestions().add(tQuestionEntity);
			mQuestionRepository.save(tQuestionEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a video to question
	 */
	
	public void unassignQuestionVideo(Long id, Long videoId) {
		Optional<VideoEntity> tVideo = mVideoRepository.findById(videoId);
		Optional<QuestionEntity> tQuestion = mQuestionRepository.findById(id);
		if(tVideo.isPresent() && tQuestion.isPresent()) {
			QuestionEntity tQuestionEntity=tQuestion.get();
			tQuestionEntity.getVideos().remove(tVideo.get());
			mQuestionRepository.save(tQuestionEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Question videos
	 */
	
	public List<VideoRO> findUnassignQuestionVideos(Long id) {
		List<VideoRO> tVideoROs= new ArrayList<>();   
		 mQuestionRepository.findById(id).ifPresent(en->{
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
	 * fetch list of Question pages
	 */
	
	public List<PageRO> findQuestionPages(Long id) {
		List<PageRO> tPageROs= new ArrayList<>();   
		Optional<QuestionEntity> tQuestion = mQuestionRepository.findById(id);
		if(tQuestion.isPresent()) {
			tQuestion.ifPresent(en->{
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
	 * assign a page to question
	 */
	
	public void addQuestionPage(Long id, Long pageId) {
		Optional<PageEntity> tPage = mPageRepository.findById(pageId);
		Optional<QuestionEntity> tQuestion = mQuestionRepository.findById(id);
		if(tPage.isPresent() && tQuestion.isPresent()) {
			QuestionEntity tQuestionEntity=tQuestion.get();
			PageEntity tPageEntity= tPage.get();
			tQuestionEntity.getPages().add(tPageEntity);
			tPageEntity.getQuestions().add(tQuestionEntity);
			mQuestionRepository.save(tQuestionEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a page to question
	 */
	
	public void unassignQuestionPage(Long id, Long pageId) {
		Optional<PageEntity> tPage = mPageRepository.findById(pageId);
		Optional<QuestionEntity> tQuestion = mQuestionRepository.findById(id);
		if(tPage.isPresent() && tQuestion.isPresent()) {
			QuestionEntity tQuestionEntity=tQuestion.get();
			tQuestionEntity.getPages().remove(tPage.get());
			mQuestionRepository.save(tQuestionEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Question pages
	 */
	
	public List<PageRO> findUnassignQuestionPages(Long id) {
		List<PageRO> tPageROs= new ArrayList<>();   
		 mQuestionRepository.findById(id).ifPresent(en->{
			 List<PageEntity> tPages = mPageRepository.findAll();
			 tPages.removeAll(en.getPages());
			 tPages.forEach(re->{					
					tPageROs.add(mModelMapper.map(re, PageRO.class));
				});
		 });
		
		return tPageROs;
	}
	
	
	
	
	
	public QuestionRO getQuestionByKeyId(String key) {
		Optional<?> tQuestion= Optional.ofNullable(mQuestionRepository.findByKeyid(key));
		 if(tQuestion.isPresent()) {
	            return mModelMapper.map(tQuestion.get(), QuestionRO.class);
	        }
	      return null;  
	}
	
	
	
	
}
