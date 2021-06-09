import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client extends Thread{
    private boolean running = false;
    private String username;
    private String password;
    private String clientIP;
    private InetAddress peerHostname;
    private String peerIP;
    private int serverPort;
    private int tokenID = 0;
    private String sharedDirectory;
    private Player clientPlayer;
    private ArrayList<Player> players;
    private Socket connection;
    private Scanner scanner = new Scanner(System.in);

    public Client(String name) {
        this.clientPlayer = new Player(name,0, new Coords(10,10,10));
        try {
            this.setPeerHostname(InetAddress.getLocalHost());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.clientIP = "0";
        this.serverPort = 0;
    }

    public Client(String clientIP,int port,String name ) {
        this.clientPlayer = new Player(name,0, new Coords(10,10,10));
        try {
            this.setPeerHostname(InetAddress.getLocalHost());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.clientIP = clientIP;
        this.serverPort = port;
    }

    public void startClient()
    {
        try
        {

            this.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void run()
    {
        setRunning(true);
        /*
        while( isRunning() )
        {
            try
            {
                //Command Execution thread(s)

                //System.out.print("Choose the type of function you wish to perform: ");
                /*
                Scanner scanner = new Scanner(System.in);
                String choice = scanner.nextLine();

                switch (choice) {
                    case "updateCoords":
                        System.out.println("Called updateCoords");
                        updateCoords();
                        break;
                    case "Login":
                        System.out.println("Logging in to game");
                        sendInfo();
                        //reply = this.server.login(message);
                        break;
                    case "Logout":
                        //reply = this.server.logout(message);
                        break;
                    case "updateGM":
                        //reply = this.server.inform(message);
                        break;
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        */
    }

    private void updateCoords(){
        Message message = new Message();
        message.setMessageType("updateClientCoords");
        message.setData(clientPlayer.getName() + " " +
                clientPlayer.getCoords().getX() + " " +
                clientPlayer.getCoords().getY() + " " +
                clientPlayer.getCoords().getZ());

        try {
            this.connection = new Socket (clientIP,serverPort);
            ObjectOutputStream out = new
                    ObjectOutputStream(this.connection.getOutputStream());
            ObjectInputStream in = new
                    ObjectInputStream(this.connection.getInputStream());
            out.writeObject(message);
            out.flush();
            Message reply = (Message) in.readObject();
            String[] lines = reply.getData().split(System.lineSeparator());
            for (int i = 1; i <= lines.length -1 ; i++){

                String[] lineParts = lines[i].split(" ");
                String playerName = lineParts[0];
                int x = Integer.parseInt(lineParts[1]);
                int y = Integer.parseInt(lineParts[2]);
                int z = Integer.parseInt(lineParts[3]);
                players.get(findIndexByname(playerName)).setCoords(new Coords(x,y,z));
            }
        }
        catch(Exception e){
            System.out.println("Failed to send or receive Player Coordinates");
            e.printStackTrace();
        }

    }

    private int findIndexByname(String playerName){
        for (int i = 0; i<= players.size() -1; i++ ){
            if (players.get(i).getName().equals(playerName)){
                return i;
            }
        }
        return -1;

    }

    private void sendInfo(){
        Message message = new Message();
        message.setMessageType("sendInfo");
        message.setData(clientPlayer.getName() + " " +
                clientPlayer.getCoords().getX() + " " +
                clientPlayer.getCoords().getY() + " " +
                clientPlayer.getCoords().getZ());

        try {
            this.connection = new Socket (clientIP,serverPort);
            ObjectOutputStream out = new ObjectOutputStream(this.connection.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(this.connection.getInputStream());
            out.writeObject(message);
            out.flush();
            Message reply = (Message) in.readObject();

            if(reply.status){
                int id = Integer.parseInt(reply.data);
                clientPlayer.setId(id);
                System.out.println("Successfully sent Player info");
            }
            else{
                System.out.println("Could not send Player info");
                System.out.println(reply.data);
            }
        }catch(Exception e){
            System.out.println("Failed to send or receive Player Coordinates");
            e.printStackTrace();
        }
    }

    public void stopServer(){
        setRunning(false);
        this.interrupt();
    }

    public String getClientIP() {
        return clientIP;
    }
    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }
    public int getServerPort() {
        return serverPort;
    }
    public void setServerPort(int port) {
        this.serverPort = port;
    }
    public int getTokenID() {
        return tokenID;
    }
    public void setTokenID(int tokenID) {
        this.tokenID = tokenID;
    }

    public String getPeerIP() {
        return peerIP;
    }
    public void setPeerHostname(InetAddress peerHostname) {
        this.peerHostname = peerHostname;
        this.peerIP = this.peerHostname.getHostAddress();
    }

    public void setPeerIP(String peerIP) {
        this.peerIP = peerIP;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public String getSharedDirectory() {
        return sharedDirectory;
    }

    public void setSharedDirectory(String sharedDirectory) {
        this.sharedDirectory = sharedDirectory;
    }
    public void setClientPlayer(Player clientPlayer){
        this.clientPlayer = clientPlayer;
    }
    public Player getClientPlayer() {
        return clientPlayer;
    }

    public static void main(String[] args) throws IOException {
        try {
            // server is listening on port 50000
            System.out.println("Creating a new Client object");
            Client client = new Client("192.168.178.26",50000,"Player1");
            System.out.println("Starting the client");
            Client client2 = new Client("192.168.178.26",50000,"Player2");
            System.out.println("Starting the client");
            client.startClient();
            client2.startClient();

            client.sendInfo();
            client2.sendInfo();
            client.sendInfo();

            System.out.println("Client 1 has a Player of ID " + client.getClientPlayer().getId());
            System.out.println("Client 2 has a Player of ID " + client2.getClientPlayer().getId());

            client.getClientPlayer().setCoords((new Coords(30,20,50)));
            client.updateCoords();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}