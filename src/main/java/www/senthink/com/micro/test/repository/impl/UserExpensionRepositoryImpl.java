package www.senthink.com.micro.test.repository.impl;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.mongo.MongoClient;
import rx.Single;
import www.senthink.com.micro.test.repository.UserExpensionRepository;

/**
 * Created by hyacinth on 2017/12/20.
 */
public class UserExpensionRepositoryImpl implements UserExpensionRepository{

    private final static String COLLECTIONS = "userExpension";

    private final MongoClient mongoClient;

    public UserExpensionRepositoryImpl(io.vertx.core.Vertx vertx, JsonObject mongoJson) {
        this.mongoClient = MongoClient.createShared(Vertx.newInstance(vertx), mongoJson);
    }

    @Override
    public Single<Void> saveOne(JsonObject userExtension) {
        return mongoClient.rxSave(COLLECTIONS, userExtension).map(r ->(Void)null);
    }

    @Override
    public Single<Void> update(String account, JsonObject userE) {
        JsonObject query = new JsonObject().put("account", account);
        JsonObject update = new JsonObject().put("$set", userE);
        return mongoClient.rxUpdate(COLLECTIONS, query, update);
    }

    @Override
    public Single<JsonObject> findOneByAccount(String account) {
        JsonObject query = new JsonObject();
        query.put("account", account);
        return mongoClient.rxFindOne(COLLECTIONS, query, null);
    }
}
