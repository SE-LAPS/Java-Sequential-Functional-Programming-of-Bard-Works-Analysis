package vandy.mooc.functional;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static vandy.mooc.functional.OrdinalSuffix.getOrdinalSuffix;

/**
 * This program implements a sequential app that computes the
 * Flesch-Kincaid grade level score for each play written by William
 * Shakespeare.  It emphasizes the use of modern Java functional
 * programming features (such as lambda expressions, method
 * references, and functional interfaces).
 */
@SuppressWarnings({"RegExpRedundantEscape", "RegExpRepeatedSpace"})
public class BardPlayAnalyzer
       implements Runnable {
    /**
     * A {@link Map} that associates the titles (key) of Shakespeare
     * plays with their content (value).
     */
    private static final Map<String, String> mBardMap = FileUtils
        .loadMapFromFolder("plays", ".txt");

    /*
     * The following fields are used to strip out non-essential
     * portions of Shakespeare's plays to make the Fresh-Kincaid grade
     * level score computations more accurate.
     */

    /**
     * Act and scene headers, stage directions, and line numbers
     * to remove from each work of Shakespeare.
     */
    private static final String sNON_ESSENTIAL_PORTIONS_REGEX =
        "(?i)(ACT [IVX]+\\.|Scene [IVX]+\\.|\\[.*?\\]|\\d+\\.|SCENE\\.)";

    /**
     * Regular expression used to remove the character names in
     * Shakespeare's plays, leaving only the lines they speak.
     */
    private static final String sCHARACTER_NAME_REGEX =
        "^  [A-Za-z]+\\.";

    /**
     * End of line regex.
     */
    public static final String sEOL_REGEX = "[\\r\\n]+";

    /**
     * This is the main entry point into the program.
     */
    static public void main(String[] args) {
        System.out.println("Starting Sequential BardPlayAnalyzer");

        RunTimer
            // Record the time needed to run the Flesch-Kincaid
            // analysis on all plays by William Shakespeare.
            .timeRun(() ->
                     // Run the Flesch-Kincaid grade level score
                     // analysis sequentially.
                     new BardPlayAnalyzer().run(),
                     "Sequential BardPlayAnalyzer");

        // Display the timing results.
        display(RunTimer.getTimingResults());

        System.out.println("Ending Sequential BardPlayAnalyzer");
    }

    /**
     * Run the Flesch-Kincaid grade level score analysis sequentially.
     */
    @Override
    public void run() {
        // Run the analysis sequentially.
        var results = runAndReturnResults();

        // Sort and print the results.
        printResults(results);
    }

    /**
     * Run the Flesch-Kincaid grade level score analysis sequentially
     * and return an {@link Array} of results.
     */
    public Array<String> runAndReturnResults() {
        // Create an empty Array (which was implemented
        // in Assignment 1).
        Array<String> results = new Array<>();

        // Process each play sequentially and update the Array of
        // results.
        mBardMap
            // Convert the Map into a Set of Map.Entry objects,
            // which contain the title and the contents of each
            // Shakespeare play.
            .entrySet()

            // Process each Bard play in the Map and add the
            // output to the Array of results,
            .forEach(entry ->
                     results.add(processInput(entry)));

        // Return the results;
        return results;
    }

    /**
     * This method runs in a background {@link Thread} and processes
     * the {@code entry} to compute the Flesch-Kincaid grade level
     * score.
     *
     * @param entry The Bard {@link Map.Entry} to process
     * @return A {@link String} containing the results of the
     *         Flesch-Kincaid grade level computation
     */
    private String processInput(Map.Entry<String, String> entry) {
        // Get the play's title.
        var title = entry.getKey();

        // Get the play's contents.
        var contents = entry.getValue();

        // Calculate the Flesch-Kincaid grade level score for a play.
        var gradeLevelScore = FleschKincaidGradeLevelCalculator
            .calculate(stripNonEssentialPortions(contents));

        // Return the formatted results of the calculation.
        return makeStringResult(title, gradeLevelScore);
    }

    /**
     * Make a string that contains the grade level score for the
     * provided {@code activeObject}.
     *
     * @param title The title of the play
     * @param gradeLevelScore The grade level score for the provided
     *                        {@code activeObject}
     * @return A {@link String} containing the grade level score for
     *         the provided {@code activeObject}
     */
    @NotNull
    private static String makeStringResult(String title,
                                           Double gradeLevelScore) {
        // Format the grade level score as a string with two decimal
        // places, append the ordinal suffix (e.g., "1st", "2nd",
        // "3rd", etc.) of the score to indicate the grade level,
        // append the title for which the score was calculated, and
        // then return the formatted String.
        return String.format("%.2f", gradeLevelScore)
            + " ("
            + getOrdinalSuffix(gradeLevelScore)
            + " grade) is the score for "
            + title;
    }

    /**
     * Print the results of the Flesch-Kincaid grade level score
     * analysis in sorted order.
     *
     * @param results An {@link Array} of results to print
     */
    private void printResults(Array<String> results) {
        // Convert the Array into a List.
        List<String> list = results.asList();

        // Sort the results.
        list.sort(Collections.reverseOrder());

        list
            // Print the sorted results of the calculation.
            .forEach(BardPlayAnalyzer::display);
    }

    /**
     * Strip non-essential portions of a Shakespeare {@code play}.
     *
     * @param play The play to strip
     * @return A play stripped of non-essential portions
     */
    public String stripNonEssentialPortions(String play) {
        // Remove act and scene headers, stage directions, and line
        // numbers.
        String strippedPlay = play
            .replaceAll(sNON_ESSENTIAL_PORTIONS_REGEX,
                        "");

        // Remove character names, keeping only their lines.
        Pattern pattern = Pattern
            .compile(sCHARACTER_NAME_REGEX,
                     Pattern.MULTILINE);

        // Create a Matcher for the pattern.
        Matcher matcher = pattern
            .matcher(strippedPlay);

        // Replace everything that matches with "".
        strippedPlay = matcher.replaceAll("");

        // Remove excessive line breaks.
        strippedPlay = strippedPlay
            .replaceAll(sEOL_REGEX,
                        System.lineSeparator());

        // Remove leading and trailing spaces.
        return strippedPlay.trim();
    }

    /**
     * Display the {@link String} to the output.
     *
     * @param string The {@link String} to display
     */
    private static void display(String string) {
        System.out.println("["
                           + Thread.currentThread().threadId()
                           + "] "
                           + string);
    }
}

