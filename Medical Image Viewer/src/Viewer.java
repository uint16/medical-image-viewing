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
import java.io.File;
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

	private DisplayState displayState;
	private Study study;

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
				if (!displayState.saved) {
					new UnsavedStatePrompt(displayState);
				} else {
					System.exit(0);
				}
			}
		});
		container = getContentPane();

		study = new Study(new File(System.getProperty("user.home")
				+ "/Desktop/cta_head/"));
		displayState = new DisplayState(study);
		displayState.addObserver(this);

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

		if (displayState.getMode() instanceof FourUp) {
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
		mainPanel = displayState.generatePanel();

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
		if (displayState.getMode() instanceof FourUp) {
			if(displayState.getIndex() < 5){
				btPrevImage.setEnabled(false);
				btNextImage.setEnabled(true);
			} else if(study.imgAmt() - displayState.getIndex() < 4){
				btNextImage.setEnabled(false);
				btPrevImage.setEnabled(true);
			} else {
				btNextImage.setEnabled(true);
				btPrevImage.setEnabled(true);
			}
		} else {
			if (displayState.getIndex() == 0) {
				btPrevImage.setEnabled(false);
			} else if (displayState.getIndex() < study.imgAmt() - 1) {
				btPrevImage.setEnabled(true);
				btNextImage.setEnabled(true);
			} else {
				btNextImage.setEnabled(false);
			}
		}

		// replace mainPanel with new images
		container.remove(mainPanel);
		mainPanel = displayState.generatePanel();
		container.add(mainPanel, BorderLayout.CENTER);
		container.validate();
	}

	/**
	 * 
	 * Handle all clicks from the view
	 * 
	 */
	class ClickListener implements ActionListener, KeyListener {
		/**
		 * Get event and perform an action depending on clicked item
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getActionCommand().equals("Next")) {
				new NextCommand(displayState).execute();
			} else if (e.getActionCommand().equals("Previous")) {
				new PrevCommand(displayState).execute();
			} else if (e.getActionCommand().equals("Single View")) {
				new ChangeToOneUp(displayState).execute();
				cbQuadViewMode.setState(false);
			} else if (e.getActionCommand().equals("Quad View")) {
				new ChangeToFourUp(displayState).execute();
				cbSingleViewMode.setState(false);
			} else if (e.getActionCommand().equals("Exit")) {
				if (!displayState.saved) {
					new UnsavedStatePrompt(displayState);
				} else {
					System.exit(0);
				}
			} else if (e.getActionCommand().equals("Save")) {
				new SaveCommand(displayState).execute();
			} else if (e.getActionCommand().equals("Open")) {
				new OpenCommand(displayState).execute();
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
