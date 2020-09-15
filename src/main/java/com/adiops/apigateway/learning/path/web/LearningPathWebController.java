package com.adiops.apigateway.learning.path.web;

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
import com.adiops.apigateway.learning.path.resourceobject.LearningPathRO;
import com.adiops.apigateway.learning.path.service.LearningPathService;

/**
 * The web controller class for LearningPath 
 * @author Deepak Pal
 *
 */
@Controller
public class LearningPathWebController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final int ROW_PER_PAGE = 30;
	
	@Autowired
	LearningPathService mLearningPathService;

	@GetMapping(value = "/admin/web/learning_paths")
	public String getLearningPaths(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
		List<LearningPathRO> learning_paths = mLearningPathService.findAll(pageNumber, ROW_PER_PAGE);
		 
	    long count = mLearningPathService.count();
	    boolean hasPrev = pageNumber > 1;
	    boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
	    model.addAttribute("learning_paths", learning_paths);
	    model.addAttribute("hasPrev", hasPrev);
	    model.addAttribute("prev", pageNumber - 1);
	    model.addAttribute("hasNext", hasNext);
	    model.addAttribute("next", pageNumber + 1);
	    model.addAttribute("selectedMenu", "learning_path");
	    return "admin/learning_path/learning_path-list";
	}

	@GetMapping(value = { "/admin/web/learning_paths/add" })
	public String showAddLearningPath(Model model) {
		LearningPathRO tLearningPathRO = new LearningPathRO();
	    model.addAttribute("selectedMenu", "learning_path");
	    model.addAttribute("learning_pathRO", tLearningPathRO);
	    return "admin/learning_path/learning_path-add";
	}

	@PostMapping(value = "/admin/web/learning_paths/add")
	public String addLearningPath(@ModelAttribute LearningPathRO tLearningPathRO,Model model)  {
		try {
		 tLearningPathRO=mLearningPathService.createOrUpdateLearningPath(tLearningPathRO);
		} catch (RestException e) {
			model.addAttribute("error", e.getMessage());
		}
		
		
		model.addAttribute("learning_pathRO", tLearningPathRO);
		model.addAttribute("selectedMenu", "learning_path");
		return "admin/learning_path/learning_path-edit";
	}

	@GetMapping(value = { "/admin/web/learning_paths/{learning_pathId}" })
	public String showEditLearningPath(Model model, @PathVariable Long learning_pathId) throws RestException {
		LearningPathRO tLearningPathRO = mLearningPathService.getLearningPathById(learning_pathId);
	    model.addAttribute("selectedMenu", "learning_path");
	    model.addAttribute("learning_pathRO", tLearningPathRO);
	    return "admin/learning_path/learning_path-edit";
	}

	

	@GetMapping(value = { "/admin/web/learning_paths/{learning_pathId}/delete" })
	public String deleteLearningPathById(Model model, @PathVariable Long learning_pathId) throws RestException {
		mLearningPathService.deleteLearningPathById(learning_pathId);
		return "redirect:/admin/web/learning_paths";
	}
	

}
