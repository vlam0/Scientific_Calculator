package eecs40;
import java.util.*;

public class ExprCalculator implements CalculatorInterface {
    private String expression = "";

    class Error {
        int nanError = 0;
    }
    // Validate if it's a simple operator i.e. +,-,/,*,mod,^
    public boolean simpleFunctions(int i, String equation) {
        if (equation.charAt(i) == '+' || equation.charAt(i) == '-' || equation.charAt(i) == '*' ||
            equation.charAt(i) == '/' || equation.charAt(i) == '^' || equation.charAt(i) == 'm')
            return true;
        else
            return false;
    }
    // Validate if it's a expr operator i.e. trig,log,ln,!fact,sqrt
    public boolean exprFunctions(int i, String equation) {
        if (!simpleFunctions(i, equation) && !isDigit(i, equation))
            return true;
        else
            return false;
    }
    // Validates if its a digit or .
    public boolean isDigit(int i, String equation) {
        if (Character.isDigit(equation.charAt(i)) || equation.charAt(i) == '.')
            return true;
        else
            return false;
    }
    // Validate for parentheses
    public boolean hasBracket(int i, String equation) {
        if (equation.charAt(i) == '(' || equation.charAt(i) == ')')
            return true;
        else
            return false;
    }
    // Boolean of Hashsets
    public boolean HashExpr(HashSet<String> data) {
        if (data.contains("sin") || data.contains("cos") || data.contains("tan") || data.contains("sqrt") ||
            data.contains("ln") || data.contains("log") || data.contains("fac"))
            return true;
        else
            return false;
    }
    public boolean HashSimple(HashSet<String> data) {
        if (data.contains("+") || data.contains("-") || data.contains("*") || data.contains("/") ||
            data.contains("mod") || data.contains("^"))
            return true;
        else
            return false;
    }
    public boolean HashBracket(HashSet<String> data) {
        if (data.contains("(") || data.contains(")"))
            return true;
        else
            return false;
    }
    // Checks for brackets
    public boolean bracketVerify(int openBracket, int closedBracket) {
        String temp = "", temp2 = "";
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                openBracket++;
                temp += '(';
            }
            else if (expression.charAt(i) == ')') {
                closedBracket++;
                temp += ')';
            }
        }

        if (openBracket == closedBracket) {
            if (temp.length() > 2) {
                for (int k = 0; k < temp.length(); k++) {
                    if (temp.charAt(k) == '(' && temp.charAt(k+1) == ')' && k < temp.length() - 1) {
                        k = k+1;
                        if (k >= temp.length())
                            break;
                    }
                    else
                        temp2 += temp.charAt(k);
                }
            }
            if (temp2.equals(")(") || temp.equals(")("))
                return false;
            else
                return true;
        }
        else
            return false;
    }
    // Checks Syntax error i.e (+4*3)
    public boolean noError(String equation) {
        int errorFlag = 0;
        for (int i = 0; i < equation.length(); i++) {
            if (equation.charAt(i) == '(' && equation.charAt(i+1) == ')' && i < equation.length() - 1) {
                errorFlag = 1; // ()
                break;
            }
            else if (equation.charAt(i) == '(' && i != equation.length() - 1) {
                if (equation.charAt(i+1) == '+' || equation.charAt(i+1) == '*' || equation.charAt(i+1) == '/') { // (+.....
                    errorFlag = 1;
                    break;
                }
            }
            else if (equation.charAt(i) == '(' && i == equation.length() - 1) {
                errorFlag = 1;; // ......(
                break;
            }
            else if (equation.charAt(i) == ')' && i != 0) {
                if (simpleFunctions(i-1, equation)) { // +)....
                    errorFlag = 1;
                    break;
                }
            }
            else if (equation.charAt(i) == ')' && i == 0) {
                errorFlag = 1; // ).....
                break;
            }
            else if (i != equation.length() - 1 && simpleFunctions(i, equation)) {
                if (equation.charAt(i+1) == '+' || equation.charAt(i+1) == '*' || equation.charAt(i+1) == '/') {
                    errorFlag = 1; // Multiple operators i.e +/
                    break;
                }
            }
            else if (i == equation.length() - 1 && simpleFunctions(i, equation)) {
                errorFlag = 1; // operator at the end of equation
                break;
            }
            else if (equation.charAt(0) == '*' || equation.charAt(0) == '/') {
                errorFlag = 1; // operator * or / in the beginning of expression
                break;
            }
            else if (equation.charAt(i) == '.' && equation.charAt(i+1) == '.' && i < equation.length() - 1) {
                errorFlag = 1; // multiple . in a row
                break;
            }
            else if (equation.charAt(i) == '.') {
                for (int k = i + 1; k < equation.length(); k++) {
                    if (!exprFunctions(k, equation) && !simpleFunctions(k, equation) && equation.charAt(k) == '.') {
                        errorFlag = 1; // multiple . in operand
                        break;
                    }
                }
            }
            else if (equation.charAt(i) == 'n' || equation.charAt(i) == 's' || equation.charAt(i) == 'g' ||
                    equation.charAt(i) == 't' || equation.charAt(i) == 'c') {
                if (i == equation.length() - 1) {
                    errorFlag = 1; // nothing after Expr functions
                    break;
                }
                else if (exprFunctions(i, equation) && simpleFunctions(i+1, equation) && i < equation.length() - 2) {
                    errorFlag = 1; // operator after Expr functions
                    break;
                }
            }
        }

        if (errorFlag == 1)
            return false;
        else
            return true;
    }
    // Count number of brackets
    public int numBrackets () {
        int num = 0;
        for (int j = 0; j < expression.length(); j++) {
            if (expression.charAt(j) == '(')
                num++;
        }
        return num;
    }
    // Add () around expr functions
    public String addBracket (String equation, HashSet<String> data) {
        String temp = "";
        int exprFlag = 0, bracketFlag = 0;
        for (int i = 0; i < equation.length(); i++) {
            if (exprFunctions(i, equation)) {
                if (i == 0 && equation.charAt(i) != '(') {
                    temp += "(";
                    data.add("(");
                    exprFlag = 1;
                }
                if (i != 0 && equation.charAt(i-1) == '('){
                    exprFlag = 0;
                }
            }
            if (exprFlag == 1 && isDigit(i, equation)) {
                if (bracketFlag == 1) {
                    while (equation.charAt(i) != ')') {
                        temp += equation.charAt(i);
                        i++;
                    }
                    bracketFlag = 0;
                }
                temp += equation.charAt(i);
                temp += ")";
                data.add(")");
                exprFlag = 0;
            }
            else {
                temp += equation.charAt(i);
                if (equation.charAt(i) == '(')
                    bracketFlag = 1;
            }
        }
        return temp;
    }
    // Add * between () if no operator i.e (2)(5)6
    public String addMult (String equation, HashSet<String> data) {
        String temp = "";
        int j = 0;

        if (equation.charAt(0) == '+')
            j++;

        for (int i = j; i < equation.length() - 1; i++) {
            temp += equation.charAt(i);

            if (isDigit(i, equation) && exprFunctions(i+1, equation) && !hasBracket(i+1, equation) ||
                equation.charAt(i) == ')' && exprFunctions(i+1, equation)||
                equation.charAt(i) == ')' && isDigit(i+1, equation) ||
                isDigit(i, equation) && equation.charAt(i+1) == '(' ||
                equation.charAt(i) == ')' && equation.charAt(i+1) == '(') {

                if (equation.charAt(i) == ')' && i == equation.length() - 1)
                    break;
                else if (equation.charAt(i) == ')' && equation.charAt(i+1) == ')' || equation.charAt(i) == '*') {}
                else if (equation.charAt(i) == ')' && equation.charAt(i-1) == 't') {}
                else {
                    temp += "*";
                    data.add("*");
                }
            }
        }
        temp += equation.charAt(equation.length() - 1);

        return temp;
    }
    //Reevaluates and removes items in arraylists
    private void modArraylist(ArrayList<Double> operand, ArrayList<String> operator, double result, int j) {
        operand.remove(j);
        operator.remove(j);
        operand.add(j, result);
        operand.remove(j+1);
    }
    // separates the equation to arraylist for operand and operator
    private void equationAssignment(ArrayList<Double> operand, ArrayList<String> operator) {
        String num = "", function = "";
        int ifNegative = 0;

        for (int i = 0; i < expression.length(); i++) {
            if (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')
            {
                num += expression.charAt(i);
                if (i == expression.length() - 1) {
                    if (ifNegative == 1 || expression.charAt(0) == '-' && operand.size() == 0) {
                        operand.add(Double.parseDouble(num) * -1);
                        ifNegative = 0;
                    }
                    else
                        operand.add(Double.parseDouble(num));
                }
            }
            else if (i != 0 && expression.charAt(i) != '(' && expression.charAt(i) != ')') {
                if (expression.charAt(i) == '-' && simpleFunctions(i-1, expression))
                    ifNegative = 1;
                else if (exprFunctions(i, expression) && !simpleFunctions(i, expression)) {
                    if (Character.isDigit(expression.charAt(i-1)))
                        operator.add("*");
                    String t1 = "";
                    while (!isDigit(i, expression) && !simpleFunctions(i, expression)) {
                        t1 += expression.charAt(i);
                        i++;
                    }
                    i--;
                    operator.add(t1);
                }
                else {
                    function += expression.charAt(i);
                    if (expression.charAt(i) == 'm') {
                        function = "mod";
                        i = i + 2;
                    }
                    operator.add(function);
                }
                if (expression.charAt(0) == '-' && operand.size() == 0) // if - operator in the beginning of expression
                    operand.add(Double.parseDouble(num) * -1);
                else if (ifNegative == 1 && num != "") {
                    operand.add(Double.parseDouble(num) * -1);
                    ifNegative = 0;
                }
                else if (num != "")
                    operand.add(Double.parseDouble(num));
                num = "";
                function = "";

            }
            else if (i == 0 && exprFunctions(i, expression) && !simpleFunctions(i, expression)) {
                String t1 = "";
                while (!isDigit(i, expression) && !simpleFunctions(i, expression)) {
                    t1 += expression.charAt(i);
                    i++;
                }
                i--;
                operator.add(t1);
            }
        }
    }
    // Handles which operator goes first
    private void orderOfOperations(ArrayList<Double> operand, ArrayList<String> operator, HashSet<String> data, Error e) {
        double result = 0;

        if (HashExpr(data)) {
            for (int i = 0; i < expression.length(); i++) {
                for (int j = 0; j < operator.size(); j++) {
                    if (operator.get(j).equals("sin")) {
                        result = Math.sin(operand.get(j));
                        operand.remove(j);
                        operator.remove(j);
                        operand.add(j, result);
                        break;
                    }
                    else if (operator.get(j).equals("cos")) {
                        result = Math.cos(operand.get(j));
                        operand.remove(j);
                        operator.remove(j);
                        operand.add(j, result);
                        break;
                    }
                    else if (operator.get(j).equals("tan")) {
                        result = Math.tan(operand.get(j));
                        operand.remove(j);
                        operator.remove(j);
                        operand.add(j, result);
                        break;
                    }
                    else if (operator.get(j).equals("log")) {
                        result = Math.log(operand.get(j)) / Math.log(10);;
                        operand.remove(j);
                        operator.remove(j);
                        operand.add(j, result);
                        break;
                    }
                    else if (operator.get(j).equals("ln")) {
                        result = Math.log(operand.get(j)) / Math.log(2.71828);
                        operand.remove(j);
                        operator.remove(j);
                        operand.add(j, result);
                        break;
                    }
                    else if (operator.get(j).equals("fac")) {
                        result = 1;
                        for (int x = 1; x <= operand.get(j); x++) {
                            result *= x;
                        }
                        operand.remove(j);
                        operator.remove(j);
                        operand.add(j, result);
                        break;
                    }
                    else if (operator.get(j).equals("sqrt")) {
                        result = Math.sqrt(operand.get(j));
                        operand.remove(j);
                        operator.remove(j);
                        operand.add(j, result);
                        break;
                    }
                }
            }
        }
        if (HashSimple(data)) {
            if (data.contains("^")) {
                for (int i = 0; i < expression.length(); i++) {
                    for (int j = 0; j < operator.size(); j++) {
                        if (operator.get(j).equals("^")) {
                            result = Math.pow(operand.get(j), operand.get(j+1));
                            modArraylist(operand, operator, result, j);
                            break;
                        }
                    }
                }
            }
            if (data.contains("*") || data.contains("/") || data.contains("mod")) {
                for (int i = 0; i < expression.length(); i++) {
                    for (int j = 0; j < operator.size(); j++) {
                        if (operator.get(j).equals("*")) {
                            result = operand.get(j) * operand.get(j+1);
                            modArraylist(operand, operator, result, j);
                            break;
                        }
                        else if (operator.get(j).equals("/")) {
                            if (operand.get(j+1) == 0) {
                                e.nanError = 1;
                                break;
                            }
                            result = operand.get(j) / operand.get(j+1);
                            modArraylist(operand, operator, result, j);
                            break;
                        }
                        else if (operator.get(j).equals("mod")) {
                            result = operand.get(j) % operand.get(j+1);
                            modArraylist(operand, operator, result, j);
                            break;
                        }
                    }
                }
            }
            if (data.contains("+") || data.contains("-")) {
                for (int i = 0; i < expression.length(); i++) {
                    for (int j = 0; j < operator.size(); j++) {
                        if (operator.get(j).equals("+")) {
                            result = operand.get(j) + operand.get(j+1);
                            modArraylist(operand, operator, result, j);
                            break;
                        }
                        else if (operator.get(j).equals("-")) {
                            result = operand.get(j) - operand.get(j+1);
                            modArraylist(operand, operator, result, j);
                            break;
                        }
                    }
                }
            }
        }
        if (e.nanError != 1)
            expression = Double.toString(operand.get(0));
    }
    // Evaluate substring inside bracket
    public void subStringEquationAssignment (ArrayList<Double> digits, ArrayList<String> function, String temp) {
        String num = "", funct = "";
        int ifNegative = 0;

        for (int i = 0; i < temp.length(); i++) {
            if (isDigit(i, temp))  {
                num += temp.charAt(i);
                if (i == temp.length() - 1) {
                    if (ifNegative == 1 || temp.charAt(0) == '-' && digits.size() == 0) {
                        digits.add(Double.parseDouble(num) * -1);
                        ifNegative = 0;
                    }
                    else
                        digits.add(Double.parseDouble(num));
                }
            }
            else if (i != 0) {
                if (temp.charAt(i) == '-' && simpleFunctions(i-1, temp))
                    ifNegative = 1;
                else if (exprFunctions(i, temp) && !simpleFunctions(i, temp)) {
                    if (Character.isDigit(temp.charAt(i-1)))
                        function.add("*");
                    String t1 = "";
                    while (!isDigit(i, temp) && !simpleFunctions(i, temp)) {
                        t1 += temp.charAt(i);
                        i++;
                    }
                    i--;
                    function.add(t1);
                }
                else {
                    funct += temp.charAt(i);
                    if (temp.charAt(i) == 'm') {
                        funct = "mod";
                        i = i + 2;
                    }
                    function.add(funct);
                }
                if (temp.charAt(0) == '-' && digits.size() == 0)
                    digits.add(Double.parseDouble(num) * -1);
                else if (ifNegative == 1 && num != "") {
                    digits.add(Double.parseDouble(num) * -1);
                    ifNegative = 0;
                }
                else if (num != "")
                    digits.add(Double.parseDouble(num));
                num = "";
                funct = "";
            }
            else if (i == 0 && exprFunctions(i, temp) && !simpleFunctions(i, temp)) {
                String t1 = "";
                while (!isDigit(i, temp) && !simpleFunctions(i, temp)) {
                    t1 += temp.charAt(i);
                    i++;
                }
                i--;
                function.add(t1);
            }
        }
    }
    // Reevaluates parentheses
    public String subStringOrderOp(ArrayList<Double> digits, ArrayList<String> function, HashSet<String> data, String temp, Error e) {
        double result = 0;

        if (HashExpr(data)) {
            for (int i = 0; i < temp.length(); i++) {
                for (int j = 0; j < function.size(); j++) {
                    if (function.get(j).equals("sin")) {
                        result = Math.sin(digits.get(j));
                        digits.remove(j);
                        function.remove(j);
                        digits.add(j, result);
                        break;
                    }
                    else if (function.get(j).equals("cos")) {
                        result = Math.cos(digits.get(j));
                        digits.remove(j);
                        function.remove(j);
                        digits.add(j, result);
                        break;
                    }
                    else if (function.get(j).equals("tan")) {
                        result = Math.tan(digits.get(j));
                        digits.remove(j);
                        function.remove(j);
                        digits.add(j, result);
                        break;
                    }
                    else if (function.get(j).equals("log")) {
                        result = Math.log(digits.get(j)) / Math.log(10);
                        digits.remove(j);
                        function.remove(j);
                        digits.add(j, result);
                        break;
                    }
                    else if (function.get(j).equals("ln")) {
                        result = Math.log(digits.get(j)) / Math.log(2.71828);
                        digits.remove(j);
                        function.remove(j);
                        digits.add(j, result);
                        break;
                    }
                    else if (function.get(j).equals("fac")) {
                        result = 1;
                        for (int x = 1; x <= digits.get(j); x++) {
                            result *= x;
                        }
                        digits.remove(j);
                        function.remove(j);
                        digits.add(j, result);
                        break;
                    }
                    else if (function.get(j).equals("sqrt")) {
                        result = Math.sqrt(digits.get(j));
                        digits.remove(j);
                        function.remove(j);
                        digits.add(j, result);
                        break;
                    }
                }
            }
        }
        if (HashSimple(data)) {
            if (data.contains("^")) {
                for (int i = 0; i < temp.length(); i++) {
                    for (int j = 0; j < function.size(); j++) {
                        if (function.get(j).equals("^")) {
                            result = Math.pow(digits.get(j), digits.get(j+1));
                            modArraylist(digits, function, result, j);
                            break;
                        }
                    }
                }
            }
            if (data.contains("*") || data.contains("/") || data.contains("mod")) {
                for (int i = 0; i < temp.length(); i++) {
                    for (int j = 0; j < function.size(); j++) {
                        if (function.get(j).equals("*")) {
                            result = digits.get(j) * digits.get(j+1);
                            modArraylist(digits, function, result, j);
                            break;
                        }
                        else if (function.get(j).equals("/")) {
                            if (digits.get(j+1) == 0) {
                                temp = "NaN";
                                e.nanError = 1;
                                break;
                            }
                            result = digits.get(j) / digits.get(j+1);
                            modArraylist(digits, function, result, j);
                            break;
                        }
                        else if (function.get(j).equals("mod")){
                            result = digits.get(j) % digits.get(j+1);
                            modArraylist(digits, function, result, j);
                            break;
                        }
                    }
                }
            }
            if (data.contains("+") || data.contains("-")) {
                for (int i = 0; i < temp.length(); i++) {
                    for (int j = 0; j < function.size(); j++) {
                        if (function.get(j).equals("+")) {
                            result = digits.get(j) + digits.get(j+1);
                            modArraylist(digits, function, result, j);
                            break;
                        }
                        else if (function.get(j).equals("-")) {
                            if (digits.size() == 1) {
                                result = digits.get(j) * -1;
                                digits.remove(j);
                                function.remove(j);
                                digits.add(j, result);
                            }
                            else {
                                result = digits.get(j) - digits.get(j + 1);
                                modArraylist(digits, function, result, j);
                            }
                            break;
                        }
                    }
                }
            }

        }
        if (e.nanError == 0) {
            if (digits.size() == 1)
                temp = Double.toString(digits.get(0));
            else
                temp = "0";
        }
        return temp;
    }
    // Modify equations inside parentheses
    public void modExpression (HashSet<String> data, Error e) {
        ArrayList<Double> digits = new ArrayList<>();
        ArrayList<String> function = new ArrayList<>();
        String tempSubstring = "", tempExpression = "", temp = "";
        int num = 0, j, k;

        for (j = 0; j < expression.length(); j++) {
            if (expression.charAt(j) == '(') {
                num++;
                if (num == numBrackets())
                    break;
                else
                    tempExpression += expression.charAt(j);
            }
            else
                tempExpression += expression.charAt(j);
        }
        for (k = j + 1; k < expression.length(); k++) {
            if (expression.charAt(k) == ')')
                break;
            else
                tempSubstring += expression.charAt(k);
        }
        subStringEquationAssignment (digits, function, tempSubstring);
        temp += subStringOrderOp(digits, function, data, tempSubstring, e);
        if (!temp.equals("NaN")) {
            tempExpression += temp;
            for (int i = k + 1; i < expression.length(); i++) {
                tempExpression  += expression.charAt(i);
            }
            expression = tempExpression;
            if (numBrackets() >= 1)
                modExpression (data, e);
        }
        else  {
            e.nanError = 1;
        }
    }

    private void eval() {
        ArrayList<Double> operand = new ArrayList<>();
        ArrayList<String> operator = new ArrayList<>();
        HashSet<String> data = new HashSet<String>();
        int openBracket = 0, closedBracket = 0;
        double tempExpression = 0;
        Error e = new Error();

        if (bracketVerify(openBracket, closedBracket) && noError(expression)) {
            // Tokenizing expression to Hashset
            int i = 0;
            expression = expression.replace(" ", "");
            while (i < expression.length()) {
                String temp = "";
                if (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.') {
                    while (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.'){
                        temp += expression.charAt(i);
                        i++;
                        if(i >= expression.length())
                            break;
                    }
                }
                else if (simpleFunctions(i, expression)) {
                    if (expression.charAt(i) == 'm') {
                        temp = "mod";
                        i = i + 3;
                    }
                    else {
                        temp += expression.charAt(i);
                        i++;
                    }
                }
                else if (hasBracket(i, expression)) {
                    temp += expression.charAt(i);
                    i++;
                }
                else {
                    while (exprFunctions(i, expression) && !hasBracket(i, expression)) {
                        temp += expression.charAt(i);
                        i++;
                    }
                }
                data.add(temp);
            }
            //System.out.println(data);
            if(HashExpr(data)) {
                for (i = 0; i < expression.length(); i++) {
                    if (exprFunctions(i, expression)) {
                        expression = addBracket(expression, data);
                        break;
                    }
                }
            }
            if (expression.charAt(expression.length() - 2) == '/' && expression.charAt(expression.length() - 1) == '0')
                e.nanError = 1;
            if (e.nanError == 0) {
                if (HashExpr(data) || HashBracket(data)) {
                    if (numBrackets() >= 1) {
                        expression = addMult(expression, data);
                        modExpression(data, e);
                    }
                }
            }
            if (e.nanError == 0) {
                equationAssignment(operand, operator);
                orderOfOperations(operand, operator, data, e);
            }
            // Round expression
            if (e.nanError == 0) {
                tempExpression = Math.round(Double.parseDouble(expression) * 1000000000.0) / 1000000000.0;
                expression = Double.toString(tempExpression);
                // Modify display to erase .0 if it's an integer
                String temp = "";
                for (int k = 0; k < expression.length(); k++) {
                    if (k == expression.length() - 2 && expression.charAt(k) == '.' && expression.charAt(k+1) == '0')
                        break;
                    else
                        temp += expression.charAt(k);
                }
                expression = temp;
            }
        }
        if (!noError(expression))
            expression += "Error";
        else if (!bracketVerify(openBracket, closedBracket))
            expression += "Error:Parentheses";
        else if (e.nanError == 1)
            expression += "NaN";

    }
    @Override
    public void acceptInput(String s) {
        if (s.equalsIgnoreCase("=")) {
            eval();
        } else if (s.equalsIgnoreCase("Backspace")) {
            expression = expression.substring(0, expression.length() - 1);
        } else if (s.equalsIgnoreCase("C")) {
            expression = ""; // clear!
        } else { // accumulate input String
            expression = expression + s;
        }
    }
    @Override
    public String getDisplayString() {
        String temp = "";

        if (expression.contains("Error:Parentheses")) {
            temp = "Error:Parentheses";
            expression = expression.replace("Error:Parentheses", "");
        }

        else if (expression.contains("Error")) {
            temp = "Error";
            expression = expression.replace("Error", "");
        }
        else if (expression.contains("NaN")) {
            temp = "NaN";
            expression = expression.replace("NaN", "");
        }

        if (temp != "")
            return temp;
        else
            return expression;
    }
}
