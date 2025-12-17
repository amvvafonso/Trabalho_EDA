package Utils;

public class WordNode {

    public String word;
    public int frequency;
    public WordNode left, right;


    WordNode(String word) {
        this.word = word;
        this.frequency = 1;
    }

}
