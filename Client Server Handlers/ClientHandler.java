import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread{
    private Socket socket;
    private Server server;
    ClientHandler(Server server,Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override public void run(){
        try
        {
            //A connection is received

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            Message message = (Message) in.readObject();
            Message reply = new Message();

            switch (message.messageType) {
                //sendInfo receives the Player info and assigns a unique ID to the player
                case "sendInfo":
                    reply =  server.sendInfo(message);
                    //reply = this.server.login(message);
                    break;
                // updateClinetCoords updates the players coordinates and returns the current coordinates of the rest of the players
                case "updateClientCoords":
                    reply = server.updateClientCoords(message);
                    //reply = this.server.inform(message);
                    break;
                case "updateGM":
                    //reply = server.updateGM(message);
                    //reply = this.server.inform(message);
                    break;
            }
            out.writeObject(reply);
            out.flush();
            //socket.close();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
}
