package Objects.ShapeObject;

public abstract class Shape {
    double x, y, multiplier;

    public Shape(double x, double y) {
        this.x = x;
        this.y = y;
        this.multiplier = 1;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }
    public double getArea(){
        return this.x * this.y * this.multiplier;
     }

}

