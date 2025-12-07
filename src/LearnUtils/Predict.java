package LearnUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Scanner;

public class Predict {
    private WordTree tree = new WordTree();

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

    private boolean matchesWithNeighbors(String prefix, String word) {
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


    public void learn(File file) {
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


        for (String word : sentence.toLowerCase().split("\\W+")) {
            if (!word.isEmpty()) tree.insert(word);
        }
    }

    public List<String> suggest(String prefix, int limit) {
        prefix = prefix.toLowerCase();
        long start = System.nanoTime();

        if (prefix.isEmpty())
            return new ArrayList<>();

        List<WordNode> allWords = tree.searchAll(prefix);
        List<WordNode> filtered = new ArrayList<>();

        // Filtrar palavras que correspondem aos vizinhos
        for (WordNode node : allWords) {
            if (matchesWithNeighbors(prefix, node.word)) {
                filtered.add(node);
            }
        }

        // Ordenar por frequência (maior primeiro)
        filtered.sort((a, b) -> Integer.compare(b.frequency, a.frequency));

        // Criar lista final de palavras
        List<String> suggestions = new ArrayList<>();
        for (int i = 0; i < Math.min(limit, filtered.size()); i++) {
            if (filtered.get(i).word.equals(prefix) || filtered.get(i).word.compareTo(prefix) > 0) {
                suggestions.add(filtered.get(i).word);
            }
            else {
                suggestions.add(filtered.get(i).word);
            }
        }

        System.out.println("Tempo de procura -> " + (System.nanoTime() - start) + " ns");
        return suggestions;
    }


}
