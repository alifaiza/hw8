package hw8;

import java.io.IOException;
import java.util.*;

public class GrinDecoder {
	public void decode(String infile, String outfile) throws IOException {
		BitInputStream in = new BitInputStream(infile);
		BitOutputStream out = new BitOutputStream(outfile);
		int magic = in.readBits(32);
		if (magic != 1846) {
			throw new IllegalArgumentException("This is not a .grin file");
		}
		int codes = in.readBits(32);
		Map<Short, Integer> map = new HashMap<Short, Integer>(codes);
		for(int i = 0; i < codes; i++) {
			map.put((short) in.readBits(16), in.readBits(32));
		}
		HuffmanTree tree = new HuffmanTree(map);
		tree.decode(in, out);
		in.close();
		out.close();
	}
}