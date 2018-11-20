import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.io.BufferedReader;


public class ProcesadorBarquitos
{
   // Tablero del juego
   private TableroBarquitos servidor;
   private TableroBarquitos cliente;
   // Random para calcular posiciones
   private Random random;
   // Socket
   private Socket socketServicio;

   // Constructor que tiene como parámetro una ref. Rerencia al socket abierto por otra clase
   public ProcesadorBarquitos(Socket socketServicio)
   {
      this.socketServicio = socketServicio;
      random = new Random();
      // LLenamos de barcos solo el tablero del servidor
      servidor = new TableroBarquitos(); servidor.addBarquitos(random);
      cliente = new TableroBarquitos();
   }

   // Aquí es donde se realiza el procesamiento realmente:
   void procesa(){
      String informacion, respuesta;
      boolean continuando = true;
      
      try {
         BufferedReader inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
         PrintWriter outPrinter = new PrintWriter(socketServicio.getOutputStream(), true);

         do
         {
            try {
               // Leemos el mensaje del cliente
               informacion = new String(inReader.readLine());
               System.out.println("Recibido: " + informacion);
               
               // Generamos la respuesta y la enviamos
               respuesta = barquitosDo(informacion);
               System.out.println("Enviado: " + respuesta);
               outPrinter.println(respuesta);
               
               // En caso de que el cliente cierre el programa antes de acabar
            } catch (NullPointerException e) {
               System.err.println("Se cerró la conexión.");
               continuando = false;
            }
            
            // Cuando se termina la partida el cliente cierra el socket
            if(socketServicio.isClosed()) continuando = false;
         } while(continuando);
         System.out.println("Fin partida. Thread: " + Thread.currentThread().getId());
      } catch (IOException e) {
         System.err.println("Error al obtener los flujos de entrada/salida.");
      }
   }

   private String barquitosDo(String informacion)
   {
      // ret = nueva_posicion + respuesta_posicion_recibida(char + posicion)
      String ret; int x, y; char i;

      // ejemplo contenido de informacion:
      // informacion ==  00a12.100
      
      i = informacion.charAt(2);
      x = Character.getNumericValue(informacion.charAt(3));
      y = Character.getNumericValue(informacion.charAt(4));
      // insertamos 'a' en cliente.tablero[1][2]
      cliente.setPos(x, y, i);

      x = Character.getNumericValue(informacion.charAt(0));
      y = Character.getNumericValue(informacion.charAt(1));
      // vemos que hay en servidor.tablero[0][0]
      i = servidor.getPos(x, y);

      // generamos la respuesta
      // primero calcula una nueva posición
      ret = calcularSiguientePosicion();
      // añadimos a la nueva posición la respuesta al cliente
      // ret = 75
      ret += String.valueOf(i) + String.valueOf(x) + String.valueOf(y) + ".200"; 
      // ret = 75a00.200
      return ret;
   }

   private String calcularSiguientePosicion()
   {
      String posicion;
      // Genera 2 números aleatorios entre 0 y 9
      posicion = String.valueOf(random.nextInt(TableroBarquitos.tamanio));
      posicion += String.valueOf(random.nextInt(TableroBarquitos.tamanio));
      
      // Si la posición ya se ha explorado antes (== 'a' || == 'b')
      while(cliente.at(Character.getNumericValue(posicion.charAt(0)), Character.getNumericValue(posicion.charAt(1))) != ' ')
      {
         // Genera una nueva posición
         posicion = String.valueOf(random.nextInt(TableroBarquitos.tamanio));
         posicion += String.valueOf(random.nextInt(TableroBarquitos.tamanio));
      }

      // Devuelve la nueva posición generada y sin explorar
      return posicion;
   }

}
