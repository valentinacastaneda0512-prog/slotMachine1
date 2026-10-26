/**
 * Represents a symbol in the SlotMachine system.
 *
 * @author Cristian Anzola, Danik Castañeda
 * @version 1
 */
public class Symbol
{
    private String color;
    private Circle circle;
    private boolean visible;
    private int size;
    private int displayLimit;

    /**
     * Small dot drawn in the corner of the symbol so each special type
     * can be recognized at a glance. Normal symbols do not have one.
     */
    protected Circle mark;

    /**
     * Constructor for objects of class Symbol.
     *
     * @param color color of the symbol
     */
    public Symbol(String color)
    {
        this.color = color;
        this.visible = false;
        this.size = 30;
        this.displayLimit = Integer.MAX_VALUE;

        circle = new Circle();
        circle.changeColor(color);
        circle.changeSize(size);
        circle.makeInvisible();
    }

    /**
     * Returns the color of the symbol.
     *
     * @return color of the symbol
     */
    public String getColor()
    {
        return color;
    }

    /**
     * Makes the symbol visible.
     */
    public void makeVisible()
    {
        visible = true;
        circle.makeVisible();

        if (mark != null) {
            mark.makeVisible();
        }
    }

    /**
     * Makes the symbol invisible.
     */
    public void makeInvisible()
    {
        visible = false;
        circle.makeInvisible();

        if (mark != null) {
            mark.makeInvisible();
        }
    }

    /**
     * Returns whether the symbol is visible.
     *
     * @return true if the symbol is visible
     */
    public boolean isVisible()
    {
        return visible;
    }

    /**
     * Sets the position of the symbol.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void setPosition(int x, int y)
    {
        circle.setPosition(x, y);

        if (mark != null) {
            mark.setPosition(x, y);
        }
    }

    /**
     * Changes the size of the symbol.
     *
     * @param newSize new size of the symbol
     */
    protected void changeSize(int newSize)
    {
        size = newSize;
        circle.changeSize(Math.min(size, displayLimit));
    }

    /**
     * Limits the drawn diameter so the symbol fits inside its wheel.
     * The logical size (getSize) does not change.
     *
     * @param limit maximum diameter to draw, in pixels
     */
    public void fitInto(int limit)
    {
        displayLimit = limit;
        circle.changeSize(Math.min(size, displayLimit));
    }

    /**
     * Returns the current size of the symbol.
     *
     * @return size of the symbol
     */
    public int getSize()
    {
        return size;
    }

    /**
     * Defines the behavior executed when the symbol
     * is selected by a wheel.
     *
     * Normal symbols do not have special behavior.
     */
    public void selected()
    {
    }
    
        public void show()
    {
        makeVisible();
    }
    
        public void hide()
    {
        makeInvisible();
    }
}