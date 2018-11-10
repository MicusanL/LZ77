package SlidingWindow;

import bitreaderwriter.BitReader;
import bitreaderwriter.BitWriter;
import bitreaderwriter.Constants;

public class Commons {

    private short offsetBitsNumber;
    private short lengthBitsNumber;
    private int windowLength;
    private int borderIndex;
    private int[] window;
    BitReader bitReaderInstance;
    BitWriter bitWriterInstance;
    int bytesLeftToRead;

    public Commons(short offset, short length, String inputFile, String outputfile) {

        offsetBitsNumber = offset;
        lengthBitsNumber = length;

        windowLength = Constants.POWER_OF_2[offset] + Constants.POWER_OF_2[length];
        borderIndex = Constants.POWER_OF_2[offset];
        window = new int[windowLength];

        bitReaderInstance = new BitReader(inputFile);
        bitWriterInstance = new BitWriter(outputfile);

        bytesLeftToRead = bitReaderInstance.fileLength;
    }

    public void codeFile() {

        int lastIndexLookAhead = fillLookAheadBuffer(borderIndex);

        boolean fileFinished = false;

        while (lastIndexLookAhead != borderIndex) {

            boolean tokenFound = false;
            int length = 0;
            int offset = borderIndex - 1;

            while (!tokenFound) {

                int tempIndexSB = searchCharacters(offset, length + 1);
                if (tempIndexSB == -1) {
                    tokenFound = true;
                } else {
                    length++;
                    offset = tempIndexSB;
                }

            }
            if (borderIndex + length == lastIndexLookAhead) {
                length--;
            }

            int numberOfCharactersToShift = length + 1;
            System.out.println(length + " " + (borderIndex - 1 - offset) + " " + window[borderIndex + length]);
            writeToken(length, borderIndex - 1 - offset, window[borderIndex + length]);

            shiftBuffer(numberOfCharactersToShift);

            if (lastIndexLookAhead == windowLength) {
                lastIndexLookAhead = fillLookAheadBuffer(windowLength - numberOfCharactersToShift);
            } else {
                lastIndexLookAhead -= numberOfCharactersToShift;

            }

            //window<<length // length+1??
            //fillLookAheadBuffer(windowLength - length)?
            // offsetFinal = borderIndex - 1 - offset !!!!!!!!!!!!!!!!!1
            if (bytesLeftToRead == 0) {
                fileFinished = true;
            }
        }

    }

    private void writeToken(int length, int offset, int newCharacter) {

        bitWriterInstance.WriteNBits(length, lengthBitsNumber);
        bitWriterInstance.WriteNBits(offset, offsetBitsNumber);
        bitWriterInstance.WriteNBits(newCharacter, 8);

    }

    private void shiftBuffer(int difference) {
        for (int i = 0; i < windowLength - difference; i++) {
            window[i] = window[i + difference];
        }
    }

    /* index from SB or -1
     * offsetFinal = borderIndex - 1 - offset */
    private int searchCharacters(int oldOffset, int sizeBufferToSearch) {

        // to check sizeBufferToSearch > LAB.length & oldOffset > SB.length
        for (int i = oldOffset; i >= 0; i--) {

            boolean sequenceFound = true;
            for (int j = 0; j < sizeBufferToSearch; j++) {
                if (window[i + j] != window[borderIndex + j]) {
                    sequenceFound = false;
                }
            }

            if (sequenceFound) {
                return i;
            }
        }

        return -1;
    }

    private void printLookAheanBuffer(int lastindex) {
        for (int i = borderIndex; i < lastindex; i++) {
            System.out.println(i + " " + window[i]);
        }
    }

    private int fillLookAheadBuffer(int startIndex) {

        if (bytesLeftToRead > windowLength - startIndex) {

            for (int i = startIndex; i < windowLength; i++) {
                window[i] = bitReaderInstance.ReadNBits(Constants.WORD_BITS_NUMBER);
                bytesLeftToRead--;
            }
            return windowLength;

        } else {

            int lastIndex = startIndex + bytesLeftToRead;
            for (int i = startIndex; i < lastIndex; i++) {
                window[i] = bitReaderInstance.ReadNBits(Constants.WORD_BITS_NUMBER);
                bytesLeftToRead--;
            }
            return lastIndex;
        }

    }
}
