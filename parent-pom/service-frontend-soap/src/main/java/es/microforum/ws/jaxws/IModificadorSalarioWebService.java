package es.microforum.ws.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface IModificadorSalarioWebService {
	@WebMethod(operationName = "callModificadorSalario")
	public void callModificadorSalario(double porcentaje);
}
