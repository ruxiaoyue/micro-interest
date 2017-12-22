package www.senthink.com.micro.test.repository;

import io.vertx.core.json.JsonObject;
import rx.Single;

/**
 * Created by lenovo on 2017/12/20.
 */
public interface UserExpensionRepository {

    Single<Void> saveOne(JsonObject userExtension);

    Single<Void> update(String account, JsonObject userE);

    Single<JsonObject> findOneByAccount(String account);
}
