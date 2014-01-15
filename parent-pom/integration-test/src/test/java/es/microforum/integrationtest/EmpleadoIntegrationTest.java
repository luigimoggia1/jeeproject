package es.microforum.integrationtest;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.GenericXmlApplicationContext;

import es.microforum.model.Empleado;
import es.microforum.serviceapi.EmpleadoService;

public class EmpleadoIntegrationTest {
	GenericXmlApplicationContext ctx;
	EmpleadoService empleadoService;
	List<Empleado> empleados;

	@Before
	public void setUp() throws Exception {
		ctx = new GenericXmlApplicationContext();
		ctx.load("classpath:spring-data-app-context.xml");
		ctx.refresh();
		
		empleadoService = ctx.getBean("springJpaEmpleadoService", EmpleadoService.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFindAll() {
		empleados = empleadoService.findAll();
		assertTrue(empleados.size() == 0);
	}

//	@Test
//	public void testFindByDni() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testAddEmpleado() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testUpdateEmpleado() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testDeleteEmpleado() {
//		fail("Not yet implemented");
//	}
}
