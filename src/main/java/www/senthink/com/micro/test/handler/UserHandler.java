package www.senthink.com.micro.test.handler;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.senthink.com.micro.test.Util.MD5Util;
import www.senthink.com.micro.test.common.BaseHttpHandler;
import www.senthink.com.micro.test.modle.User;
import www.senthink.com.micro.test.service.UserExtensionService;
import www.senthink.com.micro.test.service.UserService;


/**
 * Created by hyacinth on 2017/12/12.
 */
public class UserHandler {

    private UserService userService;

    private UserExtensionService userEService;

    private BaseHttpHandler baseHttpHandler;

    private final static Logger LOGGER = LoggerFactory.getLogger(UserHandler.class);


    public UserHandler(UserService service, UserExtensionService userEService, BaseHttpHandler baseHttpHandler){
        this.userService = service;
        this.userEService = userEService;
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

    /**
     * 完善用户信息
     * @param context
     */
    public void completeUserInfo(RoutingContext context) {
        JsonObject body = context.getBodyAsJson();
        String account = body.getString("account");
        if (account != null && !account.equalsIgnoreCase("")) {
            userEService.saveOrUpDate(account, body).setHandler(baseHttpHandler.resultVoidHandler(context, "operation success"));
        }else {
            baseHttpHandler.badRequest(context, "params is wrong");
        }
    }

    public void modifyUserInfo(RoutingContext context) {
        JsonObject body = context.getBodyAsJson();
        String account = body.getString("account");
        if (account != null && !account.equalsIgnoreCase("")) {
            userEService.saveOrUpDate(account, body).setHandler(baseHttpHandler.resultVoidHandler(context, "operation success"));
        }else {
            baseHttpHandler.badRequest(context, "params is wrong");
        }
    }


    /**
     * 获取邮箱验证码
     * @param context
     */
    public void getEmailCode(RoutingContext context) {
        String account = context.request().getParam("account");
        String email = context.request().getParam("email");
        if (email ==null || account == null) {
            baseHttpHandler.badRequest(context, "param is wrong");
        }
        userService.getMailCode(account, email).setHandler(baseHttpHandler.resultVoidHandler(context, "operation is success"));
    }

    /**
     * 重置密码
     * @param context
     */
    public void resetPassword(RoutingContext context) {
        JsonObject body = context.getBodyAsJson();
        LOGGER.debug("resetPwd body={}", body);
        userService.resetPassword(body).setHandler(baseHttpHandler.resultVoidHandler(context, "reset password success"));
    }

}
