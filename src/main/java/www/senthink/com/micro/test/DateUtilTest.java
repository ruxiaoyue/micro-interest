package www.senthink.com.micro.test;

import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.senthink.com.micro.test.Util.DateUtil;

/**
 * Created by lenovo on 2017/12/1.
 */
public class DateUtilTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(DateUtilTest.class);

    public static void main(String[] args){
        /*DateUtil dateUtil = new DateUtil();
        JsonObject beaconTime = dateUtil.getBeaconStartTime();
        long before = beaconTime.getLong("before");
        long after = beaconTime.getLong("after");
        //String time = dateUtil.getNow();
        LOGGER.debug("before={}, after={}", before, after);
        //LOGGER.debug("time={}", time);
        long timeValue = 1512981096000l;
        String timeToString = dateUtil.longTimeToString(timeValue);
        LOGGER.debug("timeToString={}", timeToString);
        long offset = dateUtil.offset(timeValue);
        LOGGER.debug(" offset={}", offset);*/
        Integer p = 2;
        LOGGER.debug("p={}", p.doubleValue());
        Double m = Math.pow(2.0, p.doubleValue());
        LOGGER.debug("m={}, longValue={}", m, m.longValue());
    }





}
