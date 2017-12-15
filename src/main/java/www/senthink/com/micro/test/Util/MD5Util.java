package www.senthink.com.micro.test.Util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hyacinth on 2017/12/14.
 */
public class MD5Util {

    public static String getMD5(String message) {
        String md5String = "";
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageByte = message.getBytes();
            byte[] md5Byte = md.digest(messageByte);
            md5String = BytesUtil.bytesToHex(md5Byte);
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5String;
    }

}

