package servicios;

import interfaces.InterfazEnviarEmails;
import modelo.Destinatario;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class EnviarEmailsPractica implements InterfazEnviarEmails {

    private final Logger logger;

    public EnviarEmailsPractica(Logger logger) {
        this.logger = logger;
    }

    @Override
    public boolean enviarEmail(Destinatario dest, String email) {
        String string1= "Enviando email ";
        logger.info(string1);
        return true;
    }
}

