package servicios;

import interfaces.InterfazContactoSim;
import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Entidad;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ServicioPractica implements InterfazContactoSim {

    private final List<DatosSolicitud> solicitudes = new ArrayList<>();
    private final List<Entidad> entidades = new ArrayList<>();
    private final Random random = new Random();

    public ServicioPractica() {
        Entidad e1 = new Entidad();
        e1.setId(1);
        e1.setName("Prueva1");
        e1.setDescripcion("Primer elemento de la lista");

        Entidad e2 = new Entidad();
        e2.setId(2);
        e2.setName("Prueba2");
        e2.setDescripcion("Segundo elemento de la lista");

        Entidad e3 = new Entidad();
        e3.setId(3);
        e3.setName("Prueba3");
        e3.setDescripcion("Tercer y ultimo elemento de la lista");


        entidades.add(e1);
        entidades.add(e2);
        entidades.add(e3);
    }

    @Override
    public int solicitarSimulation(DatosSolicitud sol) {
        solicitudes.add(sol);
        return random.nextInt(2000000);
    }

    @Override
    public DatosSimulation descargarDatos(int ticket) {
        return new DatosSimulation();
    }

    @Override
    public List<Entidad> getEntities() {
        return entidades;
    }

    @Override
    public boolean isValidEntityId(int id) {
        return false;
    }


    public boolean isValidEntityId() {
        return true;
    }
}
