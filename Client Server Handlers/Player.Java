public class Player {

    private Coords coords;
    private String name;
    private int id = 0;

    Player(String name, int id, Coords coords) {
        this.name = name;
        this.id = id;
        this.coords = coords;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCoords(Coords coords) {
        this.coords = coords;
    }

    public Coords getCoords() {
        return coords;
    }
    public int getId(){
        return id;
    }
}