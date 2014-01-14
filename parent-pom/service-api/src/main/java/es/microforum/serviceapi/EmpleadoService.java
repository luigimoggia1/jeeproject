package es.microforum.serviceapi;

import java.util.List;

import es.microforum.model.Empleado;

public interface EmpleadoService {
	public List<Empleado> findAll();
	public Empleado findByDni(String dni);
	public Empleado addEmpleado(Empleado empleado);
	public Empleado updateEmpleado(Empleado empleado);
	public void deleteEmpleado(Empleado empleado);
}
