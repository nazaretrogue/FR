import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

//
// YodafyServidorIterativo
// (CC) jjramos, 2012
//
public class YodafyServidorIterativo {

	public static void main(String[] args) {
	
		// Puerto de escucha
		int port=8989;
		// array de bytes auxiliar para recibir o enviar datos.
		byte []buffer=new byte[256];
		// Número de bytes leídos
		int bytesLeidos=0;
		// Socket del servidor
		ServerSocket socket_serv;
		
		try {
			socket_serv = new ServerSocket(port);
			
			// Mientras ... siempre!
			do 
			{
				Socket conexion = null;

				try{
					conexion = socket_serv.accept();
				} catch(IOException e) {
					System.err.println("Error al intentar la conexion\n");
				}

				ClaseHebra h=new ClaseHebra(conexion);
				h.start();	
			} while (true);
			
		} catch (IOException e) {
			System.err.println("Error al escuchar en el puerto "+port);
		}

	}

}
