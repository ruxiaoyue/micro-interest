package www.senthink.com.micro.test.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * Created by lenovo on 2017/12/12.
 */
public interface UserService {

    Future<Void> saveOne(JsonObject userJson, String userName);

    Future<List<JsonObject>> getLists();

    Future<Long> countByName(String userName);

    Future<JsonObject> findByAccount(String account);

}
