package LearnUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Trie {

    private final TrieNode root;
    public int limit;

    public Trie(int limit) {
        root = new TrieNode();
        this.limit = limit;
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

        String[] words = sentence.split("\\W+");
        for (String c : words) {
            this.insert(c);
        }


    }


    public List<String> fuzzyAutocomplete(String prefix) {
        List<String> results = new ArrayList<>();
        if (prefix.isEmpty()) return results;

        // Obtem as letras vizinhas
        char first = Character.toLowerCase(prefix.charAt(0));
        List<Character> candidates = neighbors.getOrDefault(first, Arrays.asList(first));

        // Tenta encontrar palavras a partir dos vizinhos
        for (char c : candidates) {
            if (results.size() >= limit) break;
            String newPrefix = c + prefix.substring(1);
            List<String> partial = autocomplete(newPrefix);
            for (String word : partial) {
                if (results.size() >= limit) break;
                if (!results.contains(word)) {
                    results.add(word);
                }
            }
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

    public List<String> autocomplete(String prefix) {
        List<String> results = new ArrayList<>();

        for (int len = prefix.length(); len >= 0; len--) {
            String subPrefix = prefix.substring(0, len);
            TrieNode node = root;


            boolean exists = true;
            for (char c : subPrefix.toCharArray()) {
                node = node.children.get(c);
                if (node == null) {
                    exists = false;
                    break;
                }
            }

            if (exists) {
                dfs(node, new StringBuilder(subPrefix), results);
                if (!results.isEmpty()) {
                    return results; // Retorna o resultado
                }
            }
        }

        return fuzzyAutocomplete(prefix); // caso nao encontre nenhum, retorna a contar com as letras a volta
    }

    // Depth-first search
    private void dfs(TrieNode node, StringBuilder prefix, List<String> results) {
        if (results.size() >= limit) return;
        if (node.isWord) results.add(prefix.toString());
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            prefix.append(entry.getKey());
            dfs(entry.getValue(), prefix, results);
            prefix.deleteCharAt(prefix.length() - 1); // backtrack
        }
    }
}
