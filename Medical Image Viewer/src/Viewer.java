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
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
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

	private static final String TITLE = "Medical Image Viewer";

	public Viewer() {
		super(TITLE);
		container = getContentPane();
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
		btPrevImage = new JButton("Previous");
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
		fileCopy = new JMenuItem("Copy");
		fileSave = new JMenuItem("Save");
		fileExit = new JMenuItem("Exit");

		/*
		 * Create checkbox for state
		 */
		cbQuadViewMode = new JCheckBoxMenuItem("Quad View");
		cbQuadViewMode.setState(true);
		cbQuadViewMode.setMnemonic(KeyEvent.VK_V);

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
		if (true) {
			quadView();
		} else {
			singleView();
		}
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
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

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
