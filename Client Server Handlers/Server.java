import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server extends Thread {

    private ServerSocket serverSocket;
    private int port = 50000;
    private boolean running = false;
    private ArrayList<Player> players;

    static int countId = 1;

    int timeLimit = 0;
    int killLimit = 0;

    public Server() {
        players = new ArrayList<Player>();
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            //this.setGM();
            this.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        running = false;
        this.interrupt();
    }

    public void run() {
        setRunning(true);
        while (isRunning()) {
            try {
                //Listening for a connection
                Socket socket = serverSocket.accept();
                // Pass the socket to the RequestHandler thread for processing
                ClientHandler requestHandler = new ClientHandler(this, socket);
                requestHandler.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public int getPort() {
        return port;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }


    public void setGM(int timeLimit, int killLimit) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input the time limit (in minutes): ");
        String input = scanner.nextLine();
        timeLimit = Integer.parseInt(input);
        System.out.println("Input the kill limit: ");
        input = scanner.nextLine();
        killLimit = Integer.parseInt(input);
    }

    //public TDM updateGM(Message message){
    //    return tdm;
    //}

    public Message updateClientCoords(Message message) {

        // Message data will be like "{Player name} {X coordinates} {Y Coordinates} {Z coordinates}"  e.g "Player 1 34.12 23.11 14.30"
        Message reply = new Message();

        String[] messageParts = message.data.split(" ");
        String name = messageParts[0];
        int x = Integer.parseInt(messageParts[1]);
        int y = Integer.parseInt(messageParts[2]);
        int z = Integer.parseInt(messageParts[3]);

        for (Player player : this.players) {
            if (player.getName().equals(name)) {
                player.setCoords(new Coords(x, y, z));
            } else {
                reply.data = reply.getData() + player.getName() + " " +
                        player.getCoords().getX() + " " +
                        player.getCoords().getY() + " " +
                        player.getCoords().getZ() +
                        System.lineSeparator();
            }
        }

        //Reply data will be like this in each line: "{Player name} {X coordinates} {Y Coordinates} {Z coordinates}"
        // For debugging purpose
//        for(Player player:players){
//            System.out.println("Player with ID " + player.getId() + " Has coordinates " + player.getCoords().toString());
//        }
        return reply;
    }

    public Message sendInfo(Message message) {
        System.out.println("Message Received");
        Message reply = new Message();
        System.out.println(message.data);
        String[] messageParts = message.data.split(" ");
        String name = messageParts[0];
        int x = Integer.parseInt(messageParts[1]);
        int y = Integer.parseInt(messageParts[2]);
        int z = Integer.parseInt(messageParts[3]);

        if (!playerExists(name)) {
            players.add(new Player(name, countId, new Coords(x, y, z)));
            reply.setStatus(true);
            reply.setData("" + countId);
            ++countId;
        } else {
            reply.setData("Player already exists");
            reply.setStatus(false);
        }
        System.out.println("Replying");
        System.out.println(reply.data);
        return reply;
    }


    private boolean playerExists(String name) {
        for (Player player : players) {
            if (player.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws Exception{
        try {
            // server is listening on port 50000
            System.out.println("Creating a new Server object");
            Server server = new Server();
            System.out.println("Starting the server");
            server.startServer();


        } catch (Exception e) {
            e.printStackTrace();
        }


        //DONT FORGET OSMap(that will be used)
    }
}