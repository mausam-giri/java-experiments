package Objects.ShapeObject.Shapes;

import Objects.ShapeObject.Shape;

public class Circle extends Shape{
    public Circle(double x){
        super(x, x);
        this.setMultiplier(3.14);
    }
}
