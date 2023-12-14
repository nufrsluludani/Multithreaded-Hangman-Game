import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.Random;

public class Server {
    int count = 1;
    ArrayList<ClientThread> clients = new ArrayList();
    TheServer server;
    private Consumer<Serializable> callback;
    String word = "";


    SerializedData data = new SerializedData();

    ArrayList<String> JMRBands = new ArrayList<String>(){
        {
            add("The Cabs");
            add("toe");
            add("Uchu Conbini");
        }
    };

    ArrayList<String> beats = new ArrayList<String>(){
        {
            add("Auditory");
            add("Don Perignon");
            add("Lada Gaga");
        }
    };

    ArrayList<String> food = new ArrayList<String>(){
        {
            add("Soup Dumplings");
            add("Sticky Rice");
            add("Pizza");
        }
    };

    Random random = new Random();

    int portNum;

    Server(Consumer<Serializable> call, int portNum) {
        this.callback = call;
        this.server = new TheServer();
        this.server.start();
        this.portNum = portNum;
    }

    public class TheServer extends Thread {
        public TheServer() {
        }

        public void run() {
            try {
                ServerSocket mysocket = new ServerSocket(portNum);

                try {
                    System.out.println("Server is waiting for a client!");

                    while(true) {
                        SerializedData data = new SerializedData();
                        ClientThread c = Server.this.new ClientThread(mysocket.accept(), Server.this.count);
                        Server.this.callback.accept("client has connected to server: client #" + Server.this.count);
                        Server.this.clients.add(c);
                        c.start();
                        ++Server.this.count;
                    }
                } catch (Throwable var5) {
                    try {
                        mysocket.close();
                    } catch (Throwable var4) {
                        var5.addSuppressed(var4);
                    }

                    throw var5;
                }
            } catch (Exception var6) {
                Server.this.callback.accept("Server socket did not launch");
            }
        }
    }

    class ClientThread extends Thread {
        Socket connection;
        int count;
        ObjectInputStream in;
        ObjectOutputStream out;

        ClientThread(Socket s, int count) {
            this.connection = s;
            this.count = count;
        }

        public void updateClients(Object data, int clientNum) {

            ClientThread t = (ClientThread)Server.this.clients.get(clientNum);

            try {
                t.out.reset();
                t.out.writeObject(data);
            } catch (Exception var5) {
            }


        }

        public void run() {
            try {
                this.in = new ObjectInputStream(this.connection.getInputStream());
                this.out = new ObjectOutputStream(this.connection.getOutputStream());
                this.connection.setTcpNoDelay(true);
            } catch (Exception var3) {
                System.out.println("Streams not open");
            }

            Object dataLock = new Object();
//            this.updateClients("new client on server: client #" + this.count);

            while (true) {
                try {
                    SerializedData recievedData = (SerializedData) this.in.readObject();

                    callback.accept(recievedData);


                    if(recievedData.getClientNum() == -999) {
                        recievedData.setClientNum(count-1);
                    }


                    StringBuilder displayText = new StringBuilder();

                    if(Objects.equals(recievedData.getUpdate(), "category")) {
                        int randInt = random.nextInt(3);


                        if (Objects.equals(recievedData.getCategory(), "Japanese Math Rock")) {
                            word = JMRBands.get(randInt);
                            for(int i = 0; i < word.length(); i++){
                                displayText.append("_");
                            }

                            System.out.println(word); // debugging

                            recievedData.setLength(word.length());
                            recievedData.setDisplay(String.valueOf(displayText));
                            System.out.println(recievedData.getDisplay());
                            this.updateClients(recievedData,recievedData.getClientNum());
                        }



                        if (Objects.equals(recievedData.getCategory(), "Scampire Beats")) {
                            word = beats.get(randInt);
                            for(int i = 0; i < word.length(); i++){
                                displayText.append("_");
                            }

                            System.out.println(word); // debugging
                            recievedData.setLength(word.length());
                            recievedData.setDisplay(String.valueOf(displayText));
                            this.updateClients(recievedData,recievedData.getClientNum());
                        }


                        if (Objects.equals(recievedData.getCategory(), "Food")){
                            word = food.get(randInt);
                            for(int i = 0; i < word.length(); i++){
                                displayText.append("_");
                            }

                            System.out.println(word); // debugging
                            recievedData.setLength(word.length());
                            recievedData.setDisplay(String.valueOf(displayText));
                            this.updateClients(recievedData,recievedData.getClientNum());
                        }

                    }
                    else if(Objects.equals(recievedData.getUpdate(), "letter")){
                        String temp = recievedData.getDisplay();
                        StringBuilder updatedTemp = new StringBuilder(temp);

                        for (int i = 0; i < word.length(); i++) {
                            if (Character.toLowerCase(recievedData.getChar()) == Character.toLowerCase(word.charAt(i))) {
                                updatedTemp.setCharAt(i, word.charAt(i));
                            }
                        }

                        if(temp.equals(updatedTemp.toString())){
                            recievedData.decrementGuess();
                        }

                        if(recievedData.getGuess() == 0){
                            recievedData.decrementAttempt();
                        }

                        recievedData.setDisplay(updatedTemp.toString());

                        if(updatedTemp.toString().equals(word)){ // win condition
                            System.out.println("win!!!!!!!!!!!!");
                            recievedData.setDisplay("you win");
                        }



//                        System.out.println(temp);
//                        System.out.println(recievedData.getGuess());
//                        System.out.println(recievedData.getCurrentCategory());
                        this.updateClients(recievedData, recievedData.getClientNum());
                    }

                } catch (Exception var2) {
                    // Handle client disconnection
                    Server.this.callback.accept("Client #" + this.count + " has left the server!");
//                    this.updateClients("Client #" + this.count + " has left the server!");
                    Server.this.clients.remove(this);
                    var2.printStackTrace();
                    return;
                }
            }
        }
    }
}
