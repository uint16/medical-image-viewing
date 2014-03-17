package view;
import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class FileChooser extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFileChooser fc;

	/**
	 * Create the dialog.
	 */
	public FileChooser() {
		setLocationByPlatform(true);
		setTitle("Choose a file");
		setModal(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		fc = new JFileChooser();
		getContentPane().add(fc);
	}

	public String showFileChooser() {
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File chosen = fc.getSelectedFile();
			return chosen.toString();
		}
		return "";
	}

	public void setFilterFolder() {
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	}

	public void setFilterModification() {
		fc.setFileFilter(new FileFilter() {

			@Override
			public boolean accept(File file) {
				return file.isDirectory();
			}

			@Override
			public String getDescription() {
				return "Directory";
			}
		});
	}

}
