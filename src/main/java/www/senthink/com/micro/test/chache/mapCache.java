package www.senthink.com.micro.test.chache;

import io.vertx.core.json.JsonObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lenovo on 2017/12/13.
 */
public class mapCache {

    private final static Map<String, JsonObject> map = new ConcurrentHashMap<>();

    public void add(String eui, Long time) {
        JsonObject json = map.get(eui);
        if (json == null) {
            json = new JsonObject().put("timeOne", time);
        }else {
            json.put("timeOne", time);
        }
        map.put(eui, json);

    }

    public Long getTimeOne(String eui) {
        JsonObject json = map.get(eui);
        if (json != null) {
            return json.getLong("timeOne");
        }else {
            return null;
        }

    }

    public Long getGpsTime(String gatewayEui) {
        JsonObject json = map.get(gatewayEui);
        if (json != null) {
            return json.getLong("gpsTime");
        }else {
            return null;
        }
    }
}
