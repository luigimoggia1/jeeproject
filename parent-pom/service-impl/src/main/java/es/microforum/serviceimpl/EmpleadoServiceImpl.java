package es.microforum.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import es.microforum.model.Empleado;
import es.microforum.model.Empresa;
import es.microforum.repository.EmpleadoRepository;
import es.microforum.serviceapi.EmpleadoService;

@Service("springJpaEmpleadoService")
@Repository
@Transactional
public class EmpleadoServiceImpl implements EmpleadoService {
	@Autowired
	EmpleadoRepository empleadoRepository;

	@Override
	@Transactional (readOnly=true)
	public List<Empleado> findAll() {
		return Lists.newArrayList(empleadoRepository.findAll());
	}

	@Override
	@Transactional (readOnly=true)
	public Empleado findByDni(String dni) {
		return empleadoRepository.findOne(dni);
	}
	
	@Override
	@Transactional (readOnly=true)
	public Empleado findByEmpresa(String dni) {
		Empleado empleado = empleadoRepository.findOne(dni);
		Empresa empresa = empleado.getEmpresa();
		empresa.getEmpleados().size();
		return empleado;
	}

	@Override
	public Empleado addEmpleado(Empleado empleado) {
		return empleadoRepository.save(empleado);
	}

	@Override
	public Empleado updateEmpleado(Empleado empleado) {
		return empleadoRepository.save(empleado);
	}

	@Override
	public void deleteEmpleado(Empleado empleado) {
		empleadoRepository.delete(empleado);
	}

	@Override
	@Transactional (readOnly=true)
	public Page<Empleado> findByNombre(String nombre, Pageable pageable) {
		return empleadoRepository.findByNombre(nombre, pageable);
	}
	
	@Override
	@Transactional (readOnly=true)
	public Page<Empleado> findAll(Pageable pageable) {
		return empleadoRepository.findAll(pageable);
	}

	@Override
	public void modificarSalario(double porcentaje) {
		List<Empleado> empleados = findAll();
		for (Empleado empleado : empleados) {
			double salarioEmpleado = empleado.getSalarioAnual();
			salarioEmpleado += (salarioEmpleado * (porcentaje / 100));
			empleado.setSalarioAnual(salarioEmpleado);
			updateEmpleado(empleado);
		}
	}
}
