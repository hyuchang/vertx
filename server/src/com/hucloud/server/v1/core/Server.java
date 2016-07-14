package com.hucloud.server.v1.core;
import com.hucloud.huchat.protocol.packet.*;
import io.vertx.core.AsyncResult;
import io.vertx.core.AsyncResultHandler;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;

/**
 * 해당 파일은 소유권은 신휴창에게 있습니다.
 * 현재 오픈 소스로 공개중인 버전은 AGPL을 따르는 오픈 소스 프로젝트이며,
 * 소스 코드를 수정하셔서 사용하는 경우에는 반드시 동일한 라이센스로 소스 코드를 공개하여야 합니다.
 * 만약 HUCLOUD를 상업적으로 이용하실 경우에는 라이센스를 구매하여 사용하셔야 합니다.
 * email : huttchang@gmail.com
 * 프로젝트명    : vertXserver
 * 작성 및 소유자 : hucloud
 * 최초 생성일   : 2016. 6. 2.
 */
public class Server {

    private Vertx vertx = VertX.getInstance().getVertx();

    private static int PORT = 19313;

    private static int LISTEN_PORT = 9313;

    private static String HOST = "localhost";

    private EventBus mEventBus;

    private static NetServerOptions serverOptions;

    private NetServer mServer;

    public void start() throws Exception {
        init();
        connection();
    }

    private void init() {
        serverOptions = new NetServerOptions();
        serverOptions.setHost(HOST);
        serverOptions.setPort(PORT);
        mEventBus = vertx.eventBus();
    }

    private void connection() throws Exception {
        mServer = vertx.createNetServer(serverOptions);

        mServer.connectHandler(new Handler<NetSocket>() {
            @Override
            public void handle(final NetSocket socket) {

                System.out.println("[ Connection ] ");
                System.out.println("Connected RegisterId : " + socket.writeHandlerID());
                System.out.println("Connected HOST : " + socket.remoteAddress().host());

                socket.handler(new Handler<Buffer>() {
                    @Override
                    public void handle(Buffer event) {
                        JsonObject message = event.toJsonObject();
                        System.out.println("Connected RegisterId : " + socket.writeHandlerID());
                        System.out.println("PacketType : " + message.getString("type"));
                        System.out.println("Body : " + message.getString("body"));
                        socket.write(message.toString());
                    }
                });

                socket.closeHandler(new Handler<Void>() {
                    @Override
                    public void handle(Void event) {
                        System.out.println("[ Disconnect ] ");
                        System.out.println("Connected RegisterId : " + socket.writeHandlerID());
                        System.out.println("Connected HOST : " + socket.remoteAddress().host());
                    }
                });
            }
        });

        mServer.listen(LISTEN_PORT, new AsyncResultHandler<NetServer>() {
            @Override
            public void handle(AsyncResult<NetServer> event) {
                if (event.succeeded()) {
                    try {
                        System.out.println("Vertx Server Running!! " + LISTEN_PORT);
                        authenticate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        addDefaultListener();
                    }
                }
            }
        });
    }

    private boolean authenticate() throws Exception {
        //todo db접속 후 인증정보를 가져온다
        return true;
    }

    public void addDefaultListener() {
        Packet packet;
        MessageConsumer<JsonObject> consumer;
        for ( PacketType eventType : PacketType.values() ) {
            consumer = mEventBus.consumer(eventType.name());
            switch (eventType.name()) {
                case "MESSAGE"  : default : packet = new MessagePacket(); break;
                case "INVITE"   : packet = new InvitePacket(); break;
                case "JOIN"     : packet = new JoinPacket(); break;
                case "READ"     : packet = new ReadPacket(); break;
            }
//            consumer.handler(packet);
        }
    }

    public void addtListener(PacketType type, Handler handler) {
        MessageConsumer<String> consumer = mEventBus.consumer(type.name());
        consumer.handler(handler);
    }
}
