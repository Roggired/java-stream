package ru.itmo.multithread.dao;

import ru.itmo.multithread.model.Student;

public interface Dao {
    void save(Student student);
    void update(Student student);
    void delete(int id);
    Student get(int id);
}
