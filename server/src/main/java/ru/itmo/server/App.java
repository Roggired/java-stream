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
import java.sql.*;
import java.util.Properties;

public class App {
    private static final Logger log = LogManager.getLogger(App.class.getName());


    public static void main(String[] args) throws IOException, SQLException {
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
            writeToDatabase(student);
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

    private static void writeToDatabase(Student student) throws SQLException {
        Driver driver = getPostgresqlDriver();
        Properties properties = new Properties();
        properties.put("user", "postgres");
        properties.put("password", "postgres");
        try(Connection connection = driver.connect("jdbc:postgresql://localhost:5432/java_stream", properties)) {
            PreparedStatement catStatement = connection.prepareStatement("" +
                    "INSERT INTO cat(name, age) VALUES(?, ?) RETURNING id");
            catStatement.setString(1, student.getCat().getName());
            catStatement.setInt(2, student.getCat().getAge());
            catStatement.execute();

            ResultSet resultSet = catStatement.getResultSet();
            resultSet.next();
            long catId = resultSet.getLong("id");
            student.getCat().setId(catId);

            PreparedStatement studentStatement = connection.prepareStatement("INSERT INTO student(name, age, cat_id) VALUES(?, ?, ?)");
            studentStatement.setString(1, student.getName());
            studentStatement.setInt(2, student.getAge());
            studentStatement.setLong(3, student.getCat().getId());
            studentStatement.execute();
        }
    }

    private static Driver getPostgresqlDriver() {
        try {
            return DriverManager.getDriver("jdbc:postgresql://localhost:5432/java_stream");
        } catch (SQLException e) {
            log.fatal(e);
            throw new RuntimeException(e);
        }
    }
}
