import java.util.ArrayDeque;
import java.util.Deque;


public class Invoker {
	private Deque<Undoable> stack = new ArrayDeque<Undoable>();
	
	public Invoker(){
		
	}

	public void add(Command c) {
		if(c instanceof Undoable){
			stack.push((Undoable) c);
		}
		c.execute();
	}
	
	public void undo(){
		Undoable lastCommand = stack.pop();
		lastCommand.undo();
	}
	
}
