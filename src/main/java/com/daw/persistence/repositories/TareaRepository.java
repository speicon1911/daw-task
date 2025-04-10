package com.daw.persistence.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import com.daw.persistence.entities.Tarea;
import com.daw.persistence.entities.enums.Estado;

public interface TareaRepository extends ListCrudRepository<Tarea, Integer>{


	//	Obtener el número total de tareas completadas.
	long countByEstado(Estado estado);
	
	//	Obtener una lista de las fechas de vencimiento de las tareas que estén en progreso.
	List<Tarea> findByEstado(Estado estado);
	
	//	Obtener las tareas vencidas.
	List<Tarea> findByFechaVencimientoBefore(LocalDate fecha);
	
	// Obtener las tareas no vencidas (fecha de vencimiento mayor que la de hoy).
	List<Tarea> findByFechaVencimientoAfter(LocalDate fecha);
		
	//	Obtener las tareas ordenadas por fecha de vencimiento.
	List<Tarea> findAllByOrderByFechaVencimiento();
	
    List<Tarea> findByTituloContainingIgnoreCase(String titulo);

}
