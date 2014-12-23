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
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingWorker;
import javax.swing.Timer;

/**
 * @author Tres
 * 
 */
public class SystemPanel extends JPanel implements Serializable {

	private static final long serialVersionUID = -3495266456015748880L;

	private JFileChooser jf;
	private FMTApplet parent;
	private CalculationType calcType = CalculationType.DensityProfile;
	private InputPanel input = new InputPanel(jf, SystemPanel.this, calcType);
	private OutputPanel output = new OutputPanel(jf, SystemPanel.this, calcType);
	private ParameterPanel params = new ParameterPanel(jf, SystemPanel.this, calcType);

	private CalculationType.ProfileType profileType = CalculationType.ProfileType.ConfinedSystem;
	private Dimensionality dimType = Dimensionality.ThreeD;

	public SystemPanel(JFileChooser jf, FMTApplet parent) {

		this.jf = jf;
		this.parent = parent;
		setName("FindDensity-3D-ConfinedSystem");

		changeLayout();
		createListeners();
		setPreferredSize(new Dimension(900, 600));
	}

	private void changeLayout() {
		this.removeAll();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 450, 450 };
		gridBagLayout.rowHeights = new int[] { 200, 400 };
		gridBagLayout.columnWeights = new double[] { 0.5, 0.5 };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0 };
		setLayout(gridBagLayout);

		// Add initial windows
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		add(params, gbc);

		if (calcType == CalculationType.ExternalInteraction) {
			gbc = new GridBagConstraints();
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.fill = GridBagConstraints.BOTH;
			add(output, gbc);

			gbc = new GridBagConstraints();
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.fill = GridBagConstraints.BOTH;
			add(input, gbc);
		} else if (calcType == CalculationType.DensityProfile) {
			gbc = new GridBagConstraints();
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.fill = GridBagConstraints.BOTH;
			add(input, gbc);

			gbc = new GridBagConstraints();
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.fill = GridBagConstraints.BOTH;
			add(output, gbc);
		}
	}

	private void createListeners() {
		input.createListeners();
		output.createListeners();
		params.createListeners();
	}

	public void make3D() {
		changeSystemDimensionality(Dimensionality.ThreeD);
	}

	public void make2D() {
		changeSystemDimensionality(Dimensionality.TwoD);
	}

	public void densityDependent() {
		changeCalculationType(CalculationType.ExternalInteraction);
	}

	public void externalDependent() {
		changeCalculationType(CalculationType.DensityProfile);
	}

	private void changeSystemDimensionality(Dimensionality dim) {
		dimType = dim;

		String name = "";
		if (dim == Dimensionality.TwoD) {
			name = getName().replace("()-()-()", "\1-2D-\3");
		} else if (dim == Dimensionality.ThreeD) {
			name = getName().replace("()-()-()", "\1-3D-\3");
		}
		setName(name);
		
		this.revalidate();
		this.repaint();
		parent.revalidate();
		parent.repaint();
	}

	private void changeCalculationType(CalculationType type) {
		calcType = type;

		String name = "";
		if (type == CalculationType.ExternalInteraction) {
			name = getName().replace("()-()-()", "FindExternal-\2-\3");
		} else if (type == CalculationType.DensityProfile) {
			name = getName().replace("()-()-()", "FindDensity-\2-\3");
		} else{
			System.err.println("Missing CalculationType in changeCalculationType.");
		}
		setName(name);

		params.changeType(type);
		input.changeType(type);
		output.changeType(type);

		changeLayout();

		this.revalidate();
		this.repaint();
		parent.revalidate();
		parent.repaint();
	}

	public void changeSystemType(CalculationType.ProfileType type) {
		profileType = type;

		String name = "";
		if (type == CalculationType.ProfileType.ConfinedSystem) {
			name = getName().replace("()-()-()", "\1-\2-Confined");
		} else if (type == CalculationType.ProfileType.SinusoidalSystem) {
			name = getName().replace("()-()-()", "\1-\2-Sinusoidal");
		}
		setName(name);

		input.changeCalcType(type);
		output.changeCalcType(type);
		this.revalidate();
		this.repaint();
		parent.revalidate();
		parent.repaint();
	}

	public void calculate() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			protected void done() {
				output.displayResults(new double[1][1]);
			}
		};
		worker.run();
		
	}

}
