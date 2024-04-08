package util;

import java.util.Stack;
import java.util.regex.Pattern;

public class CalculateUtils {

    private static void formatExpression(StringBuilder expression) {
        expression.replace(0, expression.length(), expression.toString().replace(" ", ""));
        expression.replace(0, expression.length(), Pattern.compile("\\+|\\-|\\*|\\/|\\%|\\^|\\)|\\(").matcher(expression).replaceAll(match -> " " + match.group() + " "));
        expression.replace(0, expression.length(), expression.toString().replace("  ", " "));
        expression.replace(0, expression.length(), expression.toString().trim());
    }

    private static boolean isOperand(String str) {
        return str.matches("^\\d+$|^[a-zA-Z]$");
    }

    private static int getPriority(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
            case "%":
                return 2;
            default:
                return 0;
        }
    }

    public static String infixToPostfix(String infix) {
        StringBuilder expression = new StringBuilder(infix);
        formatExpression(expression);

        String[] str = expression.toString().split(" ");
        Stack<String> stack = new Stack<>();
        StringBuilder postfix = new StringBuilder();

        for (String s : str) {
            if (isOperand(s))
                postfix.append(s).append(" ");
            else if (s.equals("("))
                stack.push(s);
            else if (s.equals(")")) {
                String x = stack.pop();
                while (!x.equals("(")) {
                    postfix.append(x).append(" ");
                    x = stack.pop();
                }
            } else { // IsOperator(s)
                while (!stack.isEmpty() && getPriority(s) <= getPriority(stack.peek()))
                    postfix.append(stack.pop()).append(" ");
                stack.push(s);
            }
        }

        while (!stack.isEmpty())
            postfix.append(stack.pop()).append(" ");

        return postfix.toString();
    }

    public static float evaluatePostfix(String postfix) {
        Stack<Float> stack = new Stack<>();
        postfix = postfix.trim();

        String[] tokens = postfix.split(" ");

        for (String s : tokens) {
            if (isOperand(s))
                stack.push(Float.parseFloat(s));
            else {
                float x = stack.pop();
                float y = stack.pop();

                switch (s) {
                    case "+":
                        y += x;
                        break;
                    case "-":
                        y -= x;
                        break;
                    case "*":
                        y *= x;
                        break;
                    case "/":
                        y /= x;
                        break;
                    case "%":
                        y %= x;
                        break;
                }
                stack.push(y);
            }
        }
        return stack.pop();
    }
}
