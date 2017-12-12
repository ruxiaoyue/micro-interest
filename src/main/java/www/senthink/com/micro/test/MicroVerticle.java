package www.senthink.com.micro.test;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by lenovo on 2017/12/12.
 */
public class MicroVerticle extends AbstractVerticle{

    private HttpServer httpServer;

    private final static Logger LOGGER = LoggerFactory.getLogger(MicroVerticle.class);

    //private final static String

    public void start(Future<Void> future) {

        JsonObject mongoConfig = config().getJsonObject("mongo");

        Router router = createRouter();
        String host = config().getString("http.host");
        int port = config().getInteger("http.port");
        createHttpServer(router, host, port);

    }

    private Router createRouter() {

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(StaticHandler.create());

        //router.route().handler(r )

        return router;
    }

    /**
     * 创建httpServer
     * @param router
     * @param host
     * @param port
     * @return
     */
    private Future<Void> createHttpServer(Router router, String host, int port) {
        Future<HttpServer> httpServerFuture = Future.future();
        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(port, host, httpServerFuture.completer());
        return httpServerFuture.map(r -> null);
    }
}
