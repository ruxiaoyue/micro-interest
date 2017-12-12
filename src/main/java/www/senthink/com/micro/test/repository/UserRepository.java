package www.senthink.com.micro.test.repository;

import io.vertx.core.json.JsonObject;
import rx.Single;

/**
 * Created by lenovo on 2017/12/12.
 */
public interface UserRepository {

    Single<Void> saveOne(JsonObject user);
}
