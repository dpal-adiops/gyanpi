package  com.adiops.apigateway.page.service;

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
 
import com.adiops.apigateway.page.entity.PageEntity;
import com.adiops.apigateway.page.repository.PageRepository;
import com.adiops.apigateway.page.resourceobject.PageRO;

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
 * This is the implementation class for Page responsible for all the CRUD operations and other business related operations.
 * @author Deepak Pal
 *
 */
@Service
public class PageService{

	@Autowired
	PageRepository mPageRepository;

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
	 * fetch list of Page
	 */
	
	public List<PageRO> getPageROs() {
		List<PageRO> tPageROs = mPageRepository.findAll(Sort.by(Sort.Direction.ASC, "keyid")).stream()
				.map(entity -> mModelMapper.map(entity, PageRO.class)).collect(Collectors.toList());
		return tPageROs;
	}

	/**
	 *
	 * get Page by id
	 */
	public PageRO getPageById(Long id) throws RestException {
        Optional<PageEntity> tPage = mPageRepository.findById(id);
         
        if(tPage.isPresent()) {
            return mModelMapper.map(tPage.get(), PageRO.class);
        } else {
            throw new RestException("No page record exist for given id");
        }
    }
	
	 
	/**
	 * create or update 
	 */
	public PageRO createOrUpdatePage(PageRO tPageRO) throws RestException 
    {
        PageEntity newEntity ;
         
        if(tPageRO.getId()!=null)
        {
        	newEntity=	 mPageRepository.findById(tPageRO.getId()).orElse(new PageEntity());
        }
        else
        {
        	newEntity=new PageEntity();
        }
       
              
      
        	if(tPageRO.getKeyid() !=null)
        	newEntity.setKeyid(tPageRO.getKeyid());
        	if(tPageRO.getName() !=null)
        	newEntity.setName(tPageRO.getName());
        	if(tPageRO.getDescription() !=null)
        	newEntity.setDescription(tPageRO.getDescription());
        	if(tPageRO.getAuthorId() !=null)
        	newEntity.setAuthorId(tPageRO.getAuthorId());
        	if(tPageRO.getDomainId() !=null)
        	newEntity.setDomainId(tPageRO.getDomainId());

  	 try {
        	 newEntity = mPageRepository.save(newEntity);   
		} catch (Exception e) {
			throw new RestException("Could not save result due to unique key exception");
		}
                 
        return  mModelMapper.map(newEntity, PageRO.class);
    } 
     
	/**
	 * delete by Id
	 */
     public void deletePageById(Long id) throws RestException {
       Optional<PageEntity> tPage = mPageRepository.findById(id);
         
        if(tPage.isPresent()) { 
        
            mPageRepository.deleteById(id);
        } else {
            throw new RestException("No page record exist for given id");
        }
    } 
    
	/**
	 * upload file to DB
	 */
	
	public void importCSV(MultipartFile file) throws RestException {
		try {
			List<PageEntity> tPages = CSVHelper.csvToPOJOs(file.getInputStream(), PageEntity.class);
			mPageRepository.deleteAll();
			mPageRepository.saveAll(tPages);
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
			List<PageEntity> tPages = CSVHelper.csvToPOJOs(is, PageEntity.class);
			mPageRepository.saveAll(tPages);
		} catch (Exception e) {
			throw new RestException(RestException.ERROR_STATUS_BAD_REQUEST,
					"fail to store csv data: " + e.getMessage());
		}
	}

	/**
	 * get count
	 */
	public long count() {
		return mPageRepository.count();
	}

	/**
	 *
	 * fetch list of Page
	 */
	
	public List<PageRO> findAll(int pageNumber, int rowPerPage) {
		Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("id").ascending());
		List<PageRO> tPageROs = mPageRepository.findAll(sortedByIdAsc).stream()
				.map(entity -> mModelMapper.map(entity, PageRO.class)).collect(Collectors.toList());
		return tPageROs;
	}
	
	
	/**
	 *
	 * fetch list of Page courses
	 */
	
	public List<CourseRO> findPageCourses(Long id) {
		List<CourseRO> tCourseROs= new ArrayList<>();   
		Optional<PageEntity> tPage = mPageRepository.findById(id);
		if(tPage.isPresent()) {
			tPage.ifPresent(en->{
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
	 * assign a course to page
	 */
	
	public void addPageCourse(Long id, Long courseId) {
		Optional<CourseEntity> tCourse = mCourseRepository.findById(courseId);
		Optional<PageEntity> tPage = mPageRepository.findById(id);
		if(tCourse.isPresent() && tPage.isPresent()) {
			PageEntity tPageEntity=tPage.get();
			CourseEntity tCourseEntity= tCourse.get();
			tPageEntity.getCourses().add(tCourseEntity);
			tCourseEntity.getPages().add(tPageEntity);
			mPageRepository.save(tPageEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a course to page
	 */
	
	public void unassignPageCourse(Long id, Long courseId) {
		Optional<CourseEntity> tCourse = mCourseRepository.findById(courseId);
		Optional<PageEntity> tPage = mPageRepository.findById(id);
		if(tCourse.isPresent() && tPage.isPresent()) {
			PageEntity tPageEntity=tPage.get();
			tPageEntity.getCourses().remove(tCourse.get());
			mPageRepository.save(tPageEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Page courses
	 */
	
	public List<CourseRO> findUnassignPageCourses(Long id) {
		List<CourseRO> tCourseROs= new ArrayList<>();   
		 mPageRepository.findById(id).ifPresent(en->{
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
	 * fetch list of Page modules
	 */
	
	public List<ModuleRO> findPageModules(Long id) {
		List<ModuleRO> tModuleROs= new ArrayList<>();   
		Optional<PageEntity> tPage = mPageRepository.findById(id);
		if(tPage.isPresent()) {
			tPage.ifPresent(en->{
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
	 * assign a module to page
	 */
	
	public void addPageModule(Long id, Long moduleId) {
		Optional<ModuleEntity> tModule = mModuleRepository.findById(moduleId);
		Optional<PageEntity> tPage = mPageRepository.findById(id);
		if(tModule.isPresent() && tPage.isPresent()) {
			PageEntity tPageEntity=tPage.get();
			ModuleEntity tModuleEntity= tModule.get();
			tPageEntity.getModules().add(tModuleEntity);
			tModuleEntity.getPages().add(tPageEntity);
			mPageRepository.save(tPageEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a module to page
	 */
	
	public void unassignPageModule(Long id, Long moduleId) {
		Optional<ModuleEntity> tModule = mModuleRepository.findById(moduleId);
		Optional<PageEntity> tPage = mPageRepository.findById(id);
		if(tModule.isPresent() && tPage.isPresent()) {
			PageEntity tPageEntity=tPage.get();
			tPageEntity.getModules().remove(tModule.get());
			mPageRepository.save(tPageEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Page modules
	 */
	
	public List<ModuleRO> findUnassignPageModules(Long id) {
		List<ModuleRO> tModuleROs= new ArrayList<>();   
		 mPageRepository.findById(id).ifPresent(en->{
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
	 * fetch list of Page topics
	 */
	
	public List<TopicRO> findPageTopics(Long id) {
		List<TopicRO> tTopicROs= new ArrayList<>();   
		Optional<PageEntity> tPage = mPageRepository.findById(id);
		if(tPage.isPresent()) {
			tPage.ifPresent(en->{
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
	 * assign a topic to page
	 */
	
	public void addPageTopic(Long id, Long topicId) {
		Optional<TopicEntity> tTopic = mTopicRepository.findById(topicId);
		Optional<PageEntity> tPage = mPageRepository.findById(id);
		if(tTopic.isPresent() && tPage.isPresent()) {
			PageEntity tPageEntity=tPage.get();
			TopicEntity tTopicEntity= tTopic.get();
			tPageEntity.getTopics().add(tTopicEntity);
			tTopicEntity.getPages().add(tPageEntity);
			mPageRepository.save(tPageEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a topic to page
	 */
	
	public void unassignPageTopic(Long id, Long topicId) {
		Optional<TopicEntity> tTopic = mTopicRepository.findById(topicId);
		Optional<PageEntity> tPage = mPageRepository.findById(id);
		if(tTopic.isPresent() && tPage.isPresent()) {
			PageEntity tPageEntity=tPage.get();
			tPageEntity.getTopics().remove(tTopic.get());
			mPageRepository.save(tPageEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Page topics
	 */
	
	public List<TopicRO> findUnassignPageTopics(Long id) {
		List<TopicRO> tTopicROs= new ArrayList<>();   
		 mPageRepository.findById(id).ifPresent(en->{
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
	 * fetch list of Page questions
	 */
	
	public List<QuestionRO> findPageQuestions(Long id) {
		List<QuestionRO> tQuestionROs= new ArrayList<>();   
		Optional<PageEntity> tPage = mPageRepository.findById(id);
		if(tPage.isPresent()) {
			tPage.ifPresent(en->{
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
	 * assign a question to page
	 */
	
	public void addPageQuestion(Long id, Long questionId) {
		Optional<QuestionEntity> tQuestion = mQuestionRepository.findById(questionId);
		Optional<PageEntity> tPage = mPageRepository.findById(id);
		if(tQuestion.isPresent() && tPage.isPresent()) {
			PageEntity tPageEntity=tPage.get();
			QuestionEntity tQuestionEntity= tQuestion.get();
			tPageEntity.getQuestions().add(tQuestionEntity);
			tQuestionEntity.getPages().add(tPageEntity);
			mPageRepository.save(tPageEntity);
		}
			
	}
	
	/**
	 *
	 * unassign a question to page
	 */
	
	public void unassignPageQuestion(Long id, Long questionId) {
		Optional<QuestionEntity> tQuestion = mQuestionRepository.findById(questionId);
		Optional<PageEntity> tPage = mPageRepository.findById(id);
		if(tQuestion.isPresent() && tPage.isPresent()) {
			PageEntity tPageEntity=tPage.get();
			tPageEntity.getQuestions().remove(tQuestion.get());
			mPageRepository.save(tPageEntity);
		}
			
	}
	
	/**
	 *
	 * fetch list of Page questions
	 */
	
	public List<QuestionRO> findUnassignPageQuestions(Long id) {
		List<QuestionRO> tQuestionROs= new ArrayList<>();   
		 mPageRepository.findById(id).ifPresent(en->{
			 List<QuestionEntity> tQuestions = mQuestionRepository.findAll();
			 tQuestions.removeAll(en.getQuestions());
			 tQuestions.forEach(re->{					
					tQuestionROs.add(mModelMapper.map(re, QuestionRO.class));
				});
		 });
		
		return tQuestionROs;
	}
	
	
}
