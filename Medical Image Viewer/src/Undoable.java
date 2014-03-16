/**
 * This interface is used by commands which are undoable
 * 
 * @author Ethan Davidson (emd1771)
 *
 */
public interface Undoable {
	/**
	 * This method is called by the invoker when it removes the command from the stack
	 */
	public void undo();
}
