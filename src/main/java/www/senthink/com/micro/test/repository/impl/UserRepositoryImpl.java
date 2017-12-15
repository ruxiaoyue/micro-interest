package www.senthink.com.micro.test.repository.impl;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.mongo.MongoClient;
import rx.Single;
import www.senthink.com.micro.test.repository.UserRepository;

import java.util.List;

/**
 * Created by lenovo on 2017/12/12.
 */
public class UserRepositoryImpl implements UserRepository{

    private static final  String COLLECTION = "users";

    private final MongoClient mongoClient;

    public UserRepositoryImpl(io.vertx.core.Vertx vertx, JsonObject mongoConfig) {

        this.mongoClient = MongoClient.createNonShared(Vertx.newInstance(vertx), mongoConfig);
    }

    @Override
    public Single<Void> saveOne(JsonObject user) {
        return mongoClient.rxSave(COLLECTION, user).map(r -> (Void)null);
    }

    @Override
    public Single<Long> countByName(String userName) {
        JsonObject query = new JsonObject().put("username", userName);
        return mongoClient.rxCount(COLLECTION, query);
    }

    @Override
    public Single<JsonObject> findByAccount(String account) {
        JsonObject query = new JsonObject().put("account", account);
        return mongoClient.rxFindOne(COLLECTION, query, null);
    }

    @Override
    public Single<List<JsonObject>> getLists() {
        JsonObject query = new JsonObject();
        return mongoClient.rxFind(COLLECTION, query);
    }
}
