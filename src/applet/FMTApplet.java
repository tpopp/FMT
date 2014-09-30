/**
 * 
 */
package applet;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.JApplet;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.Timer;

/**
 * @author Tres
 *
 */
public class FMTApplet extends JApplet implements Serializable {

		private static final long serialVersionUID = -6574435618466046038L;
		private JTabbedPane calculations;
		private JTabbedPane systems;
		private int i = 0;
		private JMenuItem newSystem;
		private JMenuItem store;
		private JMenuItem load;
		private JMenuItem removeSys;
		private JMenuItem loadSys;
		private JMenuItem storeSys;
		private float[][] G0 = new float[65][1001];
		private float[][][] G1 = new float[65][1001][101];
		public double gr_xMin = 0, gr_xMax = 2.5, gr_yMin = 0, gr_yMax = 4;
		public double pot_xMin = 0, pot_xMax = 2.5, pot_yMin = -1, pot_yMax = 3;

		public void init() {

			final JFileChooser jf = new JFileChooser();

			// menubar
			JMenuBar menu = new JMenuBar();
			JMenu options = new JMenu("Systems");
//			newSystem = new JMenuItem(Messages.getString("FMTApplet.newSystem"));
//			removeSys = new JMenuItem(Messages.getString("FluidApp.removeSystem"));
//			store = new JMenuItem(Messages.getString("FluidApp.storeState"));
//			load = new JMenuItem(Messages.getString("FluidApp.loadState"));
			storeSys = new JMenuItem("Store System");
			loadSys = new JMenuItem("Load System");
			JMenu saveLoad = new JMenu("Store/Load");

			saveLoad.add(storeSys);
			saveLoad.add(loadSys);
			menu.add(options);
			menu.add(saveLoad);
			setJMenuBar(menu);

			// Initial Layout
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWidths = new int[] { 450, 450 };
			gridBagLayout.rowHeights = new int[] { 0, 200 };
			gridBagLayout.columnWeights = new double[] { 0.5, 0.5 };
			gridBagLayout.rowWeights = new double[] { 1.0, 0.0 };
			setLayout(gridBagLayout);
			systems = new JTabbedPane(JTabbedPane.NORTH,
					JTabbedPane.WRAP_TAB_LAYOUT);
			calculations = new JTabbedPane(JTabbedPane.NORTH,
					JTabbedPane.WRAP_TAB_LAYOUT);

			// Add initial windows
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridwidth = 1;
			gbc.gridheight = 2;
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.fill = GridBagConstraints.BOTH;
			add(systems, gbc);

			gbc = new GridBagConstraints();
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.fill = GridBagConstraints.BOTH;
			add(calculations, gbc);

			gbc = new GridBagConstraints();
			gbc.gridwidth = 2;
			gbc.gridheight = 1;
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.fill = GridBagConstraints.BOTH;
			createListeners();
			setPreferredSize(new Dimension(900, 600));
			resize(900, 600);
		}

		/**
		 * @param args
		 *            Doesn't use any arguments
		 */
		public static void main(String[] args) {
			// Create and start app
			FMTApplet app = new FMTApplet();
			app.init();
			app.start();

			javax.swing.JFrame window = new javax.swing.JFrame("Fluid Info"); // Title //$NON-NLS-1$
			window.setContentPane(app);
			window.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
			window.pack(); // Arrange the components.
			window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			window.setVisible(true); // Make the window visible.
		}

		private void readObject(ObjectInputStream in) throws Exception {
			in.defaultReadObject();
			createListeners();
		}

		private void createListeners() {
		}


}
