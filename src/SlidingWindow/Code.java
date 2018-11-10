package SlidingWindow;

import javax.swing.*;

public class Code {
    JFileChooser fileChooser = new JFileChooser();

    public void CodeFileUsingLz77(final int offset, final int length) {

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            String inputFile;
            inputFile = fileChooser.getSelectedFile().toString();

            Commons codeLZ77 = new Commons(offset, length, inputFile);
            codeLZ77.codeFile();

        }
    }
}
