package es.jose.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Esta excepción se usa para indicar errores controlados.
 * 
 * @author raszagar
 */
public class MiException extends Exception {

	private static final long serialVersionUID = -4132635848988172954L;
	
	private static final Logger log = LogManager.getLogger(MiException.class);

	public MiException() {
		super();
	}

	public MiException(String message) {
		super(message);
		log.error("[" +  getClaseMetodoPadre(this) + "] " + message);
	}

	public MiException(String message, Throwable cause) {
		super(message, cause);
		log.error("[" +  getClaseMetodoPadre(this) + "] " + message);
		log.error("[" +  getClaseMetodoPadre(cause) + "] " 
				+ cause.getClass().getName() + ": " + cause.getMessage());
	}

	public MiException(Throwable cause) {
		super(cause);
		log.error("[" +  getClaseMetodoPadre(cause) + "] " 
				+ cause.getClass().getName() + ": " + cause.getMessage());
	}
	
	private String getClaseMetodoPadre(Throwable t){
		try {
			StackTraceElement[] elements = t.getStackTrace();
			String clase = elements[0].getClassName().substring(elements[0].getClassName().lastIndexOf('.') + 1);
			String metodo = elements[0].getMethodName();
			int linea = elements[0].getLineNumber();
			
			if(!this.equals(t)) {
				//Si es una causa nos interesa la linea de la clase que lanza la excepcion
				StackTraceElement[] elementsThis = this.getStackTrace();
				String claseThis = elementsThis[0].getClassName().substring(elementsThis[0].getClassName().lastIndexOf('.') + 1);
				for (StackTraceElement stackTraceElement : elements) {
					String claseElement = stackTraceElement.getClassName().substring(stackTraceElement.getClassName().lastIndexOf('.') + 1);
					if(claseThis.equals(claseElement)) {
						clase = claseElement;
						metodo = stackTraceElement.getMethodName();
						linea = stackTraceElement.getLineNumber();
						break;
					}
				}
			}
			
			return clase + "." + metodo + ":" + linea;
		} catch (Exception e) {
			return "";
		}
	}
}
