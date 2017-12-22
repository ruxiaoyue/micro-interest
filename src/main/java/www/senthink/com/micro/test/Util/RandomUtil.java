package www.senthink.com.micro.test.Util;

import org.apache.commons.lang3.Validate;

import java.security.SecureRandom;

/**
 * Created by lenovo on 2017/12/18.
 */
public class RandomUtil {
    private static SecureRandom random = new SecureRandom();

    public static byte[] randomBytes(int bytesNum) {
        Validate.isTrue(bytesNum > 0, "bytes number must bigger than zero!");
        byte[] bytes = new byte[bytesNum];
        random.nextBytes(bytes);

        return bytes;
    }

    /**
     * 产生一个范围从0(包括0)~bound(不包括bound)的随机整形数据
     * @param bound
     * @return
     */
    public static int getRandomInt(int bound) {
        return random.nextInt(bound);
    }

    public static int randomInt() {
        return random.nextInt();
    }
}
