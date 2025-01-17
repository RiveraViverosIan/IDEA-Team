package mx.uam.ayd.proyecto.negocio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mx.uam.ayd.proyecto.datos.*;
import mx.uam.ayd.proyecto.negocio.modelo.*;

@Slf4j
@Service
public class ServicioEmpleado {
	@Autowired
	private EmpleadoRepository empleadoRepository;
	
	@Autowired
	private PuestoRepository puestoRepository;
	
	
	//tiene el servicio de Agregar al empleado con los datos ingresados
	public Empleado agregarEmpleado(String nombre, String apellidoP, String apellidoM,String direccion, String tel,  String email, String tarea,  String nombrePuesto) {
		Empleado empleado=empleadoRepository.findByEmail(email);
		if(empleado!=null) {
			throw new IllegalArgumentException("El Usuario con el email ingresado ingresado ya Existe");
		}
		
		Puesto puesto=puestoRepository.findByNombre(nombrePuesto);
		if(puesto==null) {
			throw new IllegalArgumentException("No se encontro el Puesto");	
		}
		
		log.info("Agregando empleado");
		empleado=new Empleado();
		empleado.setNombre(nombre);
		empleado.setApellidoP(apellidoP);
		empleado.setApellidoM(apellidoM);
		empleado.setDireccion(direccion);
		empleado.setTel(tel);
		empleado.setEmail(email);
		empleado.setTarea(tarea);
		
		empleadoRepository.save(empleado);
		puesto.addEmpleado(empleado);
		puestoRepository.save(puesto);

		return empleado;
	}

	//Tiene el servicio de Actualizar los datos del Empleado
	public void actualizarEmpleado(Empleado empleado,Puesto puestoRecuperado,String nombre, String apellidoP, String apellidoM, String direccion,String tel, String email, String tarea,String Puestos) {
		//log.info("Actulizar Empleado");
		Puesto puesto=puestoRepository.findByNombre(Puestos);
		if(puesto==null) {
			throw new IllegalArgumentException("No se encontro el Puesto");	
		}
		//Edicion de los atributos de empleado
		empleado.setNombre(nombre);
		empleado.setApellidoP(apellidoP);
		empleado.setApellidoM(apellidoM);
		empleado.setDireccion(direccion);
		empleado.setTel(tel);
		empleado.setEmail(email);
		empleado.setTarea(tarea);
		empleadoRepository.save(empleado);
		
		//Edicion del grupo del Puesto al que pertenezca el empleado
		if(!puestoRecuperado.getNombre().equals(puesto.getNombre())) {
			if(puesto.addEmpleado(empleado)==false) {
				puestoRecuperado.eliminarEmpleadoPuesto(empleado);
				puestoRepository.save(puestoRecuperado);
				puesto.addEmpleado(empleado);													
			}
			puestoRepository.save(puesto);
		}
	}
	
	
	//Tiene el servicio de recuperar al Empleado a trabajar
	public Empleado recuperarEmpleado(String email) {
		Empleado empleado=empleadoRepository.findByEmail(email);
		return empleado;
	}
	
	//Tiene el servicio de recuperar al Puesto a trabajar
	public  Puesto recuperaPuestoEmpleado(Empleado empleado, List <Puesto> puestos) {
		for(Puesto puesto:puestos) {
			if(puesto.validarPuestoEmpleado(empleado)!=false) {
				return puesto;
			}
				
		}
		return null;
	}
}
