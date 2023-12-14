import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread {
    Socket socketClient;
    ObjectOutputStream out;
    ObjectInputStream in;
    private Consumer<Serializable> callback;

    SerializedData data;
    int portNum;

    Client(Consumer<Serializable> call, int portNum) {
        this.callback = call;
        this.portNum = portNum;
    }

    public void run() {
        try { // connected
            this.socketClient = new Socket("127.0.0.1", portNum);
            System.out.println("port NUMBER: " + portNum);
            this.out = new ObjectOutputStream(this.socketClient.getOutputStream());
            this.in = new ObjectInputStream(this.socketClient.getInputStream());
            this.socketClient.setTcpNoDelay(true);
        } catch (Exception var2) { // not connected

        }

            while(true) {
                try {
                    data = (SerializedData)this.in.readObject();
                    System.out.println(data.getClientNum() + " " + data.getLength() + " " + data.getDisplay());
                    callback.accept(data);
                } catch (Exception var3) {
                }
            }
    }

    public void send(Object data) {
        try {
            if (out != null) {
                this.out.reset();
                out.writeObject(data);
                out.flush();
            } else {
                System.err.println("Error");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
