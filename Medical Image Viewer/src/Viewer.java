/**
 * @author Damas Mlabwa
 * Course: Engineering of Software SubSystems, SWEN262
 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Viewer extends JFrame implements Observer {

	private static final long serialVersionUID = 1L;
	// Navigation Buttons
	private JButton btNextImage, btPrevImage;
	// Label for Image currently displayed

	// Panels for layouts
	private JPanel mainPanel, navigationPanel;
	// Menubar and items
	private JMenu jmFile, jmView, jmHelp;
	private JMenuBar menubar;
	private JCheckBoxMenuItem cbQuadViewMode;
	private JMenuItem fileOpen, fileSave, fileCopy, fileExit;
	// Layouts for Images and Buttons
	private FlowLayout navigationAreaLayout = new FlowLayout();
	// Application container
	private Container container;

	private StudyController controller;

	private static final String TITLE = "Medical Image Viewer";
	private JCheckBoxMenuItem cbSingleViewMode;
	private ClickListener listener = new ClickListener();

	public Viewer() {
		super(TITLE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO: exit command? this code is copied from the exit menu
				// item
				if (!controller.curState.saved) {
					new UnsavedStatePrompt(controller.curState);
				} else {
					System.exit(0);
				}
			}
		});
		container = getContentPane();

		controller = new StudyController();
		controller.addObserver(this);

		initComponents();
		setMenu();
		buildUI();
		displayApplication();
	}

	/**
	 * Initialize GUI Components
	 */
	public void initComponents() {
		btNextImage = new JButton("Next");
		btNextImage.addActionListener(listener);

		btPrevImage = new JButton("Previous");
		btPrevImage.addActionListener(listener);

		jmFile = new JMenu("File");
		jmView = new JMenu("View");
		jmHelp = new JMenu("Help");
		menubar = new JMenuBar();
		mainPanel = new JPanel();
		navigationPanel = new JPanel();

		container.addMouseWheelListener(listener);

	}

	/**
	 * Add menu commands
	 */
	public void setMenu() {
		fileOpen = new JMenuItem("Open");
		fileOpen.addActionListener(listener);

		fileCopy = new JMenuItem("Copy");
		fileSave = new JMenuItem("Save");
		fileSave.addActionListener(listener);

		fileExit = new JMenuItem("Exit");
		fileExit.addActionListener(listener);

		/*
		 * Create checkbox for state
		 */
		cbQuadViewMode = new JCheckBoxMenuItem("Quad View");
		cbQuadViewMode.addActionListener(listener);

		cbSingleViewMode = new JCheckBoxMenuItem("Single View");
		cbSingleViewMode.addActionListener(listener);

		if (controller.curState.getMode() instanceof FourUp) {
			cbQuadViewMode.setState(true);
			cbSingleViewMode.setState(false);
		} else {
			cbSingleViewMode.setState(true);
			cbQuadViewMode.setState(false);
		}

		/*
		 * Add file menus
		 */
		jmFile.add(fileOpen);
		jmFile.add(fileCopy);
		jmFile.add(fileSave);
		jmFile.add(fileExit);

		/*
		 * Add view menu
		 */

		jmView.add(cbQuadViewMode);
		jmView.add(cbSingleViewMode);

		menubar.add(jmFile);
		menubar.add(jmView);
		menubar.add(jmHelp);
	}

	/**
	 * Setup the layout and all UI components
	 */
	public void buildUI() {
		// receive panel from the display state
		mainPanel = controller.generatePanel();

		// complete other UI elements
		navigationPanel.setLayout(navigationAreaLayout);
		navigationPanel.add(btPrevImage);
		navigationPanel.add(btNextImage);
		container.add(navigationPanel, BorderLayout.SOUTH);
		container.add(mainPanel, BorderLayout.CENTER);
	}

	/**
	 * Add accessibility text for menus and buttons
	 */
	public void setAccessibility() {
		btNextImage.getAccessibleContext().setAccessibleDescription(
				"Navigate to the next image in current study");
		btPrevImage.getAccessibleContext().setAccessibleDescription(
				"Navigate to the previous image in current study");
		jmFile.getAccessibleContext().setAccessibleDescription(
				"Menu for operations on files");
		jmFile.getAccessibleContext().setAccessibleDescription(
				"Menu for operation on view modes");
		jmHelp.getAccessibleContext().setAccessibleDescription(
				"Menu for getting help on using this application");
		fileOpen.getAccessibleContext().setAccessibleDescription(
				"Menu for opening a study");
		fileCopy.getAccessibleContext().setAccessibleDescription(
				"Menu for copying a study");
		fileSave.getAccessibleContext().setAccessibleDescription(
				"Menu for saving a study");
		fileExit.getAccessibleContext().setAccessibleDescription(
				"Menu for exiting application");
		cbQuadViewMode.getAccessibleContext().setAccessibleDescription(
				"Menu for enabling or disabling quad view");
	}

	/**
	 * Set application size,location, and close operation
	 */
	public void displayApplication() {
		this.setSize(640, 640);
		this.setLocation(100, 100);
		this.setJMenuBar(menubar);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Observer for the Viewer
	 */
	@Override
	public void update(Observable obs, Object obj) {

		// disable/enable buttons if first or last image
		if (!controller.curState.hasPrev()) {
			btPrevImage.setEnabled(false);
		} else {
			btPrevImage.setEnabled(true);
		}
		if (!controller.curState.hasNext()) {
			btNextImage.setEnabled(false);
		} else {
			btNextImage.setEnabled(true);
		}

		// replace mainPanel with new images
		container.remove(mainPanel);
		mainPanel = controller.generatePanel();
		container.add(mainPanel, BorderLayout.CENTER);
		container.validate();
	}

	/**
	 * 
	 * Handle all clicks from the view
	 * 
	 */
	class ClickListener implements ActionListener, KeyListener,
			MouseWheelListener {
		/**
		 * Get event and perform an action depending on clicked item
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getActionCommand().equals("Next")) {
				new NextCommand(controller.curState).execute();
			} else if (e.getActionCommand().equals("Previous")) {
				new PrevCommand(controller.curState).execute();
			} else if (e.getActionCommand().equals("Single View")) {
				new ChangeToOneUp(controller.curState).execute();
				cbQuadViewMode.setState(false);
			} else if (e.getActionCommand().equals("Quad View")) {
				new ChangeToFourUp(controller.curState).execute();
				cbSingleViewMode.setState(false);
			} else if (e.getActionCommand().equals("Exit")) {
				if (!controller.curState.saved) {
					new UnsavedStatePrompt(controller.curState);
				}
				System.exit(0);
			} else if (e.getActionCommand().equals("Save")) {
				new SaveCommand(controller.curState).execute();
			} else if (e.getActionCommand().equals("Open")) {
				new OpenCommand(controller).execute();
				// TODO complete switching to new study

			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (e.getWheelRotation() < 0) {
				new NextCommand(controller.curState).execute();
			} else {
				new PrevCommand(controller.curState).execute();
			}
			update(controller, mainPanel);

		}

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Viewer v = new Viewer();
				v.setVisible(true);
			}
		});
	}

}
