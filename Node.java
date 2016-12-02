package hw8;

public class Node implements Comparable<Node> {
	public Short ch;
	public int freq;
	public Node left;
	public Node right;
	public Node(Short ch, int freq, Node left, Node right) {
		this.ch = ch;
		this.freq = freq;
		this.left = left;
		this.right = right;
	}
	public Node(Short ch, int freq) {
		this.ch = ch;
		this.freq = freq;
		this.left = null;
		this.right = null;
	}
	public Node(int freq, Node left, Node right) {
		this.ch = null;
		this.freq = freq;
		this.left = left;
		this.right = right; 
	}
	public int compareTo(Node n) {
		if(this.freq < n.freq) {
			return -1;
		} else if(this.freq > n.freq) {
			return 1;
		} else {
			return 0;
		}
	}
}
