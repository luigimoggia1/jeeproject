package es.microforum.integrationtest;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.microforum.model.Empleado;
import es.microforum.serviceapi.EmpleadoService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring-data-app-context.xml")
@TransactionConfiguration(defaultRollback=true)
public class EmpleadoIntegrationTest {
	@Autowired
	private ApplicationContext ctx;
	private static final Logger logger = LoggerFactory.getLogger(EmpleadoIntegrationTest.class);
	
	EmpleadoService empleadoService;
	List<Empleado> empleados;
	Empleado empleado, empleado1;
	double porcentaje;

	@Before
	public void setUp() throws Exception {
		empleadoService = ctx.getBean("springJpaEmpleadoService", EmpleadoService.class);
		empleado = new Empleado("77325837K");
		empleado1 = new Empleado("77330577T");
		porcentaje = 10;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	@Transactional
	public void testFindAll() {
		empleados = empleadoService.findAll();
		assertTrue(empleados.size() == 0);
		logger.trace("testFindAll executed !!!");
	}

	@Test
	@Transactional
	public void testFindByDni() {
		String dni = "77325837K";
		empleadoService.addEmpleado(empleado);
		empleados = empleadoService.findAll();
		assertTrue(empleados.size() == 1);
		Empleado empleado2 = empleadoService.findByDni(dni);
		assertTrue(empleado.equals(empleado2));
		logger.trace("testFindByDni executed !!!");
	}

	@Test
	@Transactional
	public void testAddEmpleado() {
		empleadoService.addEmpleado(empleado);
		empleadoService.addEmpleado(empleado1);
		empleados = empleadoService.findAll();
		assertTrue(empleados.size() == 2);
		logger.trace("testAddEmpleado executed !!!");
	}

	@Test
	@Transactional
	public void testUpdateEmpleado() {
		String direccion = "Santo Rostro 24";
		empleadoService.addEmpleado(empleado);
		empleado.setDireccion(direccion);
		empleadoService.addEmpleado(empleado);
		assertTrue(empleadoService.findByDni(empleado.getDni()).getDireccion().equals(direccion));
		logger.trace("testUpdateEmpleado executed !!!");
	}

	@Test
	@Transactional
	public void testDeleteEmpleado() {
		empleadoService.addEmpleado(empleado);
		empleadoService.addEmpleado(empleado1);
		empleadoService.deleteEmpleado(empleado1);
		empleados = empleadoService.findAll();
		assertTrue(empleadoService.findByDni(empleado1.getDni()) == null);
		assertTrue(empleados.size() == 1);
		logger.trace("testDeleteEmpleado executed !!!");
	}
	
	@Test
	@Transactional
	public void testModificarSalarioEmpleado() {
		Empleado empleado = empleadoService.findByDni("77325837K");
		double salarioAntes = empleado.getSalarioAnual();
		empleadoService.modificarSalario(porcentaje);
		double salarioDespues = empleado.getSalarioAnual();
		assertTrue(salarioDespues == (salarioAntes + (salarioAntes * (porcentaje / 100))));
		logger.trace("testModificarSalarioEmpleado executed !!!");
	}
}
