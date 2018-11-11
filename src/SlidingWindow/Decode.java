package SlidingWindow;

import javax.swing.*;

public class Decode {
    JFileChooser fileChooser = new JFileChooser();

    public void DecodeFileUsingLz77() {

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            String inputFile = fileChooser.getSelectedFile().toString();
            String outputFile = getOutputFileName(inputFile);

            Commons codeLZ77 = new Commons(inputFile, outputFile);
            codeLZ77.readHeader();
            codeLZ77.decodeFile();

            System.out.println("DeCode finished");
            System.exit(0);
        }
    }

    private String getOutputFileName(String inputFile) {
        String[] parts = inputFile.split("\\\\"); // regex: need to escape dot
        String outputFile = parts[parts.length - 1]; // outputs "en"
        parts = outputFile.split("\\.");
        outputFile = "C://Users//lidia//Desktop//Shannon//" + parts[0] + "-Lz77-Decrypted." + parts[1];

        return outputFile;
    }

}

