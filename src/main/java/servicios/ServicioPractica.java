package servicios;

import com.tt1.trabajo.Servicios.ServicioConsumibleClient;

import interfaces.InterfazContactoSim;
import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Entidad;
import modelo.Punto;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServicioPractica implements InterfazContactoSim {

    private final ServicioConsumibleClient client;
    private final List<Entidad> entidades;

    public ServicioPractica(ServicioConsumibleClient client) {
        this.client = client;
        this.entidades = crearEntidades();
    }

    @Override
    public int solicitarSimulation(DatosSolicitud sol) {
        return client.solicitarSimulation(sol);
    }

    @Override
    public DatosSimulation descargarDatos(int ticket) {
        String respuesta = client.descargarResultados(ticket);
        return parsearRespuestaGrid(respuesta);
    }

    @Override
    public List<Entidad> getEntities() {
        return entidades;
    }

    @Override
    public boolean isValidEntityId(int id) {
        return entidades.stream().anyMatch(e -> e.getId() == id);
    }

    private List<Entidad> crearEntidades() {
        List<Entidad> lista = new ArrayList<>();

        Entidad e1 = new Entidad();
        e1.setId(1);
        e1.setName("Prueba1");
        e1.setDescripcion("Primer elemento de la lista");

        Entidad e2 = new Entidad();
        e2.setId(2);
        e2.setName("Prueba2");
        e2.setDescripcion("Segundo elemento de la lista");

        Entidad e3 = new Entidad();
        e3.setId(3);
        e3.setName("Prueba3");
        e3.setDescripcion("Tercer y último elemento de la lista");

        lista.add(e1);
        lista.add(e2);
        lista.add(e3);

        return lista;
    }

    private DatosSimulation parsearRespuestaGrid(String raw) {
        DatosSimulation ds = new DatosSimulation();
        Map<Integer, List<Punto>> puntosPorTiempo = new HashMap<>();

        ds.setAnchoTablero(0);
        ds.setMaxSegundos(1);
        ds.setPuntos(puntosPorTiempo);
        puntosPorTiempo.put(0, new ArrayList<>());

        if (raw == null || raw.isBlank()) {
            return ds;
        }

        String[] lineas = raw.strip().split("\\R+");

        try {
            ds.setAnchoTablero(Integer.parseInt(lineas[0].trim()));
        } catch (NumberFormatException e) {
            return ds;
        }

        int maxTiempo = -1;

        for (int i = 1; i < lineas.length; i++) {
            String linea = lineas[i].trim();
            if (linea.isEmpty()) {
                continue;
            }

            String[] partes = linea.split(",");
            if (partes.length != 4) {
                continue;
            }

            try {
                int tiempo = Integer.parseInt(partes[0].trim());
                int y = Integer.parseInt(partes[1].trim());
                int x = Integer.parseInt(partes[2].trim());
                String color = partes[3].trim();

                Punto p = new Punto();
                p.setY(y);
                p.setX(x);
                p.setColor(color);

                puntosPorTiempo.computeIfAbsent(tiempo, k -> new ArrayList<>()).add(p);
                maxTiempo = Math.max(maxTiempo, tiempo);

            } catch (NumberFormatException ignored) {
            }
        }

        if (maxTiempo >= 0) {
            for (int t = 0; t <= maxTiempo; t++) {
                puntosPorTiempo.putIfAbsent(t, new ArrayList<>());
            }
            ds.setMaxSegundos(maxTiempo + 1);
        }

        return ds;
    }
}