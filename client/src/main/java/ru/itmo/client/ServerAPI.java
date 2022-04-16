package ru.itmo.client;

import ru.itmo.common.Response;
import ru.itmo.common.model.Student;

public interface ServerAPI {
    Response addStudent(Student student);
}
