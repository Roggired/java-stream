package ru.itmo.client;


import ru.itmo.common.Response;
import ru.itmo.common.model.Student;

public class App {
    public static void main(String[] args) {
        Student student = new Student(
                1,
                "Test",
                20
        );

        ServerAPI serverAPI = new ServerAPIImpl();
        Response response = serverAPI.addStudent(student);

        if (response.status == Response.Status.OK) {
            System.out.println("Студент успешно добавлен в коллекцию");
        } else {
            System.out.println("Ошибка при добавлении студента: ");
            System.out.println(response.getArgumentAs(String.class));
        }
    }
}
