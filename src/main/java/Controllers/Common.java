package Controllers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Common {
    private static Common common ;

    private Common() {
    }
    public static Common getInstance(){
        if (common == null)
            common = new Common();
        return common;
    }
    public static String getMd5(String input)  {

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashText = no.toString(16);
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }
            return hashText;
    }


    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println(getMd5("man ye parandgcjmyghjctyjtcjtcfjftujdtuam"));
    }
}
