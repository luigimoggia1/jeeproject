package es.microforum.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmpleadoTest {
	Empleado empleado1;

	@Before
	public void setUp() throws Exception {
		empleado1 = new Empleado("77325837K");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEqualsObject() {
		Empleado empleado2 = new Empleado("77325837K");
		assertTrue(empleado1.equals(empleado2));
	}
}
