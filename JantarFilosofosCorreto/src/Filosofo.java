import java.util.ArrayList;
import java.util.concurrent.Semaphore;


public class Filosofo implements Runnable {
	
	public static final int N = 5;
	private static final int THINKING = 0;
	private static final int HUNGRY = 1;
	private static final int EATING = 2;
	
	private static int state[] = {0,0,0,0,0};
	
	private final Object mutex = new Object();
	private static ArrayList s = new ArrayList<Semaphore>(); 
	
	private int id;
		
		public Filosofo(int id){
			this.id = id;
			s.add(new Semaphore(1));
			try {
				((Semaphore) s.get(id)).acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		
		public void take_forks(){
			synchronized(mutex){
				state[this.id]=HUNGRY;
				test(this.id);
			}
			try {
				((Semaphore) s.get(id)).acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void test(int id) {
			
			if(state[id] == HUNGRY && state[(id+N-1)%N]!=EATING && state[(id+1)%N]!=EATING ){
				state[id] = EATING;
				((Semaphore) s.get(id)).release();
			}
			
		}
		
		public void put_forks(){
			synchronized(mutex){
				state[this.id]=THINKING;
				test((this.id+N-1)%N);
				test((this.id+1)%N);
			}
		}

		@Override
		public void run() {
			while(true){
				Think();
				take_forks();
				take_forks();
				Eat();
				put_forks();
			}
		}
}
