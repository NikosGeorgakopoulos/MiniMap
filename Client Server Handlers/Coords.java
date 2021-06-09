public class Coords {

    private int x;
    private int y;
    private int z;

    Coords() {

        x = 0;
        y = 0;
        z = 0;
    }

    Coords(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return z;
    }

    public int getZ() {
        return z;
    }

    @Override
    public String toString(){
        return x + " " + y + " " + z;
    }
}