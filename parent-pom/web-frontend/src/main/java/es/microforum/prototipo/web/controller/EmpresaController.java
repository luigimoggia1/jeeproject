package es.microforum.prototipo.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.microforum.prototipo.web.form.EmpresaGrid;
import es.microforum.prototipo.web.form.Message;
import es.microforum.prototipo.web.util.UrlUtil;

import es.microforum.model.Empresa;
import es.microforum.serviceapi.EmpresaService;

@RequestMapping("/empresas")
@Controller
public class EmpresaController {
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	MessageSource messageSource;

	@Autowired
	private EmpresaService empresaService;

	@RequestMapping(method = RequestMethod.GET)
	public String list(Model uiModel) {
		logger.info("Listing empresas");
		return "empresas/list";
	}
	
	@RequestMapping(value = "/{nif}", method = RequestMethod.GET)
	public String show(@PathVariable("nif") String nif, Model uiModel) {
		Empresa empresa = empresaService.findByNif(nif);
		uiModel.addAttribute("empresa", empresa);
		return "empresas/show";
	}
	
	@RequestMapping(value = "/{nif}", params = "form", method = RequestMethod.POST)
	public String update(@Valid Empresa empresa, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes, Locale locale) {
		logger.info("Updating empresa");
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("message", new Message("error", messageSource.getMessage("empresa_save_fail", new Object[] {}, locale)));
			uiModel.addAttribute("empresa", empresa);
			return "empresas/update";
		}
		uiModel.asMap().clear();
		redirectAttributes.addFlashAttribute("message", new Message("success", messageSource.getMessage("empresa_save_success", new Object[] {}, locale)));
		
		empresaService.updateEmpresa(empresa);
		return "redirect:/empresas/" + UrlUtil.encodeUrlPathSegment(empresa.getNif().toString(), httpServletRequest);
	}
	
	@RequestMapping(value = "/{nif}", params = "form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("nif") String nif, Model uiModel) {
		uiModel.addAttribute("empresa", empresaService.findByNif(nif));
		return "empresas/update";
	}
	
	@RequestMapping(value = "/delete/{nif}", method = RequestMethod.GET)
	public String delete(@PathVariable("nif") String nif, RedirectAttributes redirectAttributes, Locale locale) {
		logger.info("Deleting empresa");
		
		Empresa empresa = empresaService.findByNif(nif);
		empresaService.deleteEmpresa(empresa);
		redirectAttributes.addFlashAttribute("message", new Message("success", messageSource.getMessage("empresa_delete_success", new Object[] {}, locale)));
		return "redirect:/empresas/delete";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String create(@Valid Empresa empresa, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes, Locale locale) {
		logger.info("Creating empresa");
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("message", new Message("error", messageSource.getMessage("empresa_save_fail", new Object[] {}, locale)));
			uiModel.addAttribute("empresa", empresa);
			return "empresas/create";
		}
		uiModel.asMap().clear();
		redirectAttributes.addFlashAttribute("message", new Message("success", messageSource.getMessage("empresa_save_success", new Object[] {}, locale)));
		logger.info("Empresa nif: " + empresa.getNif());
		
		empresaService.addEmpresa(empresa);
		return "redirect:/empresas/" + UrlUtil.encodeUrlPathSegment(empresa.getNif().toString(), httpServletRequest);
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(params = "form", method = RequestMethod.GET)
	public String createForm(Model uiModel) {
		Empresa empresa = new Empresa();
		uiModel.addAttribute("empresa", empresa);
		return "empresas/create";
	}

	/**
	 * Support data for front-end grid
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/listgrid", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public EmpresaGrid listGrid(@RequestParam("name") String name,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "rows", required = false) Integer rows,
			@RequestParam(value = "sidx", required = false) String sortBy,
			@RequestParam(value = "sord", required = false) String order) {
		logger.info("Listing empresas for grid with page: {}, rows: {}", page, rows);
		logger.info("Listing empresas for grid with sort: {}, order: {}", sortBy, order);

		PageRequest pageRequest = null;
		pageRequest = new PageRequest(page - 1, rows);

		EmpresaGrid empresaGrid = new EmpresaGrid();
		Page<Empresa> empresas;
		if (name == null || name.equals("undefined") || name.equals("")) {
			// empresas = empresaService.findByNombre("", pageRequest);
			empresas = empresaService.findAll(pageRequest);
		} else {
			empresas = empresaService.findByNombre(name, pageRequest);
		}
		
		empresaGrid.setCurrentPage(empresas.getNumber() + 1);
		empresaGrid.setTotalPages(empresas.getTotalPages());
		empresaGrid.setTotalRecords(empresas.getTotalElements());
		empresaGrid.setEmpresaData(empresas.getContent());

		return empresaGrid;
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yy");
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
}
