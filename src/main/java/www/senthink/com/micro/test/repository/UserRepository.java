package www.senthink.com.micro.test.repository;

import io.vertx.core.json.JsonObject;
import rx.Single;

import java.util.List;

/**
 * Created by hyacinth on 2017/12/12.
 */
public interface UserRepository {

    Single<Void> saveOne(JsonObject user);

    Single<List<JsonObject>> getLists();

    Single<Long> countByName(String userName);

    Single<JsonObject> findByAccount(String account);

    Single<Void> updateUserOfValable(String account, Long available, String code);

    Single<Void> updatePassword(String password, String account);

}
