package www.senthink.com.micro.test.handler;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by lenovo on 2017/12/12.
 */
public class UserContextHandler {

    public void handler(RoutingContext context) {
        JsonObject body = context.getBodyAsJson();
        String name = body.getString("name");
        String password = body.getString("password");
    }
}
