public class Filosofo implements Runnable {
	
	private static final Object forks_mutex = new Object();/* Esse mutex ser� utilizado para
	controlar o uso da vari�vel "forks[]"*/
	public static final int N = 5;
	private static int forks[] = {-1,-1,-1,-1,-1}; /* Essa lista est�tica 
	ir� armazenar o inteiro correspondende a id do fil�sofo que cont�m cada garfo.
	Se o garfo estiver na mesa, o valor ser� -1.*/
	private int id;
	
	public Filosofo(int id){
		this.id = id;
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
	 * i: n�mero do garfo desejado.
	 * 
	 * Essa fun��o � uma regi�o cr�tica pois le e escreve valores da vari�vel est�tica "forks",
	 * portanto, apenas uma thread poder� execut�-la por vez.
	 * O Filos�fo tentar� adquirir o garfo i. Caso ele esteja dispon�vel, armazena o valor de
	 *  sua pr�pria id no �ndice do garfo e libera o mutex. Caso esteja com outro fil�sofo,
	 *  escolhe bloquear at� que seja acordada pela devolu��o de um garfo, liberando o mutex.  
	 * */
	public void Take_fork(int i){
		synchronized(forks_mutex){
		System.out.println("Filosofo " + this.id + " vai tentar pegar o garfo " + i);
			while(forks[i]>=0){
				try {
					System.out.println("Filosofo " + this.id + " vai dormir. Garfo com o Fil�sofo " + forks[i]);
					forks_mutex.wait();
					System.out.println("Filosofo " + this.id + " acordou");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			System.out.println("Filosofo " + this.id + " pegou o garfo " + i);
			forks[i]=this.id;
		}	
	}
	
	/*
	 * i: garfo a ser devolvido
	 * Essa fun��o � uma regi�o cr�tica pois escreve na vari�vel est�tica "forks",
	 * portanto, apenas uma thread poder� execut�-la por vez.
	 * Seta o valor do garfo devolvido para -1, acorda threads que estejam bloqueadas
	 * e ent�o libera o mutex.
	 * */
	public void Put_fork(int i){
		synchronized(forks_mutex){
		forks[i] = -1;
			System.out.println("Filosofo " + this.id + " vai devolver o garfo " + i);
			forks_mutex.notifyAll();
		}
	}
	
	
	/*
	 * O Fil�sofo repete a rotina de pensar, pegar separadamente os garfos, comer
	 * e devolver separadamente os garfos.
	 * */
	@Override
	public void run() {

		while(true){
			Think();
			Take_fork(this.id);
			Take_fork((this.id+1)%N);
			Eat();
			Put_fork(this.id);
			Put_fork((this.id+1)%N);
		}
		
	}
	
	

}
