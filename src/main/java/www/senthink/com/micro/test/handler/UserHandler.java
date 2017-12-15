package www.senthink.com.micro.test.handler;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.senthink.com.micro.test.Util.MD5Util;
import www.senthink.com.micro.test.common.BaseHttpHandler;
import www.senthink.com.micro.test.modle.User;
import www.senthink.com.micro.test.service.UserService;


/**
 * Created by hyacinth on 2017/12/12.
 */
public class UserHandler {

    private UserService userService;

    private BaseHttpHandler baseHttpHandler;

    private final static Logger LOGGER = LoggerFactory.getLogger(UserHandler.class);


    public UserHandler(UserService service, BaseHttpHandler baseHttpHandler){
        this.userService = service;
        this.baseHttpHandler = baseHttpHandler;
    }

    /**
     * 用户注册
     * @param ctx
     */
    public void registerUser(RoutingContext ctx) {
        JsonObject body = ctx.getBodyAsJson();
        User user = new User(body);
        if (checkParams(user)) {
           String md5String = MD5Util.getMD5(user.getPassword());
           user.setPassword(md5String);
           userService.saveOne(user.toJson(), user.getUsername()).setHandler(baseHttpHandler.resultVoidHandler(ctx, "register success"));
        }else {
            baseHttpHandler.badRequest(ctx, "params is wrong");
        }
    }



    public void getLists(RoutingContext context) {
        LOGGER.debug("getLists");
        userService.getLists().setHandler(baseHttpHandler.resultHandler(context));
    }

    /**
     * 校验user参数
     * @param user
     * @return
     */
    private boolean checkParams(User user) {
        if (user.getUsername() == null)
            return false;
        if (user.getPassword() == null)
            return false;
        if (user.getAccount() == null)
            return false;

        if (user.getRegistration() == null)
            return false;
        return true;
    }

    public void completeUserInfo(RoutingContext context) {
        LOGGER.debug("completeUserInfo");
        JsonObject body = context.getBodyAsJson();
        if (body == null) {
            baseHttpHandler.badRequest(context, "params is wrong");
        }else {

        }
    }

    public void getUserLists() {

    }



}
