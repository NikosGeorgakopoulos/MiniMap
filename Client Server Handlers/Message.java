import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

public class Message implements Serializable {

    String messageType;
    String data;
    byte[] file;
    boolean status;
    private Map<String,byte[]> mapData;

    public Message() {
        this.messageType = "none";
        this.data = "empty";
        mapData = new HashMap<String,byte[]>();
    }

    public Message(String messageType,String data)
    {
        this.messageType = messageType;
        this.data = data;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public void setmapData(Map<String,byte[]> mapData){
        this.mapData = mapData;
    }

    public Map<String,byte[]> getmapData(){
        return mapData;
    }
}