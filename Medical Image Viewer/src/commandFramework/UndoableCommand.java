package commandFramework;
/**
 * This interface is used by commands which are undoable
 * concrete commands that implement this interface should not be responsible for collecting user input
 * 
 * @author Ethan Davidson (emd1771)
 *
 */
public interface UndoableCommand extends Command{
	/**
	 * This method is called by the invoker when it removes the command from the stack
	 */
	public void undo();
}
