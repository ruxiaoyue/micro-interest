package www.senthink.com.micro.test.service.impl;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import www.senthink.com.micro.test.repository.UserRepository;
import www.senthink.com.micro.test.repository.impl.UserRepositoryImpl;
import www.senthink.com.micro.test.service.UserService;

import java.util.List;


/**
 * Created by lenovo on 2017/12/12.
 */
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    public UserServiceImpl(Vertx vertx, JsonObject mongoConfig) {
        this.userRepository = new UserRepositoryImpl(vertx, mongoConfig);
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
    public Future<Long> countByName(String userName) {
        Future<Long> future = Future.future();
        userRepository.countByName(userName).subscribe(future::complete);
        return future;
    }

    @Override
    public Future<JsonObject> findByAccount(String account) {
        Future<JsonObject> future = Future.future();
        userRepository.findByAccount(account).subscribe(future::complete, future::fail);
        return future;
    }
}
