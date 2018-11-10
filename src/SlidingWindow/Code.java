package SlidingWindow;

import javax.swing.*;

public class Code {
    JFileChooser fileChooser = new JFileChooser();

    public void CodeFileUsingLz77(final short offset, final short length) {

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            String inputFile = fileChooser.getSelectedFile().toString();
            String outputFile = getOutputFileName(inputFile);

            Commons codeLZ77 = new Commons(offset, length, inputFile, outputFile);
            codeLZ77.codeFile();

        }
    }

    private String getOutputFileName(String inputFile) {
        String[] parts = inputFile.split("\\\\"); // regex: need to escape dot
        String outputFile = parts[parts.length - 1]; // outputs "en"
        parts = outputFile.split("\\.");
        outputFile = "C://Users//lidia//Desktop//Shannon//" + parts[0] + "-Lz77-encrypted." + parts[1];

        return outputFile;
    }
}
