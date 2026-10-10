/**
 * Represents a shy symbol.
 *
 * The symbol changes its visibility every time
 * it is selected.
 *
 * @author Cristian Anzola, Danik Castañeda
 * @version 2
 */
public class ShySymbol extends Symbol
{
    private boolean shyVisible;

    /**
     * Constructor for objects of class ShySymbol.
     *
     * @param color color of the symbol
     */
    public ShySymbol(String color)
    {
        super(color);
        shyVisible = false;
    }

    /**
     * Alternates the visibility state of the symbol.
     *
     * The symbol is only drawn by show(), so a machine
     * that is invisible never opens the canvas.
     */
    @Override
    public void selected()
    {
        shyVisible = !shyVisible;
    }

    /**
     * Returns the visibility state of the shy symbol.
     *
     * @return true if the symbol must be shown
     */
    @Override
    public boolean isVisible()
    {
        return shyVisible;
    }

    /**
     * Shows the symbol only if its state is visible.
     */
    @Override
    public void show()
    {
        if (shyVisible) {
            makeVisible();
        }
        else {
            makeInvisible();
        }
    }
}