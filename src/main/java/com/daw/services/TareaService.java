package com.daw.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daw.persistence.entities.Tarea;
import com.daw.persistence.entities.enums.Estado;
import com.daw.persistence.repositories.TareaRepository;
import com.daw.services.exceptions.TareaNotFoundException;

@Service
public class TareaService {

	@Autowired
	private TareaRepository tareaRepository;

	public List<Tarea> findAll() {
		return this.tareaRepository.findAll();
	}

	public Tarea findById(int idTarea) {
		if(!this.tareaRepository.existsById(idTarea)) {
			throw new TareaNotFoundException("El ID de la tarea no existe");
		}
		
		return this.tareaRepository.findById(idTarea).get();
	}

	public boolean existsById(int idTarea) {
		return this.tareaRepository.existsById(idTarea);
	}

	public boolean deleteById(int idTarea) {
		boolean result = false;

		if (this.tareaRepository.existsById(idTarea)) {
			this.tareaRepository.deleteById(idTarea);
			result = true;
		}

		return result;
	}

	public Tarea create(Tarea tarea) {
		tarea.setFechaCreacion(LocalDate.now());
		tarea.setEstado(Estado.PENDIENTE);

		Tarea tareaBD = this.tareaRepository.save(tarea);

		return tareaBD;
	}

	public Tarea update(Tarea tarea) {
		Tarea tareaDB = this.findById(tarea.getId());
		/*ESTO HACE QUE NO SE ACTUALICE EN CASO DE QUE LA MODIFIQUE
		 * ALGUIEN YA QUE LA RECOGE DE LA BASE DE DATOS*/		
		tarea.setFechaCreacion(tareaDB.getFechaCreacion());
		tarea.setEstado(tareaDB.getEstado());

		return this.tareaRepository.save(tarea);
	}
}
