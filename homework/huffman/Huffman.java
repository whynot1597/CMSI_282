package huffman;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Huffman instances provide reusable Huffman Encoding Maps for
 * compressing and decompressing text corpi with comparable
 * distributions of characters.
 */
public class Huffman {
    
    // -----------------------------------------------
    // Construction
    // -----------------------------------------------

    private HuffNode trieRoot;
    private Map<Character, String> encodingMap;
    
    /**
     * Creates the Huffman Trie and Encoding Map using the character
     * distributions in the given text corpus
     * @param corpus A String representing a message / document corpus
     *        with distributions over characters that are implicitly used
     *        throughout the methods that follow. Note: this corpus ONLY
     *        establishes the Encoding Map; later compressed corpi may
     *        differ.
     */
    Huffman (String corpus) {
        PriorityQueue<HuffNode> trieQueue = new PriorityQueue<HuffNode>();
        for (int i = 0; i < corpus.length(); i++) {
        	char toTest = corpus.charAt(i);
        	Boolean contains = false;
        	for (HuffNode testNode : trieQueue) {
        		if (testNode.character == toTest) {
        			contains = true;
        		}
        	}
        	if (!contains) {
        		int count = 0;
        		for (int j = i; j < corpus.length(); j++) {
        			if (corpus.charAt(j) == toTest) {
        				count++;
        			}
        		}
        		trieQueue.add(new HuffNode(toTest, count));
        	}
        }
        createTrie(trieQueue);
        encodingMap = new HashMap<Character, String>();
        createMap(trieRoot);
    }
    
    
    // -----------------------------------------------
    // Compression
    // -----------------------------------------------
    
    /**
     * Compresses the given String message / text corpus into its Huffman coded
     * bitstring, as represented by an array of bytes. Uses the encodingMap
     * field generated during construction for this purpose.
     * @param message String representing the corpus to compress.
     * @return {@code byte[]} representing the compressed corpus with the
     *         Huffman coded bytecode. Formatted as 3 components: (1) the
     *         first byte contains the number of characters in the message,
     *         (2) the bitstring containing the message itself, (3) possible
     *         0-padding on the final byte.
     */
    public byte[] compress (String message) {
    	ByteArrayOutputStream answer = new ByteArrayOutputStream();
    	answer.write(message.length());
    	String toAdd = "";
    	for (int i = 0; i < message.length(); i++) {
    		toAdd = toAdd + encodingMap.get(message.charAt(i));
    	}
    	while (toAdd.length() % 8 != 0) {
    		toAdd = toAdd + "0";
    	}
    	for (int j = 0; j + 7 < toAdd.length(); j += 8) {
    		String stringByte = toAdd.substring(j, j + 8);
    		int toAddByte = Integer.parseInt(stringByte, 2);
    		answer.write((byte) toAddByte);
    	}
    	return answer.toByteArray();
    }
    
    
    // -----------------------------------------------
    // Decompression
    // -----------------------------------------------
    
    /**
     * Decompresses the given compressed array of bytes into their original,
     * String representation. Uses the trieRoot field (the Huffman Trie) that
     * generated the compressed message during decoding.
     * @param compressedMsg {@code byte[]} representing the compressed corpus with the
     *        Huffman coded bytecode. Formatted as 3 components: (1) the
     *        first byte contains the number of characters in the message,
     *        (2) the bitstring containing the message itself, (3) possible
     *        0-padding on the final byte.
     * @return Decompressed String representation of the compressed bytecode message.
     */
    public String decompress (byte[] compressedMsg) {
        int size = compressedMsg[0];
        String byteString = "";
        for (int i = 1; i < compressedMsg.length; i++) {
        	byte b = compressedMsg[i];
        	byteString = byteString + String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        }
        String toAdd = "";
        String answer = "";
        
        Set<Map.Entry<Character, String>> entrySet = encodingMap.entrySet();
        
        for (int i = 0; i < byteString.length() && answer.length() < size; i++) {
        	toAdd = toAdd + byteString.charAt(i);
        	if (encodingMap.containsValue(toAdd)) {
        		answer = answer + getKey(toAdd, entrySet);
        		toAdd = "";
        	}
        }
        return answer;
        
    }
    
    private void createTrie(PriorityQueue<HuffNode> trieQueue) {
    	while (trieQueue.size() > 1) {
    		HuffNode first = trieQueue.poll();
    		HuffNode second = trieQueue.poll();
    		HuffNode toAdd = new HuffNode('\0' , first.count + second.count);
    		toAdd.right = second;
    		toAdd.left = first;
    		trieQueue.add(toAdd);
    	}
    	trieRoot = trieQueue.poll();
    }
    
    private void createMap(HuffNode trieRoot) {
    	String code = "";
    	generateCode(trieRoot, code);
    }
    
    private void generateCode(HuffNode currentNode, String currentCode) {
    	if (currentNode.isLeaf()) {
    		encodingMap.put(currentNode.character, currentCode);
    	} else {
    		generateCode(currentNode.left, currentCode + "0");
    		generateCode(currentNode.right, currentCode + "1");
    	}
    }
    
    private char getKey(String value, Set<Map.Entry<Character, String>> entrySet) {
    	for (Map.Entry<Character, String> entry : entrySet) {
    		if (entry.getValue().equals(value)) {
    			return entry.getKey();
    		}
    	}
    	return '\0';
    }
    
    // -----------------------------------------------
    // Huffman Trie
    // -----------------------------------------------
    
    /**
     * Huffman Trie Node class used in construction of the Huffman Trie.
     * Each node is a binary (having at most a left and right child), contains
     * a character field that it represents (in the case of a leaf, otherwise
     * the null character \0), and a count field that holds the number of times
     * the node's character (or those in its subtrees) appear in the corpus.
     */
    private static class HuffNode implements Comparable<HuffNode> {
        
        HuffNode left, right;
        char character;
        int count;
        
        HuffNode (char character, int count) {
            this.count = count;
            this.character = character;
        }
        
        public boolean isLeaf () {
            return left == null && right == null;
        }
        
        public int compareTo (HuffNode other) {
            return this.count - other.count;
        }
        
    }

}
