package eecs40;

public interface CalculatorInterface {
    public void acceptInput(String s);
    public String getDisplayString();

    public static void main(String[] args) {

        CalculatorInterface calc = new ExprCalculator();


        calc.acceptInput("(41-67)*12+sin(fac(3)-6)");

        System.out.println(calc.getDisplayString()); // show (41-67)*12+sin(fac(3)-6)

        calc.acceptInput("=");

        System.out.println("Display: " + calc.getDisplayString()); // show -312

        calc.acceptInput("/0");

        System.out.println("Display: " + calc.getDisplayString()); // show -312/0

        calc.acceptInput("=");

        System.out.println("Display: " + calc.getDisplayString()); // show NaN

        calc.acceptInput("Backspace");

        System.out.println("Display: " + calc.getDisplayString()); // show -312/

        calc.acceptInput("Backspace");

        System.out.println("Display: " + calc.getDisplayString()); // show -312

        calc.acceptInput("+31");

        System.out.println("Display: " + calc.getDisplayString()); // show -312+31

        calc.acceptInput("=");

        System.out.println("Display: " + calc.getDisplayString()); // show -281


    }
}
