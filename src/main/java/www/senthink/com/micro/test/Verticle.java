package www.senthink.com.micro.test;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.senthink.com.micro.test.handler.RequestHandler;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by hyacinth on 2017/9/18.
 */
public class Verticle extends AbstractVerticle{

    private final static Logger LOGGER = LoggerFactory.getLogger(Verticle.class);

    private RequestHandler requestHandler;

    public void start(Future<Void> voidFuture) {

        //redisServer(vertx);

        vertx.createHttpServer().requestHandler(createRouter()::accept)
                .listen(8010, "0.0.0.0", result->{
            if (result.failed()) {
                voidFuture.fail(result.cause());
            }else {
                LOGGER.debug("httpServer start success");
                voidFuture.succeeded();
            }
        });

        this.requestHandler = new RequestHandler();

        /*vertx.setPeriodic(10000, handler-> {
            socketJsSend();
            *//*LOGGER.debug("eventBus send data");
            eventBus.publish("feed", new JsonObject().put("time", new Date().toString()));*//*
        });*/

    }



    private Router createRouter() {
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        router.route().handler(StaticHandler.create());

        enableCorsSupport(router);

        router.get("/index").handler(routingContext -> requestHandler.loginPage(routingContext));

        router.post("/api/hdis/up").handler(context -> requestHandler.nodeUpDataHandler(context));

        router.get("").handler(context -> requestHandler.getUser(context));

        return router;
    }


    protected void enableCorsSupport(Router router) {

        Set<String> allowHeaders = new HashSet<>();
        allowHeaders.add("x-requested-with");
        allowHeaders.add("Access-Control-Allow-Origin");
        allowHeaders.add("origin");
        allowHeaders.add("Content-Type");
        allowHeaders.add("accept");
        allowHeaders.add("Authorization");

        Set<HttpMethod> allowMethods = new HashSet<>();
        allowMethods.add(HttpMethod.GET);
        allowMethods.add(HttpMethod.POST);
        allowMethods.add(HttpMethod.DELETE);
        allowMethods.add(HttpMethod.PATCH);
        allowMethods.add(HttpMethod.PUT);

        router.route().handler(CorsHandler.create("*")
                .allowedHeaders(allowHeaders)
                .allowedMethods(allowMethods));
    }
}
