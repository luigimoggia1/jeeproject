package com.adlanda.prototipo.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.adlanda.prototipo.web.form.AjaxFormObject;

@Controller
@RequestMapping(value = "/combosAjax")
public class CombosAjaxController {

	final Logger logger = LoggerFactory.getLogger(this.getClass());
	String[] marcas = { "seat", "ford" };
	String[] modelosSeat = { "malaga", "ibiza" };
	String[] modelosFord = { "fiesta", "focus" };

	@RequestMapping(method = RequestMethod.GET)
	public String getUploadForm(Model uiModel) {
		AjaxFormObject ajaxFormObject = new AjaxFormObject();
		uiModel.addAttribute("ajaxFormObject", ajaxFormObject);
		uiModel.addAttribute("marcas", marcas);
		uiModel.addAttribute("modelos", modelosSeat);
		return "combosAjax/combosForm";
	}

	@RequestMapping(value = "/modelos", method = RequestMethod.GET)
	public @ResponseBody
	String[] citiesForState(
			@RequestParam(value = "marcaValue", required = true) String marcaValue) {
		if (marcaValue.equals("seat")) {
			return modelosSeat;
		} else {
			return modelosFord;
		}
	}

}