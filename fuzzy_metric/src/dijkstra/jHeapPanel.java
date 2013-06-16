/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dijkstra;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author Linh
 */
public class jHeapPanel extends JPanel {

    public FibonacciHeap heap;
    private static final Color Normal = Color.GREEN;
    private static final Color Mark = Color.BLUE;
    private static final Color Min = Color.RED;
    private static final Color Text = Color.BLACK;
    private static final int radius = 25;

    public jHeapPanel() {
	super();
	this.heap = null;
    }

    @Override
    protected void paintComponent(Graphics g) {
	super.paintComponent(g);
	this.drawHeap(g);
    }

    private void drawHeap(Graphics g) {
	if (this.heap == null)
	    return;
	if (!this.heap.isEmpty()) {
	    Color c = g.getColor();
	    int i = this.drawNode(g, this.heap.findMin(), 50, 40, 0, 0);
	    g.setColor(c);
	}
    }

    private int drawNode(Graphics g, Node vertical, int x, int y, int xO, int yO) {
	int delta = 0;
	Node temp = vertical;
	do {
	    if (xO > 0 && yO > 0) {
		g.setColor(jHeapPanel.Normal);
		g.drawLine(xO + jHeapPanel.radius/2, yO + jHeapPanel.radius/2, x + delta + jHeapPanel.radius/2, y + jHeapPanel.radius/2);
	    }
	    int i = 0;
	    if (temp.getChildren() != null)
		i = this.drawNode(g, temp.getChildren(), x + delta, y + radius + 10, x + delta, y);
	    if (temp == this.heap.findMin())
		g.setColor(jHeapPanel.Min);
	    else if (temp.isMark())
		g.setColor(jHeapPanel.Mark);
	    else
		g.setColor(jHeapPanel.Normal);
	    
	    g.fillOval(x + delta, y, jHeapPanel.radius, jHeapPanel.radius);
	    g.setColor(jHeapPanel.Text);
	    g.drawString(Integer.toString(temp.getData()), x + delta + 2*jHeapPanel.radius/5, y + 3*jHeapPanel.radius/4);
	    g.drawString(Double.toString(temp.getKey()), x + delta + jHeapPanel.radius, y + jHeapPanel.radius/2);
	    if (i>0)
		delta += i;
	    else
		delta += jHeapPanel.radius + 30;
	    temp = temp.getRightSibling();
	} while (temp != vertical);
	return delta;
    }

}
