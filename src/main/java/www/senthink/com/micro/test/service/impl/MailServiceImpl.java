package www.senthink.com.micro.test.service.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailMessage;
import io.vertx.ext.mail.MailResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import www.senthink.com.micro.test.service.MailService;

/**
 * Created by hyacinth on 2017/12/18.
 */
public class MailServiceImpl implements MailService {

    private final MailClient mailClient;

    private final static Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    public MailServiceImpl(MailClient mailClient) {
        this.mailClient = mailClient;
    }

    @Override
    public void send(MailMessage message, Handler<AsyncResult<MailResult>> resultHandler) {
        mailClient.sendMail(message, resultHandler);
    }
}
