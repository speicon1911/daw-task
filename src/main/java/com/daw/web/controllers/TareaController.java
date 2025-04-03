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

	@GetMapping
	public ResponseEntity<List<Tarea>> list() {
		return ResponseEntity.status(HttpStatus.OK).body(this.tareaService.findAll());
	}

	@GetMapping("/{idTarea}")
	public ResponseEntity<?> findById(@PathVariable int idTarea) {
		try {
			return ResponseEntity.ok(this.tareaService.findById(idTarea));
		} catch (TareaNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}

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

	@PostMapping
	public ResponseEntity<Tarea> create(@RequestBody Tarea tarea) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.tareaService.create(tarea));
	}

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
}
