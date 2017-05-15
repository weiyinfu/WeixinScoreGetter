package web;

import java.io.InputStream;
import java.util.Scanner;

public class Util {
public static String toHexString(byte[] data) {
   StringBuilder builder = new StringBuilder();
   for (byte b : data) {
      builder.append(String.format("%02x", b));
   }
   return builder.toString();
}

public static String tos(InputStream cin) {
   StringBuilder builder = new StringBuilder();
   Scanner scanner = new Scanner(cin);
   while (scanner.hasNext()) {
      builder.append(scanner.nextLine());
   }
   return builder.toString();
}

public static void main(String[] args) {
   System.out.println(toHexString(new byte[]{1, 2, 3, 4}));
}
}
