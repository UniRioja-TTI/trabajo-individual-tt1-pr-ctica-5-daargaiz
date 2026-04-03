package com.tt1.trabajo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import interfaces.InterfazContactoSim;
import modelo.DatosSolicitud;

@Controller
public class SolicitudController {

    private final InterfazContactoSim ics;
    private final Logger logger;

    public SolicitudController(InterfazContactoSim ics, Logger logger) {
        this.ics = ics;
        this.logger = logger;
    }

    @GetMapping("/solicitud")
    public String solicitud(Model model) {
        model.addAttribute("entities", ics.getEntities());
        return "solicitud";
    }

    @PostMapping("/solicitud")
    public String handleSolicitud(@RequestParam Map<String, String> formData, Model model) {
        Map<Integer, Integer> validData = new HashMap<>();
        List<String> errors = new ArrayList<>();

        formData.forEach((key, value) -> {
            int id;
            try {
                id = Integer.parseInt(key);
            } catch (NumberFormatException e) {
                errors.add("Identificador de entidad no válido: " + key);
                return;
            }

            int num = 0;
            if (value != null && !value.isBlank()) {
                try {
                    num = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    errors.add("El valor de " + key + " debe ser un número entero");
                    return;
                }
            }

            if (num < 0) {
                errors.add("El valor de " + key + " no puede ser negativo");
                return;
            }

            if (!ics.isValidEntityId(id)) {
                errors.add(key + " no se corresponde con una entidad");
                return;
            }

            validData.put(id, num);
        });

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            logger.warn("Atendida petición con errores: {}", errors);
            return "formResult";
        }

        DatosSolicitud ds = new DatosSolicitud(validData);
        int tok = ics.solicitarSimulation(ds);

        if (tok == -1) {
            model.addAttribute("errors", List.of("No se ha podido contactar con el servicio de simulación."));
            logger.error("Error en comunicación con servidor de simulación");
            return "formResult";
        }

        model.addAttribute("token", tok);
        logger.info("Atendida petición. Token generado: {}", tok);
        return "formResult";
    }
}