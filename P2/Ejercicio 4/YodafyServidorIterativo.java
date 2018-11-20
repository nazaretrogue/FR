import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

//
// YodafyServidorIterativo
// (CC) jjramos, 2012
//
public class YodafyServidorIterativo {

	public static void main(String[] args) {

		DatagramSocket socket_serv;
		// Puerto de escucha
		int port=8888;
		// array de bytes auxiliar para recibir o enviar datos.

		try {

			// Mientras ... siempre!
			do {
				socket_serv = new DatagramSocket(port);

				// Creamos un objeto de la clase ProcesadorYodafy, pasándole como
				// argumento el nuevo socket, para que realice el procesamiento
				// Este esquema permite que se puedan usar hebras más fácilmente.
				ProcesadorYodafy procesador=new ProcesadorYodafy(socket_serv);
				procesador.procesa();

				socket_serv.close();
			} while (true);



		} catch (IOException e) {
			System.err.println("Error al escuchar en el puerto "+port);
		}
	}

}
