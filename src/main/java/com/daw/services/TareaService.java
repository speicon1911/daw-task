package com.daw.services;

import java.time.LocalDate;
import java.util.List;
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

	// Obtener todas las tareas.
	public List<Tarea> findAll() {
		return this.tareaRepository.findAll();
	}

	// Obtener una tarea mediante su ID.
	public Tarea findById(int idTarea) {
		if (!this.tareaRepository.existsById(idTarea)) {
			throw new TareaNotFoundException("No existe la tarea con ID: " + idTarea);
		}

		return this.tareaRepository.findById(idTarea).get();
	}

	public boolean existsById(int idTarea) {
		return this.tareaRepository.existsById(idTarea);
	}

	// Crear una tarea.
	public Tarea create(Tarea tarea) {
		tarea.setFechaCreacion(LocalDate.now());
		if (tarea.getFechaVencimiento().isBefore(tarea.getFechaCreacion())) {
			throw new TareaExceptions("La fecha de vencimiento debe ser mayor que la de creacion");
		}
		tarea.setEstado(Estado.PENDIENTE);
		tarea.setId(0);
		Tarea tareaBD = this.tareaRepository.save(tarea);

		return tareaBD;
	}

	// Modificar una tarea.
	public Tarea update(int idTarea, Tarea tarea) {
		if (idTarea != tarea.getId()) {
			throw new TareaExceptions(
					"El ID del path (" + idTarea + ") y el id del body (" + tarea.getId() + ") no coinciden");
		}
		if (!this.tareaRepository.existsById(idTarea)) {
			throw new TareaNotFoundException("No existe la tarea con ID: " + idTarea);
		}
		if (tarea.getFechaCreacion() != null || tarea.getEstado() != null) {
			throw new TareaExceptions("No se permite modificar la fecha de creación y/o el estado. ");
		}

		Tarea tareaBD = this.findById(tarea.getId());
		tareaBD.setTitulo(tarea.getTitulo());
		tareaBD.setDescripcion(tarea.getDescripcion());
		tareaBD.setFechaVencimiento(tarea.getFechaVencimiento());

		return this.tareaRepository.save(tareaBD);
	}

	// Borrar una tarea.
	public void deleteById(int idTarea) {
		if (!existsById(idTarea)) {
			throw new TareaNotFoundException("El ID introducido no aparece en la base de datos");
		}
		this.tareaRepository.deleteById(idTarea);
	}

	// Iniciar una tarea
	public Tarea iniciarTarea(int idTarea) {
		if (!this.tareaRepository.existsById(idTarea)) {
			throw new TareaNotFoundException("No existe la tarea con ID: " + idTarea);
		}
		if (this.tareaRepository.findById(idTarea).get().getEstado() != Estado.PENDIENTE) {
			throw new TareaExceptions("Para iniciar nuna tarea, su estado debe ser PENDIENTE");
		}
		Tarea tarea = this.findById(idTarea);
		tarea.setEstado(Estado.EN_PROGRESO);

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
		return this.tareaRepository.findById(idTarea).map(t -> {
			this.tareaRepository.deleteById(idTarea);
			return true;
		}).orElse(false);
	}

	public Tarea findByIdFuncional(int idTarea) {
		return this.tareaRepository.findById(idTarea)
				.orElseThrow(() -> new TareaNotFoundException("No existe la tarea con ID: " + idTarea));
	}

	// Ejemplos Stream

	// Obtener el numero total de tareas completadas
	public long totalTareasCompletadasFuncional() {
		return this.tareaRepository.findAll().stream().filter(t -> t.getEstado() == Estado.COMPLETADA).count();
	}

	// Con repository
	public long totalTareasCompletadas() {
		return this.tareaRepository.countByEstado(Estado.COMPLETADA);
	}

	// Obtener una lista de las fechas de vencimientos de las tareas que esten en
	// progreso
	public List<LocalDate> fechasVencimientoEnProgresoFuncional() {
		return this.tareaRepository.findAll().stream().filter(t -> t.getEstado() == Estado.EN_PROGRESO)
				.map(t -> t.getFechaVencimiento()).collect(Collectors.toList());
	}

	// Con repository
	public List<LocalDate> fechaVencimientoEnProgreso() {
		return this.tareaRepository.findByEstado(Estado.EN_PROGRESO).stream().map(t -> t.getFechaVencimiento())
				.collect(Collectors.toList());
	}

	// Obtener las tareas vencidas.
	// Stream
	public void tareasVencidasFuncional() {
		this.tareaRepository.findAll().stream().filter(t -> t.getFechaVencimiento().isBefore(LocalDate.now()))
				.collect(Collectors.toList());
	}

	// Con repository
	public List<Tarea> tareasVencidas() {
		return this.tareaRepository.findByFechaVencimientoBefore(LocalDate.now());
	}

	// Obtener las tareas no vencidas
	// Repository
	public List<Tarea> tareasNoVencidas() {
		return this.tareaRepository.findByFechaVencimientoAfter(LocalDate.now());
	}

	// Stream
	public void tareasNoVencidadFuncional() {
		this.tareaRepository.findAll().stream().filter(t -> t.getFechaVencimiento().isAfter(LocalDate.now()))
				.collect(Collectors.toList());
	}

	/*
	 * Otra forma public List<Tarea> tareasVencidas() { return
	 * this.tareaRepository.findAll().stream() .filter(t ->
	 * t.getFechaVencimiento().isBefore(LocalDate.now()))
	 * .collect(Collectors.toList());
	 */

	// Obtener los titulos de las tareas pendientes

	public List<String> tituloTareasPendientes() {
		return this.tareaRepository.findByEstado(Estado.PENDIENTE).stream().map(t -> t.getTitulo())
				.collect(Collectors.toList());
	}

	// Obtener las tareas en progreso.
	public List<?> tareasEnProgresoFuncional() {
		return this.tareaRepository.findAll().stream().filter(t -> t.getEstado() == Estado.EN_PROGRESO)
				.collect(Collectors.toList());
	}

	// Obtener las tareas completadas.
	public List<?> tareasCompletadas() {
		return this.tareaRepository.findAll().stream().filter(t -> t.getEstado() == Estado.COMPLETADA)
				.collect(Collectors.toList());
	}

	// Obtener las tareas ordenadas por fecha de vencimiento.
	public List<Tarea> ordenarPorFechaVencimientoFuncional() {
		return this.tareaRepository.findAll().stream()
				.sorted((t1, t2) -> t1.getFechaVencimiento().compareTo(t2.getFechaVencimiento()))
				.collect(Collectors.toList());
	}

	// Con repository
	public List<Tarea> ordenarPorFechaVencimiento() {
		return this.tareaRepository.findAllByOrderByFechaVencimiento();
	}

	// Obtener tareas mediante su título (que contenga el String que se pasa como
	// título).
	public List<Tarea> obtenerTareasPorTitulo(String title) {
		return tareaRepository.findByTituloContainingIgnoreCase(title);
	}

	// Completar una tarea (solo se puden completar tareas EN_PROGRESO).
	public Tarea completarTarea(int idTarea, Tarea tarea) {
		if (idTarea != tarea.getId()) {
			throw new TareaExceptions( "El ID del Path (" + idTarea + ") y el body (" + tarea.getId() + ") no coinciden");
		}
		if (!this.tareaRepository.existsById(idTarea)) {
			throw new TareaExceptions("No se ha encontrado la tarea con ID: " + idTarea);
		}
		Tarea tareaDB = this.tareaRepository.findById(idTarea)
				.orElseThrow(() -> new TareaExceptions("No se ha encontrado la tarea con ID: " + idTarea));
		if (tareaDB.getEstado() != Estado.EN_PROGRESO) {
			throw new TareaExceptions("La tarea no se puede completar, ya que su estado debe estar en progreso");
		}

		tareaDB.setEstado(Estado.COMPLETADA);

		return this.tareaRepository.save(tareaDB);
	}

}
