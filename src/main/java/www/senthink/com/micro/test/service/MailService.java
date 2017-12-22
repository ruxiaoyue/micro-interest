package www.senthink.com.micro.test.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.mail.MailMessage;
import io.vertx.ext.mail.MailResult;

/**
 * Created by hyacinth on 2017/12/18.
 */
public interface MailService {

    void send(MailMessage message, Handler<AsyncResult<MailResult>> resultHandler);
}
