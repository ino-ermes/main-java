public class StringShori {
    public String toUpperCaseString(String str) {
        return str.toUpperCase();
    }

    public String toLowerCaseString(String str) {
        return str.toLowerCase();
    }

    public String toOppsiteCaseString(String str) {
        int strlen = str.length();
        char result[] = new char[strlen + 1];
        for(int i = 0; i < strlen; ++i) {
            result[i] = str.charAt(i);
            // TO-DO
        }
        result[strlen] = '\0';
        return new String(result);
    }

    public int getLength(String str) {
        return str.length();
    }
}
