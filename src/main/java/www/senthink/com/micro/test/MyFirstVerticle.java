package www.senthink.com.micro.test;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.templ.JadeTemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hyacinth on 2017/9/19.
 */
public class MyFirstVerticle extends AbstractVerticle{

    private final static Logger LOGGER = LoggerFactory.getLogger(MyFirstVerticle.class);

    public void start(Future<Void> future) throws Exception{

        final Router router = Router.router(vertx);

        final MongoClient mongoClient = MongoClient.createShared(vertx, new JsonObject().put("db_name", "micro").put("host", "10.200.2.118"));

        final JadeTemplateEngine jade = JadeTemplateEngine.create();

        router.route().handler(BodyHandler.create());

        router.get("/").handler(ctx ->{
            ctx.put("title", "Vert.x.Web");

            jade.render(ctx, "templates/index", res ->{
                if (res.succeeded()) {
                    ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/html").end(res.result());
                }else {
                    ctx.fail(res.cause());
                }
            });

            router.get("/users").handler(context ->{
                mongoClient.find("users", new JsonObject(), lookup -> {
                    if (lookup.failed()) {
                        ctx.fail(lookup.cause());
                        return;
                    }

                    LOGGER.debug("find users size={}", lookup.result().size());
                    final JsonArray json = new JsonArray();
                    for (JsonObject o : lookup.result())
                        json.add(o);

                    ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");

                    ctx.response().end(json.encode());
                });
            });
        });


        router.post("/users").handler(ctx -> {
            JsonObject user = new JsonObject()
                    .put("username", ctx.request().getFormAttribute("username"))
                    .put("email", ctx.request().getFormAttribute("email"))
                    .put("fullname", ctx.request().getFormAttribute("fullname"))
                    .put("location", ctx.request().getFormAttribute("loaction"))
                    .put("age", ctx.request().getFormAttribute("age"))
                    .put("gender", ctx.request().getFormAttribute("gender"));

            mongoClient.insert("users", user, lookup->{
                if (lookup.failed()) {
                    ctx.fail(lookup.cause());
                    return;
                }

                ctx.response().setStatusCode(201);
                ctx.response().end();

            });
        });

        router.delete("/users/:id").handler(ctx -> {
            mongoClient.removeDocument("users",
                    new JsonObject().put("_id", ctx.request().getParam("id")), lookup ->{
                if (lookup.failed()) {
                    ctx.fail(lookup.cause());
                    return;
                }
                ctx.response().setStatusCode(204);
                ctx.response().end();
            });
        });


       router.route().handler(StaticHandler.create());

        vertx.createHttpServer().requestHandler(router::accept).listen(8080, handler ->{
            if (handler.succeeded()) {
                LOGGER.debug("httpServer listen 8080");
                future.complete();
            }
        });

    }
}
