package es.microforum.apiresttest;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import es.microforum.model.Empresa;
import es.microforum.repository.EmpresaRepository;

@ContextConfiguration(locations = {"classpath:spring-data-app-context.xml"})
public class EmpresaRepositoryJsonClientTest {
	String jpaWebContext;
	
	@Autowired
	ApplicationContext context;
	
	@Autowired
	EmpresaRepository empresaRepository;
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Before
	public void before() {
		DataSource dataSource = (DataSource) context.getBean("dataSource");
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.execute("DELETE FROM empresa");
	}

	@Test
	public void getTest() {
		try {
			Resource<Empresa> resource = getEmpresa(new URI("http://localhost:8081/service-frontend-0.0.2-SNAPSHOT/empresa/111A"));
			assertTrue(resource.getContent().getDireccionFiscal().equals("Direccion1"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	private Resource<Empresa> getEmpresa(URI uri) {
		return restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<Resource<Empresa>>() {
		}).getBody();
	}
}
