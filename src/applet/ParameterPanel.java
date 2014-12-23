package applet;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import applet.CalculationType.ProfileType;

public class ParameterPanel extends JPanel implements Serializable {

	private static final long serialVersionUID = -2172146589258289819L;
	private JFileChooser jf;
	private SystemPanel parent;
	private CalculationType calcType;

	private int mixture = 0;
	private int inputColumns = 15;
	private double averageDensity;
	private double[] molFractionValues;
	private double[] sizeRatioValues;

	private JTextField[] molFractions;
	private JTextField[] sizeRatios;
	private JTextField mixtureInput = new JTextField(10);
	private JTextField averageDensityInput = new JTextField(10);

	private JLabel paramTitle = new JLabel("System Parameters:");
	private JLabel molFractionTitle = new JLabel("Mole fractions:");
	private JLabel sizeRatioTitle = new JLabel("Size ratios:");
	private JLabel averageDensityTitle = new JLabel("Average Density:");
	private JLabel mixtureNumberTitle = new JLabel(
			"# particles:");
	private JCheckBox densityProfileCheck = new JCheckBox();
	private JCheckBox externalInteractionCheck = new JCheckBox();
	private JCheckBox twoDCheck = new JCheckBox();
	private JCheckBox threeDCheck = new JCheckBox();

	private JButton densityProfileHint = new JButton("  ?  ");
	private JButton externalInteractionHint = new JButton("  ?  ");
	private JButton calculationTypeHint = new JButton("  ?  ");
	private JButton DimensionalityHint = new JButton("  ?  ");
	private JButton mixtureNumberHint = new JButton("  ?  ");
	private JButton averageDensityHint = new JButton("  ?  ");
	private JButton molFractionHint = new JButton("  ?  ");
	private JButton sizeRatioHint = new JButton("  ?  ");

//	private JLabel molFractionUnits = new JLabel("mol / mol System");
//	private JLabel sizeRatioUnits = new JLabel("size / size System 1");
	private JLabel slitporeImg;
	private JLabel sinusoidalImg;
	private ProfileType profileType = CalculationType.ProfileType.ConfinedSystem;

	public ParameterPanel(JFileChooser jf, SystemPanel parent,
			CalculationType type) {
		this.jf = jf;
		this.parent = parent;
		this.calcType = type;
		
		densityProfileHint.setBorder(null);
		externalInteractionHint.setBorder(null);
		calculationTypeHint.setBorder(null);
		DimensionalityHint.setBorder(null);
		mixtureNumberHint.setBorder(null);
		averageDensityHint.setBorder(null);
		molFractionHint.setBorder(null);
		sizeRatioHint.setBorder(null);

		BufferedImage sinusoidalPic = null;
		BufferedImage slitporePic = null;
		try {
			sinusoidalPic = ImageIO.read(this.getClass().getResourceAsStream(
					"/sinusoidal.png"));
			slitporePic = ImageIO.read(this.getClass().getResourceAsStream(
					"/slitpore.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		slitporeImg = new JLabel(new ImageIcon(slitporePic));
		sinusoidalImg = new JLabel(new ImageIcon(sinusoidalPic));

		changeMixture(1);

		molFractionValues[0] = 1.;
		sizeRatioValues[0] = 1.;
		averageDensity = 0.5;
		molFractions[0].setText("" + molFractionValues[0]);
		sizeRatios[0].setText("" + sizeRatioValues[0]);
		mixtureInput.setText("" + 1);
		averageDensityInput.setText("" + averageDensity);

		this.setBorder(BorderFactory.createLineBorder(Color.black));

		if(type == CalculationType.ExternalInteraction)
			externalInteractionCheck.setSelected(true);
		else if (type == CalculationType.DensityProfile)
			densityProfileCheck.setSelected(true);
			
		threeDCheck.setSelected(true);
	}

	private void changeMixture(int n) {

		removeAll();

		// Resize the per particle input arrays
		JTextField[] tmp = molFractions;
		JTextField[] tmp2 = sizeRatios;
		double[] tmp3 = molFractionValues;
		double[] tmp4 = sizeRatioValues;

		molFractions = new JTextField[n];
		sizeRatios = new JTextField[n];
		molFractionValues = new double[n];
		sizeRatioValues = new double[n];

		if (mixture < n) {
			int ii;
			for (ii = 0; ii < mixture; ii++) {
				molFractions[ii] = tmp[ii];
				sizeRatios[ii] = tmp2[ii];
				molFractionValues[ii] = tmp3[ii];
				sizeRatioValues[ii] = tmp4[ii];
			}
			for (; ii < n; ii++) {
				molFractions[ii] = new JTextField(inputColumns);
				sizeRatios[ii] = new JTextField(inputColumns);
			}
		} else {
			molFractionValues[0] = 1.;
			sizeRatioValues[0] = 1.;
			for (int ii = 0; ii < n; ii++) {
				molFractions[ii] = tmp[ii];
				sizeRatios[ii] = tmp2[ii];
			}
		}

		// Create layout
		mixture = n;

		GridBagLayout gridBagLayout = new GridBagLayout();

		gridBagLayout.columnWidths = new int[12];
		gridBagLayout.columnWidths[2] = 20;
		gridBagLayout.rowHeights = new int[] { 30, 100, 100 };
		gridBagLayout.columnWeights = new double[12];
		gridBagLayout.rowWeights = new double[] { 0.0, 0.5, 0.5 };
		setLayout(gridBagLayout);

		int col = 0;
		GridBagConstraints gbc;

		// Column 1 titles
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(paramTitle, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = col;
		gbc.gridy = 1;
		add(mixtureNumberTitle, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = col;
		gbc.gridy = 2;
		add(averageDensityTitle, gbc);

		col++;

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = col;
		gbc.gridy = 1;
		add(mixtureNumberHint, gbc);

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = col;
		gbc.gridy = 2;
		add(averageDensityHint, gbc);

		col++;

		// Column 2 input fields
		gbc = new GridBagConstraints();
		gbc.gridx = col;
		gbc.gridy = 1;
		// add(mixtureInput, gbc);
		add(new JLabel("1"), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = col;
		gbc.gridy = 2;
		add(averageDensityInput, gbc);

		col++;

		// Column 3 titles
		gbc = new GridBagConstraints();
		gbc.gridx = col;
		gbc.gridy = 1;
		add(molFractionTitle, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = col;
		gbc.gridy = 2;
		add(sizeRatioTitle, gbc);

		col++;

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = col;
		gbc.gridy = 1;
		add(molFractionHint, gbc);

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = col;
		gbc.gridy = 2;
		add(sizeRatioHint, gbc);

		col++;

		gbc = new GridBagConstraints();
		gbc.gridx = col;
		gbc.gridy = 0;
		add(new JLabel("System " + 1), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = col;
		gbc.gridy = 1;
		// add(molFractions[0], gbc);
		add(new JLabel("1"), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = col;
		gbc.gridy = 2;
		add(new JLabel("1"), gbc);

//		col++;
//
//		gbc = new GridBagConstraints();
//		gbc.gridx = col;
//		gbc.gridy = 1;
//		add(molFractionUnits, gbc);
//
//		gbc = new GridBagConstraints();
//		gbc.gridx = col;
//		gbc.gridy = 2;
//		add(sizeRatioUnits, gbc);

		col++;

		// Iterate through per particle inputs and add them
		for (int ii = 1; ii < n; ++col, ii++) {
			gbc = new GridBagConstraints();
			gbc.gridx = col;
			gbc.gridy = 0;
			add(new JLabel("System " + (ii + 1)), gbc);

			gbc = new GridBagConstraints();
			gbc.gridx = col;
			gbc.gridy = 1;
			add(molFractions[ii], gbc);

			gbc = new GridBagConstraints();
			gbc.gridx = col;
			gbc.gridy = 2;
			add(sizeRatios[ii], gbc);

//			col++;
//
//			gbc = new GridBagConstraints();
//			gbc.gridx = col;
//			gbc.gridy = 1;
//			add(molFractionUnits, gbc);
//
//			gbc = new GridBagConstraints();
//			gbc.gridx = col;
//			gbc.gridy = 2;
//			add(sizeRatioUnits, gbc);

		}

		// Calculation types
		gbc = new GridBagConstraints();
		gbc.gridx = col;
		gbc.gridy = 0;
		add(new JLabel("Calculation Type"), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = col;
		gbc.gridy = 1;
		add(new JLabel("Find Density Profile"), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = col;
		gbc.gridy = 2;
		add(new JLabel("Find External Interaction"), gbc);

		col++;

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = col;
		gbc.gridy = 0;
		add(calculationTypeHint, gbc);

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = col;
		gbc.gridy = 1;
		add(densityProfileHint, gbc);

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = col;
		gbc.gridy = 2;
		add(externalInteractionHint, gbc);

		col++;

		gbc = new GridBagConstraints();
		gbc.gridx = col;
		gbc.gridy = 1;
		add(densityProfileCheck, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = col;
		gbc.gridy = 2;
		add(externalInteractionCheck, gbc);

		col++;

		// Dimensionality types
		gbc = new GridBagConstraints();
		gbc.gridx = col;
		gbc.gridy = 0;
		add(new JLabel("Dimensionality"), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = col;
		gbc.gridy = 1;
		add(new JLabel("2D"), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = col;
		gbc.gridy = 2;
		add(new JLabel("3D"), gbc);

		col++;

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = col;
		gbc.gridy = 0;
		add(DimensionalityHint, gbc);

		col++;

		gbc = new GridBagConstraints();
		gbc.gridx = col;
		gbc.gridy = 1;
		add(twoDCheck, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = col;
		gbc.gridy = 2;
		add(threeDCheck, gbc);

		col++;

		gbc = new GridBagConstraints();
		gbc.gridx = col;
		gbc.gridy = 0;
		add(new JLabel("Current System Type"), gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = col;
		gbc.gridy = 1;
		gbc.gridheight = 2;
		if (profileType == CalculationType.ProfileType.ConfinedSystem) {
			add(slitporeImg, gbc);
		} else {
			add(sinusoidalImg, gbc);
		}

		col++;
	}

	public void changeType(CalculationType type) {
		this.calcType = type;
		changeMixture(mixture);
	}

	public void setDimensionality() {

	}

	public void createListeners() {
		for (int ii = 0; ii < molFractions.length; ii++) {
			final int i = ii;
			molFractions[ii].addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent arg0) {
					double d = 0;
					try {
						d = Double.parseDouble(molFractions[i].getText());
					} catch (Exception ex) {
						molFractions[i].setText(String.format("%.3f",
								molFractionValues[i]));
						return;
					}
					if (d <= 0 || d > 1.0) {
						molFractions[i].setText(String.format("%.3f",
								molFractionValues[i]));
					} else {
						molFractionValues[i] = d;
					}
				}

				@Override
				public void focusGained(FocusEvent arg0) {
					return;
				}
			});

			sizeRatios[ii].addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					double d = 0;
					try {
						d = Double.parseDouble(sizeRatios[i].getText());
					} catch (Exception ex) {
						sizeRatios[i].setText(String.format("%.3f",
								sizeRatioValues[i]));
						return;
					}
					if (d <= 0 || d > 1.0) {
						sizeRatios[i].setText(String.format("%.3f",
								sizeRatioValues[i]));
					} else {
						sizeRatioValues[i] = d;
					}
				}

				@Override
				public void focusGained(FocusEvent e) {
					return;
				}
			});
		}

		mixtureInput.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				int i = 0;
				try {
					i = Integer.parseInt(mixtureInput.getText());
				} catch (Exception ex) {
					mixtureInput.setText("" + mixture);
					return;
				}
				if (i < 1 || i > 3) {
					mixtureInput.setText("" + mixture);
				} else {
					mixture = i;
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				return;
			}
		});

		averageDensityInput.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				double i = 0;
				try {
					i = Integer.parseInt(averageDensityInput.getText());
				} catch (Exception ex) {
					averageDensityInput.setText("" + averageDensity);
					return;
				}
				if (i <= 0 || i >= 1) {
					averageDensityInput.setText("" + averageDensity);
				} else {
					averageDensity = i;
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				return;
			}
		});

		densityProfileCheck.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (densityProfileCheck.isSelected()) {
					externalInteractionCheck.setSelected(false);
					parent.externalDependent();
				} else {
					densityProfileCheck.setSelected(true);
				}
				System.out.println("densityProfileCheck clicked");
				if (densityProfileCheck.isSelected())
					System.out.println("densityProfileCheck selected");
				if (externalInteractionCheck.isSelected())
					System.out.println("externalInteractionCheck selected");
			}
		});

		externalInteractionCheck.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (externalInteractionCheck.isSelected()) {
					densityProfileCheck.setSelected(false);
					parent.densityDependent();
				} else {
					externalInteractionCheck.setSelected(true);
				}
				System.out.println("externalInteractionCheck clicked");
				if (densityProfileCheck.isSelected())
					System.out.println("externalInteractionCheck selected");
				if (externalInteractionCheck.isSelected())
					System.out.println("externalInteractionCheck selected");
			}
		});

		twoDCheck.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (twoDCheck.isSelected()) {
					threeDCheck.setSelected(false);
					parent.make2D();
				} else {
					twoDCheck.setSelected(true);
				}
			}
		});

		threeDCheck.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (threeDCheck.isSelected()) {
					twoDCheck.setSelected(false);
					parent.make3D();
				} else {
					threeDCheck.setSelected(true);
				}

			}
		});

		densityProfileHint.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane
						.showMessageDialog(getRootPane(),
								"Solve for external interaction, given a density profile.");

			}
		});
		externalInteractionHint.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane
						.showMessageDialog(getRootPane(),
								"Solve for density profile, given an external interaction.");

			}
		});
		calculationTypeHint.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane
						.showMessageDialog(getRootPane(),
								"Choose whether to solve for density profile or external interaction.");

			}
		});
		DimensionalityHint.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane
						.showMessageDialog(getRootPane(),
								"Decide between a two dimensional or three dimensional system.");

			}
		});
		mixtureNumberHint.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane
						.showMessageDialog(getRootPane(),
								"Specify how many different particles are in the system (1-3).");

			}
		});
		averageDensityHint.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(getRootPane(),
						"Specify the average density of the entire system.");

			}
		});
		molFractionHint.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane
						.showMessageDialog(getRootPane(),
								"Specify the mole fraction of each particle in the system (should add to 1).");

			}
		});
		sizeRatioHint.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane
						.showMessageDialog(
								getRootPane(),
								"Specify the size ratio of each particle relative to the largest particle (Largest is always system 1).");

			}
		});

	}
}
