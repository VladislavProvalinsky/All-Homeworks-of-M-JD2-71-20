package org.academy.maven;

/**
 * Hello world!
 *
 */
public class App {

    public static double addition(double x1, double x2) {
        return x1 + x2;
    }

    public static double minus(double x1, double x2) {
        return x1 - x2;
    }

    public static double multiplication(double x1, double x2) {
        return x1 * x2;
    }

    public static double division(double x1, double x2) {
        return x1 / x2;
    }

    public static void main( String[] args )
    {
        System.out.println(App.addition(7,5));
        System.out.println(App.division(7,3));
        System.out.println(App.minus(8,5));
        System.out.println(App.multiplication(7,6));
    }
}
