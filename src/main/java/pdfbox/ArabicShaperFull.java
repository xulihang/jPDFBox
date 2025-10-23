package pdfbox;

import java.util.*;

public class ArabicShaperFull {
    // Mapping table for Arabic contextual forms:
    // key: base form, value: [isolated, final, initial, medial]
    private static final Map<Character, char[]> FORMS = new HashMap<>();

    // Letters that do NOT connect to the following character
    private static Set<Character> nonConnectableAfter;

    static {
        nonConnectableAfter = Set.of(
                '\u0627', // Alef
                '\u062F', // Dal
                '\u0630', // Thal
                '\u0631', // Reh
                '\u0632', // Zain
                '\u0648', // Waw
                '\u0623', // Alef with hamza above
                '\u0625', // Alef with hamza below
                '\u0622', // Alef with madda
                '\u0629'  // Teh marbuta
        );

        // Common Arabic letters and their contextual forms:
        // add(base, isolated, final, initial, medial)
        add('\u0627', '\uFE8D', '\uFE8E', '\uFE8D', '\uFE8E'); // Alef
        add('\u0628', '\uFE8F', '\uFE90', '\uFE91', '\uFE92'); // Beh
        add('\u062A', '\uFE95', '\uFE96', '\uFE97', '\uFE98'); // Teh
        add('\u062B', '\uFE99', '\uFE9A', '\uFE9B', '\uFE9C'); // Theh
        add('\u062C', '\uFE9D', '\uFE9E', '\uFE9F', '\uFEA0'); // Jeem
        add('\u062D', '\uFEA1', '\uFEA2', '\uFEA3', '\uFEA4'); // Hah
        add('\u062E', '\uFEA5', '\uFEA6', '\uFEA7', '\uFEA8'); // Khah
        add('\u062F', '\uFEA9', '\uFEAA', '\uFEA9', '\uFEAA'); // Dal
        add('\u0630', '\uFEAB', '\uFEAC', '\uFEAB', '\uFEAC'); // Thal
        add('\u0631', '\uFEAD', '\uFEAE', '\uFEAD', '\uFEAE'); // Reh
        add('\u0632', '\uFEAF', '\uFEB0', '\uFEAF', '\uFEB0'); // Zain
        add('\u0633', '\uFEB1', '\uFEB2', '\uFEB3', '\uFEB4'); // Seen
        add('\u0634', '\uFEB5', '\uFEB6', '\uFEB7', '\uFEB8'); // Sheen
        add('\u0635', '\uFEB9', '\uFEBA', '\uFEBB', '\uFEBC'); // Sad
        add('\u0636', '\uFEBD', '\uFEBE', '\uFEBF', '\uFEC0'); // Dad
        add('\u0637', '\uFEC1', '\uFEC2', '\uFEC3', '\uFEC4'); // Tah
        add('\u0638', '\uFEC5', '\uFEC6', '\uFEC7', '\uFEC8'); // Zah
        add('\u0639', '\uFEC9', '\uFECA', '\uFECB', '\uFECC'); // Ain
        add('\u063A', '\uFECD', '\uFECE', '\uFECF', '\uFED0'); // Ghain
        add('\u0641', '\uFED1', '\uFED2', '\uFED3', '\uFED4'); // Feh
        add('\u0642', '\uFED5', '\uFED6', '\uFED7', '\uFED8'); // Qaf
        add('\u0643', '\uFED9', '\uFEDA', '\uFEDB', '\uFEDC'); // Kaf
        add('\u0644', '\uFEDD', '\uFEDE', '\uFEDF', '\uFEE0'); // Lam
        add('\u0645', '\uFEE1', '\uFEE2', '\uFEE3', '\uFEE4'); // Meem
        add('\u0646', '\uFEE5', '\uFEE6', '\uFEE7', '\uFEE8'); // Noon
        add('\u0647', '\uFEE9', '\uFEEA', '\uFEEB', '\uFEEC'); // Heh
        add('\u0648', '\uFEED', '\uFEEE', '\uFEED', '\uFEEE'); // Waw
        add('\u064A', '\uFEF1', '\uFEF2', '\uFEF3', '\uFEF4'); // Yeh
        add('\u0621', '\uFE80', '\uFE80', '\uFE80', '\uFE80'); // Hamza
        add('\u0629', '\uFE93', '\uFE94', '\uFE93', '\uFE94'); // Teh marbuta
        add('\u0623', '\uFE83', '\uFE84', '\uFE83', '\uFE84'); // Alef with hamza above
        add('\u0625', '\uFE87', '\uFE88', '\uFE87', '\uFE88'); // Alef with hamza below
        add('\u0622', '\uFE81', '\uFE82', '\uFE81', '\uFE82'); // Alef with madda
    }

    private static void add(char base, char iso, char fin, char ini, char med) {
        FORMS.put(base, new char[]{iso, fin, ini, med});
    }

    // Returns true if a letter can connect to the next one
    private static boolean canConnectAfter(char base) {
        return !nonConnectableAfter.contains(base);
    }

    /**
     * Performs basic Arabic shaping:
     * - Converts base Arabic letters to contextual forms (isolated, initial, medial, final)
     * - Simulates ICU's ArabicShaping(LETTERS_SHAPE).shape(s)
     * - Then reverses the shaped string to display correctly in LTR rendering
     */
    public static String shape(String text) {
        char[] input = text.toCharArray();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < input.length; i++) {
            char c = input[i];
            char[] forms = FORMS.get(c);
            if (forms == null) {
                sb.append(c);
                continue;
            }

            boolean connectPrev = (i > 0 && canConnectAfter(input[i - 1]) && FORMS.containsKey(input[i - 1]));
            boolean connectNext = (i < input.length - 1 && canConnectAfter(c) && FORMS.containsKey(input[i + 1]));

            if (connectPrev && connectNext)
                sb.append(forms[3]); // medial
            else if (connectPrev)
                sb.append(forms[1]); // final
            else if (connectNext)
                sb.append(forms[2]); // initial
            else
                sb.append(forms[0]); // isolated
        }

        // Reverse for LTR environments (same as ICU .reverse())
        return sb.reverse().toString();
    }
}
