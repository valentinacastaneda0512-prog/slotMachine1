/**
 * Represents a shy symbol.
 *
 * The symbol changes its visibility every time
 * it is selected.
 *
 * @author Cristian Anzola, Danik Castañeda
 * @version 1
 */
public class ShySymbol extends Symbol
{
    private boolean shyVisible;

    public ShySymbol(String color)
    {
        super(color);
        shyVisible = false;
    }

    @Override
    public void selected()
    {
        if (shyVisible) {
            shyVisible = false;
            makeInvisible();
        }
        else {
            shyVisible = true;
            makeVisible();
        }
    }

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