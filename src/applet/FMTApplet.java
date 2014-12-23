/**
 * 
 */
package applet;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

/**
 * @author Tres
 * 
 */
public class FMTApplet extends JApplet implements Serializable {

	private static final long serialVersionUID = -6574435618466046038L;
	private int i = 0;
	private JMenuItem loadSys;
	private JMenuItem storeSys;
	public double gr_xMin = 0, gr_xMax = 2.5, gr_yMin = 0, gr_yMax = 4;
	public double pot_xMin = 0, pot_xMax = 2.5, pot_yMin = -1, pot_yMax = 3;
	private JMenuItem twoD;
	private JMenuItem threeD;
	private JMenuItem densityProfile;
	private JMenuItem externalInteraction;
	private JMenuItem tracerInteraction;
	private JTabbedPane systems;
	private JMenuItem delSys;
	private JMenuItem newSys;
	private JMenuItem loadApplet;
	private JMenuItem saveApplet;
	private JFileChooser jf;

	public void init() {

		jf = new JFileChooser();
		systems = new JTabbedPane(JTabbedPane.NORTH,
				JTabbedPane.WRAP_TAB_LAYOUT);

		// menubar
		JMenuBar menu = new JMenuBar();
		storeSys = new JMenuItem("Store System");
		loadSys = new JMenuItem("Load System");
		saveApplet = new JMenuItem("Save Applet");
		loadApplet = new JMenuItem("Load Applet");
		newSys = new JMenuItem("New System");
		delSys = new JMenuItem("Delete System");
		JMenu saveLoad = new JMenu("Store/Load");

		JMenu options = new JMenu("Options");

		options.add(newSys);
		options.add(delSys);
		saveLoad.add(storeSys);
		saveLoad.add(loadSys);
		saveLoad.add(saveApplet);
		saveLoad.add(loadApplet);

		menu.add(options);
		menu.add(saveLoad);
		setJMenuBar(menu);

		addSystem();

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

		javax.swing.JFrame window = new javax.swing.JFrame("FMT Calculation");
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

		storeSys.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				SystemPanel sys = (SystemPanel) systems.getSelectedComponent();
				int choice = jf.showSaveDialog(FMTApplet.this);
				if (choice == JFileChooser.APPROVE_OPTION) {
					try {
						ObjectOutputStream os = new ObjectOutputStream(
								new FileOutputStream(jf.getSelectedFile()));
						FMTApplet.this.stop();
						os.writeObject(systems.getSelectedComponent());
						FMTApplet.this.start();
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(getRootPane(),
								"Error: \n\n" + e1);
					}
				}

			}
		});

		loadSys.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				SystemPanel sys = (SystemPanel) systems.getSelectedComponent();
				int choice = jf.showOpenDialog(FMTApplet.this);
				if (choice == JFileChooser.APPROVE_OPTION) {
					try {
						ObjectInputStream is = new ObjectInputStream(
								new FileInputStream(jf.getSelectedFile()));
						SystemPanel tab = (SystemPanel) is.readObject();
						systems.add(tab);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(getRootPane(),
								"Error: \n\n" + e1);
					}
				}
			}
		});

		newSys.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				addSystem();
			}
		});

		delSys.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int j = systems.getSelectedIndex();
				systems.remove(j);
			}
		});

		saveApplet.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int choice = jf.showSaveDialog(FMTApplet.this);
				if (choice == JFileChooser.APPROVE_OPTION) {
					try {
						ObjectOutputStream os = new ObjectOutputStream(
								new FileOutputStream(jf.getSelectedFile()));
						FMTApplet.this.stop();
						os.writeObject(FMTApplet.this);
						FMTApplet.this.start();
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(getRootPane(),
								"Error: \n\n" + e1); //$NON-NLS-1$
					}
				}

			}
		});

		loadApplet.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int choice = jf.showOpenDialog(FMTApplet.this);
				if (choice == JFileChooser.APPROVE_OPTION) {
					try {
						ObjectInputStream is = new ObjectInputStream(
								new FileInputStream(jf.getSelectedFile()));
						FMTApplet app = (FMTApplet) is.readObject();
						app.start();
						javax.swing.JFrame window = new javax.swing.JFrame(
								"Fluid Info"); // Title
						window.setContentPane(app);
						window.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
						window.pack(); // Arrange the components.
						window.setVisible(true); // Make the window visible.
						window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(getRootPane(),
								"Error: \n\n" + e1);
					}
				}

			}
		});
	}

	private void addSystem() {
		SystemPanel sys = new SystemPanel(jf, FMTApplet.this);
		systems.add(sys);

		add(systems);
	}


}
