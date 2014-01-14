package es.microforum.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import es.microforum.model.Empresa;
import es.microforum.repository.EmpresaRepository;
import es.microforum.serviceapi.EmpresaService;

@Service("springJpaEmpresaService")
@Repository
@Transactional
public class EmpresaServiceImpl implements EmpresaService {
	EmpresaRepository empresaRepository;

	@Override
	public List<Empresa> findAll() {
		return Lists.newArrayList(empresaRepository.findAll());
	}

	@Override
	public Empresa findByNif(String nif) {
		return empresaRepository.findOne(nif);
	}

	@Override
	public Empresa addEmpresa(Empresa empresa) {
		return empresaRepository.save(empresa);
	}

	@Override
	public Empresa updateEmpresa(Empresa empresa) {
		return empresaRepository.save(empresa);
	}

	@Override
	public void deleteEmpresa(Empresa empresa) {
		empresaRepository.delete(empresa);
	}
}
