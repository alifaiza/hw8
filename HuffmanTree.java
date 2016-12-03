package hw8;

import java.io.IOException;
import java.util.*;

public class HuffmanTree {
	public Node root;
	public Map<Short, String> encodes;
	
	public HuffmanTree(Map<Short, Integer> m) {
		m.put((short) 256, 1);
		PriorityQueue<Node> queue = new PriorityQueue<Node>();
		Iterator<Short> iter = m.keySet().iterator();
		while(iter.hasNext()) {
			Short c = (Short) iter.next();
			queue.add(new Node(c, m.get(c)));
		}
		while (queue.size() > 1)  {
			Node temp1 = queue.poll();
			Node temp2 = queue.poll();
			queue.add(new Node(temp1.freq + temp2.freq, temp1, temp2));
		}
		this.root = queue.poll();
		this.encodes = new HashMap<Short, String>();
		buildEncodes(this.root, "");
	}
	
	public void inOrder() {
		inOrder(root);
	}
	
	public static void inOrder(Node n) {
		if(n.left == null && n.right == null) {
			System.out.println("Character code: " + n.ch);
			System.out.println("Frequency: " + n.freq);
		} else if(n.left == null) {
			System.out.println("Character code: " + n.ch);
			System.out.println("Frequency: " + n.freq);
			inOrder(n.right);
		} else if(n.right == null) {
			inOrder(n.left);
			System.out.println("Character code: " + n.ch);
			System.out.println("Frequency: " + n.freq);
		} else {
			inOrder(n.left);
			System.out.println("Character code: " + n.ch);
			System.out.println("Frequency: " + n.freq);
			inOrder(n.right);
		}
	}

	private void buildEncodes(Node node, String str) {
		if (node.left == null && node.right == null) {
			encodes.put(node.ch, str);
			str = "";
		}
		if (node.left != null) {
			buildEncodes(node.left, str + "0");
		}
		if (node.right != null) {
			buildEncodes(node.right, str + "1");
		}
	}
	
	public void encode(BitInputStream in, BitOutputStream out) {
		short value = (short) in.readBits(8);
		while(value != -1) {
			String treePath = this.encodes.get((short) value);
			for(int i = 0; i < treePath.length(); i++) {
				if(treePath.charAt(i) == '1')  {
					out.writeBit(1);
					System.out.print('1');
				} else if(treePath.charAt(i) == '0')  {
					out.writeBit(0);
					System.out.print('0');
				} else {
					throw new IllegalArgumentException("Not binary");
				}
 			}
			value = (short) in.readBits(8);
			System.out.println("");
		}
		String eof = encodes.get((short) 256);
		for(int i = 0; i < eof.length(); i++) {
			if(eof.charAt(i) == '1')  {
				out.writeBit(1);
				System.out.print('1');
			} else if(eof.charAt(i) == '0')  {
				out.writeBit(0);
				System.out.print('0');
			} else {
				throw new IllegalArgumentException("Not binary");
			}
		}
	}
	
	public void decode(BitInputStream in, BitOutputStream out) {
		while(in.hasBits())  {
			Node node = this.root;
			while(node.left != null || node.right != null)  {
				if(in.readBit() == 0) {
					node = node.left;
				} else if (in.readBit() == 0) {
					node = node.right;
				}	
			}
			Short sh = (short) (int) node.ch;
			if (sh == 256)  {
				out.writeBits(sh, 9);
				return;
			} else {
				out.writeBits(sh, 8);
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		GrinEncoder g = new GrinEncoder();
		GrinDecoder u = new GrinDecoder();
		g.encode("/Users/zoegrubbs/Documents/workspace/HuffmanTree/src/huffman-example.txt", 
				"/Users/zoegrubbs/Documents/workspace/HuffmanTree/src/test.grin");
		u.decode("/Users/zoegrubbs/Documents/workspace/HuffmanTree/src/test.grin", 
				"/Users/zoegrubbs/Documents/workspace/HuffmanTree/src/test2.txt");
	}
}
