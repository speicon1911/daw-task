package com.daw.services;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daw.persistence.entities.Tarea;
import com.daw.persistence.entities.enums.Estado;
import com.daw.persistence.repositories.TareaRepository;
import com.daw.services.exceptions.TareaExceptions;
import com.daw.services.exceptions.TareaNotFoundException;

@Service
public class TareaService {

	@Autowired
	private TareaRepository tareaRepository;

	public List<Tarea> findAll() {
		return this.tareaRepository.findAll();
	}

	public Tarea findById(int idTarea) {
		if (!this.tareaRepository.existsById(idTarea)) {
			throw new TareaNotFoundException("No existe la tarea con ID: " + idTarea);
		}

		return this.tareaRepository.findById(idTarea).get();
	}

	public boolean existsById(int idTarea) {
		return this.tareaRepository.existsById(idTarea);
	}

	public void deleteById(int idTarea) {
		if (!existsById(idTarea)) {
			throw new TareaNotFoundException("El ID introducido no aparece en la base de datos");
		}
		this.tareaRepository.deleteById(idTarea);
	}

	public Tarea create(Tarea tarea) {
		tarea.setFechaCreacion(LocalDate.now());
		tarea.setEstado(Estado.PENDIENTE);

		Tarea tareaBD = this.tareaRepository.save(tarea);

		return tareaBD;
	}

	public Tarea update(int idTarea, Tarea tarea) {
		if (idTarea != tarea.getId()) {
			throw new TareaExceptions(
					"El ID del Path (" + idTarea + ") y el body (" + tarea.getId() + ") no coinciden");
		}
		if (!this.tareaRepository.existsById(idTarea)) {
			throw new TareaExceptions("No se ha enconyrado la tarea con ID: " + idTarea);
		}
		if (tarea.getFechaCreacion() != null || tarea.getEstado() != null) {
			throw new TareaExceptions("No se permite modificar la fecha de creación y/o el estado");
		}

		Tarea tareaDB = this.findById(tarea.getId());
		/*
		 * ESTO HACE QUE NO SE PUEDA ACTUALIZAR
		 * tarea.setFechaCreacion(tareaDB.getFechaCreacion());
		 * tarea.setEstado(tareaDB.getEstado());
		 */

		/* ESTO SI DEJA ACTUALZIAR DATOS, ES DECIR, MODIFCARLOS */
		tareaDB.setTitulo(tarea.getTitulo());
		tareaDB.setDescripcion(tarea.getDescripcion());
		tareaDB.setFechaVencimiento(tarea.getFechaVencimiento());

		return this.tareaRepository.save(tarea);
	}
	
	// Ejemplos OPTIONAL
	public boolean deleteDeclarativo(int idTarea) {
		boolean result = false;

		if (this.tareaRepository.existsById(idTarea)) {
			this.tareaRepository.deleteById(idTarea);
			result = true;
		}
		return result;
	}

	public boolean deleteFuncional(int idTarea) {
		return this.tareaRepository.findById(idTarea)
				.map(t -> {
			this.tareaRepository.deleteById(idTarea);
			return true;
		}).orElse(false);
	}

	public Tarea findByIdFuncional(int idTarea) {
		return this.tareaRepository.findById(idTarea)
				.orElseThrow(() -> new TareaNotFoundException("No existe la tarea con ID: " + idTarea));
	}

	// Ejemplos Stream
	
	//Obtener el numero total de tareas completadas
	public long totalTareasCompletadas() {
		return this.tareaRepository.findAll().stream()
				.filter(t -> t.getEstado() == Estado.COMPLETADA)
				.count();
	}
	
	//Obtener una lista de las fechas de vencimientos de las tareas que esten en progreso
	public List<LocalDate> fechasVencimientoEnProgreso(){
		return this.tareaRepository.findAll().stream()
				.filter(t -> t.getEstado() == Estado.EN_PROGRESO)
				.map(t -> t.getFechaVencimiento())
				.collect(Collectors.toList());
	}
}
