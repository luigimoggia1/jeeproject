package es.microforum.prototipo.web.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.microforum.model.Empleado;
import es.microforum.model.Empresa;
import es.microforum.prototipo.web.form.EmpleadoGrid;
import es.microforum.prototipo.web.form.Message;
import es.microforum.prototipo.web.util.UrlUtil;
import es.microforum.serviceapi.EmpleadoService;
import es.microforum.serviceapi.EmpresaService;

@RequestMapping("/empleados")
@Controller
public class EmpleadoController {
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	MessageSource messageSource;

	@Autowired
	private EmpleadoService empleadoService;
	
	@Autowired
	private EmpresaService empresaService;

	@RequestMapping(method = RequestMethod.GET)
	public String list(Model uiModel) {
		logger.info("Listing empleados");
		return "empleados/list";
	}
	
	@RequestMapping(value = "/{dni}", method = RequestMethod.GET)
	public String show(@PathVariable("dni") String dni, Model uiModel) {
		Empleado empleado = empleadoService.findByEmpresa(dni);
		uiModel.addAttribute("empleado", empleado);
		return "empleados/show";
	}
	
	@PreAuthorize("hasRole('supervisor')")
	@RequestMapping(value = "/{dni}", params = "form", method = RequestMethod.POST)
	public String update(@RequestParam(value="empresa") String nif, @Valid Empleado empleado, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes, Locale locale) {
		logger.info("Updating empleado");
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("message", new Message("error", messageSource.getMessage("empleado_save_fail", new Object[] {}, locale)));
			uiModel.addAttribute("empleado", empleado);
			return "empleados/update";
		}
		uiModel.asMap().clear();
		redirectAttributes.addFlashAttribute("message", new Message("success", messageSource.getMessage("empleado_save_success", new Object[] {}, locale)));
		
		Empresa empresa = empresaService.findByNif(nif);
		empleado.setEmpresa(empresa);
		empleadoService.updateEmpleado(empleado);
		return "redirect:/empleados/" + UrlUtil.encodeUrlPathSegment(empleado.getDni().toString(), httpServletRequest);
	}
	
	@RequestMapping(value = "/{dni}", params = "form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("dni") String dni, Model uiModel) {
		uiModel.addAttribute("empresas", empresaService.findAll());
		uiModel.addAttribute("empleado", empleadoService.findByEmpresa(dni));
		return "empleados/update";
	}
	
	@RequestMapping(value = "/delete/{dni}", method = RequestMethod.GET)
	public String delete(@PathVariable("dni") String dni, RedirectAttributes redirectAttributes, Locale locale) {
		logger.info("Deleting empleado");
		
		Empleado empleado = empleadoService.findByDni(dni);
		empleadoService.deleteEmpleado(empleado);
		redirectAttributes.addFlashAttribute("message", new Message("success", messageSource.getMessage("empleado_delete_success", new Object[] {}, locale)));
		return "redirect:/empleados/delete";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String create(@Valid Empleado empleado, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes, Locale locale) {
		logger.info("Creating empleado");
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("message", new Message("error", messageSource.getMessage("empleado_save_fail", new Object[] {}, locale)));
			uiModel.addAttribute("empleado", empleado);
			return "empleados/create";
		}
		uiModel.asMap().clear();
		redirectAttributes.addFlashAttribute("message", new Message("success", messageSource.getMessage("empleado_save_success", new Object[] {}, locale)));
		logger.info("Empleado dni: " + empleado.getDni());
		
		empleadoService.addEmpleado(empleado);
		return "redirect:/empleados/" + UrlUtil.encodeUrlPathSegment(empleado.getDni().toString(), httpServletRequest);
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(params = "form", method = RequestMethod.GET)
	public String createForm(Model uiModel) {
		Empleado empleado = new Empleado();
		uiModel.addAttribute("empleado", empleado);
		return "empleados/create";
	}

	/**
	 * Support data for front-end grid
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/listgrid", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public EmpleadoGrid listGrid(@RequestParam("name") String name,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "rows", required = false) Integer rows,
			@RequestParam(value = "sidx", required = false) String sortBy,
			@RequestParam(value = "sord", required = false) String order) {
		logger.info("Listing empleados for grid with page: {}, rows: {}", page, rows);
		logger.info("Listing empleados for grid with sort: {}, order: {}", sortBy, order);

		PageRequest pageRequest = null;
		pageRequest = new PageRequest(page - 1, rows);

		EmpleadoGrid empleadoGrid = new EmpleadoGrid();
		Page<Empleado> empleados;
		if (name == null || name.equals("undefined") || name.equals("")) {
			// empleados = empleadoService.findByNombre("", pageRequest);
			empleados = empleadoService.findAll(pageRequest);
		} else {
			empleados = empleadoService.findByNombre(name, pageRequest);
		}
		
		empleadoGrid.setCurrentPage(empleados.getNumber() + 1);
		empleadoGrid.setTotalPages(empleados.getTotalPages());
		empleadoGrid.setTotalRecords(empleados.getTotalElements());
		empleadoGrid.setEmpleadoData(empleados.getContent());

		return empleadoGrid;
	}
}
