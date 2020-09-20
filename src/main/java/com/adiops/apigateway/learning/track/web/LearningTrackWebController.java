package com.adiops.apigateway.learning.track.web;

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
import com.adiops.apigateway.learning.track.resourceobject.LearningTrackRO;
import com.adiops.apigateway.learning.track.service.LearningTrackService;

/**
 * The web controller class for LearningTrack 
 * @author Deepak Pal
 *
 */
@Controller
public class LearningTrackWebController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final int ROW_PER_PAGE = 30;
	
	@Autowired
	LearningTrackService mLearningTrackService;

	@GetMapping(value = "/admin/web/learning_tracks")
	public String getLearningTracks(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
		List<LearningTrackRO> learning_tracks = mLearningTrackService.findAll(pageNumber, ROW_PER_PAGE);
		 
	    long count = mLearningTrackService.count();
	    boolean hasPrev = pageNumber > 1;
	    boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
	    model.addAttribute("learning_tracks", learning_tracks);
	    model.addAttribute("hasPrev", hasPrev);
	    model.addAttribute("prev", pageNumber - 1);
	    model.addAttribute("hasNext", hasNext);
	    model.addAttribute("next", pageNumber + 1);
	    model.addAttribute("selectedMenu", "learning_track");
	    return "admin/learning_track/learning_track-list";
	}

	@GetMapping(value = { "/admin/web/learning_tracks/add" })
	public String showAddLearningTrack(Model model) {
		LearningTrackRO tLearningTrackRO = new LearningTrackRO();
	    model.addAttribute("selectedMenu", "learning_track");
	    model.addAttribute("learning_trackRO", tLearningTrackRO);
	    return "admin/learning_track/learning_track-add";
	}

	@PostMapping(value = "/admin/web/learning_tracks/add")
	public String addLearningTrack(@ModelAttribute LearningTrackRO tLearningTrackRO,Model model)  {
		try {
		 tLearningTrackRO=mLearningTrackService.createOrUpdateLearningTrack(tLearningTrackRO);
		} catch (RestException e) {
			model.addAttribute("error", e.getMessage());
		}
		
		
		model.addAttribute("learning_trackRO", tLearningTrackRO);
		model.addAttribute("selectedMenu", "learning_track");
		return "admin/learning_track/learning_track-edit";
	}

	@GetMapping(value = { "/admin/web/learning_tracks/{learning_trackId}" })
	public String showEditLearningTrack(Model model, @PathVariable Long learning_trackId) throws RestException {
		LearningTrackRO tLearningTrackRO = mLearningTrackService.getLearningTrackById(learning_trackId);
	    model.addAttribute("selectedMenu", "learning_track");
	    model.addAttribute("learning_trackRO", tLearningTrackRO);
	    return "admin/learning_track/learning_track-edit";
	}

	

	@GetMapping(value = { "/admin/web/learning_tracks/{learning_trackId}/delete" })
	public String deleteLearningTrackById(Model model, @PathVariable Long learning_trackId) throws RestException {
		mLearningTrackService.deleteLearningTrackById(learning_trackId);
		return "redirect:/admin/web/learning_tracks";
	}
	

}
