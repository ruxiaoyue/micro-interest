package www.senthink.com.micro.test.repository.impl;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.mongo.MongoClient;
import rx.Single;
import www.senthink.com.micro.test.repository.UserRepository;

/**
 * Created by lenovo on 2017/12/12.
 */
public class UserRepositoryImpl implements UserRepository{

    private static final  String COLLECTION = "user";

    private final MongoClient mongoClient;

    public UserRepositoryImpl(io.vertx.core.Vertx vertx, JsonObject mongoConfig) {

        this.mongoClient = MongoClient.createNonShared(Vertx.newInstance(vertx), mongoConfig);
    }

    @Override
    public Single<Void> saveOne(JsonObject user) {
        return mongoClient.rxSave(COLLECTION, user).map(r -> (Void)null);
    }
}
