
public class Disparador {
	public static void main(String[] args){
		int i;
		System.out.println("INICIO");
		for(i=0;i<Filosofo.N;i++){
			Filosofo f = new Filosofo(i);
			Thread t = new Thread(f);
			System.out.println("Filosofo " + i + " vai iniciar");
			t.start();
		}
	}
}
