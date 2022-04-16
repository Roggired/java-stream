package ru.itmo.client;

import ru.itmo.common.Request;
import ru.itmo.common.Response;
import ru.itmo.common.model.Student;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerAPIImpl implements ServerAPI {
    @Override
    public Response addStudent(Student student) {
        Request request = new Request(
                "add",
                student
        );

        try {
            return sendToServer(request);
        } catch (IOException e) {
            /*handle error*/
            throw new RuntimeException(e);
        }
    }

    private Response sendToServer(Request request) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", 65100));

        socket.getOutputStream().write(request.toJson().getBytes(StandardCharsets.UTF_8));
        byte[] buffer = new byte[4096];
        int amount = socket.getInputStream().read(buffer);
        byte[] responseBytes = new byte[amount];
        System.arraycopy(buffer, 0, responseBytes, 0, amount);

        socket.close();

        String json = new String(responseBytes, StandardCharsets.UTF_8);
        return Response.fromJson(json);
    }
}
