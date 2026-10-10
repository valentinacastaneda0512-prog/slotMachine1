/**
 * Represents an ephemeral symbol.
 *
 * The symbol decreases its size every time it is selected,
 * until it reaches the minimum size of one.
 *
 * @author Cristian Anzola, Danik Castañeda
 * @version 1
 */
public class EphemeralSymbol extends Symbol
{
    private static final int MIN_SIZE = 1;

    /**
     * Constructor for objects of class EphemeralSymbol.
     *
     * @param color color of the symbol
     */
    public EphemeralSymbol(String color)
    {
        super(color);
    }

    /**
     * Decreases the size of the symbol when selected.
     *
     * The size can never be smaller than one.
     */
    @Override
    public void selected()
    {
        if (getSize() > MIN_SIZE) {
            changeSize(getSize() - 1);
        }
    }
}