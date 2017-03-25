public class Filosofo implements Runnable {
	
	//private final Object forks_mutex = new Object();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Eat(){
		System.out.println("Filosofo " + this.id + " esta comendo");
		try{
			Thread.sleep(1000);
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void Take_fork(int i){
		//(forks_mutex){
		System.out.println("Filosofo " + this.id + " vai tentar pegar o garfo " + i);
			while(forks[i]>0){
				try {
					System.out.println("Filosofo " + this.id + " vai dormir");
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			System.out.println("Filosofo " + this.id + " pegou o garfo " + i);
			forks[i]=this.id;
		//}	
	}
	
	public synchronized void Put_fork(int i){
		forks[i] = -1;
		System.out.println("Filosofo " + this.id + " vai devolver o garfo " + i);
		notifyAll();
	}
	
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
