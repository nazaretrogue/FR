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
import java.net.UnknownHostException;

public class YodafyClienteUDP {

	public static void main(String[] args) {

		// Puerto en el que espera el servidor:
		int port=8888;
		InetAddress direccion;
		DatagramPacket paquete;
		byte [] bufer = new byte[256];
		DatagramSocket socket;

		try {
			socket = new DatagramSocket();
			direccion = InetAddress.getByName("localhost");

			bufer = "Al monte del volcán debes ir sin demora".getBytes();

			paquete = new DatagramPacket(bufer, bufer.length, direccion, port);

			socket.send(paquete);

			paquete = new DatagramPacket(bufer, bufer.length);
			socket.receive(paquete);
			System.out.println("hola\n");
			paquete.getData();
			paquete.getAddress();
			paquete.getPort();

			// MOstremos la cadena de caracteres recibidos:
			System.out.println("Recibido: ");
			for(int i=0;i<bufer.length;i++){
				System.out.print((char)bufer[i]);
			}

			socket.close();

			// Excepciones:
		} catch (UnknownHostException e) {
			System.err.println("Error: Nombre de host no encontrado.");
		} catch (IOException e) {
			System.err.println("Error de entrada/salida al abrir el socket.");
		}
	}
}
