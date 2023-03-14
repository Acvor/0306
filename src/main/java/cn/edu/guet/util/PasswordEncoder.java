package cn.edu.guet.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;
import java.util.UUID;

/**
 * 密码加密
 *
 * @Author Liwei
 * @Date 2021-08-13 09:18
 */
public class PasswordEncoder {

    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
            "e", "f"};

    private final static String MD5 = "MD5";
    private final static String SHA = "SHA";

    private Object salt;
    private String algorithm;

    public PasswordEncoder(Object salt) {
        this(salt, MD5);
    }

    public PasswordEncoder(Object salt, String algorithm) {
        this.salt = salt;
        this.algorithm = algorithm;
    }
    public PasswordEncoder(){}

    /**
     * 密码加密
     *
     * @param rawPass
     * @return
     */
    public String encode(String rawPass) {
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            // 加密后的字符串
            result = byteArrayToHexString(md.digest(mergePasswordAndSalt(rawPass).getBytes("utf-8")));
        } catch (Exception ex) {
        }
        return result;
    }

    /**
     * 密码匹配验证
     *
     * @param encPass 密文
     * @param rawPass 明文
     * @return
     */
/*
    public boolean matches(String encPass, String rawPass) {
        String pass1 = "" + encPass;
        String pass2 = encode(rawPass);

        return pass1.equals(pass2);
    }


 */

    public static boolean matches(String encPass, String rawPass) {
        String pass1 = "" + encPass;
        PasswordEncoder passwordEncoder = new PasswordEncoder("我的盐");
        String pass2 = passwordEncoder.encode(rawPass);

        return pass1.equals(pass2);
    }

    /**
     * MD5加密之方法一
     *
     * @param str 待加密字符串
     * @return 16进制加密字符串
     * @explain 借助apache工具类DigestUtils实现
     */
    public static String encryptToMD5(String str) {
        return DigestUtils.md5Hex(str);
    }

    private String mergePasswordAndSalt(String password) {
        if (password == null) {
            password = "";
        }

        if ((salt == null) || "".equals(salt)) {
            return password;
        } else {
            return password + "{" + salt.toString() + "}";
        }
    }

    /**
     * 转换字节数组为16进制字串
     *
     * @param b 字节数组
     * @return 16进制字串
     */
    private String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * 将字节转换为16进制
     *
     * @param b
     * @return
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String GetMySaltPassword(String password){
        PasswordEncoder passwordEncoder = new PasswordEncoder("我的盐");
        String pass = passwordEncoder.encode(password);
        return pass;
    }
    public static void main(String[] args) {
        // System.out.println(UUID.randomUUID().toString().replace("-",""));
        //PasswordEncoder encoderMd5 = new PasswordEncoder("6e9e4676d01c434da03d1aaf45c7413e", "MD5");

        //String encode = encoderMd5.encode("guet1234");

        //PasswordEncoder passwordEncoder = new PasswordEncoder();

        //String pass = passwordEncoder.encode("hgs1234");
        //String pass = new String("hgs1234");
        //pass = PasswordEncoder.GetMySaltPassword(pass);

        //System.out.println(passwordEncoder.matches("40a4b5fa0df0975572b310c9cb124229","hgs1234"));
        //String DatabasePassword = new String("40a4b5fa0df0975572b310c9cb124229");
        //System.out.println(DatabasePassword.equals(PasswordEncoder.GetMySaltPassword(pass)));
        //System.out.println(PasswordEncoder.matches("40a4b5fa0df0975572b310c9cb124229","hgs1234"));



        PasswordEncoder passwordEncoder = new PasswordEncoder("a");
        String password = passwordEncoder.encode("hgs1234");
        System.out.println(password);
    }
//	public static void main(String[] args) {
//		String salt = "helloworld";
//		PasswordEncoder encoderMd5 = new PasswordEncoder(salt, "MD5");
//		String encode = encoderMd5.encode("test");
//		System.out.println(encode);
//		boolean passwordValid = encoderMd5.validPassword("1bd98ed329aebc7b2f89424b5a38926e", "test");
//		System.out.println(passwordValid);
//
//		PasswordEncoder encoderSha = new PasswordEncoder(salt, "SHA");
//		String pass2 = encoderSha.encode("test");
//		System.out.println(pass2);
//		boolean passwordValid2 = encoderSha.validPassword("1bd98ed329aebc7b2f89424b5a38926e", "test");
//		System.out.println(passwordValid2);
//	}

}