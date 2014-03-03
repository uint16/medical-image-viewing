/**
 * @author Damas Mlabwa
 * Course: Engineering of Software SubSystems, SWEN262
 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Viewer extends JFrame implements Observer {

	private static final long serialVersionUID = 1L;
	// Navigation Buttons
	private JButton btNextImage, btPrevImage;
	// Label for Image currently displayed
	private JLabel jlFileName;
	// Panels for layouts
	private JPanel mainPanel, navigationPanel;
	// Menubar and items
	private JMenu jmFile, jmView, jmHelp;
	private JMenuBar menubar;
	private JCheckBoxMenuItem cbQuadViewMode;
	private JMenuItem fileOpen, fileSave, fileCopy, fileExit;
	// Image and Button Click listener
	private ClickListener btListener;
	private List<BufferedImage> images;
	// Layouts for Images and Buttons
	private GridLayout quadView = new GridLayout(2, 2);
	private GridLayout singleView = new GridLayout(1, 1);
	private FlowLayout navigationAreaLayout = new FlowLayout();
	// Application container
	private Container container;
	
	private DisplayState displayState;
	private Study study;

	private static final String TITLE = "Medical Image Viewer";

	public Viewer() {
		super(TITLE);
		container = getContentPane();
		
		study = new Study(new File(System.getProperty("user.home")+"/Pictures/SuzyWallpapers/"));
		displayState = new DisplayState(study);
		
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
		btNextImage.addActionListener(btListener);
		
		btPrevImage = new JButton("Previous");
		btPrevImage.addActionListener(btListener);
		
		jlFileName = new JLabel("Image Name Goes Here");
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
		fileOpen.addActionListener(btListener);
		
		fileCopy = new JMenuItem("Copy");
		fileSave = new JMenuItem("Save");
		fileExit = new JMenuItem("Exit");

		/*
		 * Create checkbox for state
		 */
		cbQuadViewMode = new JCheckBoxMenuItem("Quad View");
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
		menubar.add(jmHelp);
	}

	/**
	 * Setup the layout and all UI components
	 */
	public void buildUI() {
		// Will check Study state and make calls for single or quad View
/*		if (true) {
			quadView();
		} else {
			singleView();
		} */
		System.out.println();
		mainPanel.add(displayState.generatePanel());
		
		// complete other UI elements
		navigationPanel.setLayout(navigationAreaLayout);
		navigationPanel.add(btPrevImage);
		navigationPanel.add(btNextImage);
		container.add(navigationPanel, BorderLayout.SOUTH);
		container.add(mainPanel, BorderLayout.CENTER);
	}

	/**
	 * Set up images from the List of Buffered Images
	 */
	public void quadView() {
		mainPanel.setLayout(quadView);
		mainPanel.add(new JButton("Image 1"));
		mainPanel.add(new JButton("Image 2"));
		mainPanel.add(new JButton("Image 3"));
		mainPanel.add(new JButton("Image 4"));
	}

	/**
	 * Display single image
	 */
	public void singleView() {
		mainPanel.setLayout(singleView);
		mainPanel.add(new JButton("Image"));
	}

	/**
	 * Add accessibility text for menus and buttons
	 */
	public void setAccessibility() {

	}

	/**
	 * Set application size,location, and close operation
	 */
	public void displayApplication() {
		this.setSize(640, 640);
		this.setLocation(100, 100);
		this.setJMenuBar(menubar);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// this.setVisible(true);
	}

	/**
	 * Observer for the Viewer
	 */
	@Override
	public void update(Observable obs, Object obj) {
		

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
			Object button = e.getSource();
				
			if(button instanceof JButton){
				if(e.getActionCommand().equals("Next")){
					//call command
					System.out.println("Test");
				} else if(e.getActionCommand().equals("Previous")){
					//call command
				}
			} else if(button instanceof JMenu){
				
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
