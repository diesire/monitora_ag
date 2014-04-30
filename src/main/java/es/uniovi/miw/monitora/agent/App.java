package es.uniovi.miw.monitora.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 * 
 */
public class App {
	static Logger logger = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) {
	    logger.debug("Logging enabled");
		System.out.println("Hello world!");
		
		/**
		 * Crear el cliente (servicio, scheduler, encriptación, persistencia)
		 * start, hacer, stop
		 * hacer = 	ping 30 minutos
		 * 			leer order + encolar + ejecutar + leer resultados
		 *  		enviar periodicamente
		 * */
	}
}
