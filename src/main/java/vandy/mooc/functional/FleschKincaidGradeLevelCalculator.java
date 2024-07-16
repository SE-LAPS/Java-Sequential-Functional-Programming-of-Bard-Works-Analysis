package vandy.mooc.functional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.BreakIterator;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The methods in this class calculate the Flesch-Kincaid grade level
 * score for a given text (see <A
 * HREF="https://en.wikipedia.org/wiki/Flesch-Kincaid_readability_tests">this
 * link</A> for information on the Flesch-Kincaid grade level score).
 *
 * This class uses Java functional programming features (such as
 * lambda expressions, method references, and functional interfaces)
 * in conjunction with the Java {@link BreakIterator} to compute the
 * Flesch-Kincaid grade level score.
 */ 
public class FleschKincaidGradeLevelCalculator {
    /**
     * Calculates the Flesch-Kincaid grade level score for the given
     * {@code text}.
     *
     * @param text The text of a work to analyze
     */
    public static double calculate(String text) {
        // Ensure that the text is not null or blank.
        if (text == null || text.isBlank()) {
            return 0.0;
        }

        // Count the number of sentences.
        int sentenceCount = countSentences(text);
        // System.out.println("sentences = " + sentenceCount);

        // Count the number of words.
        int wordCount = countWords(text);

        // System.out.println("words = " + wordCount);

        // Count the number of syllables.
        int syllableCount = countSyllables(text);

        // System.out.println("syllables = " + syllableCount);

        // Compute the Flesch-Kincaid grade level score.
        return computeFleschKincaidGradeLevelScore(sentenceCount,
                                                   wordCount,
                                                   syllableCount);
    }

    /**
     * Compute the Flesch-Kincaid grade level score for the given
     * parameters.
     *
     * @param sentenceCount The number of sentences
     * @param wordCount     The number of words
     * @param syllableCount The number of syllables
     * @return The Flesch-Kincaid grade level score
     */
    public static double computeFleschKincaidGradeLevelScore
        (int sentenceCount,
         int wordCount,
         int syllableCount) {
        // Apply the Flesch-Kincaid formula to return the grade level
        // score.
        return (0.39 * (double) wordCount / sentenceCount)
            + (11.8 * (double) syllableCount / wordCount)
            - 15.59;
    }

    /**
     * Count the number of sentences in the {@code text}.
     *
     * @param text The text to count sentences in
     * @return A count of the number of sentences in the text
     */
    static int countSentences(String text) {
        // This method calls the countItems() helper method with the
        // parameters needed to count the number of sentences in the
        // text parameter.  The Predicate parameter should always
        // return true and the Function parameter should always return
        // 1.

        // TODO -- you fill in here, replacing 'return 0' with
        // the proper code.
        return 0;
    }

    /**
     * Count the number of words in the {@code text}.
     *
     * @param text The text to count words in
     * @return A count of the number of words in the text
     */
    static int countWords(String text) {
        // This method calls the countItems() helper method with the
        // parameters needed to count the number of words in the text
        // parameter.  The Predicate parameter should return true if
        // the first character of a word is a letter or digit and the
        // Function parameter should always return 1.

        // TODO -- you fill in here, replacing 'return 0' with the
        // proper code.
        return 0;
    }

    /**
     * Count the number of syllables in the {@code text}.
     *
     * @param text The text to count syllables in
     * @return A count of the number of syllabes in the text
     */
    static int countSyllables(String text) {
        // This method calls the countItems() helper method with the
        // parameters needed to count the number of words in the text
        // parameter.  The Predicate parameter should return true if
        // the first character of a word is a letter or digit. The
        // Function parameter should pass the countSyllablesInWord
        // method reference.

        // TODO -- you fill in here, replacing 'return 0' with
        // the proper code.
        return 0;
    }

    /**
     * Use the {@link BreakIterator} to iterate through the {@code
     * text} and apply the {@code mapper} to count the number of items
     * that match the {@code predicate}.
     *
     * @param text      The text to count items in
     * @param iterator  The {@link BreakIterator} to use to count items
     * @param predicate The {@link Predicate} to match items
     * @param mapper    The {@link Function} to map items to counts
     * @return A count of the number of matching items in the text
     */
    static int countItems(String text,
                          @NotNull BreakIterator iterator,
                          @Nullable Predicate<String> predicate,
                          @Nullable Function<String, Integer> mapper) {
        // Use the BreakIterator to iterate through the text until
        // it's done.  At each step during the iteration create a
        // substring containing the contents between the previous and
        // current boundaries of the text.  Then test this substring
        // to see if it matches the given predicate.  If so, apply the
        // mapper Function to increment a running count the number of
        // items that match the predicate and return this count at the
        // end of the method.

        // TODO -- you fill in here, replacing 'return 0' with the
        // proper code.
        return 0;
    }

    /**
     * Count the number of syllables in the {@code word}.
     *
     * @param word The {@code word} to count syllables in
     * @return The number of syllables in the {@code word}
     */
    private static int countSyllablesInWord(String word) {
        // Return 0 if the word is empty.
        if (word == null || word.isEmpty()) {
            return 0;
        }

        // Lowercase the word.
        word = word.toLowerCase();

        // Create a Predicate that tests if a character is a vowel.
        Predicate<Character> isVowel =
            c -> "aeiouy".indexOf(Character.toLowerCase(c)) != -1;

        int syllableCount = 0;
        boolean previousCharIsVowel = false;

        // Iterate through all the characters in the word.
        for (char c : word.toCharArray()) {
            // Determine if the current character is a vowel.
            boolean currentCharIsVowel = isVowel.test(c);

            // Increment the syllable count if the current character
            // is a vowel and the previous character was not a vowel.
            //
            // Note: If the current character is a vowel, then the
            // previous character must also be a vowel.  This is
            // because the current character is part of a vowel
            // digraph.
            //
            // For example, "eyes" is a vowel digraph.
            //
            // If the current character is not a vowel, then the
            // previous character must not be a vowel.  This is
            // because the current character is not part of a vowel
            // digraph.
            //
            // For example, "chair" is not a vowel digraph.
            if (currentCharIsVowel && !previousCharIsVowel) {
                syllableCount++;
            }

            // Update the previous character to the current
            // character.
            previousCharIsVowel = currentCharIsVowel;
        }

        // Remove one syllable for words ending with an 'e' that is
        // not part of a vowel digraph.
        if (word.endsWith("e") 
            && !previousCharIsVowel && syllableCount > 0) {
            syllableCount--;
        }

        // Return the syllable count.
        return syllableCount;
    }
}
