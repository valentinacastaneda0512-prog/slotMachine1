/**
 * Represents a symbol whose size changes according to its color.
 *
 * A yellow symbol increases its size when it is selected.
 * A red symbol decreases its size when it is selected.
 * A blue symbol keeps its normal size.
 * Any other color also keeps its normal size.
 *
 * The size of the symbol is limited to avoid values that are
 * too large or too small.
 *
 * @author Cristian Anzola
 * @author Danik Castañeda
 * @version 1
 */
public class RainbowSymbol extends Symbol {


    private static final int MAX_SIZE = 60;
    private static final int MIN_SIZE = 5;
    private static final int NORMAL_SIZE = 30;

    /**
     * Creates a RainbowSymbol with the specified color.
     *
     * @param color the color of the symbol
     */
    public RainbowSymbol(String color) {
        super(color);
    }

    /**
     * Changes the size of the symbol according to its color.
     *
     * Yellow symbols increase their size by 5 until they
     * reach the maximum size.
     *
     * Red symbols decrease their size by 5 until they
     * reach the minimum size.
     *
     * Blue symbols and symbols with any other color keep
     * their normal size.
     */ 
    @Override
    public void selected() {

        if (getColor().equals("yellow")) {

            if (getSize() < MAX_SIZE) {
                changeSize(getSize() + 5);
            }

        } else if (getColor().equals("red")) {

            if (getSize() > MIN_SIZE) {
                changeSize(getSize() - 5);
            }

        } else if (getColor().equals("blue")) {

            changeSize(NORMAL_SIZE);

        } else {

            changeSize(NORMAL_SIZE);
        }
    }
}