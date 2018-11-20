import java.util.Random;
import java.util.Arrays;

// Clase de apoyo para los tableros del juego

public class TableroBarquitos
{
   public static final int tamanio = 10; 
   
   // Para simplificar hay 8 barcos, todos de tamaño 3
   // En total 21 casillas con barcos, con las reglas originales hay 20
   public static final int numBarquitos = 7;
   public static final int tamanioBarquitos = 3;
   
   private int numBarquitosRestantes; // Contador barcos no tocados
   private char[][] tablero; 

   TableroBarquitos()
   {
      this.numBarquitosRestantes = numBarquitos * tamanioBarquitos;
      inicializarTablero();
   }

   void inicializarTablero()
   {
      tablero = new char[tamanio][tamanio];
      for(int i = 0; i < tamanio; i++)
         for(int j = 0; j < tamanio; j++)
            // Se inicializa todo a "vacío"
            tablero[i][j] = ' ';
   }

   // Se usa en el juego para saber si es agua o un barco
   // Si es un barco reduce el contador numBarquitosRestantes
   char getPos(int x, int y)
   {
      assert(x < tamanio && x >= 0 && y < tamanio && y >= 0);
      char ret = tablero[x][y];
      if(ret == ' ') ret = 'a'; // Si esta "vacío" devuelve agua "a"
      else if(ret =='b') numBarquitosRestantes--;
      return ret;
   }

   // Se usa en el juego para modificar el tablero del contrario
   // así podemos visualizar que casillas hemos probado ya
   void setPos(int x, int y, char i)
   {
      assert(x < tamanio && x >= 0 && y < tamanio && y >= 0);
      // Si toca un barco reduce el contador
      if(i == 'b') numBarquitosRestantes--;
      tablero[x][y] = i;
   }
   
   // Consulta de una casilla
   char at(int x, int y)
   {
      assert(x < tamanio && x >= 0 && y < tamanio && y >= 0);
      return tablero[x][y];
   }

   // Consulta del contador de barcos
   int barquitosRestantes()
   {
      return numBarquitosRestantes;
   }

   // Añade barcos en posiciones y direcciones aleatorias
   void addBarquitos(Random random)
   {
      int a, b, direccion;
      for(int i = 0; i < numBarquitos; i++)
      {
         boolean valido;
         do
         {
            valido = true;
            // direccion == 0 Horizontal
            // direccion == 1 Vertical
            direccion = random.nextInt(2);
            a = random.nextInt(tamanio);
            b = random.nextInt(8);
            
            // Comprobamos que no se solape con otros barcos ya añadidos
            for(int k = 0; k < tamanioBarquitos; k++)
            {
               if(direccion == 0 && tablero[a][b+k] == 'b')
               {
                  valido = false;
                  break;
               }
               else if(direccion == 1 && tablero[b+k][a] == 'b')
               {
                  valido = false;  
                  break;
               }
            }
         } while(!valido);
         
         // Metemos el barco
         for(int k = 0; k < tamanioBarquitos; k++)
         {
            if(direccion == 0) tablero[a][b+k] = 'b';      // Horizontal
            else if(direccion == 1) tablero[b+k][a] = 'b'; // Vertical
         }
      }
   }
   
   // Muestra por pantalla this.tablero (jugador) junto a otro tablero c (servidor)
   void imprimirPartida(TableroBarquitos c)
   {
      // Números marcadores de las columnas
      System.out.print("  ");
      for(int j = 0; j < 2; j++)
      {
        for(int i = 0; i < tamanio; i++)
        {
          System.out.print(" " + i + " ");
        }
        System.out.print("\t");
      }
      System.out.print("\n");
      
      // Tableros
      for(int i = 0; i < tamanio; i++)
      {
         System.out.print(i + " " + Arrays.toString(tablero[i]));
         System.out.println("\t" + Arrays.toString(c.tablero[i]));
      }

   }
}
