package service;

import java.util.HashMap;

public class MaXacNhanManager {
    private static HashMap<String, String> emailToCode = new HashMap<>();

    public static void luuMa(String email, String code) {
        emailToCode.put(email, code);
    }

    public static boolean kiemTraMa(String email, String code) {
        return code.equals(emailToCode.get(email));
    }

    public static void xoaMa(String email) {
        emailToCode.remove(email);
    }
}
