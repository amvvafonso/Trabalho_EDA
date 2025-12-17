package Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Trie {

    private final TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    private static final Map<Character, List<Character>> neighbors = new HashMap<>();

    static {
        neighbors.put('a', Arrays.asList('a','q','w','s','z'));
        neighbors.put('s', Arrays.asList('s','a','w','d','z'));
        neighbors.put('d', Arrays.asList('d','e','s','x','f'));
        neighbors.put('g', Arrays.asList('g','f','t','h','v'));
        neighbors.put('h', Arrays.asList('h','g','y','j','b'));
        neighbors.put('j', Arrays.asList('j','h','u','k','n'));
        neighbors.put('k', Arrays.asList('k','j','i','l','m'));
        neighbors.put('l', Arrays.asList('l','k','o','p','m'));
        neighbors.put('o', Arrays.asList('o','i','p','k','l'));
        neighbors.put('q', Arrays.asList('q','a','w'));
        neighbors.put('w', Arrays.asList('w','q','s','d'));
        neighbors.put('e', Arrays.asList('e','w','d','r'));
        neighbors.put('r', Arrays.asList('r','e','d', 'f','r'));
        neighbors.put('t', Arrays.asList('t','r','g', 'f','y'));
        neighbors.put('y', Arrays.asList('y','t','g', 'h','u'));
        neighbors.put('u', Arrays.asList('u','y','h', 'j','i'));
        neighbors.put('i', Arrays.asList('i','u','j', 'k','o'));
        neighbors.put('z', Arrays.asList('z','s','x'));
        neighbors.put('x', Arrays.asList('x','z','d', 'c'));
        neighbors.put('c', Arrays.asList('c','x','f', 'v'));
        neighbors.put('v', Arrays.asList('v','c','g', 'b'));
        neighbors.put('b', Arrays.asList('b','v','h', 'n'));
        neighbors.put('n', Arrays.asList('n','b','j', 'm'));
        neighbors.put('m', Arrays.asList('m','n','k', 'm'));
    }


    public void learn(File file){

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

        String[] words = sentence.split("[^\\p{L}]+");

        for (String c : words) {
            if (!this.search(c)){
                this.insert(c);
            }
        }


    }


    public List<String> autocomplete(String prefix, int limit) {
        prefix = prefix.toLowerCase();
        List<String> results = new ArrayList<>();
        if (prefix.isEmpty()) return results;

        // Primeira letra
        char first = prefix.charAt(0);
        List<Character> candidates = neighbors.getOrDefault(first, List.of(first));

        Set<String> seen = new HashSet<>();

        for (char c : candidates) {
            TrieNode node = root.children.get(c);
            if (node == null) continue;

            StringBuilder sb = new StringBuilder();
            sb.append(c);

            // Percorre o resto
            boolean match = true;
            for (int i = 1; i < prefix.length(); i++) {
                char ch = prefix.charAt(i);
                node = node.children.get(ch);
                if (node == null) {
                    match = false;
                    break;
                }
                sb.append(ch);
            }

            if (match) {
                dfsWithLimit(node, sb, results, seen, limit);
            }

            if (limit > 0 && results.size() >= limit) break;
        }

        return results;
    }




    // Insere a palavra
    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        node.isWord = true;
    }

    // Retorna true se existir a palavra
    public boolean search(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children.get(c);
            if (node == null) return false;
        }
        return node.isWord;
    }


    // Depth-first search com limite
    private void dfsWithLimit(TrieNode node,
                              StringBuilder sb,
                              List<String> results,
                              Set<String> seen,
                              int limit) {
        if (limit > 0 && results.size() >= limit) return;

        if (node.isWord) {
            String word = sb.toString();
            if (!seen.contains(word)) {
                results.add(word);
                seen.add(word);
                if (limit > 0 && results.size() >= limit) return;
            }
        }

        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            sb.append(entry.getKey());
            dfsWithLimit(entry.getValue(), sb, results, seen, limit);
            sb.deleteCharAt(sb.length() - 1);

            if (limit > 0 && results.size() >= limit) return;
        }
    }


}
