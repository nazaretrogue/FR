import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashSet;

public class BarquitosCliente {

   public static void main(String[] args) {

      String buferEnvio;
      String buferRecepcion;
      String respuesta = " 00";
      Jugador jugador = new Jugador();


      // Nombre del host donde se ejecuta el servidor:
      String host="localhost";
      // Puerto en el que espera el servidor:
      int port=8989;

      // Socket para la conexión TCP
      Socket socketServicio=null;

      try {
         socketServicio = new Socket(host, port);
         PrintWriter outPrinter = new PrintWriter(socketServicio.getOutputStream(), true);
         BufferedReader inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
         

         do {
            System.out.print("\033[H\033[2J"); System.out.flush();  
            jugador.imprimirPartida();
           
            // El usuario introduce una posición
            buferEnvio = jugador.introducirDatos();
            
            if(buferEnvio.equals("r")) { System.out.println("Te has rendido"); break; }
            
            // Añadimos la respuesta del servidor y enviamos
            buferEnvio += respuesta + ".100";
            System.out.println("Enviado: " + buferEnvio);
            outPrinter.println(buferEnvio);

            // Leemos la nueva posición del servidor + nuestra respuesta
            buferRecepcion = inReader.readLine();
            System.out.println("Recibido: " + buferRecepcion);

            // Actualizamos la respuesta
            respuesta = jugador.analizarInformacion(buferRecepcion);
         } while(jugador.continuarPartida());

         socketServicio.close();

         // Excepciones:
      } catch (UnknownHostException e) {
         System.err.println("Error: Nombre de host no encontrado.");
      } catch (IOException e) {
         System.err.println("Error de entrada/salida al abrir el socket.");
      }
   }
}

// Clase auxiliar para el manejo del juego
class Jugador {
      // Para leer la entrada
      private Scanner scanner;
      
      // Variables para comprobar la entrada
      private Pattern r;
      private Matcher m;
      
      // Set para comprobar que no se repiten posiciones
      private HashSet<String> yaEnviados;

      // Tableros propio (jugador) y adversario (servidor)
      TableroBarquitos servidor;
      TableroBarquitos jugador;
      
      Jugador()
      {
         scanner = new Scanner(System.in);
         // La entrada solo puede ser 00..99 ó r
         r = Pattern.compile("\\b[0-9][0-9]\\b|\\br\\b");
         yaEnviados = new HashSet<String>();
         servidor = new TableroBarquitos();
         // Añadimos barcos solo en el tablero propio (jugador)
         jugador = new TableroBarquitos(); jugador.addBarquitos(new Random());
      }
      
      String introducirDatos()
      {
         String entrada;
         do
         {
            System.out.println("Introduce una posición (3, 7) == 37, r == rendirse: ");
            entrada = scanner.nextLine();
            m = r.matcher(entrada);
         } while(!m.find() || !yaEnviados.add(entrada));
         return entrada;
      }
      
      String analizarInformacion(String informacion)
      {
            int x, y; char i; String ret;
            
            // ejemplo contenido de informacion:
            // informacion ==  00a12.200
            
            
            i = informacion.charAt(2);
            x = Character.getNumericValue(informacion.charAt(3));
            y = Character.getNumericValue(informacion.charAt(4));
            // insertamos 'a' en servidor.tablero[1][2]
            servidor.setPos(x, y, i);
            
            x = Character.getNumericValue(informacion.charAt(0));
            y = Character.getNumericValue(informacion.charAt(1));
            // vemos que hay en jugador.tablero[0][0]
            i = jugador.getPos(x, y);
            
            // Generamos la respueta
            ret = String.valueOf(i) + String.valueOf(x) + String.valueOf(y);
            
            // Actualizamos el tablero propio (jugador) para saber
            // donde ha ido a explorar el adversario
            if(i == 'b') jugador.setPos(x, y, 'X'); // Barco tocado
            else jugador.setPos(x, y, '#');         // Agua
            
            return ret;
      }
      
      // Comprueba si se ha acabo la partida
      boolean continuarPartida()
      {
         if(jugador.barquitosRestantes() == 0){ System.out.println("Has perdido"); return false; }
         else if (servidor.barquitosRestantes() == 0){ System.out.println("Has ganado"); return false; }
         return true;
      }
      
      void imprimirPartida()
      {
         jugador.imprimirPartida(servidor);
      }
}
