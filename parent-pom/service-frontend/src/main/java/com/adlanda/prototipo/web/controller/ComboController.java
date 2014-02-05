package com.adlanda.prototipo.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.adlanda.prototipo.web.form.FormObject;

@Controller
@RequestMapping(value = "/combo")
public class ComboController {

	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(method = RequestMethod.GET)
	public String getComboForm(Model uiModel) {
		FormObject formObject = new FormObject();
		uiModel.addAttribute("formObject", formObject);
		formObject.setCountry("2");
		return "combo/comboForm";
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView showValue(FormObject formObject) {
		return new ModelAndView("combo/showValue", "comboValue",
				formObject.getCountry());
	}
}