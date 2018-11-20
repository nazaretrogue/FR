import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

// Batalla naval / Hundir la flota

public class BarquitosServidor {

   public static void main(String[] args) {

      int port=8989;
      ServerSocket socketServidor;

      try {
         socketServidor = new ServerSocket(port);

         // Mientras ... siempre!
         do
         {
            Socket socketConexion = null;

            try{
               socketConexion = socketServidor.accept();
            } catch(IOException e) {
               System.err.println("Error al intentar la conexion\n");
            }

            ProcesadorBarquitos procesador = new ProcesadorBarquitos(socketConexion);
            Hebrita h = new Hebrita(procesador);
            h.start();

         } while (true);

      } catch (IOException e) {
         System.err.println("Error al escuchar en el puerto "+port);
      }
   }
}
