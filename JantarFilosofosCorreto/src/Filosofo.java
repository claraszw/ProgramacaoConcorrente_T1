import java.util.ArrayList;
import java.util.concurrent.Semaphore;


public class Filosofo implements Runnable {
	
	public static final int N = 5;
	private static final int THINKING = 0;
	private static final int HUNGRY = 1;
	private static final int EATING = 2;
	
	private static int state[] = {0,0,0,0,0};
	
	private final Object mutex = new Object();
	private static ArrayList<Semaphore> s = new ArrayList<>(); 
	
	private int id;
		
		public Filosofo(int id){
			this.id = id;
			s.add(new Semaphore(0));

		}
		
		public void Think(){
			System.out.println("Filosofo " + this.id + " esta pensando");
			try{
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		public void Eat(){
			System.out.println("Filosofo " + this.id + " esta comendo");
			try{
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		/*
		 * Em uma regi�o cr�tica seta o estado do Fil�sofo para faminto e ent�o realiza
		 * um teste para tentar come�ar a comer. Por �ltimo tenta passar por um sem�foro,
		 * isso ocorrer� somente se o Fil�sofo tiver adquirido os garfos, caso contr�rio
		 * ficar� bloqueado at� que ele esteja comendo.
		 * */
		public void take_forks(){
			synchronized(mutex){
				state[this.id]=HUNGRY;
				test(this.id);
			}
			try {
				((Semaphore) s.get(id)).acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		/*
		 * id: id do Fil�sofo a ser testado
		 * 
		 * Testa se o Fil�sofo em quest�o deveria come�ar a comer, verificando se ele est� com fome
		 * e se ambos os vizinhos n�o est�o comendo, deixando os garfos necess�rios dispon�veis.
		 * Se os garfos estiverem dispon�veis, o estado � setado para comendo e o sem�foro liberado
		 * para sinalizar que os garfos foram adquiridos com sucesso.
		 * */
		private void test(int id) {
			
			if(state[id] == HUNGRY && state[(id+N-1)%N]!=EATING && state[(id+1)%N]!=EATING ){
				state[id] = EATING;
				((Semaphore) s.get(id)).release();
			}
			
		}
		
		/*
		 * Seta o estado do Fil�sofo para pensando e verifica se essa mudan�a faz com que algum
		 * de seus vizinhos comece a comer.
		 * */
		public void put_forks(){
			synchronized(mutex){
				state[this.id]=THINKING;
				test((this.id+N-1)%N);
				test((this.id+1)%N);
			}
		}

		/*
		 * O Fil�sofo repete a rotina de pensar, pegar os dois garfos ao mesmo tempo, comer
		 * e devolver os dois garfos ao mesmo tempo.
		 * */
		@Override
		public void run() {
			while(true){
				Think();
				take_forks();
				Eat();
				put_forks();
			}
		}
}
