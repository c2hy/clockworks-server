package io.github.c2hy.clockworks.base;

import io.github.c2hy.clockworks.common.JsonUtils;
import io.undertow.Undertow;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public class UndertowServer implements ActionDefinable {
    private final RoutingHandler routingHandler = new RoutingHandler();

    @Override
    public void ready() {
        int httpPort = Integer.parseInt(System.getProperty("HTTP_PORT", "8080"));
        var server = Undertow.builder()
                .addHttpListener(httpPort, "localhost")
                .setHandler(routingHandler)
                .build();
        server.start();
        log.info("clockworks started on {}", httpPort);
    }

    @Override
    public ActionDefinable action(String routing, Runnable runnable) {
        routingHandler.add("GET", "/actions/" + routing, exchange ->
                this.handleError(exchange, () -> {
                    runnable.run();
                    this.responseEmpty(exchange);
                }));
        return this;
    }

    @Override
    public <T> ActionDefinable action(String routing, Supplier<T> supplier) {
        routingHandler.add("GET", "/actions/" + routing, exchange ->
                this.handleError(exchange, () -> this.responseJson(exchange, supplier.get()))
        );
        return this;
    }

    @Override
    public <T> ActionDefinable action(String routing, Class<T> tClass, Consumer<T> consumer) {
        routingHandler.add("POST", "/actions/" + routing, exchange ->
                exchange.getRequestReceiver().receiveFullBytes((httpServerExchange, bytes) ->
                        this.handleError(exchange, () -> {
                            consumer.accept(JsonUtils.fromJson(bytes, tClass));
                            this.responseEmpty(exchange);
                        })
                )
        );
        return this;
    }

    @Override
    public <T, E> ActionDefinable action(String routing, Class<T> tClass, Function<T, E> function) {
        routingHandler.add("POST", "/actions/" + routing, exchange ->
                exchange.getRequestReceiver().receiveFullBytes((httpServerExchange, bytes) ->
                        this.handleError(exchange, () ->
                                this.responseJson(
                                        exchange,
                                        function.apply(JsonUtils.fromJson(bytes, tClass))
                                ))
                )
        );
        return this;
    }

    private void handleError(HttpServerExchange exchange, Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            this.responseError(exchange, e);
        }
    }

    private void responseError(HttpServerExchange exchange, Exception exception) {
        exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR);
        exchange.getResponseSender().send(String.format("""
                "error": "%s"
                """, exception.getMessage()));
    }

    private void responseEmpty(HttpServerExchange exchange) {
        exchange.setStatusCode(StatusCodes.NO_CONTENT);
        exchange.getResponseSender().close();
    }

    private void responseJson(HttpServerExchange exchange, Object o) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange.getResponseSender().send(JsonUtils.toJson(o));
    }
}
