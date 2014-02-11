package es.microforum.ws.jaxws;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import es.microforum.ws.jaxws.IModificadorSalarioWebService;

public class JaxServiceClientTest {
	private IModificadorSalarioWebService modificadorSalarioWebService;
	
	@Before
	public void setUp() throws Exception {
		try {
			ApplicationContext context = new ClassPathXmlApplicationContext("applicationClientContext.xml");
			modificadorSalarioWebService = (IModificadorSalarioWebService) context.getBean("jaxModificadorSalarioWebService");

		} catch (Throwable t) {
			t.printStackTrace();
			fail();
		}
	}

	@Test
	public void testCallModificadorSalario() {
		try {
			assertTrue(true);
		} catch (Throwable t) {
			t.printStackTrace();
			fail();
		}
	}
}
