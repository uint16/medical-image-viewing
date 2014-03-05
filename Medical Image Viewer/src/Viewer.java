/**
 * @author Damas Mlabwa
 * Course: Engineering of Software SubSystems, SWEN262
 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

public class Viewer extends JFrame implements Observer {

	private static final long serialVersionUID = 1L;
	// Navigation Buttons
	private JButton btNextImage, btPrevImage;

	// Panels for layouts
	private JPanel mainPanel, navigationPanel;

	// Menubar and items
	private JMenu jmFile, jmView, jmHelp;
	private JMenuBar menubar;
	private JCheckBoxMenuItem cbQuadViewMode, cbSingleViewMode;
	private JMenuItem fileSwitchStudy, fileSave, fileCopy, fileExit, fileOpen, fileSetInit;
	
	// Layouts for Images and Buttons
	private FlowLayout navigationAreaLayout = new FlowLayout();
	
	// Application container
	private Container container;
	
	// Controller for current study
	private StudyController controller;
	
	//Action Listener instance
	private ClickListener listener = new ClickListener();

	//JFrame window tittle
	private static final String TITLE = "Medical Image Viewer";
	
	
	/**
	 * Viewer class, Application's GUI
	 */
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
		fileSwitchStudy = new JMenuItem("Switch Study");
		fileSwitchStudy.addActionListener(listener);

		fileOpen = new JMenuItem("Open");
		fileOpen.addActionListener(listener);

		fileCopy = new JMenuItem("Copy");
		fileCopy.addActionListener(listener);

		fileSave = new JMenuItem("Save");
		fileSave.addActionListener(listener);

		fileExit = new JMenuItem("Exit");
		fileExit.addActionListener(listener);
		
		fileSetInit = new JMenuItem("Set Initial Study");
		fileSetInit.addActionListener(listener);

		/*
		 * Create checkbox for state
		 */
		cbQuadViewMode = new JCheckBoxMenuItem("Quad View");
		cbQuadViewMode.addActionListener(listener);

		cbSingleViewMode = new JCheckBoxMenuItem("Single View");
		cbSingleViewMode.addActionListener(listener);

		/*
		 * Mark the correct state on initiate
		 */
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
		jmFile.add(fileSwitchStudy);
		jmFile.add(fileOpen);
		jmFile.add(fileCopy);
		jmFile.add(fileSave);
		jmFile.add(fileSetInit);
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
		fileSwitchStudy.getAccessibleContext().setAccessibleDescription(
				"Menu for switching studies");
		fileCopy.getAccessibleContext().setAccessibleDescription(
				"Menu for copying a study");
		fileSave.getAccessibleContext().setAccessibleDescription(
				"Menu for saving a study");
		fileExit.getAccessibleContext().setAccessibleDescription(
				"Menu for exiting application");
		fileSetInit.getAccessibleContext().setAccessibleDescription(
				"Menu item for setting the initial study.");
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
	class ClickListener implements ActionListener, MouseWheelListener {
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
			} else if (e.getActionCommand().equals("Switch Study")) {
				new OpenCommand(controller).execute();
			} else if (e.getActionCommand().equals("Open")) {
				new OpenCommand(controller).execute();
			} else if (e.getActionCommand().equals("Copy")){
				new CopyStudyCommand(controller).execute();
			} else if (e.getActionCommand().equals("Set Initial Study")){
				new SetInitialStudyCommand(controller).execute();
			}
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (e.getWheelRotation() < 0) {
				new NextCommand(controller.curState).execute();
			} else {
				new PrevCommand(controller.curState).execute();
			}
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
