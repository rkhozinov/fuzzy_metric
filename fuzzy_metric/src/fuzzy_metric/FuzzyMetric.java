package fuzzy_metric;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import net.sourceforge.jFuzzyLogic.FIS;
import java.util.Random;
import javax.swing.JFileChooser;

/**
 * Test parsing an FCL file
 */
public class FuzzyMetric {

    private static int[] delays;
    private static int[] bandwidth;
    private static double[] simpleMetric;
    private static double[] fuzzyMetric;
    private static File file;
    private static final int SCALE = 1000;

    public static void main(String[] args) throws Exception {

        // open .gph file
        Graph graph = new Graph();
        graph.open();

        // Get nodes count
        int linksCount = graph.listLinks.size();

        int minimum = 0;
        int maximum = 1000;

        // generate random test data
        delays = generateDelays(maximum, minimum, linksCount);
        bandwidth = generateBandwidth(maximum, minimum, linksCount);

        // calculate simple metric with random test data
        simpleMetric = getSimpleMetric(delays, bandwidth);

        graph.saveSimpleMetric(simpleMetric);

        // calculate fuzzy metric with same test data
        fuzzyMetric = getFuzzyMetric(delays, bandwidth);

        graph.saveFuzzyMetric(fuzzyMetric);

    }

    public static double[] getSimpleMetric(int[] delays, int[] bandwidth) {
        double[] metrics = new double[delays.length];
        for (int i = 0; i < delays.length; i++) {
            metrics[i] = 1 / ((double) delays[i] + (double) bandwidth[i]) * 100;
        }
        return metrics;
    }

    public static double[] getFuzzyMetric(int[] delays, int[] bandwidth) throws IOException {
        String current = new java.io.File(".").getCanonicalPath();
        String fileName = current + "\\src\\tipper.fcl";

        // load FCL file
        FIS fis = FIS.load(fileName, true);

        // Show fuzzy charts
        // fis.chart();
        // Error while loading?
        if (fis == null) {
            throw new IOException("Can't load file: '" + fileName + "'");
        }

        // Calculate fuzzy metric
        double[] fuzzyMetric = new double[delays.length];
        for (int i = 0; i < delays.length; i++) {
            // Set inputs
            fis.setVariable("delay", delays[i]);
            fis.setVariable("bandwidth", bandwidth[i]);

            // Evaluate
            fis.evaluate();
            fuzzyMetric[i] = (double) fis.getVariable("metric").getValue() * 100 / SCALE;
        }
        // show chart for fuzzy metric
//        fis.getVariable("metric").chartDefuzzifier(true);
        return fuzzyMetric;
    }

    private static int[] generateDelays(int maximum, int minimum, int count) {
        //init test data
        Random rand = new Random();
        int[] delays = new int[count];
        for (int i = 0; i < delays.length; i++) {
            delays[i] = rand.nextInt(maximum - minimum + 1) + minimum;
        }
        return delays;
    }

    private static int[] generateBandwidth(int maximum, int minimum, int count) {
        //init test data
        Random rand = new Random();
        int[] bandwidth = new int[count];
        for (int i = 0; i < bandwidth.length; i++) {
            bandwidth[i] = rand.nextInt(maximum - minimum + 1) + minimum;
        }
        return bandwidth;
        // prepare data
//        Arrays.sort(delays);
//        Arrays.sort(bandwidth);
    }
}

/**
 *
 * @author Administrator
 */
class Graph {

    public ArrayList<Node> listNodes;
    public ArrayList<Link> listLinks;
    public File file;

    public Graph() {
    }

    public void open() throws IOException, Exception {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

            this.file = fc.getSelectedFile();

            try (BufferedReader input = new BufferedReader(new FileReader(this.file))) {
                String line = input.readLine();
                this.listNodes = new ArrayList<>();
                this.listLinks = new ArrayList<>();
                while (line != null) {
                    String splittedLine[] = line.split(":");
                    switch (splittedLine[0]) {
                        case "Node":
                            int d = Integer.parseInt(splittedLine[1]);
                            int x = Integer.parseInt(splittedLine[2]);
                            int y = Integer.parseInt(splittedLine[3]);
                            listNodes.add(new Node(x, y, d, -1));
                            break;
                        case "Link":
                            Node headNode = listNodes.get(Integer.parseInt(splittedLine[1]));
                            Node tailVertex = listNodes.get(Integer.parseInt(splittedLine[2]));
                            double metric = Double.parseDouble(splittedLine[3]);
                            Link link = new Link(headNode, tailVertex, metric);
                            headNode.outgoingLinks.add(link);
                            tailVertex.incomingLinks.add(link);
                            listLinks.add(link);
                            break;
                    }
                    line = input.readLine();
                }
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                throw e;
            }
        }
    }

    public void saveSimpleMetric(double[] simpleMetrics) throws IOException, Exception {
        String simpleFilePath = this.file.getCanonicalPath() + "-simple" + ".gph";
        try (BufferedWriter output = new BufferedWriter(new FileWriter(new File(simpleFilePath)))) {

            for (Node node : listNodes) {
                output.write(String.format(Locale.US, "Node:%d:%d:%d", node.getData(), node.getX_cor(), node.getY_cor()));
                output.newLine();
            }
            int counter = 0;
            for (Link link : listLinks) {
                while (counter < simpleMetrics.length) {

                    output.write(String.format(Locale.US, "Link:%d:%d:%.4f",
                            link.getHead().getData(),
                            link.getTail().getData(),
                            simpleMetrics[counter]));
                    output.newLine();
                    counter++;
                    break;
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void saveFuzzyMetric(double[] fuzzyMetrics) throws IOException, Exception {
        String fuzzyFilePath = this.file.getCanonicalPath() + "-fuzzy" + ".gph";
        try (BufferedWriter output = new BufferedWriter(new FileWriter(new File(fuzzyFilePath)))) {
            for (Node node : listNodes) {
                output.write(String.format("Node:%d:%d:%d", node.getData(), node.getX_cor(), node.getY_cor()));
                output.newLine();
            }
            int counter = 0;
            for (Link link : listLinks) {
                while (counter < fuzzyMetrics.length) {
                    output.write(String.format(Locale.US, "Link:%d:%d:%.4f",
                            link.getHead().getData(),
                            link.getTail().getData(),
                            fuzzyMetrics[counter]));
                    output.newLine();
                    counter++;
                    break;
                }
            }
        } catch (Exception e) {
            throw e;
        }

    }
}