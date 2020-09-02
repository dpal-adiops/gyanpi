package com.adiops.apigateway.page.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.adiops.apigateway.common.response.RestException;
import com.adiops.apigateway.page.resourceobject.PageRO;
import com.adiops.apigateway.page.service.PageService;

/**
 * The web controller class for Page 
 * @author Deepak Pal
 *
 */
@Controller
public class PageWebController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final int ROW_PER_PAGE = 30;
	
	@Autowired
	PageService mPageService;

	@GetMapping(value = "/admin/web/pages")
	public String getPages(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
		List<PageRO> pages = mPageService.findAll(pageNumber, ROW_PER_PAGE);
		 
	    long count = mPageService.count();
	    boolean hasPrev = pageNumber > 1;
	    boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
	    model.addAttribute("pages", pages);
	    model.addAttribute("hasPrev", hasPrev);
	    model.addAttribute("prev", pageNumber - 1);
	    model.addAttribute("hasNext", hasNext);
	    model.addAttribute("next", pageNumber + 1);
	    model.addAttribute("selectedMenu", "page");
	    return "admin/page/page-list";
	}

	@GetMapping(value = { "/admin/web/pages/add" })
	public String showAddPage(Model model) {
		PageRO tPageRO = new PageRO();
	    model.addAttribute("selectedMenu", "page");
	    model.addAttribute("pageRO", tPageRO);
	    return "admin/page/page-add";
	}

	@PostMapping(value = "/admin/web/pages/add")
	public String addPage(@ModelAttribute PageRO tPageRO,Model model)  {
		try {
		 tPageRO=mPageService.createOrUpdatePage(tPageRO);
		} catch (RestException e) {
			model.addAttribute("error", e.getMessage());
		}
		
		
		model.addAttribute("pageRO", tPageRO);
		model.addAttribute("selectedMenu", "page");
		return "admin/page/page-edit";
	}

	@GetMapping(value = { "/admin/web/pages/{pageId}" })
	public String showEditPage(Model model, @PathVariable Long pageId) throws RestException {
		PageRO tPageRO = mPageService.getPageById(pageId);
	    model.addAttribute("selectedMenu", "page");
	    model.addAttribute("pageRO", tPageRO);
	    return "admin/page/page-edit";
	}

	

	@GetMapping(value = { "/admin/web/pages/{pageId}/delete" })
	public String deletePageById(Model model, @PathVariable Long pageId) throws RestException {
		mPageService.deletePageById(pageId);
		return "redirect:/admin/web/pages";
	}
	
	
	@GetMapping(value = { "/admin/web/pages/{pageId}/courses" })
	public String getPageCourses(Model model, @PathVariable Long pageId) throws RestException {
		model.addAttribute("pageRO", mPageService.getPageById(pageId));
		model.addAttribute("courses", mPageService.findPageCourses(pageId));
		model.addAttribute("selectedMenu", "page");
		return "admin/page/course/course-list";
	}
	
	@GetMapping(value = { "/admin/web/pages/{pageId}/courses/assign" })
	public String assignCourses(Model model, @PathVariable Long pageId) throws RestException {
		model.addAttribute("pageRO", mPageService.getPageById(pageId));		
		model.addAttribute("courses", mPageService.findUnassignPageCourses(pageId));
		model.addAttribute("selectedMenu", "page");
		return "admin/page/course/course-assign";
	}
	
	@GetMapping(value = { "/admin/web/pages/{pageId}/courses/{courseId}/assign" })
	public String getCourses(Model model, @PathVariable Long pageId, @PathVariable Long courseId) throws RestException {
		mPageService.addPageCourse(pageId, courseId);
		model.addAttribute("pageRO", mPageService.getPageById(pageId));
		model.addAttribute("courses", mPageService.findPageCourses(pageId));
		model.addAttribute("selectedMenu", "page");
		return "admin/page/course/course-list";
	}
	
	@GetMapping(value = { "/admin/web/pages/{pageId}/courses/{courseId}/unassign" })
	public String unassignCourses(Model model, @PathVariable Long pageId, @PathVariable Long courseId) throws RestException {
		mPageService.unassignPageCourse(pageId, courseId);
		model.addAttribute("pageRO", mPageService.getPageById(pageId));
		model.addAttribute("courses", mPageService.findPageCourses(pageId));
		model.addAttribute("selectedMenu", "page");
		return "admin/page/course/course-list";
	}
	
	
	@GetMapping(value = { "/admin/web/pages/{pageId}/modules" })
	public String getPageModules(Model model, @PathVariable Long pageId) throws RestException {
		model.addAttribute("pageRO", mPageService.getPageById(pageId));
		model.addAttribute("modules", mPageService.findPageModules(pageId));
		model.addAttribute("selectedMenu", "page");
		return "admin/page/module/module-list";
	}
	
	@GetMapping(value = { "/admin/web/pages/{pageId}/modules/assign" })
	public String assignModules(Model model, @PathVariable Long pageId) throws RestException {
		model.addAttribute("pageRO", mPageService.getPageById(pageId));		
		model.addAttribute("modules", mPageService.findUnassignPageModules(pageId));
		model.addAttribute("selectedMenu", "page");
		return "admin/page/module/module-assign";
	}
	
	@GetMapping(value = { "/admin/web/pages/{pageId}/modules/{moduleId}/assign" })
	public String getModules(Model model, @PathVariable Long pageId, @PathVariable Long moduleId) throws RestException {
		mPageService.addPageModule(pageId, moduleId);
		model.addAttribute("pageRO", mPageService.getPageById(pageId));
		model.addAttribute("modules", mPageService.findPageModules(pageId));
		model.addAttribute("selectedMenu", "page");
		return "admin/page/module/module-list";
	}
	
	@GetMapping(value = { "/admin/web/pages/{pageId}/modules/{moduleId}/unassign" })
	public String unassignModules(Model model, @PathVariable Long pageId, @PathVariable Long moduleId) throws RestException {
		mPageService.unassignPageModule(pageId, moduleId);
		model.addAttribute("pageRO", mPageService.getPageById(pageId));
		model.addAttribute("modules", mPageService.findPageModules(pageId));
		model.addAttribute("selectedMenu", "page");
		return "admin/page/module/module-list";
	}
	
	
	@GetMapping(value = { "/admin/web/pages/{pageId}/topics" })
	public String getPageTopics(Model model, @PathVariable Long pageId) throws RestException {
		model.addAttribute("pageRO", mPageService.getPageById(pageId));
		model.addAttribute("topics", mPageService.findPageTopics(pageId));
		model.addAttribute("selectedMenu", "page");
		return "admin/page/topic/topic-list";
	}
	
	@GetMapping(value = { "/admin/web/pages/{pageId}/topics/assign" })
	public String assignTopics(Model model, @PathVariable Long pageId) throws RestException {
		model.addAttribute("pageRO", mPageService.getPageById(pageId));		
		model.addAttribute("topics", mPageService.findUnassignPageTopics(pageId));
		model.addAttribute("selectedMenu", "page");
		return "admin/page/topic/topic-assign";
	}
	
	@GetMapping(value = { "/admin/web/pages/{pageId}/topics/{topicId}/assign" })
	public String getTopics(Model model, @PathVariable Long pageId, @PathVariable Long topicId) throws RestException {
		mPageService.addPageTopic(pageId, topicId);
		model.addAttribute("pageRO", mPageService.getPageById(pageId));
		model.addAttribute("topics", mPageService.findPageTopics(pageId));
		model.addAttribute("selectedMenu", "page");
		return "admin/page/topic/topic-list";
	}
	
	@GetMapping(value = { "/admin/web/pages/{pageId}/topics/{topicId}/unassign" })
	public String unassignTopics(Model model, @PathVariable Long pageId, @PathVariable Long topicId) throws RestException {
		mPageService.unassignPageTopic(pageId, topicId);
		model.addAttribute("pageRO", mPageService.getPageById(pageId));
		model.addAttribute("topics", mPageService.findPageTopics(pageId));
		model.addAttribute("selectedMenu", "page");
		return "admin/page/topic/topic-list";
	}
	
	
	@GetMapping(value = { "/admin/web/pages/{pageId}/questions" })
	public String getPageQuestions(Model model, @PathVariable Long pageId) throws RestException {
		model.addAttribute("pageRO", mPageService.getPageById(pageId));
		model.addAttribute("questions", mPageService.findPageQuestions(pageId));
		model.addAttribute("selectedMenu", "page");
		return "admin/page/question/question-list";
	}
	
	@GetMapping(value = { "/admin/web/pages/{pageId}/questions/assign" })
	public String assignQuestions(Model model, @PathVariable Long pageId) throws RestException {
		model.addAttribute("pageRO", mPageService.getPageById(pageId));		
		model.addAttribute("questions", mPageService.findUnassignPageQuestions(pageId));
		model.addAttribute("selectedMenu", "page");
		return "admin/page/question/question-assign";
	}
	
	@GetMapping(value = { "/admin/web/pages/{pageId}/questions/{questionId}/assign" })
	public String getQuestions(Model model, @PathVariable Long pageId, @PathVariable Long questionId) throws RestException {
		mPageService.addPageQuestion(pageId, questionId);
		model.addAttribute("pageRO", mPageService.getPageById(pageId));
		model.addAttribute("questions", mPageService.findPageQuestions(pageId));
		model.addAttribute("selectedMenu", "page");
		return "admin/page/question/question-list";
	}
	
	@GetMapping(value = { "/admin/web/pages/{pageId}/questions/{questionId}/unassign" })
	public String unassignQuestions(Model model, @PathVariable Long pageId, @PathVariable Long questionId) throws RestException {
		mPageService.unassignPageQuestion(pageId, questionId);
		model.addAttribute("pageRO", mPageService.getPageById(pageId));
		model.addAttribute("questions", mPageService.findPageQuestions(pageId));
		model.addAttribute("selectedMenu", "page");
		return "admin/page/question/question-list";
	}
	

}
