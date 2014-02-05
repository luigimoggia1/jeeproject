//package com.adlanda.prototipo.web.controller;
//
//import java.util.List;
//import java.util.Locale;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.MessageSource;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import com.adlanda.prototipo.domain.Contacto;
//import com.adlanda.prototipo.service.ContactoService;
//import com.adlanda.prototipo.web.form.ContactoGrid;
//import com.adlanda.prototipo.web.form.Message;
//import com.adlanda.prototipo.web.util.UrlUtil;
//
//@RequestMapping("/contactos")
//@Controller
//public class ContactoController {
//	final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//	@Autowired
//	MessageSource messageSource;
//
//	@Autowired
//	private ContactoService contactoService;
//
//	@RequestMapping(method = RequestMethod.GET)
//	public String list(Model uiModel) {
//		logger.info("Listing contactos");
//
//		List<Contacto> contactos = contactoService.findAll();
//		uiModel.addAttribute("contactos", contactos);
//
//		logger.info("No. of contactos: " + contactos.size());
//
//		return "contactos/list";
//	}
//
//	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
//	public String show(@PathVariable("id") Long id, Model uiModel) {
//		Contacto contacto = contactoService.findById(id);
//		uiModel.addAttribute("contacto", contacto);
//		return "contactos/show";
//	}
//
//	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.POST)
//	public String update(@Valid Contacto contacto, BindingResult bindingResult,
//			Model uiModel, HttpServletRequest httpServletRequest,
//			RedirectAttributes redirectAttributes, Locale locale) {
//		logger.info("Updating contacto");
//		if (bindingResult.hasErrors()) {
//			uiModel.addAttribute(
//					"message",
//					new Message("error", messageSource.getMessage(
//							"contact_save_fail", new Object[] {}, locale)));
//			uiModel.addAttribute("contacto", contacto);
//			return "contactos/update";
//		}
//		uiModel.asMap().clear();
//		redirectAttributes.addFlashAttribute(
//				"message",
//				new Message("success", messageSource.getMessage(
//						"contact_save_success", new Object[] {}, locale)));
//
//		contactoService.save(contacto);
//		return "redirect:/contactos/"
//				+ UrlUtil.encodeUrlPathSegment(contacto.getId().toString(),
//						httpServletRequest);
//	}
//
//	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
//	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
//		uiModel.addAttribute("contacto", contactoService.findById(id));
//		return "contactos/update";
//	}
//
//	@PreAuthorize("hasRole('PRES 0')")
//	@RequestMapping(method = RequestMethod.POST)
//	public String create(@Valid Contacto contacto, BindingResult bindingResult,
//			Model uiModel, HttpServletRequest httpServletRequest,
//			RedirectAttributes redirectAttributes, Locale locale) {
//		logger.info("Creating contacto");
//		if (bindingResult.hasErrors()) {
//			uiModel.addAttribute(
//					"message",
//					new Message("error", messageSource.getMessage(
//							"contact_save_fail", new Object[] {}, locale)));
//			uiModel.addAttribute("contacto", contacto);
//			return "contactos/create";
//		}
//		uiModel.asMap().clear();
//		redirectAttributes.addFlashAttribute(
//				"message",
//				new Message("success", messageSource.getMessage(
//						"contact_save_success", new Object[] {}, locale)));
//
//		logger.info("Contacto id: " + contacto.getId());
//
//		contactoService.save(contacto);
//		return "redirect:/contactos/"
//				+ UrlUtil.encodeUrlPathSegment(contacto.getId().toString(),
//						httpServletRequest);
//	}
//
//	@PreAuthorize("isAuthenticated()")
//	@RequestMapping(params = "form", method = RequestMethod.GET)
//	public String createForm(Model uiModel) {
//		Contacto contacto = new Contacto();
//		uiModel.addAttribute("contacto", contacto);
//		return "contactos/create";
//	}
//
//	/**
//	 * Support data for front-end grid
//	 * 
//	 * @param name
//	 * @return
//	 */
//	@RequestMapping(value = "/listgrid", method = RequestMethod.GET, produces = "application/json")
//	@ResponseBody
//	public ContactoGrid listGrid(@RequestParam("name") String name,
//			@RequestParam(value = "page", required = false) Integer page,
//			@RequestParam(value = "rows", required = false) Integer rows,
//			@RequestParam(value = "sidx", required = false) String sortBy,
//			@RequestParam(value = "sord", required = false) String order) {
//		logger.info("Listing contacts for grid with page: {}, rows: {}", page, rows);
//		logger.info("Listing contacts for grid with sort: {}, order: {}", sortBy, order);
//
//		PageRequest pageRequest = null;
//		pageRequest = new PageRequest(page - 1, rows);
//
//		ContactoGrid contactoGrid = new ContactoGrid();
//		Page<Contacto> contactos;
//		if (name == null || name.equals("undefined")) {
//			contactos = contactoService.findByNamePageable("", pageRequest);
//		} else {
//			contactos = contactoService.findByNamePageable(name, pageRequest);
//		}
//
//		contactoGrid.setCurrentPage(contactos.getNumber() + 1);
//		contactoGrid.setTotalPages(contactos.getTotalPages());
//		contactoGrid.setTotalRecords(contactos.getTotalElements());
//		contactoGrid.setContactoData(contactos.getContent());
//
//		return contactoGrid;
//	}
//
//	@RequestMapping(value = "/photo/{id}", method = RequestMethod.GET)
//	@ResponseBody
//	public byte[] downloadPhoto(@PathVariable("id") Long id) {
//
//		Contacto contacto = contactoService.findById(id);
//
//		if (contacto.getImagen() != null) {
//			logger.info("Downloading photo for id: {} with size: {}",
//					contacto.getId(), contacto.getImagen().length);
//		}
//
//		return contacto.getImagen();
//	}
//
//}
