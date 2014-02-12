package es.microforum.ws.jaxws;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import es.microforum.model.Empleado;
import es.microforum.serviceapi.EmpleadoService;
import es.microforum.ws.jaxws.IModificadorSalarioWebService;

public class JaxServiceClientTest {
	private IModificadorSalarioWebService modificadorSalarioWebService;
	private EmpleadoService empleadoService;
	
	@Before
	public void setUp() throws Exception {
		try {
			// Creación de contexto y bean jax
			ApplicationContext context = new ClassPathXmlApplicationContext("spring-data-app-context.xml");
			modificadorSalarioWebService = (IModificadorSalarioWebService) context.getBean("jaxModificadorSalarioWebService");
			// Creacion del bean de servicio
			empleadoService = context.getBean("springJpaEmpleadoService", EmpleadoService.class);
		} catch (Throwable t) {
			t.printStackTrace();
			fail();
		}
	}

	@Test
	public void testCallModificadorSalario() {
		try {
			double porcentaje = 10;
			Empleado empleado = empleadoService.findByDni("77325837K");
			double salarioAntes = empleado.getSalarioAnual();
			modificadorSalarioWebService.callModificadorSalario(porcentaje);
			empleado = empleadoService.findByDni("77325837K");
			assertTrue(salarioAntes != empleado.getSalarioAnual());
			assertTrue(((salarioAntes*porcentaje)/100) + salarioAntes == empleado.getSalarioAnual());
		} catch (Throwable t) {
			t.printStackTrace();
			fail();
		}
	}
}
