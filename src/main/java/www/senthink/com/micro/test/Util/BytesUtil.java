package www.senthink.com.micro.test.Util;


/**
 * Created by lenovo on 2017/12/14.
 */
public class BytesUtil {

    /**
     * 二进制转十六进制
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer str = new StringBuffer();
        // 把数组每一字节换成16进制连成字符串
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];

            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                str.append("0");
            }
            str.append(Integer.toHexString(digital));
        }
        return str.toString().toUpperCase();
    }
}
