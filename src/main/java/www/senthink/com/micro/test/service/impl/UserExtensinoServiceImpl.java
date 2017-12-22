package www.senthink.com.micro.test.service.impl;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.senthink.com.micro.test.repository.UserExpensionRepository;
import www.senthink.com.micro.test.repository.impl.UserExpensionRepositoryImpl;
import www.senthink.com.micro.test.service.UserExtensionService;

/**
 * Created by hyacinth on 2017/12/20.
 */
public class UserExtensinoServiceImpl implements UserExtensionService{

    private UserExpensionRepository userERepository;

    private final static Logger LOGGER = LoggerFactory.getLogger(UserExtensinoServiceImpl.class);

    public UserExtensinoServiceImpl(Vertx vertx, JsonObject mongoJson) {
        this.userERepository = new UserExpensionRepositoryImpl(vertx, mongoJson);
    }

    @Override
    public Future<Void> saveOrUpDate(String account, JsonObject userE) {
        Future<JsonObject> future = Future.future();
        userERepository.findOneByAccount(account).subscribe(future::complete, future::fail);
        return future.compose(jsonObject -> checkAndSave(jsonObject, userE, account));
    }

    private Future<Void> checkAndSave(JsonObject userExtension, JsonObject userE, String account) {
        Future<Void> future = Future.future();
        if (userExtension != null) {
            userERepository.update(account, userE).subscribe(future::complete, future::fail);
        }else {
            userERepository.saveOne(userE).subscribe(future::complete, future::fail);
        }
        return future;
    }
}
