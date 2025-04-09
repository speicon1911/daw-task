package com.daw.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.persistence.entities.Tarea;
import com.daw.services.TareaService;
import com.daw.services.exceptions.TareaExceptions;
import com.daw.services.exceptions.TareaNotFoundException;

@RestController
@RequestMapping("/tareas")
public class TareaController {

	@Autowired
	private TareaService tareaService;

	// Obtener todas las tareas.
	@GetMapping
	public ResponseEntity<List<Tarea>> list() {
		return ResponseEntity.status(HttpStatus.OK).body(this.tareaService.findAll());
	}

	// Obtener una tarea mediante su ID.
	@GetMapping("/{idTarea}")
	public ResponseEntity<?> findById(@PathVariable int idTarea) {
		try {
			return ResponseEntity.ok(this.tareaService.findById(idTarea));
		} catch (TareaNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}

	// Borrar una tarea.
	@DeleteMapping("/{idTarea}")
	public ResponseEntity<?> delete(@PathVariable int idTarea) {
		try {
			this.tareaService.deleteById(idTarea);
			return ResponseEntity.ok().build();
			// return ResponseEntity.ok("La tarea con ID(" + idTarea + ") ha sido borrada correctamente" )
		} catch (TareaNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}

	// Crear una tarea.
	@PostMapping
	public ResponseEntity<Tarea> create(@RequestBody Tarea tarea) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.tareaService.create(tarea));
	}
	
	// Modificar una tarea.
	@PutMapping("/{idTarea}")
	public ResponseEntity<?> update(@PathVariable int idTarea, @RequestBody Tarea tarea) {
		try {
			return ResponseEntity.ok(this.tareaService.update(idTarea, tarea));
		} catch (TareaNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());

		} catch (TareaExceptions ex) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
		}
	}
	
	// Obtener las tareas pendientes.
	@GetMapping("/tarea-pendiente")
	public ResponseEntity<List<String>> findByTareaPendiente(){
		return ResponseEntity.status(HttpStatus.OK).body(this.tareaService.tituloTareasPendientes());
	}
	
	// Obtener las tareas en progreso.
	@GetMapping("/tarea-en-progreso")
	public ResponseEntity<?> findByTareaEnProceso(){
		return ResponseEntity.status(HttpStatus.OK).body(this.tareaService.tareasEnProgresoFuncional());
	}
	
	// Obtener las tareas completadas.
	@GetMapping("/tareas-completadas")
	public ResponseEntity<?> findByTareaCompletada(){
		return ResponseEntity.status(HttpStatus.OK).body(this.tareaService.tareasCompletadas());
	}
	
	// Obtener las tareas vencidas (fecha de vencimiento menor que la de hoy).
	@GetMapping("/tareas-vencidas")
	public ResponseEntity<List<Tarea>> findByTareasVencidas(){
		return ResponseEntity.status(HttpStatus.OK).body(this.tareaService.tareasVencidas());
	}
	
	// Obtener las tareas no vencidas (fecha de vencimiento mayor que la de hoy).
	@GetMapping("/tareas-no-vencidas")
	public ResponseEntity<List<Tarea>> findByTareasNoVencidas(){
		return ResponseEntity.status(HttpStatus.OK).body(this.tareaService.tareasNoVencidas());
	}
	
	// Obtener tareas mediante su título (que contenga el String que se pasa como título).
	@GetMapping("/titulo")
	public ResponseEntity<List<String>> findByTitulo(){
		return ResponseEntity.status(HttpStatus.OK).body(this.tareaService.tituloTareaFuncional());
	}
	
	// Completar una tarea (solo se puden completar tareas EN_PROGRESO).
	@PutMapping("/completar")
	public ResponseEntity<?> completarTarea(@PathVariable int idTarea, @RequestBody Tarea tarea){
		try {
			return ResponseEntity.ok(this.tareaService.completarTarea(idTarea, tarea));
		} catch (TareaNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());

		} catch (TareaExceptions ex) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
		}
		
	}
	
}
