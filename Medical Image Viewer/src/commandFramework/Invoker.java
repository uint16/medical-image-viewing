package commandFramework;
import java.util.ArrayDeque;
import java.util.Deque;



public class Invoker {
	private Deque<UndoableCommand> stack;
	
	public Invoker(){
		stack = new ArrayDeque<UndoableCommand>();
	}

	public void add(Command c) {
		if(c instanceof UndoableCommand){
			stack.push((UndoableCommand) c);
		}
		c.execute();
	}
	
	public void undo(){
		if(!stack.isEmpty()){
			UndoableCommand lastCommand = stack.pop();
			lastCommand.undo();
		}
	}
	
}
