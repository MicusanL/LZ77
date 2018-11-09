package SlidingWindow;

import javax.swing.*;

public class Code {
    JFileChooser fileChooser = new JFileChooser();

    public void CodeFileUsingLz77(int offset, int length) {

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            String inputFile;
            inputFile = fileChooser.getSelectedFile().toString();

            System.out.println(inputFile + "\n" + length + " " + offset);
        }
    }
}
