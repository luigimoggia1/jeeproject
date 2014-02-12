package es.microforum.ws.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebService;

import es.microforum.serviceapi.EmpleadoService;

@WebService
public class ModificadorSalarioWebService implements IModificadorSalarioWebService {
	private EmpleadoService modificadorSalario;

	@WebMethod(exclude=true)
	public void setModificadorSalario(EmpleadoService modificadorSalario) {
		this.modificadorSalario = modificadorSalario;
	}
	
	@WebMethod(operationName="callModificadorSalario")
	public void callModificadorSalario(double porcentaje) {
		modificadorSalario.modificarSalario(porcentaje);
	}
}
