package SlidingWindow;

import bitreaderwriter.BitReader;
import bitreaderwriter.Constants;

public class Commons {

    private int windowLength;
    private int borderIndex;
    private int[] window;
    BitReader bitReaderInstance;

    public Commons(int offset, int length, String inputFile) {

        windowLength = Constants.POWER_OF_2[offset] + Constants.POWER_OF_2[length];
        borderIndex = Constants.POWER_OF_2[offset];
        window = new int[windowLength];
        bitReaderInstance = new BitReader(inputFile);
    }

    public void codeFile() {

        int bytesLeftToRead = bitReaderInstance.fileLength;
        int lastIndexLookAhead = fillLookAheadBuffer(borderIndex, bytesLeftToRead);
        for (int i = borderIndex; i < lastIndexLookAhead; i++) {
            System.out.println(i + " " + window[i]);
        }

    }

    private int fillLookAheadBuffer(int startIndex, int bytesLeftToRead) {

        int lookAheadBufferBytes;

        if (bytesLeftToRead > windowLength - startIndex) {

            for (int i = startIndex; i < windowLength; i++) {
                window[i] = bitReaderInstance.ReadNBits(Constants.WORD_BITS_NUMBER);
            }
            return windowLength;
        } else {

            int lastIndex = startIndex + bytesLeftToRead;
            for (int i = 0; i < lastIndex; i++) {
                window[i] = bitReaderInstance.ReadNBits(Constants.WORD_BITS_NUMBER);
            }
            return lastIndex;
        }

    }
}
