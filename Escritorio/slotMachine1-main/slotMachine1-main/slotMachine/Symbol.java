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

    /**
     * Constructor for objects of class Symbol.
     * @param color color of the symbol
     */
    public Symbol(String color) {
        this.color = color;
        this.visible = false;
        
        circle = new Circle();
        circle.changeColor(color);
        circle.makeInvisible();
    }

    /**
     * Returns the color of the symbol.
     * @return color string
     */
    public String getColor() {
        return color;
    }

    /**
     * Makes the symbol visual representation visible.
     */
    public void makeVisible() {
        visible = true;
        circle.makeVisible();
    }

    /**
     * Makes the symbol visual representation invisible.
     */
    public void makeInvisible() {
        visible = false;
        circle.makeInvisible();
    }

    /**
     * Checks if the symbol is visible.
     * @return true if visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets the position of the symbol's visual representation.
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void setPosition(int x, int y) {
        circle.setPosition(x, y);
    }
}