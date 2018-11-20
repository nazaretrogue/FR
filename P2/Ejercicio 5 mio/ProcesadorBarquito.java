//
// YodafyServidorIterativo
// (CC) jjramos, 2012
//
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.io.BufferedReader;

//
// Nota: si esta clase extendiera la clase Thread, y el procesamiento lo hiciera el método "run()",
// ¡Podríamos realizar un procesado concurrente!
//
public class ProcesadorBarquito
{
    // servidor
    private ArrayList<ArrayList<String>> servidor;
    private ArrayList<ArrayList<String>> adversario;
	private Socket socketServicio;

    private int hundidos_ser;
    private int hundidos_adv;
    private boolean fin;

    private String antigua_pos;

	// Para que la respuesta sea siempre diferente, usamos un generador de números aleatorios.
	private Random random;

	// Constructor que tiene como parámetro una ref. Rerencia al socket abierto en por otra clase
	public ProcesadorBarquito(Socket socketServicio)
    {
		this.socketServicio = socketServicio;
		servidor = new ArrayList<ArrayList<String>>();
        adversario = new ArrayList<ArrayList<String>>();
        random = new Random();
        antigua_pos = "";
        hundidos_ser = 0;
        hundidos_adv = 0;
        fin = false;

        for(int i=0; i<10; i++)
        {
            servidor.add(new ArrayList<String>());
            adversario.add(new ArrayList<String>());
        }

        for(int i=0; i<10; i++)
        {
			for(int j=0; j<10; j++)
            {
				servidor.get(i).add("a");
                adversario.get(i).add("a");
            }
        }

        for(int i=0; i<2; i++)
            for(int j=0; j<10; j++)
                servidor.get(i).set(j, "b");
	}

	// Aquí es donde se realiza el procesamiento realmente:
	void procesa(){

		try {
			BufferedReader inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
			PrintWriter outPrinter = new PrintWriter(socketServicio.getOutputStream(), true);

            while(hundidos_adv < 20 && hundidos_ser < 20 && !fin)
            {
                // Cogemos la posición
    			String posicion = new String(inReader.readLine());

                if(!posicion.equals("me rindo"))
                {
    			    // Enviamos dicha posición
        			String respuesta = barquitosDo(posicion);
                    String pos_enviar = calculaPosicion();

                    respuesta =  pos_enviar + " " + respuesta;

                    antigua_pos = posicion;

        			outPrinter.println(respuesta);

                    pintarTablero();
                }

                else
                    fin = true;
            }

            if(hundidos_ser == 20)
                System.out.println("¡Has ganado!");

            else if(hundidos_adv == 20)
                System.out.println("Has perdido :(");

		} catch (IOException e) {
			System.err.println("Error al obtener los flujos de entrada/salida.");
		}
	}

	private String barquitosDo(String informacion)
    {
		int pos_x = informacion.charAt(0) - '0';
        int pos_y = informacion.charAt(1) - '0';

		String casilla = servidor.get(pos_x).get(pos_y), resultado;

        // Actualizamos la matriz del servidor
        if(casilla.charAt(0) == 'b')
        {
            resultado = "x";
            hundidos_ser++;
        }

        else
            resultado = casilla;

        servidor.get(pos_x).set(pos_y, resultado);
        adversario.get(pos_x).set(pos_y, resultado);

        // Actualizamos la matriz del adversario
        if(!antigua_pos.equals(""))
        {
            pos_x = antigua_pos.charAt(0) - '0';
            pos_y = antigua_pos.charAt(1) - '0';

            adversario.get(pos_x).set(pos_y, Character.toString(informacion.charAt(3)));
        }

		return resultado;
	}

    private String calculaPosicion()
    {
        String posicion;

        int num = random.nextInt(10);
        posicion = Integer.toString(num);

        num = random.nextInt(10);
        posicion += Integer.toString(num);

        return posicion;
    }

    private void pintarTablero()
	{
		for(int i=0; i<10; i++)
		{
			for(int j=0; j<10; j++)
				System.out.print(adversario.get(i).get(j) + "  ");

			System.out.println("\n");
		}

        System.out.println("-------------------------------------------------");
	}
}
