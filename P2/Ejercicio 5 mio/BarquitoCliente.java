//
// YodafyServidorIterativo
// (CC) jjramos, 2012
//
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.*;

public class BarquitoCliente {

	private static int hundidos;
	private static ArrayList<ArrayList<String>> tablero;

	public BarquitoCliente()
	{
		tablero = new ArrayList<>();
		hundidos = 0;

		for(int i=0; i<10; i++)
			tablero.add(new ArrayList<String>());

		for(int i=0; i<10; i++)
			for(int j=0; j<10; j++)
				tablero.get(i).add("a");

		tablero.get(8).set(0, "b");
		tablero.get(8).set(1, "b");
		tablero.get(1).set(1, "b");
		tablero.get(5).set(2, "b");
		tablero.get(9).set(2, "b");
		tablero.get(9).set(3, "b");
		tablero.get(9).set(4, "b");
		tablero.get(9).set(5, "b");
		tablero.get(7).set(5, "b");
		tablero.get(1).set(5, "b");
		tablero.get(2).set(5, "b");
		tablero.get(3).set(5, "b");
		tablero.get(3).set(7, "b");
		tablero.get(4).set(7, "b");
		tablero.get(8).set(7, "b");
		tablero.get(8).set(8, "b");
		tablero.get(1).set(9, "b");
		tablero.get(5).set(9, "b");
		tablero.get(6).set(9, "b");
		tablero.get(7).set(9, "b");

		pintarTablero();
	}

	public static void main(String[] args) {

		// Nombre del host donde se ejecuta el servidor:
		String host="localhost";
		// Puerto en el que espera el servidor:
		int port=8989;
		String antigua_pos;
		BarquitoCliente barcos = new BarquitoCliente();

		// Socket para la conexión TCP
		Socket socketServicio=null;

		try {
			socketServicio = new Socket(host, port);

			procesa(socketServicio, barcos);

			socketServicio.close();

			// Excepciones:
		} catch (UnknownHostException e) {
			System.err.println("Error: Nombre de host no encontrado.");
		} catch (IOException e) {
			System.err.println("Error de entrada/salida al abrir el socket.");
		}
	}

	private static void procesa(Socket socketServicio, BarquitoCliente barcos){

		try {
			BufferedReader inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
			PrintWriter outPrinter = new PrintWriter(socketServicio.getOutputStream(), true);

			Scanner scan = new Scanner(System.in);
			boolean seguir = true;

			System.out.println("Introduce la coordenadas de la posición sin espacios. Ejemplo (posción (3, 7)): 37\nIntroduce 'me rindo' si no quieres seguir");
			String posicion = scan.nextLine();

			outPrinter.println(posicion);

			while(seguir)
			{
				if(!posicion.equals("me rindo") && hundidos < 20)
				{
					// Extraemos la posición del adversario
					System.out.println("Posicion recibida del adversario: ");
					String pos_adv = new String(inReader.readLine());

					if(pos_adv.charAt(3) == 'x')
						hundidos++;

					// Enviamos dicha posición
					String respuesta = barquitosDo(pos_adv);

					respuesta =  posicion + " " + respuesta;

					outPrinter.println(respuesta);

					barcos.pintarTablero();

					System.out.println("Introduce la coordenadas de la posición sin espacios. Ejemplo (posción (3, 7)): 37\nIntroduce 'me rindo' si no quieres seguir");
					posicion = scan.nextLine();
				}

				else if(hundidos < 20)
				{
					seguir = false;
					System.out.println("Elegiste rendirte. ¡Tu adversario ganó!");
				}

				else
				{
					seguir = false;
					System.out.println("¡Has ganado!");
				}
			}

		} catch (IOException e) {
			System.err.println("Error al obtener los flujos de entrada/salida.");
		}
	}

	private static String barquitosDo(String informacion)
	{
		int pos_x = informacion.charAt(0) - '0';
		int pos_y = informacion.charAt(1) - '0';

		return tablero.get(pos_x).get(pos_y);
	}

	private static void pintarTablero()
	{
		for(int i=0; i<10; i++)
		{
			for(int j=0; j<10; j++)
				System.out.print(tablero.get(i).get(j) + "  ");

			System.out.println("\n");
		}
	}
}
