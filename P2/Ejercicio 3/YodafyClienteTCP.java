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
import java.net.Socket;
import java.net.UnknownHostException;

public class YodafyClienteTCP {

	public static void main(String[] args) {
		
		// Nombre del host donde se ejecuta el servidor:
		String host="localhost";
		// Puerto en el que espera el servidor:
		int port=8989;
		
		// Socket para la conexión TCP
		Socket socketServicio=null;

		int i=0;
		
		while(i<1000)
		{
			try {
				socketServicio = new Socket(host, port);
				
				BufferedReader inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
				PrintWriter outPrinter = new PrintWriter(socketServicio.getOutputStream(), true);
				
				// Si queremos enviar una cadena de caracteres por un OutputStream, hay que pasarla primero
				// a un array de bytes:

				outPrinter.println("Al monte del volcán debes ir sin demora");
				
				// Aunque le indiquemos a TCP que queremos enviar varios arrays de bytes, sólo
				// los enviará efectivamente cuando considere que tiene suficientes datos que enviar...
				
				// Mostremos la cadena de caracteres recibidos:
				System.out.println("Recibido: ");
				System.out.print(inReader.readLine());
				
				socketServicio.close();
				
				// Excepciones:
			} catch (UnknownHostException e) {
				System.err.println("Error: Nombre de host no encontrado.");
			} catch (IOException e) {
				System.err.println("Error de entrada/salida al abrir el socket.");
			}

			i++;
		}
	}
}
