package vector;

public class SizeVector {
    private int width, height;

    public  SizeVector(){

    }

    public SizeVector(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSize(int width, int height){
        setHeight(height);
        setWidth(width);
    }
}
