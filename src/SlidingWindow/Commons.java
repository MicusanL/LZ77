package SlidingWindow;

import bitreaderwriter.BitReader;
import bitreaderwriter.Constants;

public class Commons {

    private int windowLength;
    private int borderIndex;
    private int[] window;
    BitReader bitReaderInstance;
    int bytesLeftToRead;

    public Commons(int offset, int length, String inputFile) {

        windowLength = Constants.POWER_OF_2[offset] + Constants.POWER_OF_2[length];
        borderIndex = Constants.POWER_OF_2[offset];
        window = new int[windowLength];
        bitReaderInstance = new BitReader(inputFile);
        bytesLeftToRead = bitReaderInstance.fileLength;
    }

    public void codeFile() {

        int lastIndexLookAhead = fillLookAheadBuffer(borderIndex);

        boolean fileFinished = false;

        while (!fileFinished) {

            boolean tookenFinded = false;
            int length = 0;
            int offset = 0;

            while (!tookenFinded) {

                int tempIndexSB = searchCharacters(offset, length + 1);
                if (tempIndexSB == -1) {
                    tookenFinded = true;
                } else {
                    length++;
                    offset = tempIndexSB;
                }

            }

            //write length,offset,window[board-length]
            //window<<length
            //fillLookAheadBuffer(windowLength - length)?

            if (bytesLeftToRead == 0) {
                fileFinished = true;
            }
        }

    }

    /* index from search-buffer OR -1 */
    private int searchCharacters(int oldOffset, int sizeBufferToSearch) {

        for (int i = oldOffset; i >= 0; i--) {

            boolean sequenceFound = true;
            for (int j = i; j < i + sizeBufferToSearch; j++) {
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
            for (int i = 0; i < lastIndex; i++) {
                window[i] = bitReaderInstance.ReadNBits(Constants.WORD_BITS_NUMBER);
                bytesLeftToRead--;
            }
            return lastIndex;
        }

    }
}
