package hw8;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GrinEncoder {
	public Map<Short, Integer> createFrequencyMap(String file) throws IOException {
		Map<Short, Integer> map = new HashMap<Short, Integer>();
		BitInputStream in = new BitInputStream(file);
		boolean go = true;
		Short sh = (short) in.readBits(8);
		while(go) {
			if(sh == -1)  {
				go = false;
			}
			if (map.containsKey(sh)) {
				map.put(sh, map.get(sh) + 1);
			} else {
				map.put(sh, 1);
			}
			sh = (short) in.readBits(8);
		}
		in.close();
		return map;
	}
	void encode(String infile, String outfile) throws IOException {
		Map<Short, Integer> map = createFrequencyMap(infile);
		BitInputStream in = new BitInputStream(infile);
		BitOutputStream out = new BitOutputStream(outfile);
		HuffmanTree tree = new HuffmanTree(map);
		out.writeBits(1846, 32);
		out.writeBits(250, 32);
		Iterator<Short> keys = map.keySet().iterator();
		while(keys.hasNext()) {
			short key = keys.next();
			out.writeBits(key, 16);
			out.writeBits(map.get(key), 32);
		}
		tree.encode(in, out);
		in.close();
		out.close();
	}
}

