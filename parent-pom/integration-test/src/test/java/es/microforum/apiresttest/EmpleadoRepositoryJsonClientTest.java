package es.microforum.apiresttest;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import es.microforum.model.Empresa;
import es.microforum.repository.EmpresaRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-data-app-context.xml"})
public class EmpleadoRepositoryJsonClientTest {
	String jpaWebContext;
	
	@Autowired
	ApplicationContext context;
	
	@Autowired
	EmpresaRepository empleadoRepository;
	
	private JdbcTemplate jdbcTemplate;
	
	RestTemplate restTemplate;
	
	@Before
	public void setUp() {
		restTemplate = new RestTemplate();
		jpaWebContext = "http://localhost:8081/service-frontend-0.0.2-SNAPSHOT/empresa/";
		DataSource dataSource = (DataSource) context.getBean("dataSource");
		jdbcTemplate = new JdbcTemplate(dataSource);
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
	
	@Test
	public void deleteTest() {
		try {
			jdbcTemplate.execute("INSERT INTO empresa values('222B', 'Empresa2', 'Direccion2', '2013-09-26 18:00:00', 0)");
			int count = jdbcTemplate.queryForObject("select count(*) from empresa where nif='222B'", Integer.class);
			assertTrue(count == 1);
			restTemplate.delete(jpaWebContext + "222B");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		int count = jdbcTemplate.queryForObject("select count(*) from empresa where nif='222B'", Integer.class);
		assertTrue(count == 0);
	}
	
	@Test
	public void postTest() throws RestClientException, URISyntaxException {
		String url = jpaWebContext;
		String acceptHeaderValue = "application/json";
		
		HttpHeaders requestHeaders = new HttpHeaders();
		List<MediaType> mediaTypes = new ArrayList<MediaType>();
		mediaTypes.add(MediaType.valueOf(acceptHeaderValue));
		requestHeaders.setAccept(mediaTypes);
		requestHeaders.setContentType(MediaType.valueOf(acceptHeaderValue));
		HttpMethod post = HttpMethod.POST;

		String body = "{\"nif\":\"222B\",\"nombre\":\"Empresa2\",\"direccionFiscal\":\"Direccion2\",\"fechaInicioActividades\":\"2014-01-01\"}";
		HttpEntity<String> entity = new HttpEntity<String>(body, requestHeaders);

		ResponseEntity<String> response = restTemplate.exchange(url, post, entity, String.class);
		assertTrue(response.getStatusCode().equals(HttpStatus.CREATED));
		int count = jdbcTemplate.queryForObject("select count(*) from empresa where nif='222B'", Integer.class);
		assertTrue(count == 1);
		jdbcTemplate.execute("DELETE FROM empresa where nif='222B'");
	}
	
	@Test
	public void putTest() throws RestClientException, URISyntaxException {
		jdbcTemplate.execute("INSERT INTO empresa values('222B', 'Empresa2', 'Direccion2', '2006-09-26 15:00:00', 0)");
		String url = jpaWebContext + "222B";
		String acceptHeaderValue = "application/json";

		HttpHeaders requestHeaders = new HttpHeaders();
		List<MediaType> mediaTypes = new ArrayList<MediaType>();
		mediaTypes.add(MediaType.valueOf(acceptHeaderValue));
		requestHeaders.setAccept(mediaTypes);
		requestHeaders.setContentType(MediaType.valueOf(acceptHeaderValue));
		HttpMethod put = HttpMethod.PUT;

		String body = "{\"direccionFiscal\":\"Avenida Maria Guerrero 92\"}";
		HttpEntity<String> entity = new HttpEntity<String>(body, requestHeaders);

		ResponseEntity<String> response = restTemplate.exchange(url, put, entity, String.class);
		assertTrue(response.getStatusCode().equals(HttpStatus.NO_CONTENT));
		int count = jdbcTemplate.queryForObject("select count(*) from empresa where direccionFiscal LIKE '%Maria%'", Integer.class);
		assertTrue(count == 1);
		jdbcTemplate.execute("DELETE FROM empresa where direccionFiscal LIKE '%Maria%'");
	}
}
