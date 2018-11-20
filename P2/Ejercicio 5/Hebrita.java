public class Hebrita extends Thread
{
   private ProcesadorBarquitos procesador;

   Hebrita(ProcesadorBarquitos p)
   {
     this.procesador = p;
   }

   public void run()
   {
      procesador.procesa();
   }
}
