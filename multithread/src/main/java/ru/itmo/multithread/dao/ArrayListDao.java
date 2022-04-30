package ru.itmo.multithread.dao;

import ru.itmo.multithread.model.Student;

import java.util.ArrayList;
import java.util.List;

public final class ArrayListDao implements Dao {
    private final List<Student> students = new ArrayList<>();

    @Override
    public synchronized void save(Student student) {
        students.add(student);
    }

    @Override
    public synchronized void update(Student student) {
        Student existedStudent = get(student.getId());
        existedStudent.setName(student.getName());
        existedStudent.setAge(student.getAge());
    }

    @Override
    public synchronized void delete(int id) {
        students.remove(get(id));
    }

    @Override
    public synchronized Student get(int id) {
        return students.stream()
                .filter(el -> el.getId() == id)
                .findAny()
                .orElseThrow();
    }
}
