package www.senthink.com.micro.test.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mail.MailResult;

import java.util.List;

/**
 * Created by hyacinth on 2017/12/12.
 */
public interface UserService {

    Future<Void> saveOne(JsonObject userJson, String userName);

    Future<List<JsonObject>> getLists();

    Future<JsonObject> findByAccount(String account);

    Future<Void> getMailCode(String account, String email);

    Future<Void> resetPassword(JsonObject body);

}
