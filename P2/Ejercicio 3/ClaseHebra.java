import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClaseHebra extends Thread
{
	private ProcesadorYodafy procesador;

	ClaseHebra(Socket conexion)
	{
 		procesador=new ProcesadorYodafy(conexion);
 	}

 // El contenido de este método se ejecutará tras llamar al
 // método "start()". Se trata del procesamiento de la hebra.
	public void run()
	{
		procesador.procesa();
	}
}
