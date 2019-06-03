import java.util.*;

public class DiningPhil {

		Philosopher[] philosophers;
		Fork[] forks;
		Thread[] threads;
		
		Scanner scan;
		int number = 5;
		
		public static void main(String args[]) {
			DiningPhil obj = new DiningPhil();
			obj.init();
			obj.startThinkingEating();
		}
		
		public void init() {
			System.out.println("DIning Philosopher Programme");
			
			philosophers = new Philosopher[number];
			forks = new Fork[number];
			threads = new Thread[number];
			
			for(int i = 0; i < number; i++) {
				philosophers[i] = new  Philosopher(i+1);
				forks[i] = new Fork(i+1);
				
			}
		}
		
		public void startThinkingEating() {
		
			for(int i = 0; i < number; i++ ) {
				
				final int index = i;
				
				threads[i] = new Thread(new Runnable() {
					
					public void run() {
						
						try {
							
							philosophers[index].start(forks[index], (index - 1 > 0)? forks[index - 1]: forks[number - 1]);
							
						}catch(InterruptedException e) {
							
							e.printStackTrace();
						}
					}
				});
				
				threads[i].start();
			}
		}

		public class Fork{
			private int forkId;
			private boolean status;
			
			Fork(int forkId){
				this.forkId = forkId;
				this.status = true;
			}
			
			public synchronized void free() throws InterruptedException{
				status = true;
			}
			
			public synchronized boolean pick(int philosopherId) throws InterruptedException{
				int counter = 0;
				int waitUntil = new Random().nextInt(10) +5;
				
				while(!status) {
					Thread.sleep(new Random().nextInt(100) + 50);
					counter++;
					
					if (counter > waitUntil) {
						return false;
					}
				}
				
				status = false;
				return true;
			}
		}
		
		public class Philosopher{
		
			private int PhilosopherId;
			private Fork left,right;
			
			public Philosopher(int PhilosopherId) {
				this.PhilosopherId = PhilosopherId;
			}
			
			public void start(Fork left,Fork right)throws InterruptedException {
				this.left = left;
				this.right = right;
				
				while(true) {
					if(new Random().nextBoolean()) {
						eat();
					}else {
						think();
					}
				}
			}
			
			public void think() throws InterruptedException{
				System.out.println("The Philosopher: "+ PhilosopherId + "is now thinking.");
				Thread.sleep(new Random().nextInt(1000) + 100);
				System.out.println("The Philosopher: " + PhilosopherId + " has stopped thinking.");
				
			}
			
			public void eat() throws InterruptedException{
				boolean rightPick = false;
				boolean leftPick = false;
				
				System.out.println("The Philosopher: " + PhilosopherId + " is now hungry and wants to eat.");
				System.out.println("The Philosopher: " + PhilosopherId + " is now picking up the fork: " + left.forkId);
				leftPick = left.pick(PhilosopherId);
				
				if(!leftPick) {
					return;
				}
				
				System.out.println("The Philosopher: " + PhilosopherId + "is now picking up the Fork: " + right.forkId);
				rightPick = right.pick(PhilosopherId);
				
				if(!rightPick) {
					left.free();
					right.free();
					
				System.out.println("The Philosopher: " + PhilosopherId + " has stopped eating and freed the Forks.");	
				
				}
			}
		}
}