/**
 * A wheel that cannot be locked, swapped or deleted.
 *
 * It is dark gray so it can be told apart from the other wheels.
 *
 * @author Cristian Anzola, Danik Castañeda
 * @version 2
 */
public class RebelWheel extends Wheel
{
    /**
     * Creates a RebelWheel.
     */
    public RebelWheel()
    {
        super();
        changeColor("darkGray");
    }

    /**
     * A RebelWheel cannot be locked.
     *
     * @return false
     */
    @Override
    public boolean canLock()
    {
        return false;
    }

    /**
     * A RebelWheel cannot be swapped.
     *
     * @return false
     */
    @Override
    public boolean canSwap()
    {
        return false;
    }

    /**
     * A RebelWheel cannot be deleted.
     *
     * @return false
     */
    @Override
    public boolean canDelete()
    {
        return false;
    }
}