package www.senthink.com.micro.test;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.auth.jwt.JWTOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.templ.JadeTemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.senthink.com.micro.test.common.BaseHttpHandler;
import www.senthink.com.micro.test.handler.LoginHandler;
import www.senthink.com.micro.test.handler.UserContextHandler;
import www.senthink.com.micro.test.handler.UserHandler;
import www.senthink.com.micro.test.service.UserService;
import www.senthink.com.micro.test.service.impl.UserServiceImpl;

/**
 * Created by hyacinth on 2017/9/19.
 */
public class MyFirstVerticle extends AbstractVerticle{

    private UserHandler userHandler;

    private UserService userService;

    private BaseHttpHandler baseHttpHandler;

    private final static String ADD_ONE = "/users";

    private final static String DELETE_USER = "/users/:id";

    private final static String APP_REGISTER = "/api/app/register";

    private final static String APP_LOGIN = "/api/app/login";

    private final static String APP_COMPLETE_USER_INFO = "/api/app/complete_user_info";

    private UserContextHandler userContextHandler;

    private LoginHandler loginHandler;


    private final static Logger LOGGER = LoggerFactory.getLogger(MyFirstVerticle.class);

    public void start(Future<Void> future) throws Exception{

        JsonObject mongoJson = new JsonObject().put("host", "10.200.2.118").put("db_name", "micro").put("port", 27017);

        JWTAuthOptions config = new JWTAuthOptions()
                .setKeyStore(new KeyStoreOptions()
                        .setPath("keystore.jceks")
                        .setPassword("secret"));

        userService = new UserServiceImpl(vertx, mongoJson);

        baseHttpHandler = new BaseHttpHandler();

        userHandler = new UserHandler(userService, baseHttpHandler);

        userContextHandler = new UserContextHandler();

        userHandler = new UserHandler(userService, baseHttpHandler);

        loginHandler = new LoginHandler(userService);

        httpServer(future, config);

    }


    private Router createRouter(JWTAuthOptions config) {
        final Router router = Router.router(vertx);
        final JadeTemplateEngine jade = JadeTemplateEngine.create();
        router.route().handler(BodyHandler.create());


        JWTAuth provider = JWTAuth.create(vertx, config);


        router.get("/").handler(ctx ->{
            ctx.put("title", "Vert.x.Web.Test");

            jade.render(ctx, "templates/index", res ->{
                if (res.succeeded()) {
                    ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/html").end(res.result());
                }else {
                    ctx.fail(res.cause());
                }
            });

            router.route("/get").handler(context -> userHandler.getLists(context));
        });


        //app
        router.route(APP_REGISTER).handler(this.userHandler::registerUser);

        router.route(APP_LOGIN).handler(r -> loginHandler.loginHandler(r, provider));

        router.get("/api/app/get").handler(this.userHandler::getLists);

        //web
        router.route(ADD_ONE).handler(r -> userHandler.registerUser(r));

        router.route("/api/*").handler(JWTAuthHandler.create(provider));

        //router.route("/api/*").handler(JWTAuthHandler.create(provider, "/api/newToken"));

        router.get("/api/newToken").handler(ctx -> {
            ctx.response().putHeader("Content-Type", "text/plain");
            ctx.response().end(provider.generateToken(new JsonObject(), new JWTOptions().setExpiresInSeconds(60L)));
        });


        //router.route("/api/app*").handler(r -> userContextHandler.handler(r, provider));

        router.route(APP_COMPLETE_USER_INFO).handler(this.userHandler::completeUserInfo);


        router.route().handler(StaticHandler.create());

        return router;
    }

    private Future<Void> httpServer(Future<Void> future, JWTAuthOptions config) {

        vertx.createHttpServer().requestHandler(createRouter(config)::accept).listen(8080, handler ->{
            if (handler.succeeded()) {
                LOGGER.debug("httpServer listen 8080");
                future.complete();
            }
        });
        return future;
    }



}
