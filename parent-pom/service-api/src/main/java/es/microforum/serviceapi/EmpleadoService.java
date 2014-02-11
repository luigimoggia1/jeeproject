package es.microforum.serviceapi;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.microforum.model.Empleado;

public interface EmpleadoService {
	public List<Empleado> findAll();
	public Empleado findByDni(String dni);
	public Empleado findByEmpresa(String dni);
	public Empleado addEmpleado(Empleado empleado);
	public Empleado updateEmpleado(Empleado empleado);
	public void deleteEmpleado(Empleado empleado);
	public Page<Empleado> findByNombre(String nombre, Pageable pageable);
	public Page<Empleado> findAll(Pageable pageable);
	public void modificarSalario(double porcentaje);
}
