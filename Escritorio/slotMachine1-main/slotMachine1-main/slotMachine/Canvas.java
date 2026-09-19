import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Canvas is a class to allow for simple graphical drawing on a canvas.
 * This is a modification of the general purpose Canvas, specially made for
 * the BlueJ "shapes" example. 
 *
 * @author: Bruce Quig
 * @author: Michael Kolling (mik)
 *
 * @version: 1.6 (shapes)
 */
public class Canvas{

    private static Canvas canvasSingleton;

    /**
     * Factory method to get the canvas singleton object.
     */
    public static Canvas getCanvas() {
        if (canvasSingleton == null) {
            canvasSingleton = new Canvas();
        }
        return canvasSingleton;
    }

    //  ----- instance part -----

    private JFrame frame;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private Color backgroundColour;
    private Image canvasImage;
    private ArrayList <Object> objects;
    private HashMap <Object,ShapeDescription> shapes;
    
    /**
     * Create a Canvas.
     * @param title  title to appear in Canvas Frame
     * @param width  the desired width for the canvas
     * @param height  the desired height for the canvas
     * @param bgClour  the desired background colour of the canvas
     */
    private Canvas() {

        canvas = new CanvasPane();
        frame = new JFrame();
        objects = new ArrayList<Object>();
        shapes = new HashMap<Object, ShapeDescription>();
        backgroundColour = Color.white;
        frame.setContentPane(canvas);
        frame.setTitle("Slot Machine");
        frame.setSize(300, 300);
        frame.setVisible(true);
    }

    /**
     * Set the canvas visibility and brings canvas to the front of screen
     * when made visible. This method can also be used to bring an already
     * visible canvas to the front of other windows.
     * @param visible  boolean value representing the desired visibility of
     * the canvas (true or false) 
     */
    public void setVisible(boolean visible){
        if(graphic == null) {
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D)canvasImage.getGraphics();
            graphic.setColor(backgroundColour);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
        }
        frame.setVisible(visible);
    }

    /**
     * Draw a given shape onto the canvas.
     * @param  referenceObject  an object to define identity for this shape
     * @param  color            the color of the shape
     * @param  shape            the shape object to be drawn on the canvas
     */
    public void draw(Object referenceObject,
                     String color,
                     Shape shape) {

        shapes.put(
            referenceObject,
            new ShapeDescription(color, shape)
        );

        if (!objects.contains(referenceObject)) {
            objects.add(referenceObject);
        }

        redraw();
    }

    /**
     * Erase a given shape's from the screen.
     * @param  referenceObject  the shape object to be erased 
     */
    public void erase(Object referenceObject) {

        shapes.remove(referenceObject);
        objects.remove(referenceObject);

        redraw();
    }
    
    /**
     * Set the foreground colour of the Canvas.
     * @param  color   the new colour for the foreground of the Canvas 
     */
    public void setForegroundColor(String color) {

        if (color.equals("red")) {
            graphic.setColor(Color.red);

        } else if (color.equals("black")) {
            graphic.setColor(Color.black);

        } else if (color.equals("blue")) {
            graphic.setColor(Color.blue);

        } else if (color.equals("yellow")) {
            graphic.setColor(Color.yellow);

        } else if (color.equals("green")) {
            graphic.setColor(Color.green);

        } else if (color.equals("magenta")) {
            graphic.setColor(Color.magenta);

        } else if (color.equals("white")) {
            graphic.setColor(Color.white);

        } else if (color.equals("orange")) {
            graphic.setColor(Color.orange);

        } else if (color.equals("pink")) {
            graphic.setColor(Color.pink);

        } else if (color.equals("cyan")) {
            graphic.setColor(Color.cyan);

        } else if (color.equals("gray")) {
            graphic.setColor(Color.gray);

        } else if (color.equals("lightGray")) {
            graphic.setColor(Color.lightGray);

        } else if (color.equals("darkGray")) {
            graphic.setColor(Color.darkGray);

        } else {
            try {
                graphic.setColor(Color.decode(color));
            } catch (Exception e) {
                graphic.setColor(Color.black);
            }
        }
    }

    /**
     * Wait for a specified number of milliseconds before finishing.
     * @param  milliseconds  the number 
     */
    public void wait(int milliseconds){
        try{
            Thread.sleep(milliseconds);
        } catch (Exception e){
            // ignoring exception at the moment
        }
    }

    /**
     * Redraw all shapes currently on the Canvas.
     */
    private void redraw() {
        canvas.repaint();
    }

    /**
     * Erase the whole canvas. (Does not repaint.)
     */
    private void erase(){
        Color original = graphic.getColor();
        graphic.setColor(backgroundColour);
        Dimension size = canvas.getSize();
        graphic.fill(new java.awt.Rectangle(0, 0, size.width, size.height));
        graphic.setColor(original);
    }

    private class CanvasPane extends JPanel {

        public void paintComponent(Graphics g) {

            super.paintComponent(g);

            Dimension size = getSize();

            if (canvasImage == null) {
                canvasImage = createImage(size.width, size.height);
                graphic = (Graphics2D) canvasImage.getGraphics();
                graphic.setColor(backgroundColour);
                graphic.fillRect(0,0,size.width,size.height);
            }
            
            for (Object object : objects) {

                ShapeDescription shape = shapes.get(object);
                if (shape != null) {
                    setForegroundColor(shape.color);
                    graphic.fill(shape.shape);
                }
            }

            g.drawImage(canvasImage,0,0,null);
        }
    }
    
    private class ShapeDescription {

        private String color;
        private Shape shape;

        public ShapeDescription(
            String color,
            Shape shape) {

            this.color = color;
            this.shape = shape;
        }
    }
}