package www.senthink.com.micro.test;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.auth.jwt.JWTOptions;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.StartTLSOptions;
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
import www.senthink.com.micro.test.service.UserExtensionService;
import www.senthink.com.micro.test.service.UserService;
import www.senthink.com.micro.test.service.impl.UserExtensinoServiceImpl;
import www.senthink.com.micro.test.service.impl.UserServiceImpl;

/**
 * Created by hyacinth on 2017/9/19.
 */
public class MyFirstVerticle extends AbstractVerticle{

    private UserHandler userHandler;

    private UserService userService;

    private BaseHttpHandler baseHttpHandler;

    private UserExtensionService userEService;

    private final static String ADD_ONE = "/users";

    private final static String DELETE_USER = "/users/:id";

    private final static String APP_REGISTER = "/api/app/register";

    private final static String APP_LOGIN = "/api/app/login";

    private final static String APP_EMAIL_CODE = "/api/app/get_email_code";

    private final static String APP_COMPLETE_USER_INFO = "/api/app/complete_user_info";

    private final static String APP_RESET_PASSWORD = "/api/app/reset_password";

    private final static String APP_MODIFAY_USER = "/api/app/modify_user_info";

    private UserContextHandler userContextHandler;

    private LoginHandler loginHandler;

    private final static Logger LOGGER = LoggerFactory.getLogger(MyFirstVerticle.class);

    public void start(Future<Void> future) throws Exception{

        JsonObject mongoJson = new JsonObject().put("host", "10.200.2.118").put("db_name", "micro").put("port", 27017);

        JWTAuthOptions config = new JWTAuthOptions()
                .setKeyStore(new KeyStoreOptions()
                        .setPath("keystore.jceks")
                        .setPassword("secret"));

        MailConfig mailConfig = new MailConfig();
        mailConfig.setHostname("mail.lierda.com");
        mailConfig.setPort(25);
        mailConfig.setUsername("ruxy@lierda.com");
        mailConfig.setPassword("ruxiaoyue0924");
        mailConfig.setStarttls(StartTLSOptions.REQUIRED);
        mailConfig.setTrustAll(true);
        MailClient mailClient = MailClient.createNonShared(vertx, mailConfig);


        userService = new UserServiceImpl(vertx, mongoJson, mailClient);

        userEService = new UserExtensinoServiceImpl(vertx, mongoJson);

        baseHttpHandler = new BaseHttpHandler();

        userHandler = new UserHandler(userService, userEService, baseHttpHandler);

        userContextHandler = new UserContextHandler();

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
        //注册用户
        router.route(APP_REGISTER).handler(this.userHandler::registerUser);
        //用户登录
        router.route(APP_LOGIN).handler(r -> loginHandler.loginHandler(r, provider));

        router.route("/api/app/get").handler(this.userHandler::getLists);
        //获取邮箱验证码
        router.route(APP_EMAIL_CODE).handler(this.userHandler::getEmailCode);
        //重置密码
        router.route(APP_RESET_PASSWORD).handler(this.userHandler::resetPassword);
        //登录权限校验
        router.route("/api/*").handler(JWTAuthHandler.create(provider));
        //完善用户信息
        router.route(APP_COMPLETE_USER_INFO).handler(this.userHandler::completeUserInfo);

        router.route(APP_MODIFAY_USER).handler(this.userHandler::modifyUserInfo);

        //web
        router.route(ADD_ONE).handler(r -> userHandler.registerUser(r));



        //router.route("/api/*").handler(JWTAuthHandler.create(provider, "/api/newToken"));

        router.get("/api/newToken").handler(ctx -> {
            ctx.response().putHeader("Content-Type", "text/plain");
            ctx.response().end(provider.generateToken(new JsonObject(), new JWTOptions().setExpiresInSeconds(60L)));
        });


        //router.route("/api/app*").handler(r -> userContextHandler.handler(r, provider));



        router.route().handler(StaticHandler.create());

        return router;
    }

    private Future<Void> httpServer(Future<Void> future, JWTAuthOptions config) {

        vertx.createHttpServer().requestHandler(createRouter(config)::accept).listen(8080, handler ->{
            if (handler.succeeded()) {
                LOGGER.debug("httpServer listen 8080");
                future.complete();
            }else {
                future.fail(handler.cause());
            }
        });
        return future;
    }
}
