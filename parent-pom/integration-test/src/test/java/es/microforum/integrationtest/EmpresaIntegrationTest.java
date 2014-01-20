package es.microforum.integrationtest;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import es.microforum.model.Empresa;
import es.microforum.serviceapi.EmpresaService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring-data-app-context.xml")
@TransactionConfiguration(defaultRollback=true)
public class EmpresaIntegrationTest {
	@Autowired
	private ApplicationContext ctx;
	
	EmpresaService empresaService;
	List<Empresa> empresas;
	Empresa empresa1, empresa2;

	@Before
	public void setUp() throws Exception {
		empresaService = ctx.getBean("springJpaEmpresaService", EmpresaService.class);
		empresa1 = new Empresa("111A");
		empresa2 = new Empresa("222B");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFindAll() {
		empresas = empresaService.findAll();
		assertTrue(empresas.size() == 0);
	}

	@Test
	public void testFindByNif() {
		String nif = "111A";
		empresaService.addEmpresa(empresa1);
		empresas = empresaService.findAll();
		assertTrue(empresas.size() == 1);
		Empresa empresa = empresaService.findByNif(nif);
		assertTrue(empresa1.equals(empresa));
	}

	@Test
	public void testAddEmpresa() {
		empresaService.addEmpresa(empresa1);
		empresaService.addEmpresa(empresa2);
		empresas = empresaService.findAll();
		assertTrue(empresas.size() == 2);
	}

	@Test
	public void testUpdateEmpresa() {
		String direccionFiscal = "Miguel Castillejo 12";
		empresaService.addEmpresa(empresa1);
		empresa1.setDireccionFiscal(direccionFiscal);
		empresaService.addEmpresa(empresa1);
		assertTrue(empresaService.findByNif(empresa1.getNif()).getDireccionFiscal().equals(direccionFiscal));
	}

	@Test
	public void testDeleteEmpresa() {
		empresaService.addEmpresa(empresa1);
		empresaService.addEmpresa(empresa2);
		empresaService.deleteEmpresa(empresa2);
		empresas = empresaService.findAll();
		assertTrue(empresaService.findByNif(empresa2.getNif()) == null);
		assertTrue(empresas.size() == 1);
	}

}
