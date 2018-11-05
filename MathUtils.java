/**
 * Created by randy on 18/11/4.
 */
public class MathUtils {

  public static double parseDouble(String s) {
    if (s == null || s.length() == 0) {
      throw new NumberFormatException("invalid float value");
    }
    int length = s.length();
    int pos = 0;
    for (; pos < length && Character.isWhitespace(s.charAt(pos)); pos++);

    int integer = -1;
    int fraction = -1;
    int fractionCount = 0;
    int exponent = 0;
    int sign = 1;
    int expSign = 1;
    int state = 0;
    char c;
    boolean valid = true;
    for (; pos < length && valid; pos++) {
      c = s.charAt(pos);
      switch (state) {
        case 0: // init state
          if (c == '+' || c == '-') {
            sign = (c == '+' ? 1 : -1);
            state = 1;
            continue;
          }
        case 1: // sign
          if (Character.isDigit(c)) {
            integer = c - '0';
            state = 2;
          } else if (c == '.') {
            state = 3;
          } else {
            valid = false;
          }
          break;
        case 2: // integer part
          if (Character.isDigit(c)) {
            integer = integer * 10 + c - '0';
          } else if (c == '.') {
            state = 3;
          } else if (c == 'e' || c == 'E') {
            state = 5;
          } else {
            valid = false;
          }
          break;
        case 3: // the point
          if (Character.isDigit(c)) {
            fraction = c - '0';
            fractionCount++;
            state = 4;
          } else if (c == 'e' || c == 'E') {
            state = 5;
          } else {
            valid = false;
          }
          break;
        case 4: // fraction part
          if (Character.isDigit(c)) {
            fraction = fraction * 10 + c - '0';
            fractionCount++;
          } else if (c == 'e' || c == 'E') {
            state = 5;
          } else {
            valid = false;
          }
          break;
        case 5: // e/E
          if (c == '+' || c == '-') {
            expSign = (c == '+' ? 1 : -1);
            state = 6;
          } else if (Character.isDigit(c)) {
            exponent = c - '0';
            state = 7;
          } else {
            valid = false;
          }
          break;
        case 6: // exponent sign
          if (Character.isDigit(c)) {
            exponent = c - '0';
            state = 7;
          } else {
            valid = false;
          }
          break;
        case 7: // exponent part
          if (Character.isDigit(c)) {
            exponent = exponent * 10 + c - '0';
          } else {
            valid = false;
          }
          break;
        default:
          valid = false;
          break;
      }
    }

    if (integer == -1 && fraction == -1 || state == 5 || state == 6) {
      throw new NumberFormatException("invalid float value");
    }

    double f = (fraction != -1 ? fraction / powOfTen(fractionCount) : 0);
    f += (integer != -1 ? integer : 0);
    if (sign == -1) {
      f = -f;
    }
    if (exponent > 0) {
      f = (expSign == 1 ? f * powOfTen(exponent) : f / powOfTen(exponent));
    }

    return f;
  }

  private static double powOfTen(int n) {
    long value = 1;
    int m = n >= 0 ? n : -n;
    for (int i = 0; i < m; i++) {
      value *= 10;
    }
    return n >= 0 ? value : 1.0 / value;
  }

  public static void main(String[] args) {
    System.out.println(MathUtils.parseDouble(" 005047e+6"));
    System.out.println(MathUtils.parseDouble("0"));
    System.out.println(MathUtils.parseDouble("1."));
    System.out.println(MathUtils.parseDouble(".2"));
    System.out.println(MathUtils.parseDouble("+.1e+3"));
    System.out.println(MathUtils.parseDouble("46.e3"));
  }
}
