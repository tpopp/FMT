package applet;

import java.awt.BasicStroke;
import java.awt.Color;
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
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
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

public class OutputPanel extends JPanel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7322943663615496810L;
	private JFileChooser jf;
	private SystemPanel parent;
	private CalculationType calcType;
	private ChartPanel chartPanel;
	private int numberParticles = 1;
	CalculationType.ProfileType profileType = CalculationType.ProfileType.ConfinedSystem;

	private JButton[] chooseFileButtons = new JButton[3];
	private JLabel completionMessage = new JLabel();
	private JButton terminateButton = new JButton("Terminate");
	private JButton calculate = new JButton("Calculate");
	private boolean calculating = false;
	LinkedList<double[][]> data = new LinkedList<>();
	String[] legend = {"System 1"};

	public OutputPanel(JFileChooser jf, SystemPanel parent, CalculationType type) {
		
		// XXX: remove
		for (int ii = 0; ii < 5; ii++) {
			double[][] arr = new double[2][2];
			arr[0][0] = 1.0;
			arr[0][1] = 2.0;
			arr[1][0] = 3.0;
			arr[1][1] = 4.0;
			data.push(arr);
		}
		chartPanel = new ChartPanel(createChart(createDataset(legend, data),"Hello World!!!"));
		chartPanel.setMinimumDrawWidth(0);
		chartPanel.setMinimumDrawHeight(0);
		chartPanel.setMaximumDrawWidth(2000);
		chartPanel.setMaximumDrawHeight(2000);
		XYPlot plot = (XYPlot) chartPanel.getChart().getPlot();
		plot.setBackgroundPaint(Color.white);
		chartPanel.getChart().setBackgroundPaint(null);

		
		
		
		this.jf = jf;
		this.parent = parent;

		for (int ii = 0; ii < 3; ii++) {
			chooseFileButtons[ii] = new JButton("Save System " + (1 + ii));
		}

		changeType(type);
		this.setBorder(BorderFactory.createLineBorder(Color.black));
	}

	public void changeType(CalculationType type) {
		this.calcType = type;

		removeAll();


		// Initial Layout
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 100, 100, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 20, 20, 20, 20, 0 };
		gridBagLayout.columnWeights = new double[] { 0.25, 0.25, 0.0, 0.0, 0.0 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.1, 0.0 };
		setLayout(gridBagLayout);
		GridBagConstraints gbc;

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(new JLabel(type.outputTitle), gbc);

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(completionMessage, gbc);

		if (calculating) {
			gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.gridx = 1;
			gbc.gridy = 1;
			add(terminateButton, gbc);
		} else {
			gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.gridx = 1;
			gbc.gridy = 1;
			add(calculate, gbc);
		}

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(new JLabel(""), gbc);

		int col = 0;

		int row = 1;
		for (; row <= numberParticles; row++) {
			gbc = new GridBagConstraints();
			gbc.gridx = col;
			gbc.gridy = row;
			add(chooseFileButtons[row-1], gbc);
		}

		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 5;
		gbc.gridheight = 2;
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

	protected void changeCompletion(String message) {
		completionMessage.setText(message);
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

	public void createListeners() {
		for (JButton button : chooseFileButtons) {
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					int option = jf.showOpenDialog(OutputPanel.this);
					if (option == JFileChooser.APPROVE_OPTION) {
						try {

							// XXX: do stuff
							// TODO: more stuff
							// FIXME: I should really do stuff
						} catch (Exception ex) {
							return;
						}

					}
				}
			});
		}
		
		calculate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				calculating = true;
				parent.calculate();
			}
		});
	}

	public void displayResults(double[][] resultData) {
		data = new LinkedList<double[][]>();
		data.add(resultData);
		calculating = false;
		chartPanel = new ChartPanel(createChart(createDataset(legend, data),"Hello World!!!"));
		
	}

}
