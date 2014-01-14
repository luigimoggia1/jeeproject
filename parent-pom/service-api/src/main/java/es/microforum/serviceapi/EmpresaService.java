package es.microforum.serviceapi;

import java.util.List;

import es.microforum.model.Empresa;

public interface EmpresaService {
	public List<Empresa> findAll();
	public Empresa findByNif(String nif);
	public Empresa addEmpresa(Empresa empresa);
	public Empresa updateEmpresa(Empresa empresa);
	public void deleteEmpresa(Empresa empresa);
}
