package es.microforum.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmpresaTest {
	Empresa empresa1;

	@Before
	public void setUp() throws Exception {
		empresa1 = new Empresa("A123-BC");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEqualsObject() {
		Empresa empresa2 = new Empresa("A123-BC");
		assertTrue(empresa1.equals(empresa2));
	}
}
