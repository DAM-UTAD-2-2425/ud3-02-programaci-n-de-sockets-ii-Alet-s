package cliente;

/** @author Alejandro Serrano, Pedro Camacho */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
	private PrintWriter outbound;

	/**
	 * Constructor con parámetros
	 */
	public ClienteTCP(String ip, int puerto) {
		this.client = null;
		this.inbound = null;
		this.outbound = null;

		try {
			client = new Socket(ip, puerto);
			System.out.println("Conexión establecida: " + client);
			inbound = new BufferedReader(new InputStreamReader(client.getInputStream()));
			outbound = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
		} catch (IOException e) {
			System.err.printf("Imposible conectar con ip:%s / puerto:%d", ip, puerto);
			System.exit(-1);
		}
	}

	/**
	 * Procesa el boleto del cliente
	 * 
	 * @param combinacion que se desea enviar
	 * @return respuesta del servidor con la respuesta del boleto
	 */
	public String comprobarBoleto(int[] combinacion) {
		StringBuilder sb = new StringBuilder();// Sbuilder
		for (int num : combinacion) {// Foreach para recorrer el array introducido
			sb.append(num).append(" ");// Append para no sobrescribir lo que ya teníamos
		}
		String boleto = sb.toString().trim();// Convertimos a string y eliminamos los espacios en blanco
		String respuesta = "";
		try {
			outbound.println(boleto);
			respuesta = inbound.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return respuesta;
	}

	/**
	 * Sirve para finalizar la la conexi�n de Cliente y Servidor
	 */
	public void finSesion() {
		try {
			outbound.println("FIN");// Mandamos la señal al servidor
			outbound.close();
			inbound.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("-> Servidor Terminado");

	}

}
