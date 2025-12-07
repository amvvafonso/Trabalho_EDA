import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PhoneKeyboardPanel extends JPanel {

    private boolean capsLock = false;

    public PhoneKeyboardPanel(JTextArea textArea) {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 2, 2, 2);

        ActionListener keyListener = e -> {
            String key = ((JButton) e.getSource()).getText();
            if (key.equals("SPACE")) {
                textArea.append(" ");
            } else if (key.equals("⌃")) { // caps symbol
                capsLock = !capsLock;
                updateLetterKeys();
            } else if (key.equals("⌫")) {
                String current = textArea.getText();
                if (!current.isEmpty()) {
                    textArea.setText(current.substring(0, current.length() - 1));
                }
            } else {
                if (capsLock) textArea.append(key.toUpperCase());
                else textArea.append(key.toLowerCase());
            }
        };

        int keyWidth = 36;
        int keyHeight = 36;
        int spaceWidth = 160;
        int backWidth = 60;

        // --- Row 1 ---
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
        String row1Keys = "QWERTYUIOP";
        for (char ch : row1Keys.toCharArray()) {
            JButton btn = makeKeyButton(String.valueOf(ch), keyWidth, keyHeight);
            btn.addActionListener(keyListener);
            row1.add(btn);
        }
        c.gridx = 0; c.gridy = 0;
        add(row1, c);

        // --- Row 2 ---
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
        String row2Keys = "ASDFGHJKL";
        for (char ch : row2Keys.toCharArray()) {
            JButton btn = makeKeyButton(String.valueOf(ch), keyWidth, keyHeight);
            btn.addActionListener(keyListener);
            row2.add(btn);
        }
        c.gridy = 1;
        add(row2, c);

        // --- Row 3 ---
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
        JButton caps = makeKeyButton("⌃", keyWidth + 10, keyHeight); // caps symbol
        caps.addActionListener(keyListener);
        row3.add(caps);
        String row3Keys = "ZXCVBNM";
        for (char ch : row3Keys.toCharArray()) {
            JButton btn = makeKeyButton(String.valueOf(ch), keyWidth, keyHeight);
            btn.addActionListener(keyListener);
            row3.add(btn);
        }
        JButton backspace = makeKeyButton("⌫", backWidth, keyHeight);
        backspace.addActionListener(keyListener);
        row3.add(backspace);
        c.gridy = 2;
        add(row3, c);

        // --- Row 4: Space ---
        JPanel row4 = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
        JButton space = makeKeyButton("SPACE", spaceWidth, keyHeight);
        space.addActionListener(keyListener);
        row4.add(space);
        c.gridy = 3;
        add(row4, c);
    }

    private void updateLetterKeys() {
        for (Component comp : getComponents()) {
            if (comp instanceof JPanel) {
                for (Component btnComp : ((JPanel) comp).getComponents()) {
                    if (btnComp instanceof JButton) {
                        JButton btn = (JButton) btnComp;
                        String text = btn.getText();
                        if (text.length() == 1 && Character.isLetter(text.charAt(0))) {
                            btn.setText(capsLock ? text.toUpperCase() : text.toLowerCase());
                        }
                    }
                }
            }
        }
    }

    private JButton makeKeyButton(String text, int width, int height) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.PLAIN, 18));
        b.setPreferredSize(new Dimension(width, height));
        return b;
    }

}
