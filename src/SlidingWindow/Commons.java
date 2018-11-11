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
    int lastIndexLookAhead;

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

    public Commons(String inputFile, String outputfile) {

        bitReaderInstance = new BitReader(inputFile);
        bitWriterInstance = new BitWriter(outputfile);

        bytesLeftToRead = bitReaderInstance.fileLength;
    }

    public void writeHeader(short offset, short length) {
        bitWriterInstance.WriteNBits(offset, 4);
        bitWriterInstance.WriteNBits(length, 3);
    }

    public void readHeader() {

        offsetBitsNumber = (short) bitReaderInstance.ReadNBits(4);
        lengthBitsNumber = (short) bitReaderInstance.ReadNBits(3);
       // System.out.println(offsetBitsNumber + " " + lengthBitsNumber);
        windowLength = Constants.POWER_OF_2[offsetBitsNumber] + Constants.POWER_OF_2[lengthBitsNumber];
        borderIndex = Constants.POWER_OF_2[offsetBitsNumber];
        window = new int[windowLength];
    }

    private void interpretToken() {

        int length = bitReaderInstance.ReadNBits(lengthBitsNumber);
        int offset = bitReaderInstance.ReadNBits(offsetBitsNumber);
        int newChar = bitReaderInstance.ReadNBits(Constants.WORD_BITS_NUMBER);

        if (length != 0) {
            int lastCharacterLAB = borderIndex + length;
            for (int i = borderIndex; i < lastCharacterLAB; i++) {
                window[i] = window[i - offset - 1];

               //System.out.println(window[i]);
            }
        }
        window[borderIndex + length] = newChar;
        int numberOfCharactersToWrite = length + 1;
        writeNewCharSequence(numberOfCharactersToWrite);
        shiftBuffer(numberOfCharactersToWrite);


    }

    private void writeNewCharSequence(int length) {

        for (int i = borderIndex; i < borderIndex + length; i++) {
            bitWriterInstance.WriteNBits(window[i], Constants.WORD_BITS_NUMBER);
            //System.out.println(window[i]);
        }
    }

    public void decodeFile() {

        int tokensNumber = getTokensNumber();

        while (tokensNumber != 0) {

            interpretToken();
            tokensNumber--;

        }

    }

    private int getTokensNumber() {
        return (bitReaderInstance.fileLength * 8 - 7) / (lengthBitsNumber + offsetBitsNumber + 8);
    }

    public void codeFile() {

        lastIndexLookAhead = fillLookAheadBuffer(borderIndex);

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

            int numberOfCharactersToShift = length + 1;

            /* if length == 0 */
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

        bitWriterInstance.WriteNBits(0, 7);

    }

    private void writeToken(int length, int offset, int newCharacter) {
        //System.out.println(length + " " + offset + " " + newCharacter);
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
        if (borderIndex + sizeBufferToSearch == lastIndexLookAhead) {
            return -1;
        }

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
            //System.out.println(i + " " + window[i]);
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
