/**
 * @author Damas Mlabwa
 * Course: Engineering of Software SubSystems, SWEN262
 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

	public Viewer() {
		super(TITLE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				//TODO: exit command? this code is copied from the exit menu item
				if(!displayState.saved){
					new UnsavedStatePrompt(displayState);
				} else {
					System.exit(0);
				}
			}
		});
		container = getContentPane();
		
		study = new Study(new File("D:\\Pictures\\Wallpapers\\"));
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
		btNextImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO: replace this with next command
				displayState.next();
			}
		});
		
		btPrevImage = new JButton("Previous");
		btPrevImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO: replace this with prev command
				displayState.prev();
			}
		});
		
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
		
		fileCopy = new JMenuItem("Copy");
		fileSave = new JMenuItem("Save");
		fileSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO: replace this with save command
				displayState.save();
			}
		});
		fileExit = new JMenuItem("Exit");
		fileExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!displayState.saved){
					new UnsavedStatePrompt(displayState);
				} else {
					System.exit(0);
				}
			}
		});

		/*
		 * Create checkbox for state
		 */
		cbQuadViewMode = new JCheckBoxMenuItem("Quad View");
		cbQuadViewMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				displayState.setMode(new FourUp());
			}
		});
		cbQuadViewMode.setState(true);
		


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

		menubar.add(jmFile);
		menubar.add(jmView);
		
		cbSingleViewMode = new JCheckBoxMenuItem("Single View");
		cbSingleViewMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayState.setMode(new OneUp());
			}
		});
		jmView.add(cbSingleViewMode);
		menubar.add(jmHelp);
	}

	/**
	 * Setup the layout and all UI components
	 */
	public void buildUI() {
		//receive panel from the display state
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
		btNextImage.getAccessibleContext().setAccessibleDescription("Navigate to the next image in current study");
		btPrevImage.getAccessibleContext().setAccessibleDescription("Navigate to the previous image in current study");
		jmFile.getAccessibleContext().setAccessibleDescription("Menu for operations on files");
		jmFile.getAccessibleContext().setAccessibleDescription("Menu for operation on view modes");
		jmHelp.getAccessibleContext().setAccessibleDescription("Menu for getting help on using this application");
		fileOpen.getAccessibleContext().setAccessibleDescription("Menu for opening a study");
		fileCopy.getAccessibleContext().setAccessibleDescription("Menu for copying a study");
		fileSave.getAccessibleContext().setAccessibleDescription("Menu for saving a study");
		fileExit.getAccessibleContext().setAccessibleDescription("Menu for exiting application");
		cbQuadViewMode.getAccessibleContext().setAccessibleDescription("Menu for enabling or disabling quad view");
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
		//TODO: set selected mode checkbox
		
		//replace mainPanel with new images
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
	class ClickListener implements ActionListener {
		/**
		 * Get event and perform an action
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
            
			if(e.getActionCommand().equals("Next")){
				displayState.next();
			} else if (e.getActionCommand().equals("Previous")){
				displayState.prev();
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
