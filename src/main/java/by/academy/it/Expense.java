package by.academy.it;

import java.io.Serializable;
import java.sql.Date;

public class Expense implements Serializable {

    private int num;
    private String paydate;
    private int receiver;
    private double value;

    private Expense(int num, String paydate, int receiver, double value) {
        this.num = num;
        this.paydate = paydate;
        this.receiver = receiver;
        this.value = value;
    }

    public int getNum() {
        return num;
    }

    public String getPaydate() {
        return paydate;
    }

    public int getReceiver() {
        return receiver;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ExpenseDto{" +
                "num=" + num +
                ", paydate=" + paydate +
                ", receiver=" + receiver +
                ", value=" + value +
                '}';
    }

    public static class ExpenseBuilder {
        private int num;
        private String paydate;
        private int receiver;
        private double value;

        public ExpenseBuilder() {
        }

        public ExpenseBuilder setNum(int num) {
            this.num = num;
            return this;
        }

        public ExpenseBuilder setPaydate(String paydate) {
            this.paydate = paydate;
            return this;
        }

        public ExpenseBuilder setReceiver(int receiver) {
            this.receiver = receiver;
            return this;
        }

        public ExpenseBuilder setValue(double value) {
            this.value = value;
            return this;
        }

        public Expense build () {
            return new Expense (num, paydate,receiver,value);
        }
    }
}
