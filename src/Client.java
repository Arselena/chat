import java.io.*;
import java.net.Socket;
import java.util.Scanner;

class Client implements Runnable {
    Socket socket;
    Scanner in;
    PrintStream out;
    ChatServer server;
    private String nickname; // имя клиента

    public Client(Socket socket, ChatServer server){
        this.socket = socket;
        this.server = server;
        // запускаем поток
        new Thread(this).start(); // клиент сам себя запускает
    }

    void receive(String message){
        out.println(message);
    }

    public void run() {
        try {
            // получаем потоки ввода и вывода
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            // создаем удобные средства ввода и вывода
            in = new Scanner(is);
            out = new PrintStream(os);

            // читаем из сети и пишем в сеть
            out.println("Welcome to chat! Your nickname: ");
            nickname = in.nextLine();
            server.sendAll("Hello " + nickname);

            String input = in.nextLine();
            while (!input.equals("bye")) {
                server.sendAll(nickname + " : " + input);
                input = in.nextLine();
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}