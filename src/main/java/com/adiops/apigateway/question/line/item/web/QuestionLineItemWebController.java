package com.adiops.apigateway.question.line.item.web;

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
import com.adiops.apigateway.question.line.item.resourceobject.QuestionLineItemRO;
import com.adiops.apigateway.question.line.item.service.QuestionLineItemService;

/**
 * The web controller class for QuestionLineItem 
 * @author Deepak Pal
 *
 */
@Controller
public class QuestionLineItemWebController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final int ROW_PER_PAGE = 30;
	
	@Autowired
	QuestionLineItemService mQuestionLineItemService;

	@GetMapping(value = "/admin/web/question_line_items")
	public String getQuestionLineItems(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
		List<QuestionLineItemRO> question_line_items = mQuestionLineItemService.findAll(pageNumber, ROW_PER_PAGE);
		 
	    long count = mQuestionLineItemService.count();
	    boolean hasPrev = pageNumber > 1;
	    boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
	    model.addAttribute("question_line_items", question_line_items);
	    model.addAttribute("hasPrev", hasPrev);
	    model.addAttribute("prev", pageNumber - 1);
	    model.addAttribute("hasNext", hasNext);
	    model.addAttribute("next", pageNumber + 1);
	    model.addAttribute("selectedMenu", "question_line_item");
	    return "admin/question_line_item/question_line_item-list";
	}

	@GetMapping(value = { "/admin/web/question_line_items/add" })
	public String showAddQuestionLineItem(Model model) {
		QuestionLineItemRO tQuestionLineItemRO = new QuestionLineItemRO();
	    model.addAttribute("selectedMenu", "question_line_item");
	    model.addAttribute("question_line_itemRO", tQuestionLineItemRO);
	    return "admin/question_line_item/question_line_item-add";
	}

	@PostMapping(value = "/admin/web/question_line_items/add")
	public String addQuestionLineItem(@ModelAttribute QuestionLineItemRO tQuestionLineItemRO,Model model)  {
		try {
		 tQuestionLineItemRO=mQuestionLineItemService.createOrUpdateQuestionLineItem(tQuestionLineItemRO);
		} catch (RestException e) {
			model.addAttribute("error", e.getMessage());
		}
		
		
		model.addAttribute("question_line_itemRO", tQuestionLineItemRO);
		model.addAttribute("selectedMenu", "question_line_item");
		return "admin/question_line_item/question_line_item-edit";
	}

	@GetMapping(value = { "/admin/web/question_line_items/{question_line_itemId}" })
	public String showEditQuestionLineItem(Model model, @PathVariable Long question_line_itemId) throws RestException {
		QuestionLineItemRO tQuestionLineItemRO = mQuestionLineItemService.getQuestionLineItemById(question_line_itemId);
	    model.addAttribute("selectedMenu", "question_line_item");
	    model.addAttribute("question_line_itemRO", tQuestionLineItemRO);
	    return "admin/question_line_item/question_line_item-edit";
	}

	

	@GetMapping(value = { "/admin/web/question_line_items/{question_line_itemId}/delete" })
	public String deleteQuestionLineItemById(Model model, @PathVariable Long question_line_itemId) throws RestException {
		mQuestionLineItemService.deleteQuestionLineItemById(question_line_itemId);
		return "redirect:/admin/web/question_line_items";
	}
	

}
