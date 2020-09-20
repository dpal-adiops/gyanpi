package com.adiops.apigateway.learning.track.path.web;

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
import com.adiops.apigateway.learning.track.path.resourceobject.LearningTrackPathRO;
import com.adiops.apigateway.learning.track.path.service.LearningTrackPathService;

/**
 * The web controller class for LearningTrackPath 
 * @author Deepak Pal
 *
 */
@Controller
public class LearningTrackPathWebController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final int ROW_PER_PAGE = 30;
	
	@Autowired
	LearningTrackPathService mLearningTrackPathService;

	@GetMapping(value = "/admin/web/learning_track_paths")
	public String getLearningTrackPaths(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
		List<LearningTrackPathRO> learning_track_paths = mLearningTrackPathService.findAll(pageNumber, ROW_PER_PAGE);
		 
	    long count = mLearningTrackPathService.count();
	    boolean hasPrev = pageNumber > 1;
	    boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
	    model.addAttribute("learning_track_paths", learning_track_paths);
	    model.addAttribute("hasPrev", hasPrev);
	    model.addAttribute("prev", pageNumber - 1);
	    model.addAttribute("hasNext", hasNext);
	    model.addAttribute("next", pageNumber + 1);
	    model.addAttribute("selectedMenu", "learning_track_path");
	    return "admin/learning_track_path/learning_track_path-list";
	}

	@GetMapping(value = { "/admin/web/learning_track_paths/add" })
	public String showAddLearningTrackPath(Model model) {
		LearningTrackPathRO tLearningTrackPathRO = new LearningTrackPathRO();
	    model.addAttribute("selectedMenu", "learning_track_path");
	    model.addAttribute("learning_track_pathRO", tLearningTrackPathRO);
	    return "admin/learning_track_path/learning_track_path-add";
	}

	@PostMapping(value = "/admin/web/learning_track_paths/add")
	public String addLearningTrackPath(@ModelAttribute LearningTrackPathRO tLearningTrackPathRO,Model model)  {
		try {
		 tLearningTrackPathRO=mLearningTrackPathService.createOrUpdateLearningTrackPath(tLearningTrackPathRO);
		} catch (RestException e) {
			model.addAttribute("error", e.getMessage());
		}
		
		
		model.addAttribute("learning_track_pathRO", tLearningTrackPathRO);
		model.addAttribute("selectedMenu", "learning_track_path");
		return "admin/learning_track_path/learning_track_path-edit";
	}

	@GetMapping(value = { "/admin/web/learning_track_paths/{learning_track_pathId}" })
	public String showEditLearningTrackPath(Model model, @PathVariable Long learning_track_pathId) throws RestException {
		LearningTrackPathRO tLearningTrackPathRO = mLearningTrackPathService.getLearningTrackPathById(learning_track_pathId);
	    model.addAttribute("selectedMenu", "learning_track_path");
	    model.addAttribute("learning_track_pathRO", tLearningTrackPathRO);
	    return "admin/learning_track_path/learning_track_path-edit";
	}

	

	@GetMapping(value = { "/admin/web/learning_track_paths/{learning_track_pathId}/delete" })
	public String deleteLearningTrackPathById(Model model, @PathVariable Long learning_track_pathId) throws RestException {
		mLearningTrackPathService.deleteLearningTrackPathById(learning_track_pathId);
		return "redirect:/admin/web/learning_track_paths";
	}
	

}
