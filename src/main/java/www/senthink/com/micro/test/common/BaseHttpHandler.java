package www.senthink.com.micro.test.common;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lenovo on 2017/12/14.
 */
public class BaseHttpHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(BaseHttpHandler.class);

    /**
     * 该方法会返回Handler<AsyncResult<T>>供REST API去调用.
     * Http返回结果的content type是Json
     * @param context routing context instance
     * @param <T>
     * @return Handler<AsyncResult<T>>
     */
    public  <T> Handler<AsyncResult<T>> resultHandler(RoutingContext context) {
        return ar -> {
            if (ar.succeeded()) {
                T res = ar.result();
                JsonObject json = new JsonObject().put("code", 8000).put("msg", res == null ? "list is empty" : "operation success").put("data", res.toString());
                context.response()
                        .putHeader("content-type", "application/json")
                        .end(json.encodePrettily());
            } else {
                internalError(context, ar.cause());
            }
        };
    }


    /**
     * 该方法会返回Handler<AsyncResult<T>>供REST API去调用.
     *
     * @param context routing context instance
     * @param result result content
     * @return Handler<AsyncResult<T>>
     */
    public Handler<AsyncResult<Void>> resultVoidHandler(RoutingContext context, String result) {
        return ar -> {
            if (ar.succeeded()) {
                context.response()
                        .setStatusCode(200)
                        .putHeader("content-type", "application/json")
                        .end(new JsonObject().put("code", 8001).put("msg", result).encodePrettily());
            } else {
                internalError(context, ar.cause());
            }
        };
    }

    /**
     * 该方法会返回Handler<AsyncResult<T>>
     * @param context
     * @param msg
     * @param data
     * @return
     */
    public Handler<AsyncResult<Void>> resultDataHandler(RoutingContext context, String msg, JsonObject data){
        return ar -> {
            if (ar.succeeded()) {
                context.response()
                        .setStatusCode(200)
                        .putHeader("content-type", "application/json")
                        .end(new JsonObject().put("code", 8001).put("msg", msg).put("data", data).encodePrettily());
            }
        };
    }


    /**
     * HTTP返回Bad Request错误(400)
     * @param context routing context instance
     * @param ex exception
     */
    public void badRequest(RoutingContext context, Throwable ex) {
        context.response().setStatusCode(400)
                .putHeader("content-type", "application/json")
                .end(new JsonObject().put("error", ex.getMessage()).encodePrettily());
    }

    /**
     * Http返回Bad Request错误 和错误消息
     * @param context
     * @param result
     */
    public void badRequest(RoutingContext context, String result) {
        context.response().setStatusCode(400)
                .putHeader("content-type", "application/json")
                .end(new JsonObject().put("code", 9000).put("msg", result).encodePrettily());
    }



    /**
     * HTTP返回服务器内部错误(500)
     * @param context routing context instance
     * @param ex exception
     */
    public void internalError(RoutingContext context, Throwable ex) {
        context.response().setStatusCode(500)
                .putHeader("content-type", "application/json")
                .end(new JsonObject().put("error", ex.getMessage()).encodePrettily());
    }

    public void requestError(RoutingContext context, int statusCode, String errMsg) {
        context.response().setStatusCode(statusCode)
                .putHeader("content-type", "application/json")
                .end(new JsonObject().put("error", errMsg).encodePrettily());
    }
}
