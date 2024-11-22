package cliente;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * TODO: Complementa esta clase para que genere la conexi�n TCP con el servidor
 * para enviar un boleto, recibir la respuesta y finalizar la sesion
 */
public class ClienteTCP {
	// Socket
	private Socket client;
	// IO
	private BufferedReader inbound;
	private PrintStream outbound;

	/**
	 * Constructor
	 */
	public ClienteTCP(String ip, int puerto) {
		
	}

	/**
	 * @param combinacion que se desea enviar
	 * @return respuesta del servidor con la respuesta del boleto
	 */
	public String comprobarBoleto(int[] combinacion) {
		String respuesta = "Sin hacer";
		return respuesta;
	}

	/**
	 * Sirve para finalizar la la conexi�n de Cliente y Servidor
	 */
	public void finSesion() {

	}

}
