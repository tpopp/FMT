package applet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTitleAnnotation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;

public class InputPanel extends JPanel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 842474287651143890L;
	private JFileChooser jf;
	private SystemPanel parent;
	private CalculationType calcType;
	private ChartPanel chartPanel;
	private int numberParticles = 1;
	CalculationType.ProfileType profileType = CalculationType.ProfileType.ConfinedSystem;

	private JTextField confinedWidthInput = new JTextField(10);
	private JTextField amplitudeInput = new JTextField(10);
	private JTextField frequencyInput = new JTextField(10);
	private JButton[] chooseFileButtons = new JButton[3];
	private JLabel[] chosenFileLabels = new JLabel[3];
	
	private JLabel confinementWidthPrompt = new JLabel("Confinement width:");
	private JLabel confinedSystemPrompt = new JLabel("Confined system:");
	private JLabel periodicSystemPrompt = new JLabel("Periodic Potential:");
	private JLabel periodicFrequencyPrompt = new JLabel("Frequency:");
	private JLabel periodicAmplitudePrompt = new JLabel("Amplitude:");
	private JCheckBox confinedSystemCheck = new JCheckBox();
	private JCheckBox periodicSystemCheck = new JCheckBox();
	private JButton confinedSystemHint = new JButton("  ?  ");
	private JButton periodicSystemHint = new JButton("  ?  ");
	private JButton confinedWidthHint = new JButton("  ?  ");
	private JButton amplitudeHint = new JButton("  ?  ");
	private JButton frequencyHint = new JButton("  ?  ");
	LinkedList<double[][]> data = new LinkedList<>();
	String[] legend = {"System 1"};
	
	private double width = 2.0;

	public InputPanel(JFileChooser jf, SystemPanel parent, CalculationType type) {
		for (int ii = 0; ii < 5; ii++) {
			double[][] arr = new double[2][2];
			arr[0][0] = 1.0;
			arr[0][1] = 2.0;
			arr[1][0] = 3.0;
			arr[1][1] = 4.0;
			data.push(arr);
		}
		confinedSystemHint.setBorder(null);
		periodicSystemHint.setBorder(null);
		confinedWidthHint.setBorder(null);
		amplitudeHint.setBorder(null);
		frequencyHint.setBorder(null);
		confinedWidthInput.setText(""+width);
		
		this.jf = jf;
		this.parent = parent;
		for (int ii = 0; ii < chooseFileButtons.length; ii++) {
			chooseFileButtons[ii] = new JButton("Custom File:");
		}
		confinedSystemCheck.setSelected(true);

		changeType(type);
		this.setBorder(BorderFactory.createLineBorder(Color.black));
	}

	public void changeType(CalculationType type) {

		this.calcType = type;

		removeAll();

		LinkedList<double[][]> temp123 = new LinkedList<>();
		for (int ii = 0; ii < 5; ii++) {
			double[][] arr = new double[2][2];
			arr[0][0] = 1.0;
			arr[0][1] = 2.0;
			arr[1][0] = 3.0;
			arr[1][1] = 4.0;
			temp123.push(arr);
		}
		JFreeChart chart = createChart(createDataset(legend, data),
				"Hello");

		// Initial Layout
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 20, 20, 20, 20, 20, 20,
				30, 30, 30, 30 };
		gridBagLayout.rowHeights = new int[] { 20, 20, 20, 20, 300 };
		gridBagLayout.columnWeights = new double[] { 0.1, 0.1, 0.1, 0.1,
				0.1, 0.2, 0.2, 0.2 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.1 };
		setLayout(gridBagLayout);
		GridBagConstraints gbc;

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(new JLabel(type.inputTitle), gbc);

		int col = 0;

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = col;
		gbc.gridy = 1;
		add(confinedSystemPrompt, gbc);

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = col;
		gbc.gridy = 2;
		add(periodicSystemPrompt, gbc);

		col++;

		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = col;
		gbc.gridy = 1;
		add(confinedSystemHint, gbc);

		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = col;
		gbc.gridy = 2;
		add(periodicSystemHint, gbc);

		col++;

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = col;
		gbc.gridy = 1;
		add(confinedSystemCheck, gbc);

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = col;
		gbc.gridy = 2;
		add(periodicSystemCheck, gbc);

		col++;

		if (this.profileType == CalculationType.ProfileType.ConfinedSystem) {
			gbc = new GridBagConstraints();
			gbc.insets = new Insets(0, 0, 5, 5);
			gbc.gridx = col;
			gbc.gridy = 1;
			add(new JLabel("Width ="), gbc);

			col++;

			gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(0, 0, 5, 5);
			gbc.gridx = col;
			gbc.gridy = 1;
			add(confinedWidthHint, gbc);

			col++;

			for (int ii = 1; ii <= numberParticles; ii++, col++) {
				gbc = new GridBagConstraints();
				gbc.insets = new Insets(0, 0, 5, 5);
				gbc.gridx = col;
				gbc.gridy = 0;
				add(new JLabel("   "), gbc);
				
				gbc = new GridBagConstraints();
				gbc.insets = new Insets(0, 0, 5, 5);
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.gridx = col;
				gbc.gridy = 1;
				add(confinedWidthInput, gbc);
			}

		} else if (this.profileType == CalculationType.ProfileType.SinusoidalSystem) {
			gbc = new GridBagConstraints();
			gbc.insets = new Insets(0, 0, 5, 5);
			gbc.gridx = col;
			gbc.gridy = 1;
			add(new JLabel("A ="), gbc);

			gbc = new GridBagConstraints();
			gbc.insets = new Insets(0, 0, 5, 5);
			gbc.gridx = col;
			gbc.gridy = 2;
			add(new JLabel("\u03BB ="), gbc);

			col++;

			gbc = new GridBagConstraints();
			gbc.insets = new Insets(0, 0, 5, 5);
			gbc.anchor = GridBagConstraints.WEST;
			gbc.gridx = col;
			gbc.gridy = 1;
			add(amplitudeHint, gbc);

			gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(0, 0, 5, 5);
			gbc.gridx = col;
			gbc.gridy = 2;
			add(frequencyHint, gbc);

			col++;

			for (int ii = 1; ii <= numberParticles; ii++, col++) {
				gbc = new GridBagConstraints();
				gbc.insets = new Insets(0, 0, 5, 5);
				gbc.gridx = col;
				gbc.gridy = 1;
				add(amplitudeInput, gbc);

				gbc = new GridBagConstraints();
				gbc.insets = new Insets(0, 0, 5, 5);
				gbc.gridx = col;
				gbc.gridy = 2;
				add(frequencyInput, gbc);
			}

		} else {
			System.err.println("Missing a profile type in InputPanel");
		}

		col++;

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = col;
		gbc.gridy = 0;
		add(new JLabel("OR"), gbc);

		col++;

		int row;
		for (row = 1; row <= numberParticles; row++) {
			gbc = new GridBagConstraints();
			gbc.gridx = col;
			gbc.insets = new Insets(0, 0, 5, 5);
			gbc.gridy = row;
			add(new JLabel("Particle " + row), gbc);

			gbc = new GridBagConstraints();
			gbc.insets = new Insets(0, 0, 5, 5);
			gbc.gridx = col + 1;
			gbc.gridy = row;
			add(chooseFileButtons[row - 1], gbc);

		}

		col += 2;

		chartPanel = new ChartPanel(chart);
		chartPanel.setMinimumDrawWidth(0);
		chartPanel.setMinimumDrawHeight(0);
		chartPanel.setMaximumDrawWidth(2000);
		chartPanel.setMaximumDrawHeight(2000);
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.white);
		chartPanel.getChart().setBackgroundPaint(null);

		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 10;
		gbc.gridheight = 3;
		gbc.fill = GridBagConstraints.BOTH;
		add(chartPanel, gbc);
	}

	public void changeNumberInputs(int numberParticles) {
		this.numberParticles = numberParticles;
		changeType(this.calcType);
	}

	public void changeCalcType(CalculationType.ProfileType type) {
		this.profileType = type;
		changeType(this.calcType);
	}

	private JFreeChart createChart(final XYDataset dataset, String title) {

		// create the chart...
		final JFreeChart chart = ChartFactory.createXYLineChart(null, // chart
																		// title
				"Radial Distance,  z/\u03c3", // x axis label
				title, // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
				);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		chart.setBackgroundPaint(Color.white);
		chart.getLegend().setPosition(RectangleEdge.RIGHT);

		// final StandardLegend legend = (StandardLegend) chart.getLegend();
		// legend.setDisplaySeriesShapes(true);

		// get a reference to the plot for further customisation...
		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		// plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		XYTitleAnnotation xyta = new XYTitleAnnotation(0.98, 0.98,
				chart.getLegend(), RectangleAnchor.TOP_RIGHT);
		chart.removeLegend();
		plot.addAnnotation(xyta);

		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(
				true, false);

		for (int i = 0; i < 2; i++) {
			renderer.setSeriesPaint(i * 6 + 0, new Color(255, 0, 0));
			renderer.setSeriesPaint(i * 6 + 1, new Color(0, 0, 255));
			renderer.setSeriesPaint(i * 6 + 2, new Color(0, 139, 0));
			renderer.setSeriesPaint(i * 6 + 3, new Color(255, 165, 0));
			renderer.setSeriesPaint(i * 6 + 4, new Color(255, 0, 255));
			renderer.setSeriesPaint(i * 6 + 5, new Color(0, 0, 0));

			renderer.setSeriesStroke(i * 6 + 0, new BasicStroke(1.3f,
					BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f,
					new float[] { 10.0f }, 0.0f));
			renderer.setSeriesStroke(i * 6 + 1, new BasicStroke(1.3f,
					BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f,
					new float[] { 50.0f, 2.0f }, 0.0f));
			renderer.setSeriesStroke(i * 6 + 2, new BasicStroke(1.3f,
					BasicStroke.JOIN_ROUND, BasicStroke.JOIN_MITER, 10.0f,
					new float[] { 30.0f, 1.0f, 1.0f }, 0.0f));
			renderer.setSeriesStroke(i * 6 + 3, new BasicStroke(1.3f,
					BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f,
					new float[] { 1.0f, 3.0f }, 0.0f));
			renderer.setSeriesStroke(i * 6 + 4, new BasicStroke(1.3f,
					BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f,
					new float[] { 1.0f, 2.0f, 3.0f, 4.0f }, 0.0f));
			renderer.setSeriesStroke(i * 6 + 5, new BasicStroke(1.3f,
					BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f,
					new float[] { 5.0f, 1.0f, 20.0f, 1.0f }, 0.0f));
		}

		plot.setRenderer(renderer);

		return chart;

	}

	public void createListeners() {

		confinedSystemCheck.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (confinedSystemCheck.isSelected()) {
					periodicSystemCheck.setSelected(false);
					parent.changeSystemType(CalculationType.ProfileType.ConfinedSystem);
				} else {
					confinedSystemCheck.setSelected(true);
				}
			}
		});

		periodicSystemCheck.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (periodicSystemCheck.isSelected()) {
					confinedSystemCheck.setSelected(false);
					parent.changeSystemType(CalculationType.ProfileType.SinusoidalSystem);
				} else {
					periodicSystemCheck.setSelected(true);
				}
			}
		});

		for (JButton button : chooseFileButtons) {
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					int option = jf.showOpenDialog(InputPanel.this);
					if (option == JFileChooser.APPROVE_OPTION) {
						try {
							Scanner sc = new Scanner(jf.getSelectedFile())
									.useDelimiter("[\\s,]+");
							// XXX: do stuff
							// TODO: more stuff
							// FIXME: I should really do stuff
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(getRootPane(), "Bad input file");
							return;
						}

					}
				}
			});
		}

	}

	private XYDataset createDataset(String[] legends, List<double[][]> yVals) {

		final XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series;
		for (int i = 0; i < legends.length; i++) {
			series = new XYSeries(legends[i]);
			series.add(0, 0);
			series.add(1, 0);
			double[][] arr = yVals.get(i);
			int j = 0;
			while (j < arr[0].length && arr[0][j] < 1)
				j++;
			for (; j < arr[0].length; j++)
				series.add(arr[0][j], arr[1][j]);
			dataset.addSeries(series);
		}

		return dataset;

	}

}
