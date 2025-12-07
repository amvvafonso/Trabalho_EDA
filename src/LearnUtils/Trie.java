package LearnUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Trie {
    private TrieNode root;
    public int limit;

    public Trie(int limit) {
        root = new TrieNode();
        this.limit = limit;
    }

    public void learn(File file){
        int counter = 0;
        long startTime = System.nanoTime();
        String sentence = "";
        try (Scanner myReader = new Scanner(file)) {
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                sentence += "\n" +  data;
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        String[] words = sentence.split("\\W+");
        for (String c : words) {
            this.insert(c);
            counter++;
        }
        System.out.println("Inseridos " + counter + "caracteres \n");
        System.out.println("Demorou -> " + (System.nanoTime() - startTime) / 1000000 + "ms");

    }


    // Insert a word into the trie
    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        node.isWord = true;
    }

    // Search for an exact word
    public boolean search(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children.get(c);
            if (node == null) return false;
        }
        return node.isWord;
    }

    // Find all words with a given prefix
    public List<String> autocomplete(String prefix) {
        List<String> results = new ArrayList<>();
        TrieNode node = root;
        long start = System.nanoTime();
        // Traverse to the end of the prefix
        for (char c : prefix.toCharArray()) {
            node = node.children.get(c);
            if (node == null) return results; // no words with this prefix
        }

        // Recursively find all words starting from this node
        dfs(node, new StringBuilder(prefix), results);
        System.out.println("Demorou -> " + (System.nanoTime() - start));
        return results;
    }

    // Depth-first search helper
    private void dfs(TrieNode node, StringBuilder prefix, List<String> results) {
        if (results.size() > limit) return;
        if (node.isWord) results.add(prefix.toString());
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            prefix.append(entry.getKey());
            dfs(entry.getValue(), prefix, results);
            prefix.deleteCharAt(prefix.length() - 1); // backtrack
        }
    }
}
