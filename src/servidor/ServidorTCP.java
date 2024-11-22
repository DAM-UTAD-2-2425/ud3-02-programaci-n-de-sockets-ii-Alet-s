package servidor;

/** @author Alejandro Serrano, Pedro Camacho */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * TODO: Complementa esta clase para que acepte conexiones TCP con clientes para
 * recibir un boleto, generar la respuesta y finalizar la sesion
 */
public class ServidorTCP {
	private String[] respuesta;
	private int[] combinacion;
	private int reintegro;
	private int complementario;
	// SOckets
	private ServerSocket server;
	private Socket client;
	// I/o
	private BufferedReader inbound;
	private PrintWriter outBound;

	/**
	 * Constructor con parámetro
	 */
	public ServidorTCP(int puerto) {
		this.respuesta = new String[9];
		this.respuesta[0] = "Boleto inv�lido - N�meros repetidos";
		this.respuesta[1] = "Boleto inv�lido - n�meros incorrectos (1-49)";
		this.respuesta[2] = "6 aciertos";
		this.respuesta[3] = "5 aciertos + complementario";
		this.respuesta[4] = "5 aciertos";
		this.respuesta[5] = "4 aciertos";
		this.respuesta[6] = "3 aciertos";
		this.respuesta[7] = "Reintegro";
		this.respuesta[8] = "Sin premio";
		generarCombinacion();
		imprimirCombinacion();
		// Inicialización sockets y I/O
		this.client = null;
		this.server = null;
		this.inbound = null;
		this.outBound = null;
		try {
			server = new ServerSocket(puerto);
			System.out.println("Esperando conexión...");
			client = server.accept();
			System.out.println("Conexión acceptada: " + client);
			inbound = new BufferedReader(new InputStreamReader(client.getInputStream()));
			outBound = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
		} catch (IOException e) {
			System.out.println("No puede escuchar en el puerto: " + puerto);
			System.exit(-1);
		}
	}

	/**
	 * Procesa la combinación de números recibida desde el cliente
	 * 
	 * @return linea combinación de números que le envia el cliente
	 */
	public String leerCombinacion() {
		String linea = "";
		try {
			linea = inbound.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return linea;
	}

	/**
	 * Comprueba si la combinación enviada por el cliente es ganadora o no,
	 * comparándola con la combinación ganadora en servidor.
	 * 
	 * @param boletoClient la combinación de numeros ene l billete del cliente
	 * @return res una de las posibles respuestas configuradas
	 */
	public String comprobarBoleto(String boletoClient) {
		int aciertos = 0;// nº de aciertos del billete del cliente
		String[] clientCombi = boletoClient.split(" ");// Separamos por espacios en blanco y lo asignamos al
														// array(.split devuelve un array)

		String res = "";// Respuesta a dar
		boolean hasComplementario = false;// Flag para comprobar el nº complementario.
		// Comprobamos el nº de aciertos totales
		for (String num : clientCombi) {// Recorremos la combinacion de cliente
			int numberClient = Integer.parseInt(num);// Pasamos el String a int
			
			for (int winningNum : combinacion) {// Bucle anidado para comparar el billete con el nº premiado
				if (numberClient == winningNum) {
					aciertos++;// Sumamos cada acierto
					if (numberClient == complementario) {// Si algun nº del boleto coincide con el complementario:
						hasComplementario = true;
					}
				}
			}
		}

		// Foreach para comprobar si algun numero es menor a 1 o mayor de 49
		for (String num : clientCombi) {
			int number = Integer.parseInt(num);
			if (number < 1 || number > 49) {
				res = respuesta[1];// Respuesta nº inválidos
			}
		}
		// Si la algún nº tiene repetidos
		// Treeset: asignamos el array al mismo ya que no admite duplicados
		TreeSet<Integer> numsBoleto = new TreeSet<Integer>();
		boolean hasRepetido = false;
		for (String num : clientCombi) {
			int n = Integer.parseInt(num);
			hasRepetido = numsBoleto.add(n);// Asignamos lo que devuelva add
		}
		if (hasRepetido) {
			res = respuesta[0];
		}

		// 6 aciertos
		if (aciertos == 3) {
			res = respuesta[6];
		}
		// 4 aciertos
		if (aciertos == 4) {
			res = respuesta[5];
		}
		// 5 aciertos
		if (aciertos == 5) {
			res = respuesta[4];
		}
		// 6 aciertos
		if (aciertos == 6) {
			res = respuesta[2];
		}
		// reintegro
		for (String string : clientCombi) {
			int num = Integer.parseInt(string);
			if (num == reintegro) {
				res = respuesta[7];
			}
		}
		//5 aciertos y complementario
		if (aciertos == 5 && hasComplementario) {
			res = respuesta[3];
		}
		if (aciertos == 0 && !hasComplementario) {
			res = respuesta[8];
		}

		return res;

	}

	/**
	 * @param respuesta se debe enviar al ciente
	 */
	public void enviarRespuesta(String respuesta) {
		outBound.println(respuesta);
	}

	/**
	 * Cierra el servidor
	 */
	public void finSesion() {
		try {
			outBound.close();
			inbound.close();
			client.close();
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("-> Servidor Terminado");

	}

	/**
	 * Metodo que genera una combinacion. NO MODIFICAR
	 */
	private void generarCombinacion() {
		Set<Integer> numeros = new TreeSet<Integer>();
		Random aleatorio = new Random();
		while (numeros.size() < 6) {
			numeros.add(aleatorio.nextInt(49) + 1);
		}
		int i = 0;
		this.combinacion = new int[6];
		for (Integer elto : numeros) {
			this.combinacion[i++] = elto;
		}
		this.reintegro = aleatorio.nextInt(49) + 1;
		this.complementario = aleatorio.nextInt(49) + 1;
	}

	/**
	 * Metodo que saca por consola del servidor la combinacion
	 */
	private void imprimirCombinacion() {
		System.out.print("Combinaci�n ganadora: ");
		for (Integer elto : this.combinacion)
			System.out.print(elto + " ");
		System.out.println("");
		System.out.println("Complementario:       " + this.complementario);
		System.out.println("Reintegro:            " + this.reintegro);
	}

}
