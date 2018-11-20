import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Hebrita extends Thread
{
	private ProcesadorBarquito procesador;

	Hebrita(Socket conexion)
	{
 		procesador=new ProcesadorBarquito(conexion);
 	}

 // El contenido de este método se ejecutará tras llamar al
 // método "start()". Se trata del procesamiento de la hebra.
	public void run()
	{
		procesador.procesa();
	}
}
