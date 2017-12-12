package www.senthink.com.micro.test.handler;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by lenovo on 2017/9/19.
 */
public class RequestHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(RequestHandler.class);

    public void loginPage(RoutingContext ctx) {
        ctx.reroute("/sockJs.html");
    }

    public void nodeUpDataHandler(RoutingContext context) {
        LOGGER.debug("get post method");
        HttpServerResponse response = context.response();
        response.setChunked(true);
        //JsonObject jsonObject = context.getBodyAsJson();
        LOGGER.debug("client reciveUp json={}");

        JsonObject resJson = new JsonObject();
        resJson.put("size", 4).put("code", 1002).put("msg", "success");
        resJson.put("data", "MDAwMDAw")
                .put("nonce", "2")
                .put("timeStamp", new Date().toString())
                .put("pending", 0)
                .put("devEui", "004A77006600173D")
                .put("dataType", 0)
                .put("sign", "9qqwQYLv0dnUirrR6b15dGjSFsvGfMK9ezS54K3ckyQ=");
        response.write(resJson.encode()).end();
    }

    public void getUser(RoutingContext context) {

    }
}
