package ru.itmo.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.itmo.common.Request;
import ru.itmo.common.Response;
import ru.itmo.common.model.Student;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class App {
    private static final Logger log = LogManager.getLogger(App.class.getName());


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(65100));

        Socket socket = serverSocket.accept();
        byte[] buffer = new byte[4096];
        int amount = socket.getInputStream().read(buffer);

        if (amount <= 0 ) {
            /*handle errors*/
        }

        byte[] bytes = new byte[amount];
        System.arraycopy(buffer, 0, bytes, 0, amount);
        String json = new String(bytes, StandardCharsets.UTF_8);

        Request request = Request.fromJson(json);
        Student student = request.getArgumentAs(Student.class);

        Response response;

        if (request.commandName.equals("add")) {
            log.info(() -> "Got request add: " + student);
            response = new Response(
                    Response.Status.OK,
                    "Студент успешно добавлен"
            );
            socket.getOutputStream().write(response.toJson().getBytes(StandardCharsets.UTF_8));
        } else {
            response = new Response(
                    Response.Status.ERROR,
                    "Неизвестная команда"
            );
        }

        socket.getOutputStream().write(response.toJson().getBytes(StandardCharsets.UTF_8));
        socket.close();
    }
}
