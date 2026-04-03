package com.tt1.trabajo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import interfaces.InterfazContactoSim;
import modelo.DatosSimulation;
import modelo.Punto;

@Controller
public class GridController {

	private final InterfazContactoSim ics;
	private final Logger logger;

	public GridController(InterfazContactoSim ics, Logger logger) {
		this.ics = ics;
		this.logger = logger;
	}

	@GetMapping("/grid")
	public String grid(@RequestParam int tok, Model model) {
		logger.info("Mostrando grid para tok={}", tok);

		DatosSimulation ds = ics.descargarDatos(tok);

		model.addAttribute("count", ds.getAnchoTablero());
		model.addAttribute("maxTime", Math.max(ds.getMaxSegundos() - 1, 0));

		Map<String, String> colors = new HashMap<>();

		if (ds.getPuntos() != null) {
			for (int t = 0; t < ds.getMaxSegundos(); t++) {
				for (Punto p : ds.getPuntos().getOrDefault(t, List.of())) {
					colors.put(t + "-" + p.getY() + "-" + p.getX(), p.getColor());
				}
			}
		}

		model.addAttribute("colors", colors);
		return "grid";
	}
}