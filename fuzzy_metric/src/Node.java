/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.ArrayList;

enum State {
    LABELED, UNLABELED, SCANNED
}
/**
 *
 * @author Linh
 */
public class Node {
    // For drawing
    private int x_cor;
    private int y_cor;

    // For a vertical in Fibonacci heap
    private Node parent;
    private Node leftSibling;
    private Node rightSibling;
    private Node children;
    private int data;
    private int key;
    private boolean mark;
    private int rank;
    
    // For graph
    private Node pred;
    public  ArrayList<Link> incomingLinks;
    public  ArrayList<Link> outgoingLinks;
    State state;

    public Node(int x_cor, int y_cor, int data, int key) {
	this.x_cor = x_cor;
	this.y_cor = y_cor;
	this.data = data;
	this.key = key;
	this.incomingLinks = new ArrayList<Link>();
	this.outgoingLinks = new ArrayList<Link>();
	this.parent = null;
	this.children = null;
	this.pred = null;
	this.mark = false;
	this.rank = 0;
	this.state = State.UNLABELED;
	this.sibling();
    }

    private void sibling() {
	this.leftSibling = this.rightSibling = this;
    }

    @Override
    public String toString() {
	return "Node{" + "incomingEdges=" + incomingLinks + "outgoingEdges=" + outgoingLinks + "data=" + data + "state=" + state + '}';
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final Node other = (Node) obj;
	if (this.x_cor != other.x_cor) {
	    return false;
	}
	if (this.y_cor != other.y_cor) {
	    return false;
	}
	if (this.data != other.data) {
	    return false;
	}
	if (this.key != other.key) {
	    return false;
	}
	return true;
    }

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 37 * hash + this.x_cor;
	hash = 37 * hash + this.y_cor;
	hash = 37 * hash + this.data;
	hash = 37 * hash + this.key;
	return hash;
    }
    
    public int getX_cor() {
	return x_cor;
    }

    public int getY_cor() {
	return y_cor;
    }

    public int getData() {
	return data;
    }

    public void setData(int data) {
	this.data = data;
    }

    public int getKey() {
	return key;
    }

    public void setKey(int key) {
	this.key = key;
    }

    public Node getParent() {
	return parent;
    }

    public void setParent(Node parent) {
	this.parent = parent;
    }

    public Node getPred() {
	return pred;
    }

    public void setPred(Node pred) {
	this.pred = pred;
    }

    public Node getChildren() {
	return children;
    }

    public void setChildren(Node children) {
	this.children = children;
    }

    public Node getLeftSibling() {
	return leftSibling;
    }

    public void setLeftSibling(Node leftSibling) {
	this.leftSibling = leftSibling;
    }

    public Node getRightSibling() {
	return rightSibling;
    }

    public void setRightSibling(Node rightSibling) {
	this.rightSibling = rightSibling;
    }

    public boolean isMark() {
	return mark;
    }

    public void setMark(boolean mark) {
	this.mark = mark;
    }

    public int getRank() {
	return rank;
    }

    public void setRank(int rank) {
	this.rank = rank;
    }

    // method for drawing
    public Link getIncomingEdge(Node head) {
	for (Link edge : incomingLinks)
	    if (edge.getHead().equals(head))
		return edge;
	return null;
    }

    public Link getOutgoingEdge(Node tail) {
	for (Link edge : incomingLinks)
	    if (edge.getTail().equals(tail))
		return edge;
	return null;
    }

    // method for a node in Fibonacci heap
    public boolean isSingle() {
	return (this == this.rightSibling);
    }

    public void addChild(Node child) {
	if (this.children != null)
	    this.children.addSibling(child);
	else
	    this.children = child;
	child.setParent(this);
	child.mark = false;
	this.rank++;
    }

    public void addSibling(Node sibling) {
	if (sibling == null)
	    return;

	Node tLeft = this.leftSibling;
	Node sLeft = sibling.getLeftSibling();

	tLeft.setRightSibling(sLeft);
	sLeft.setRightSibling(this);

	this.setLeftSibling(sLeft);
	sibling.setLeftSibling(tLeft);
    }

    public void removeSibling() {
	this.getLeftSibling().setRightSibling(this.rightSibling);
	this.getRightSibling().setLeftSibling(this.leftSibling);
	this.setRightSibling(this);
	this.setLeftSibling(this);
    }

    public void remove() {
	if (this.parent != null) {
	    // node have parent
	    if (this == this.rightSibling)
		this.parent.setChildren(null);
	    else
		this.parent.setChildren(this.rightSibling);
	    this.parent.rank--;
	}

	if (this != this.rightSibling) this.removeSibling();
	this.setParent(null);
	this.mark = false;
    }
}
