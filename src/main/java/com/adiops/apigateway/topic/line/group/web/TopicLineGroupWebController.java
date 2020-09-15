package com.adiops.apigateway.topic.line.group.web;

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
import com.adiops.apigateway.topic.line.group.resourceobject.TopicLineGroupRO;
import com.adiops.apigateway.topic.line.group.service.TopicLineGroupService;

/**
 * The web controller class for TopicLineGroup 
 * @author Deepak Pal
 *
 */
@Controller
public class TopicLineGroupWebController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final int ROW_PER_PAGE = 30;
	
	@Autowired
	TopicLineGroupService mTopicLineGroupService;

	@GetMapping(value = "/admin/web/topic_line_groups")
	public String getTopicLineGroups(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
		List<TopicLineGroupRO> topic_line_groups = mTopicLineGroupService.findAll(pageNumber, ROW_PER_PAGE);
		 
	    long count = mTopicLineGroupService.count();
	    boolean hasPrev = pageNumber > 1;
	    boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
	    model.addAttribute("topic_line_groups", topic_line_groups);
	    model.addAttribute("hasPrev", hasPrev);
	    model.addAttribute("prev", pageNumber - 1);
	    model.addAttribute("hasNext", hasNext);
	    model.addAttribute("next", pageNumber + 1);
	    model.addAttribute("selectedMenu", "topic_line_group");
	    return "admin/topic_line_group/topic_line_group-list";
	}

	@GetMapping(value = { "/admin/web/topic_line_groups/add" })
	public String showAddTopicLineGroup(Model model) {
		TopicLineGroupRO tTopicLineGroupRO = new TopicLineGroupRO();
	    model.addAttribute("selectedMenu", "topic_line_group");
	    model.addAttribute("topic_line_groupRO", tTopicLineGroupRO);
	    return "admin/topic_line_group/topic_line_group-add";
	}

	@PostMapping(value = "/admin/web/topic_line_groups/add")
	public String addTopicLineGroup(@ModelAttribute TopicLineGroupRO tTopicLineGroupRO,Model model)  {
		try {
		 tTopicLineGroupRO=mTopicLineGroupService.createOrUpdateTopicLineGroup(tTopicLineGroupRO);
		} catch (RestException e) {
			model.addAttribute("error", e.getMessage());
		}
		
		
		model.addAttribute("topic_line_groupRO", tTopicLineGroupRO);
		model.addAttribute("selectedMenu", "topic_line_group");
		return "admin/topic_line_group/topic_line_group-edit";
	}

	@GetMapping(value = { "/admin/web/topic_line_groups/{topic_line_groupId}" })
	public String showEditTopicLineGroup(Model model, @PathVariable Long topic_line_groupId) throws RestException {
		TopicLineGroupRO tTopicLineGroupRO = mTopicLineGroupService.getTopicLineGroupById(topic_line_groupId);
	    model.addAttribute("selectedMenu", "topic_line_group");
	    model.addAttribute("topic_line_groupRO", tTopicLineGroupRO);
	    return "admin/topic_line_group/topic_line_group-edit";
	}

	

	@GetMapping(value = { "/admin/web/topic_line_groups/{topic_line_groupId}/delete" })
	public String deleteTopicLineGroupById(Model model, @PathVariable Long topic_line_groupId) throws RestException {
		mTopicLineGroupService.deleteTopicLineGroupById(topic_line_groupId);
		return "redirect:/admin/web/topic_line_groups";
	}
	

}
