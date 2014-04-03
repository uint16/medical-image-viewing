package view;

import java.awt.Component;
import java.io.File;
import java.io.FileFilter;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;

/**
 * Helper class(es) for JTree in Viewer, handles expansion and directories
 * representation
 * 
 * @author Damas Mlabwa
 * 
 */

public class DirectoryModel extends DefaultTreeModel implements
		TreeWillExpandListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DefaultMutableTreeNode root;

	public DirectoryModel(File _root) {
		super(new DefaultMutableTreeNode(_root));
		root = (DefaultMutableTreeNode) getRoot();
		
		File[] f = _root.listFiles(new FileFilter() {
			@Override
			public boolean accept(File arg0) {
				return !arg0.isHidden() && arg0.isDirectory();
			}

		});
		
		for (int i = 0; i < f.length; i++) {
			DefaultMutableTreeNode fileRoot = new DefaultMutableTreeNode(f[i]);
			fileRoot.add(new DefaultMutableTreeNode());
			root.add(fileRoot);
		}

	}



	@Override
	public void treeWillExpand(TreeExpansionEvent e) throws ExpandVetoException {
		Object node = e.getPath().getLastPathComponent();
		if (node instanceof DefaultMutableTreeNode) {
			Object userObj = ((DefaultMutableTreeNode) node).getUserObject();
			if (userObj instanceof File) {
				((DefaultMutableTreeNode) node).removeAllChildren();
				File f = (File) userObj;
				try {
					File[] childFile = f.listFiles();
					for (int i = 0; i < childFile.length; i++) {
						if (childFile[i].isDirectory()) {
							DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(
									childFile[i]);
							childNode.add(new DefaultMutableTreeNode());
							((DefaultMutableTreeNode) node).add(childNode);
						}
					}
				} catch (Exception x) {
					// Problem reading directory...
					x.printStackTrace();
				}
			}
		}

	}

	@Override
	public void treeWillCollapse(TreeExpansionEvent e)
			throws ExpandVetoException {
		Object node = e.getPath().getLastPathComponent();
		if (node instanceof DefaultMutableTreeNode) {
			((DefaultMutableTreeNode) node).removeAllChildren();
			((DefaultMutableTreeNode) node).add(new DefaultMutableTreeNode());
		}

	}
	
	class DirectoryRenderer extends DefaultTreeCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			JLabel l = (JLabel) super.getTreeCellRendererComponent(tree, value,
					sel, expanded, leaf, row, hasFocus);
			Object userObject = ((DefaultMutableTreeNode) value)
					.getUserObject();
			if (userObject instanceof File) {
				String name = ((File) userObject).getName();
				if (name.trim().equals("")) {
					l.setText(((File) userObject).toString());
				} else {
					l.setText(((File) userObject).getName());
				}
			}
			return l;
		}
	}
}
