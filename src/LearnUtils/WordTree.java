package LearnUtils;

import java.util.*;

public class WordTree {

    private WordNode root;

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




    public List<WordNode> searchAll(String prefix) {
        List<WordNode> list = new ArrayList<>();
        collect(root, list, prefix);
        return list;
    }

    private void collect(WordNode node, List<WordNode> list, String prefix) {
        if (node == null) return;
        if(prefix.compareTo(node.word) >= 0) list.add(node);
        collect(node.left, list, prefix);
        list.add(node);
        collect(node.right, list, prefix);
    }

    public void searchPrefixRec(WordNode node, String prefix, List<WordNode> list) {
        if (node == null) return;

        if (node.word.startsWith(prefix))
            list.add(node);


        if (prefix.compareTo(node.word) <= 0)
            searchPrefixRec(node.left, prefix, list);

        if (prefix.compareTo(node.word) >= 0)
            searchPrefixRec(node.right, prefix, list);
    }
}
