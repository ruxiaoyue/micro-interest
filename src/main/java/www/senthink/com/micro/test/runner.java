package www.senthink.com.micro.test;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hyacinth on 2017/9/19.
 */
public class runner {

    private final static Logger LOGGER = LoggerFactory.getLogger(runner.class);

    public static void main(String[] arg) {
        Vertx vertx = Vertx.vertx();
        DeploymentOptions options = new DeploymentOptions();
        options.setConfig(new JsonObject().put("host", "0.0.0.0").put("port", 8080));
        vertx.deployVerticle(MyFirstVerticle.class.getCanonicalName(), options, result ->{
            if (result.succeeded()) {
                LOGGER.debug("verticle deploy success");
            }else {
                LOGGER.debug("Verticle deploy faield ::{}", result.cause().getLocalizedMessage());
            }
        });
    }
}
