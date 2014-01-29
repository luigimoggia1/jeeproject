package es.microforum.serviceapi;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.microforum.model.Empresa;

public interface EmpresaService {
	public List<Empresa> findAll();
	public Empresa findByNif(String nif);
	public Empresa addEmpresa(Empresa empresa);
	public Empresa updateEmpresa(Empresa empresa);
	public void deleteEmpresa(Empresa empresa);
	public Page<Empresa> findByNombre(String nombre, Pageable pageable);
	public Page<Empresa> findAll(Pageable pageable);
}
