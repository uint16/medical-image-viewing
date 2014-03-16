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

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
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
	private JRadioButtonMenuItem rbQuadViewMode, rbSingleViewMode;
	private JMenuItem fileSwitchStudy, fileSave, fileCopy, fileExit, fileSetInit, fileUndo;
	
	// Layouts for Images and Buttons
	private FlowLayout navigationAreaLayout = new FlowLayout();
	
	// Application container
	private Container container;
	
	// Controller for current study
	private StudyController controller;
	
	//Invoker for commands
	private Invoker invoker;
	
	//Action Listener instance
	private ClickListener listener = new ClickListener();

	//JFrame window title
	private static final String TITLE = "Medical Image Viewer";
	
	//ButtonGroup for display strategies
	private ButtonGroup stratButtonGroup = new ButtonGroup();
	
	
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
					new UnsavedStatePrompt(controller);
				} else {
					System.exit(0);
				}
			}
		});
		container = getContentPane();

		controller = new StudyController();
		controller.addObserver(this);
		invoker = new Invoker();

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

		fileCopy = new JMenuItem("Copy");
		fileCopy.addActionListener(listener);

		fileSave = new JMenuItem("Save");
		fileSave.addActionListener(listener);
		
		fileSetInit = new JMenuItem("Set Initial Study");
		fileSetInit.addActionListener(listener);
		
		fileUndo = new JMenuItem("Undo");
		fileUndo.addActionListener(listener);

		fileExit = new JMenuItem("Exit");
		fileExit.addActionListener(listener);

		/*
		 * Create checkbox for state
		 */
		rbQuadViewMode = new JRadioButtonMenuItem("Quad View");
		rbQuadViewMode.addActionListener(listener);
		stratButtonGroup.add(rbQuadViewMode);

		rbSingleViewMode = new JRadioButtonMenuItem("Single View");
		rbSingleViewMode.addActionListener(listener);
		stratButtonGroup.add(rbSingleViewMode);

		/*
		 * Add file menus
		 */
		jmFile.add(fileSwitchStudy);
		jmFile.add(fileCopy);
		jmFile.add(fileSave);
		jmFile.add(fileSetInit);
		jmFile.add(fileUndo);
		jmFile.add(fileExit);

		/*
		 * Add view menu
		 */

		jmView.add(rbQuadViewMode);
		jmView.add(rbSingleViewMode);

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
		fileUndo.getAccessibleContext().setAccessibleDescription(
				"Menu item for undoing the last action");
		rbQuadViewMode.getAccessibleContext().setAccessibleDescription(
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
		
		//select menu item corresponding to current display strategy
		DisplayStrategy curStrat = controller.curState.strategy;
		if (curStrat instanceof FourUp) {
			stratButtonGroup.setSelected(rbQuadViewMode.getModel(), true);
		} else if (curStrat instanceof OneUp) {
			stratButtonGroup.setSelected(rbSingleViewMode.getModel(), true);
		} else {
			System.err.println("Error: Current display strategy was not recognized!");
			stratButtonGroup.clearSelection();
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
			String command = e.getActionCommand();

			if (command.equals("Next")) {
				invoker.add(new NextCommand(controller.curState));
			} else if (command.equals("Previous")) {
				invoker.add(new PrevCommand(controller.curState));
			} else if (command.equals("Single View")) {
				invoker.add(new ChangeToOneUp(controller.curState));
			} else if (command.equals("Quad View")) {
				invoker.add(new ChangeToFourUp(controller.curState));
			} else if (command.equals("Exit")) {
				if (!controller.curState.saved) {
					new UnsavedStatePrompt(controller);
				}
				System.exit(0);
			} else if (command.equals("Save")) {
				invoker.add(new SaveCommand(controller.curState));
			} else if (command.equals("Switch Study")) {
				invoker.add(new OpenCommand(controller));
			} else if (command.equals("Copy")){
				invoker.add(new CopyStudyCommand(controller));
			} else if (command.equals("Set Initial Study")){
				invoker.add(new SetInitialStudyCommand(controller));
			} else if (command.equals("Undo")){
				invoker.undo();
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
