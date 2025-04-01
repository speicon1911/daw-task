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
import com.daw.services.exceptions.TareaNotFoundException;

@RestController
@RequestMapping("/tareas")
public class TareaController {

	@Autowired
	private TareaService tareaService;

	@GetMapping
	public List<Tarea> list() {
		return this.tareaService.findAll();
	}

	@GetMapping("/{idTarea}")
	public ResponseEntity<?> findById(@PathVariable int idTarea) {
		try {
			return ResponseEntity.ok(this.tareaService.findById(idTarea));
		} 
		catch (TareaNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}

	@DeleteMapping("/{idTarea}")
	public boolean delete(@PathVariable int idTarea) {
		return this.tareaService.deleteById(idTarea);
	}

	@PostMapping
	public Tarea create(@RequestBody Tarea tarea) {
		return this.tareaService.create(tarea);
	}

	@PutMapping("/{idTarea}")
	public Tarea update(@PathVariable int idTarea, @RequestBody Tarea tarea) {
		return this.tareaService.update(tarea);
	}
}
