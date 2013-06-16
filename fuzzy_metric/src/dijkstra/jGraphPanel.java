/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dijkstra;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.util.ArrayList;

/**
 *
 * @author Linh
 */
public class jGraphPanel extends JPanel {

    public ArrayList<Node> listNodes;
    public ArrayList<Link> listLinks;
    public boolean showResult = false;
    public boolean mDrag = false;
    public int xM = 0;
    public int yM = 0;
    public Node head = null;
    public static final int radius = 25;
    private static final int delta = 7;
    private static final Color Normal = Color.GREEN;
    private static final Color Mark = Color.RED;
    private static final Color Scan = Color.BLUE;
    private static final Color Text = Color.BLACK;

    public jGraphPanel() {
        super();
        this.listNodes = new ArrayList<Node>();
        this.listLinks = new ArrayList<Link>();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.mDrag && this.head != null) {
            Color c = g.getColor();
            g.setColor(jGraphPanel.Mark);
            g.drawLine(head.getX_cor() + radius / 2, head.getY_cor() + radius / 2, xM, yM);
            g.setColor(c);
        }
        for (Link edge : listLinks) {
            drawLink(g, edge);
        }
        for (Node Vertex : listNodes) {
            drawNode(g, Vertex);
        }
    }

    public void drawNode(Graphics g, Node vertical) {
        Color c = g.getColor();
        if (vertical.state == State.LABELED) {
            g.setColor(jGraphPanel.Mark);
        } else if (!this.showResult && vertical.state == State.SCANNED) {
            g.setColor(jGraphPanel.Scan);
        } else {
            g.setColor(jGraphPanel.Normal);
        }

        g.fillOval(vertical.getX_cor(), vertical.getY_cor(), radius, radius);
        g.setColor(Text);
        g.drawString(Integer.toString(vertical.getData()), vertical.getX_cor() + 2 * radius / 5, vertical.getY_cor() + 3 * radius / 4);
        //if (!this.showResult && Node.getKey() > 0)
        //    g.drawString(Integer.toString(Node.getKey()), Node.getX_cor() + radius, Node.getY_cor());
        g.setColor(c);
    }

    public void drawLink(Graphics g, Link link) {
        Color c = g.getColor();
        if (link.linkState == State.LABELED) {
            g.setColor(jGraphPanel.Mark);
        } else if (link.linkState == State.SCANNED) {
            g.setColor(jGraphPanel.Scan);
        } else {
            g.setColor(Normal);
        }

        //for edge
        double x1 = link.getHead().getX_cor() + radius / 2;
        double x2 = link.getTail().getX_cor() + radius / 2;
        double y1 = link.getHead().getY_cor() + radius / 2;
        double y2 = link.getTail().getY_cor() + radius / 2;
        double sin = this.sinLink(x1, y1, x2, y2);
        double cos = this.cosLink(x1, y1, x2, y2);
        boolean mark = false;

        if (sin * cos >= 0) {
            mark = true;
        }
        sin = Math.abs(sin);
        cos = Math.abs(cos);

        if (link.type == LinkType.FIRST) {
            if (mark) {
                x1 += delta * sin;
                x2 += delta * sin;
            } else {
                x1 -= delta * sin;
                x2 -= delta * sin;
            }
            y1 -= delta * cos;
            y2 -= delta * cos;
        } else if (link.type == LinkType.SECOND) {
            if (mark) {
                x1 -= delta * sin;
                x2 -= delta * sin;
            } else {
                x1 += delta * sin;
                x2 += delta * sin;
            }
            y1 += delta * cos;
            y2 += delta * cos;
        }
        g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);

        //for arrow
        double x3 = x2 + 0.5 * delta * sin;
        double x4 = x2 - 0.5 * delta * sin;
        if (!mark) {
            double x = x4;
            x4 = x3;
            x3 = x;
        }
        double x5 = x2;
        double y3 = y2 - 0.5 * delta * cos;
        double y4 = y2 + 0.5 * delta * cos;
        double y5 = y2;
        double xtb = Math.abs(x2 - (x1 + x2) / 2);
        double ytb = Math.abs(y2 - (y1 + y2) / 2);

        if (x2 > x1) {
            x3 -= ((delta) * cos + xtb);
            x4 -= ((delta) * cos + xtb);
            x5 -= xtb;
        } else {
            x3 += ((delta) * cos + xtb);
            x4 += ((delta) * cos + xtb);
            x5 += xtb;
        }
        if (y2 > y1) {
            y3 -= ((delta) * sin + ytb);
            y4 -= ((delta) * sin + ytb);
            y5 -= ytb;
        } else {
            y3 += ((delta) * sin + ytb);
            y4 += ((delta) * sin + ytb);
            y5 += ytb;
        }

        g.drawLine((int) x5, (int) y5, (int) x3, (int) y3);
        g.drawLine((int) x5, (int) y5, (int) x4, (int) y4);
        g.setColor(Text);
        g.drawString(Double.toString(link.getMetric()), (int) (x1 + x2) / 2, (int) (y1 + y2) / 2);
        g.setColor(c);
    }

    public Node checkInLink(int x, int y) {
        for (Node vertical : listNodes) {
            int xn = vertical.getX_cor() - jGraphPanel.radius / 2;
            int yn = vertical.getY_cor() - jGraphPanel.radius / 2;
            if (xn < x && yn < y && xn + radius > x && yn + radius > y) {
                return vertical;
            }
        }
        return null;
    }

    LinkType checkLink(Node head, Node tail) {
        for (Link link : listLinks) {
            Node eHead = link.getHead();
            Node eTail = link.getTail();
            if (eHead.equals(tail) && eTail.equals(head)) {
                link.type = LinkType.FIRST;
                return LinkType.SECOND;
            } else if (eHead.equals(head) && eTail.equals(tail)) {
                LinkType temp = link.type;
                eHead.outgoingLinks.remove(link);
                eTail.incomingLinks.remove(link);
                this.listLinks.remove(link);
                return temp;
            }
        }
        return LinkType.SINGLE;
    }

    public double sinLink(double x1, double y1, double x2, double y2) {
        return ((y2 - y1) / Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)));
    }

    public double cosLink(double x1, double y1, double x2, double y2) {
        return ((x2 - x1) / Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)));
    }
}
