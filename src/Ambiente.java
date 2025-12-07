import LearnUtils.Trie;
import LearnUtils.WordTree;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Ambiente extends JFrame {

    private Trie trie;
    private WordTree tree;

    private JLabel resultTime;
    private JTextArea trieTextArea;
    private PhoneKeyboardPanel phoneKeyboardPanel;
    private JPanel suggestionPanel;
    private JPanel optionPanel;
    private JButton trieOption;
    private JButton treeOption;

    public String type = "";



    public Ambiente() {

        trie = new Trie(3);
        trie.learn(new File("src/Assets/FeedingText.txt"));

        tree = new WordTree();
        tree.learn(new File("src/Assets/FeedingText.txt"));


        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.setTitle("Ambiente");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 400);
        this.setResizable(false);

        optionPanel = new JPanel();
        optionPanel.setLayout(new GridLayout(1, 2));
        optionPanel.setSize(this.getWidth(), 10);

        trieOption = new JButton("Trie");
        trieOption.setOpaque(true);
        trieOption.setBorder(new LineBorder(Color.LIGHT_GRAY));
        trieOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trieOption.setBackground(Color.LIGHT_GRAY);
                treeOption.setBackground(Color.white);
                type = "trie";
            }
        });

        treeOption = new JButton("Tree");
        treeOption.setOpaque(true);
        treeOption.setBorder(new LineBorder(Color.LIGHT_GRAY));
        treeOption.setBackground(Color.white);
        trieOption.setBackground(Color.LIGHT_GRAY);

        treeOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trieOption.setBackground(Color.white);
                treeOption.setBackground(Color.LIGHT_GRAY);
                type = "tree";
            }
        });
        JLabel title = new JLabel("Selecione -> ");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setSize(this.getWidth(), 10);

        optionPanel.add(title);
        optionPanel.add(trieOption);
        optionPanel.add(treeOption);




        trieTextArea = new JTextArea();
        trieTextArea.setSize(this.getWidth(), 10);
        trieTextArea.setFont(new Font("Arial", Font.PLAIN, 18));

        resultTime = new JLabel();
        resultTime.setSize(this.getWidth(), 20);
        resultTime.setText("Time: ");
        resultTime.setFont(new Font("Arial", Font.BOLD, 20));

        phoneKeyboardPanel = new PhoneKeyboardPanel(trieTextArea);



        suggestionPanel = new JPanel();
        suggestionPanel.setLayout(new GridLayout(1, 3));
        suggestionPanel.setSize(this.getWidth(), 50);


        this.add(optionPanel);
        this.add(resultTime);
        this.add(trieTextArea);
        this.add(suggestionPanel);

        this.add(phoneKeyboardPanel);

        trieTextArea.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                updateSuggestions();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSuggestions();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSuggestions();
            }
        });



        this.setBackground(Color.gray);
        this.setVisible(true);
    }

    private void updateSuggestions() {
        long startTime = System.nanoTime();

        suggestionPanel.removeAll();

        List<String> suggestions = new ArrayList<>();
        if(trieTextArea.getText().isBlank()){
            suggestions = new ArrayList<>();
        }
        else {
            String[] text = trieTextArea.getText().split(" ");
            if (trieTextArea.getText().substring(trieTextArea.getText().length()  - 1).equals(" ")){
                suggestionPanel.removeAll();
                return;
            }
            switch (type){
                case "tree":
                    suggestions = tree.suggest(text[text.length-1], 3);
                    break;
                default:
                    suggestions = trie.autocomplete(text[text.length-1]);
                    break;
            }
        }
        for (String s : suggestions) {
            JButton lbl = new JButton(s);
            lbl.setOpaque(true);
            lbl.setBackground(Color.WHITE);
            lbl.setFont(new Font("Arial", Font.PLAIN, 16));
            lbl.setHorizontalAlignment(SwingConstants.CENTER);

            lbl.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    String[] text = trieTextArea.getText().split(" ");
                    trieTextArea.setText(trieTextArea.getText().replace(text[text.length-1], s));

                }
            });

            suggestionPanel.add(lbl);
            resultTime.setText("Time: " + (System.nanoTime() - startTime) / 1000000 + "ms");
        }






        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        new Ambiente();
    }


}
