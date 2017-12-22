package www.senthink.com.micro.test.service.impl;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailMessage;
import io.vertx.ext.mail.MailResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.senthink.com.micro.test.Util.BytesUtil;
import www.senthink.com.micro.test.Util.DateUtil;
import www.senthink.com.micro.test.Util.MD5Util;
import www.senthink.com.micro.test.Util.RandomUtil;
import www.senthink.com.micro.test.repository.UserExpensionRepository;
import www.senthink.com.micro.test.repository.UserRepository;
import www.senthink.com.micro.test.repository.impl.UserExpensionRepositoryImpl;
import www.senthink.com.micro.test.repository.impl.UserRepositoryImpl;
import www.senthink.com.micro.test.service.MailService;
import www.senthink.com.micro.test.service.UserExtensionService;
import www.senthink.com.micro.test.service.UserService;

import java.util.List;


/**
 * Created by hyacinth on 2017/12/12.
 */
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    private UserExtensionService userEService;

    private MailService mailService;

    private UserExpensionRepository userERepository;

    private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(Vertx vertx, JsonObject mongoConfig, MailClient mailClient) {
        this.userRepository = new UserRepositoryImpl(vertx, mongoConfig);
        this.mailService = new MailServiceImpl(mailClient);
        this.userEService = new UserExtensinoServiceImpl(vertx, mongoConfig);
        this.userERepository = new UserExpensionRepositoryImpl(vertx, mongoConfig);
    }

    @Override
    public Future<Void> saveOne(JsonObject userJson, String userName) {
        Future<Long> countFuture = Future.future();
        userRepository.countByName(userName).subscribe(countFuture::complete, countFuture::fail);
        return countFuture.compose(count -> checkAndSave(userJson, count));
    }

    /**
     * 校验userName并添加user
     * @param userJson
     * @param count
     * @return
     */
    private Future<Void> checkAndSave(JsonObject userJson, Long count) {
        Future<Void> future = Future.future();
        if (count > 0) {
            return Future.failedFuture("userName is exist");
        }
        userRepository.saveOne(userJson).subscribe(future::complete);
        return future;
    }

    @Override
    public Future<List<JsonObject>> getLists() {
        Future<List<JsonObject>> future = Future.future();
        userRepository.getLists().subscribe(future::complete);
        return future;
    }

    @Override
    public Future<JsonObject> findByAccount(String account) {
        Future<JsonObject> future = Future.future();
        userRepository.findByAccount(account).subscribe(future::complete, future::fail);
        return future;
    }

    @Override
    public Future<Void> getMailCode(String account, String email) {
        Future<MailResult> future = Future.future();
        MailMessage message = new MailMessage();
        message.setFrom("ruxy@lierda.com");
        message.setTo(email);
        message.setSubject("micro-interest 云平台用户找回密码");

        byte[] bytes = RandomUtil.randomBytes(3);
        String val = BytesUtil.bytesToHex(bytes);

        message.setText("您好！\n  您此次重置密码的验证码如下，请在 30 分钟内输入验证码进行下一步操作。 如非你本人操作，请忽略此邮件。\n" + val);
        mailService.send(message, future.completer());
        Long available = DateUtil.getTimeLong() + 30 * 60 * 1000;//邮箱验证码有效时间
        JsonObject userExtension = new JsonObject().put("activeTime", available)
                .put("code", val).put("account", account);
        Future<Void> future2 = userEService.saveOrUpDate(account, userExtension);

        return CompositeFuture.all(future2, future).map(r -> (Void)null);

    }

    @Override
    public Future<Void> resetPassword(JsonObject body) {
        Future<JsonObject> future = Future.future();
        String account = body.getString("account");
        userRepository.findByAccount(account).subscribe(future::complete, future::fail);
        Future<JsonObject> future1 = Future.future();
        userERepository.findOneByAccount(account).subscribe(future1::complete, future1::fail);
        return CompositeFuture.all(future, future1).compose(compositeFuture -> checkAndSetPassword(compositeFuture, body));
    }

    /**
     * 校验用户account 和邮箱验证码code 并修改用户密码
     * @param compositeFuture
     * @param body
     * @return
     */
    private Future<Void> checkAndSetPassword(CompositeFuture compositeFuture, JsonObject body) {
        JsonObject user = compositeFuture.resultAt(0);
        JsonObject userE = compositeFuture.resultAt(1);
        if (user != null && userE != null) {
            Future<Void> future = Future.future();
            String code = userE.getString("code");
            Long available = userE.getLong("activeTime");
            Long offset = available - DateUtil.getTimeLong();
            if (code.equalsIgnoreCase(body.getString("code")) && offset > 0) {
                String password = body.getString("password");
                String pwdNew = MD5Util.getMD5(password);
               userRepository.updatePassword(pwdNew, user.getString("account")).subscribe(future::complete, future::fail);
               return future;
            }else {
                return Future.failedFuture("code is timeout or code is error");
            }
        }else {
            return Future.failedFuture("user is not exist");
        }
    }
}

