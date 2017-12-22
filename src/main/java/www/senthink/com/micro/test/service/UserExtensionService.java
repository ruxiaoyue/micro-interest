package www.senthink.com.micro.test.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * Created by hyacinth on 2017/12/20.
 */
public interface UserExtensionService {

    Future<Void> saveOrUpDate(String account, JsonObject userE);
}
