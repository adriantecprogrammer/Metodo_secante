package org.example;

import org.nfunk.jep.JEP;
import org.nfunk.jep.function.NaturalLogarithm;
import org.nfunk.jep.function.SquareRoot;

import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {
    static double result_c = 0;
    static double error = 0;
    static double c = 0;

    public static void main(String[] args) {
        // Configurar el objeto JEP
        double tolerance = 0.00001;
        int maxIterations = 100;
        JEP jep = new JEP();
        jep.addStandardFunctions();
        jep.addStandardConstants();
        jep.addFunction("ln", new NaturalLogarithm());
        jep.addFunction("sqrt", new SquareRoot());
        double m = 4;


        double x = 0;
        double xi = 0;
        double result1 = 0;
        double resultado = 0;

        // Pedir al usuario que ingrese la expresión matemática
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingresa la expresión matemática (por ejemplo, x^2-2, ln(2*x), e^(2*x)-3 ): ");
        String expresion = scanner.nextLine();

        boolean band = false;

        do {
            try {


                band = true;
                System.out.print("Ingresa un valor inicial Xo: ");

                x = scanner.nextDouble();
                jep.addVariable("x", x);
                jep.parseExpression(expresion);
                result1 = jep.getValue();

                System.out.print("Ingresa un valor inicial Xi: ");

                xi = scanner.nextDouble();
                jep.addVariable("x", x);
                jep.parseExpression(expresion);
                result1 = jep.getValue();

                secante(x, xi, jep, expresion, tolerance, maxIterations);

                if (error > 100) {
                    System.out.println("El metodo divergio, elige otros valores iniciales");
                    System.out.println();
                    band = false;
                } else {
                    DecimalFormat decimalFormat = new DecimalFormat("#.#####");
                    String raiz = decimalFormat.format(c);
                    System.out.printf("\nEl resultado es: " + raiz);
                    band = true;

                }
            } catch (Exception e) {
                System.out.println("Upss ocurrio un error");
                band = false;
                scanner.next();
            }

        } while (!band);


    }

    public static void secante(double a, double b, JEP funcion, String expresion, double tolerance, int maxIteration) {

        double result_a = 0;
        double result_b = 0;
        int contador = 0;

        do {
            try {

                funcion.addVariable("x", a);
                funcion.parseExpression(expresion);
                result_a = funcion.getValue(); //obtener valor de f(xi-1)

                funcion.addVariable("x", b);
                funcion.parseExpression(expresion);
                result_b = funcion.getValue(); //obtener valor de f(xi)

                c = b - (result_b * (a - b)) / (result_a - result_b); //sacar la xi+1

                error = Math.abs((c - b) / c) * 100; //sacar el error

                funcion.addVariable("x", c);
                funcion.parseExpression(expresion);
                result_c = funcion.getValue(); //obtener valor de f(xi+1)
                System.out.println();
                System.out.println("EL error es: " + error);

                if (Math.abs(result_c) < tolerance || error > 100) {
                    break;
                }
                a = b;

                b = c;

                contador++;
            } catch (Exception e) {
                e.printStackTrace();
            }

        } while (contador < maxIteration);

    }

}
