package view;

/**
 * @author Damas Mlabwa
 * Course: Engineering of Software SubSystems, SWEN262
 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.apache.commons.io.FilenameUtils;

import commandFramework.ChangeToCoronal;
import commandFramework.ChangeToFourUp;
import commandFramework.ChangeToOneUp;
import commandFramework.ChangeToReconstruction;
import commandFramework.ChangeToSagittal;
import commandFramework.CopyStudyCommand;
import commandFramework.Invoker;
import commandFramework.NextCommand;
import commandFramework.OpenCommand;
import commandFramework.PrevCommand;
import commandFramework.SaveCommand;
import commandFramework.SetInitialStudyCommand;
import commandFramework.SetWindowingCommand;

import controller.StudyController;
import displayStrategyFramework.CoronalReconstructionStrategy;
import displayStrategyFramework.DisplayStrategy;
import displayStrategyFramework.FourUpStrategy;
import displayStrategyFramework.OneUpStrategy;
import displayStrategyFramework.ReconstructionStrategy;
import displayStrategyFramework.SagittalReconstructionStrategy;

public class Viewer extends JFrame implements Observer {

	private static final long serialVersionUID = 1L;
	// Navigation Buttons
	private JButton btNextImage, btPrevImage;

	// Panels for layouts
	private JPanel mainPanel, navigationPanel, studiesPanel;

	// Menubar and items
	private JMenu jmFile, jmView, jmHelp;
	private JMenuBar menubar;

	private JRadioButtonMenuItem rbQuadViewMode, rbSingleViewMode, rbCoronalViewMode, rbSagittalViewMode, rbReconstructionViewMode;
	private JMenuItem fileSwitchStudy, fileSave, fileCopy, fileExit, fileSetInit, fileUndo;
	private JSeparator viewSeparator;
	private JMenuItem viewSetWindowing;

	// Layouts for Images and Buttons
	private FlowLayout navigationAreaLayout = new FlowLayout();

	// Application container
	private Container container;

	// Controller for current study
	private StudyController controller;

	// Invoker for commands
	private Invoker invoker;

	// Action Listener instance
	private ClickListener listener = new ClickListener();

	// JFrame window title
	private static final String TITLE = "Medical Image Viewer";

	// ButtonGroup for display strategies
	private ButtonGroup stratButtonGroup = new ButtonGroup();

	// layeredPane containing mainPanel, for catching drag events
	private JLayeredPane layeredPane;

	private JTree studies;
	DirectoryModel model;
	

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

		invoker = new Invoker();
		controller = new StudyController(invoker);
		controller.addObserver(this);

		initComponents();
		setMenu();
		navigationJTree();
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
		studiesPanel = new JPanel();

		container.addMouseWheelListener(controller);
	}

	public void navigationJTree() {
		//fs = new FileSystemModel(controller.getHomeDir());
		model = new DirectoryModel(controller.getHomeDir());
		studies = new JTree(model);
		studies.setCellRenderer(model.new DirectoryRenderer());
		studies.addTreeWillExpandListener(model);
		studies.setEditable(false);
		studies.setRootVisible(false);

		studies.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				String study_name = FilenameUtils.getName(e.getPath().getLastPathComponent().toString());
				controller.openStudy(study_name);

				studies.expandPath(e.getPath());
			}
		});

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
		viewSetWindowing = new JMenuItem("Set Windowing");
		viewSetWindowing.addActionListener(listener);
		jmView.add(viewSetWindowing);

		viewSeparator = new JSeparator();
		jmView.add(viewSeparator);

		rbQuadViewMode = new JRadioButtonMenuItem("Quad View");
		rbQuadViewMode.addActionListener(listener);
		stratButtonGroup.add(rbQuadViewMode);

		rbSingleViewMode = new JRadioButtonMenuItem("Single View");
		rbSingleViewMode.addActionListener(listener);
		stratButtonGroup.add(rbSingleViewMode);

		rbCoronalViewMode = new JRadioButtonMenuItem("Coronal Reconstruction");
		rbCoronalViewMode.addActionListener(listener);
		stratButtonGroup.add(rbCoronalViewMode);

		rbSagittalViewMode = new JRadioButtonMenuItem("Sagittal Reconstruction");
		rbSagittalViewMode.addActionListener(listener);
		stratButtonGroup.add(rbSagittalViewMode);
		
		rbReconstructionViewMode = new JRadioButtonMenuItem("Reconstruction View");
		rbReconstructionViewMode.addActionListener(listener);
		stratButtonGroup.add(rbReconstructionViewMode);

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
		jmView.add(rbCoronalViewMode);
		jmView.add(rbSagittalViewMode);
		jmView.add(rbReconstructionViewMode);

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
		mainPanel.addMouseListener(listener);
		mainPanel.addMouseMotionListener(controller);

		// complete other UI elements
		navigationPanel.setLayout(navigationAreaLayout);
		navigationPanel.add(btPrevImage);
		navigationPanel.add(btNextImage);
		JScrollPane scrollpane = new JScrollPane(studies);
		scrollpane.getViewport().add(studies);
		studiesPanel.add(scrollpane);
		container.add(navigationPanel, BorderLayout.SOUTH);
		container.add(studiesPanel, BorderLayout.WEST);

		// set up layered pane
		layeredPane = new JLayeredPane();
		getContentPane().add(layeredPane, BorderLayout.CENTER);
		layeredPane.setLayout(new BorderLayout(0, 0));
		layeredPane.addMouseListener(listener);
		layeredPane.addMouseMotionListener(controller);
	}



	/**
	 * Set application size,location, and close operation
	 */
	public void displayApplication() {
		//this.setSize(640, 640);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setLocation(100, 100);
		this.setJMenuBar(menubar);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// get the buttons, menu items, etc to init to correct states
		controller.notifyObservers();
	}

	/**
	 * Observer for the Viewer
	 */
	@Override
	public void update(Observable obs, Object obj) {
		
		// show the name of the current study in the title
		this.setTitle(TITLE + ": " + controller.curState.getStudy().toString());

		// disable/enable buttons if first or last image
		btPrevImage.setEnabled(controller.curState.hasPrev());
		btNextImage.setEnabled(controller.curState.hasNext());

		// select menu item corresponding to current display strategy
		DisplayStrategy curStrat = controller.curState.getCurStrategy();
		if (curStrat instanceof FourUpStrategy) {
			stratButtonGroup.setSelected(rbQuadViewMode.getModel(), true);
		} else if (curStrat instanceof OneUpStrategy) {
			stratButtonGroup.setSelected(rbSingleViewMode.getModel(), true);
		} else if (curStrat instanceof CoronalReconstructionStrategy) {
			stratButtonGroup.setSelected(rbCoronalViewMode.getModel(), true);
		} else if (curStrat instanceof SagittalReconstructionStrategy) {
			stratButtonGroup.setSelected(rbSagittalViewMode.getModel(), true);
		} else if (curStrat instanceof ReconstructionStrategy){
			stratButtonGroup.setSelected(rbReconstructionViewMode.getModel(), true);
		} else {
			System.err
					.println("Error: Current display strategy was not recognized!");
			stratButtonGroup.clearSelection();
		}
		
		// replace mainPanel with new images
		layeredPane.remove(mainPanel);
		mainPanel = controller.generatePanel();
		layeredPane.add(mainPanel);
		container.validate();
	}

	/**
	 * 
	 * Handle all clicks from the view
	 * 
	 */
	class ClickListener implements ActionListener, MouseListener {
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
			} else if (command.equals("Coronal Reconstruction")) {
				invoker.add(new ChangeToCoronal(controller.curState));
			} else if (command.equals("Sagittal Reconstruction")) {
				invoker.add(new ChangeToSagittal(controller.curState));
			} else if (command.equals("Reconstruction View")){
				invoker.add(new ChangeToReconstruction(controller.curState));
			} else if (command.equals("Exit")) {
				if (!controller.curState.saved) {
					new UnsavedStatePrompt(controller);
				}
				System.exit(0);
			} else if (command.equals("Save")) {
				invoker.add(new SaveCommand(controller.curState));
			} else if (command.equals("Switch Study")) {
				invoker.add(new OpenCommand(controller));
			} else if (command.equals("Copy")) {
				invoker.add(new CopyStudyCommand(controller));
				
			} else if (command.equals("Set Initial Study")) {
				StudySelectorPrompt s = new StudySelectorPrompt(controller);
				String result = s.showPrompt();
				if (result != null) {
					invoker.add(new SetInitialStudyCommand(controller, result));
				}
			} else if (command.equals("Undo")) {
				invoker.undo();
			} else if (command.equals("Set Windowing")) {
				int curLow = controller.curState.getLowCutoff();
				int curHigh = controller.curState.getHighCutoff();
				WindowingValuesPrompt wvp = new WindowingValuesPrompt(curLow, curHigh);
				int[] newCutoffs = wvp.showPrompt();
				if (newCutoffs != null && newCutoffs.length == 2) {
					invoker.add(new SetWindowingCommand(controller.curState,
							newCutoffs[0], newCutoffs[1]));
				}
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			controller.mouseDragged(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

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
