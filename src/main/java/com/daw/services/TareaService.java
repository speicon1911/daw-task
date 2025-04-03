package com.daw.services;

import java.time.LocalDate;
import java.util.List;

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
}
