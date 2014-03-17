package commandFramework;
import java.util.ArrayDeque;
import java.util.Deque;



public class Invoker {
	private Deque<Undoable> stack;
	
	public Invoker(){
		stack = new ArrayDeque<Undoable>();
	}

	public void add(Command c) {
		if(c instanceof Undoable){
			stack.push((Undoable) c);
		}
		c.execute();
	}
	
	public void undo(){
		if(!stack.isEmpty()){
			Undoable lastCommand = stack.pop();
			lastCommand.undo();
		}
	}
	
}
