/*
 * This explores the 'correct solution' to
 * http://softcycle.blogspot.com/2012/09/puzzle-31-ghost-of-looper.html
 * see also https://youtu.be/hcY8cYfAEwU?t=650
 */
package logicalrightshiftshort;

/**
 *
 * @author mb
 */
public final class LogicalRightShiftShort {

    private LogicalRightShiftShort() {}

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        // TODO code application logic here

        final short minusOne_Short = -1; // bitwise: 111111....
        // We will attempt to perform a logical right-shift by 1 place.
        // We expect the result to be Short.MAX_VALUE

        short surprising = minusOne_Short;
        // surprising = surprising >>> 1; // won't compile!
        surprising >>>= 1; // Unchanged! Still holds -1


        final boolean weAreSurprised = (surprising != Short.MAX_VALUE);


        // 1111... logically right-shifted by 1 should give 0111...
        final short expected_1 = logicalRightShiftShort(minusOne_Short, 1);
        final boolean myMethodWorks_1 = (expected_1 == Short.MAX_VALUE);

        final short expected_2 = logicalRightShiftShort(minusOne_Short, 0);
        final boolean myMethodWorks_2 = (expected_2 == minusOne_Short);

        final short expected_3 = logicalRightShiftShort(Short.MAX_VALUE, 1);
        final boolean myMethodWorks_3 = (expected_3 == (Short.MAX_VALUE >> 1));

        final short expected_4 = logicalRightShiftShort(Short.MIN_VALUE, 1);
        final int smaxInt = Short.MAX_VALUE;
        final int smaxIntRS1 = smaxInt >>> 1;
        final int zozzz = (smaxInt ^ smaxIntRS1); // 01000...
        final short zozzz_Short = (short)zozzz;
        final boolean myMethodWorks_4 = (expected_4 == zozzz_Short);
        // implicit conversions behave ok

        final short expected_5 = logicalRightShiftShort((short)1, 1);
        final boolean myMethodWorks_5 = (expected_5 == 0);


        int safetyCounter = 0;

        short i = -1;
        while (i != 0)
        {
            // i = i >>> 1; // won't compile
            i >>>= 1;
            ++safetyCounter;
            if (safetyCounter >= 100)
            {
                break;
            }
        }



        int safetyCounter_2 = 0;

        short j = -1;
        while (j != 0)
        {
            j = logicalRightShiftShort(j,1);
            ++safetyCounter_2;
            if (safetyCounter_2 >= 100)
            {
                break;
            }
        }


        // A tricky case. Output must be negative.

        final short shouldEqualMinusOne = logicalRightShiftShort(minusOne_Short,0);



        return;
    }

    // https://wiki.sei.cmu.edu/confluence/display/java/NUM14-J.+Use+shift+operators+correctly

    /***
     * 
     * @param input
     * @param shiftBy Will be masked by 31 by the JVM, so peculiar values are safe.
     * @return
     */
    public static short logicalRightShiftShort(final short input, final int shiftBy)
    {
        // We must be careful to correctly handle (input < 0), and where (0 == shiftBy).

        // Remember that in Java:
        // > A narrowing conversion of a signed integer to an integral type T
        // > simply discards all but the n lowest order bits,
        // > where n is the number of bits used to represent type T.
        // > In addition to a possible loss of information about the magnitude of the numeric value,
        // > this may cause the sign of the resulting value to differ from the sign of the input value.
        // https://docs.oracle.com/javase/specs/jls/se8/html/jls-5.html#jls-5.1.3

        // 1. Cast from short to int
        // 2. Mask off all but the lower 16 bits
        // 3. Do the logical shift on the int
        // 4. Convert back to short
        // 5. Return

        // It's vital that we mask *before* the shift, so that we shift a zero
        // into the 16'th bit (which becomes the M.S.B. of the output short)

        final int input_Int = (int)input;

        final int mask = -1 >>> 16; // -1 is 1111....

        final int inputMasked_Int = input_Int & mask;

        final int rightShifted_Int = inputMasked_Int >>> shiftBy;
        // Doesn't matter if we use >> or >>>
        // Trivia: just about all CPUs have instructions for both


        short ret = (short)rightShifted_Int;

        return ret;
    }

}
