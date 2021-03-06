package ru.itmo.common.model;

public class Student {
    private int id;
    private String name;
    private int age;
    private Cat cat;


    public Student(int id, String name, int age, Cat cat) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.cat = cat;
    }


    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
