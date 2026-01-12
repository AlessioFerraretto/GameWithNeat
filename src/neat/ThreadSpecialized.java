package neat;

public class ThreadSpecialized<T> extends Thread {

	T t;
	RunnableTyped<T> task;

	public ThreadSpecialized(T t, RunnableTyped<T> task) {
		this.t = t;
		this.task = task;
	}

	public void run() {
		task.run(t);
		
	}
}

