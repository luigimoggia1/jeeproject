package es.microforum.ws.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebService;

import es.microforum.serviceapi.EmpleadoService;

@WebService
public class ModificadorSalarioWebService implements IModificadorSalarioWebService {
	private EmpleadoService empleadoService;

	@WebMethod(exclude=true)
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	
	@WebMethod(operationName="callModificadorSalario")
	public void callModificadorSalario(double porcentaje){
		empleadoService.modificarSalario(porcentaje);
	}
}
