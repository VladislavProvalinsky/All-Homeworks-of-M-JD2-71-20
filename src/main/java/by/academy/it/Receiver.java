package by.academy.it;

import java.io.Serializable;
import java.util.Objects;

public class Receiver implements Serializable {

    private int num;
    private String name;

    private Receiver(int num, String name) {
        this.num = num;
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ReceiverDto{" +
                "num=" + num +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receiver receiver = (Receiver) o;
        return num == receiver.num &&
                Objects.equals(name, receiver.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(num, name);
    }

    public static class ReceiverBuilder {
        private int num;
        private String name;

        public ReceiverBuilder() {
        }

        public ReceiverBuilder setNum(int num) {
            this.num = num;
            return this;
        }

        public ReceiverBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public Receiver build () {
            return new Receiver(num, name);
        }
    }
}
