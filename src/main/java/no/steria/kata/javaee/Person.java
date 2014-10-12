package no.steria.kata.javaee;

public class Person {
    private String name;

    public static Person withName(String name) {
        Person person = new Person();
        person.name = name;
        return person;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Person)) return false;
        return nullSafeEquals(name, ((Person) obj).name);
    }

    @Override
    public int hashCode() {
        return 1;
    }

    private static <T> boolean nullSafeEquals(T a, T b) {
        if (a == null) return (b == null);
        return a.equals(b);
    }

    @Override
    public String toString() {
        return "Person<" + name + ">";
    }
}
