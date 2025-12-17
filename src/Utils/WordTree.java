package Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class WordTree {

    public WordNode root;

    public void insert(String word) {
        root = insertRec(root, word);
    }

    private WordNode insertRec(WordNode root, String word) {
        if (root == null) return new WordNode(word);

        int cmp = word.compareTo(root.word);
        if (cmp < 0) root.left = insertRec(root.left, word);
        else if (cmp > 0) root.right = insertRec(root.right, word);
        else root.frequency++;

        return root;
    }

    public void learn(File file) {
        try (Scanner myReader = new Scanner(file)) {
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                for (String word : data.toLowerCase().split("[^\\p{L}]+")) {
                    if (!word.isEmpty()) this.insert(word);

                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

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

    private boolean matchNeighbor(String prefix, String word) {
        if (prefix.length() > word.length())
            return false;

        for (int i = 0; i < prefix.length(); i++) {
            char typedChar = prefix.charAt(i);
            char wordChar = word.charAt(i);

            List<Character> validChars = neighbors.get(typedChar);

            if (validChars == null)
                validChars = Arrays.asList(typedChar);

            if (!validChars.contains(wordChar))
                return false;
        }

        return true;
    }

    public List<String> suggest(String prefix, int limit) {
        prefix = prefix.toLowerCase();

        if (prefix.isEmpty())
            return new ArrayList<>();

        List<WordNode> allWords = this.searchAll(prefix);
        List<WordNode> filtered = new ArrayList<>();


        for (WordNode node : allWords) {
            if (matchNeighbor(prefix, node.word)) {
                filtered.add(node);
            }
        }

        // Ordenar por frequência (maior primeiro)
        filtered.sort((a, b) -> Integer.compare(b.frequency, a.frequency));

        // Criar lista final de palavras
        List<String> suggestions = new ArrayList<>();
        for (int i = 0; i < filtered.size(); i++) {
            if (filtered.get(i).word.equals(prefix) || filtered.get(i).word.compareTo(prefix) > 0) {
                suggestions.add(filtered.get(i).word);
            }
            else {
                suggestions.add(filtered.get(i).word);
            }
        }

        if (limit == 0){
            return suggestions;
        }
        else {
            return suggestions.stream()
                    .limit(limit)
                    .toList();

        }
    }

    public List<WordNode> searchAll(String prefix) {
        List<WordNode> list = new ArrayList<>();
        collect(root, list, prefix);
        return list;
    }

    private void collect(WordNode node, List<WordNode> list, String prefix) {
        if (node == null) return;
        collect(node.left, list, prefix);
        list.add(node);
        collect(node.right, list, prefix);
    }

}
