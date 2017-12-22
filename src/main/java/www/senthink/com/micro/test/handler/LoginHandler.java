package www.senthink.com.micro.test.handler;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTOptions;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.senthink.com.micro.test.Util.MD5Util;
import www.senthink.com.micro.test.service.UserService;

/**
 * Created by hyacinth on 2017/12/15.
 */
public class LoginHandler {

    private UserService userService;

    private final static Logger LOGGER = LoggerFactory.getLogger(LoginHandler.class);

    public LoginHandler(UserService userService) {
        this.userService = userService;
    }

    public void loginHandler(RoutingContext context, JWTAuth provider) {

        JsonObject body = context.getBodyAsJson();
        if (body == null) {
            context.response().setStatusCode(400).putHeader("content-type", "application/json").end("post body is null");
        }
        String account = body.getString("account");
        String password = body.getString("password");
        Integer registration = body.getInteger("registration");
        LOGGER.debug("account={}, password={}, registeration={}", account, password, registration);
        if (account == null || password == null || registration == null) {
            context.response().setStatusCode(400).putHeader("content-type", "application/json").end("params wrong");
        }else {
            LOGGER.debug("login check");
            userService.findByAccount(account).setHandler(r -> {
                if (r.succeeded()) {
                    JsonObject user = r.result();
                    if (user != null) {
                        String pwd = user.getString("password");
                        String md5Pwd = MD5Util.getMD5(password);
                        LOGGER.debug("userPwd={}, md5pwd ={}", pwd, md5Pwd);
                        if (pwd.equalsIgnoreCase(md5Pwd)
                                && user.getInteger("registration").equals(registration)) {

                            String token = provider.generateToken(new JsonObject().put("account", account), new JWTOptions().setExpiresInMinutes(60l));
                            JsonObject result = new JsonObject().put("username", user.getString("username")).put("token", token);
                            context.response().setStatusCode(200).putHeader("content-type", "application/json").end(result.encodePrettily());
                        }else {
                            context.response().setStatusCode(400).putHeader("content-type", "application/json").end("password wrong");
                        }
                    }else {
                        context.response().setStatusCode(400).putHeader("content-type", "application/json").end("user is not exist");
                    }
                }else {
                    context.response().setStatusCode(500).putHeader("content-type", "application/json").end("interval wrong");
                }
            });
        }




    }
}
