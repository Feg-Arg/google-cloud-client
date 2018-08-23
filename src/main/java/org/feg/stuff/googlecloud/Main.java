package org.feg.stuff.googlecloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args){

        if (args.length != 2) {
            LOG.error("Argumentos incorrectos.");
            LOG.error("indicar fichero con propiedades de conexion seguido del comando a ejecutar.");
            LOG.error("Ej: application.properties -si");
            LOG.error("\n\n Available Commands:\n");
            LOG.error("\t-si: stop instance, detiene las instancias que fueron informadas en el " +
                    "archivo properties, bajo la clave \"project.instance\"");

            System.exit(-1);
        }

        LOG.info("Instanciando cliente de GoogleCloud");

        List<String> responses = new ArrayList<>();
        try {
            GoogleClient gc = new GoogleClient(args[0]);

            if ("-stopi".equals(args[1])){
                responses = gc.stopInstances();
            } else if ("-starti".equals(args[1])){
                responses = gc.startInstances();
            }

            LOG.info("Proceso finalizado: ");

            for (String response : responses){
                LOG.info("\t- " + response);
            }

        } catch (IOException e) {
            LOG.error("Error: " + e.getMessage());
        } catch (GeneralSecurityException e) {
            LOG.error("Error: " + e.getMessage());
        }
    }
}
