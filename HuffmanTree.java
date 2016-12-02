package hw8;

import java.util.*;

public class HuffmanTree {
	public Node root;
	public Map<Integer, String> encodes;
	public Map<String, Integer> decodes;
	
	
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
		if (node.left != null && node.right != null)  {
			buildEncodes(node.left, str + "0");
			buildEncodes(node.right, str + "1");
		} else {
			encodes.put((int) node.ch, str);
		}
	}



	
	public void encode(BitInputStream in, BitOutputStream out) {
		int index = 0;
		boolean go = true;
		
		while(go) {
			if(in.hasBits()) {
				index = in.readBits(8);
			} else {
				index = 256;
				go = false;
			}
			
			String treePath = this.encodes.get(index);
			for(int i = 0; i < treePath.length(); i++) {
				if(treePath.charAt(i) == '1')  {
					out.writeBit(1);
				}
				if(treePath.charAt(i) == '0')  {
					out.writeBit(0);
				} else {
					throw new IllegalArgumentException("Not binary");
				}
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
	
	public static void main(String[] args) {
		Map<Short, Integer> m = new HashMap<Short, Integer>();
		for(int i = 0; i < 4; i++) {
			m.put((short) i, i + 1);
		}
		HuffmanTree tree = new HuffmanTree(m);
		tree.inOrder();
	}

	
}