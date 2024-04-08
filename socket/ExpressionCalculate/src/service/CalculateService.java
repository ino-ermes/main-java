package service;

import util.CalculateUtils;

public class CalculateService {
    public static String evaluateExpression(String input) {
        try {
            return "" + (int)CalculateUtils.evaluatePostfix(CalculateUtils.infixToPostfix(input));
        } catch(Exception e) {
            //e.printStackTrace();
            return "Error";
        }
    }
}
