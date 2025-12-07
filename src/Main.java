import LearnUtils.Trie;

import java.io.File;
import java.util.*;


class main {

    // Demo
    public static void main(String[] args) {
        Trie trie = new Trie(4);
        trie.learn(new File("src/Assets/FeedingText.txt"));

        System.out.println(trie.autocomplete("ne"));;

    }
}
