/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dijkstra;


/**
 *
 * @author Linh
 */
enum LinkType {
    SINGLE, FIRST, SECOND
}

public class Link {
    //For draw
    LinkType type;

    //Data structure
    private Node tail;
    private Node head;
    private double metric;
    State linkState;

    public Link(Node head, Node tail, double metric) {
	this.tail = tail;
	this.head = head;
	this.metric = metric;
	this.type = LinkType.SINGLE;
	this.linkState = State.UNLABELED;
    }

    @Override
    public String toString() {
	return "Link{" + "Type=" + type + "metric=" + metric + '}';
    }

    public Node getHead() {
	return head;
    }

    public void setHead(Node head) {
	this.head = head;
    }

    public double getMetric() {
	return metric;
    }

    public void setMetric(double length) {
	this.metric = length;
    }

    public Node getTail() {
	return tail;
    }

    public void setTail(Node tail) {
	this.tail = tail;
    }
    
}
