import LearnUtils.Predict;
import LearnUtils.Trie;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Ambiente extends JFrame {

    private Trie pt;

    private JTextArea textArea;
    private JPanel suggestionPanel;

    public Ambiente() {
        super("Predict");

        // Load predictor
        pt = new Trie(2);
        pt.learn(new File("src/Assets/FeedingText.txt"));

        // Window setup
        this.setSize(400, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);

        // TextArea
        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 20));
        textArea.setBounds(20, 20, 360, 150);
        this.add(textArea);

        // Panel for suggestions
        suggestionPanel = new JPanel();
        suggestionPanel.setLayout(new GridLayout(4, 1, 5, 5)); // 4 rows
        suggestionPanel.setBounds(20, 200, 360, 150);
        this.add(suggestionPanel);

        // Key listener updates suggestions
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateSuggestions();
            }
        });

        this.setVisible(true);
    }

    private void updateSuggestions() {
        suggestionPanel.removeAll();


        List<String> suggestions = new ArrayList<>();
        if(textArea.getText().isBlank()){
            suggestions = new ArrayList<>();
        }
        else {
            String[] text = textArea.getText().split(" ");
            suggestions = pt.autocomplete(text[text.length-1]);
        }
        for (String s : suggestions) {
            JButton lbl = new JButton(s);
            lbl.setOpaque(true);
            lbl.setBackground(Color.WHITE);
            lbl.setBorder(new LineBorder(Color.BLACK, 1));
            lbl.setFont(new Font("Arial", Font.PLAIN, 16));
            lbl.setHorizontalAlignment(SwingConstants.CENTER);

            lbl.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    textArea.setText(s);
                }
            });

            suggestionPanel.add(lbl);
        }

        suggestionPanel.revalidate();
        suggestionPanel.repaint();
    }

    public static void main(String[] args) {
        new Ambiente();
    }
}
