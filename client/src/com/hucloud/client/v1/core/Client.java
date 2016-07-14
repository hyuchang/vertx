package com.hucloud.client.v1.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hucloud.huchat.protocol.listener.*;
import com.hucloud.huchat.protocol.packet.*;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 해당 파일은 소유권은 신휴창에게 있습니다.
 * 현재 오픈 소스로 공개중인 버전은 AGPL을 따르는 오픈 소스 프로젝트이며,
 * 소스 코드를 수정하셔서 사용하는 경우에는 반드시 동일한 라이센스로 소스 코드를 공개하여야 합니다.
 * 만약 HUCLOUD를 상업적으로 이용하실 경우에는 라이센스를 구매하여 사용하셔야 합니다.
 * email : huttchang@gmail.com
 * 프로젝트명    : vertXserver
 * 작성 및 소유자 : hucloud
 * 최초 생성일   : 2016. 6. 5.
 */
public class Client {

    static {
        mSocketConnection = new Connection();
    }

    public static Client instance;

    private static Vertx vertxInstance;

    private static final String HOST = "localhost";

    private static final int IP = 9313;

    private static NetClient mNetclient;

    private static Packet receivedPacket;

    private PacketListener mPacketListener;

    private Client socketResult;

    private static Connection mSocketConnection;

    private static ChatManager chatManager;

    private Client() {}

    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    private static Vertx getVertxInstance() {
        if (vertxInstance == null) {
            VertxOptions options = new VertxOptions();
            options.setMaxEventLoopExecuteTime(Long.MAX_VALUE);
            vertxInstance = Vertx.factory.vertx(options);
        }
        return vertxInstance;
    }

    public Client init() {
        vertxInstance = getVertxInstance();
        return this;
    }

    public Client connect() {
        mNetclient = vertxInstance.createNetClient().connect(IP, HOST, new Handler<AsyncResult<NetSocket>>() {

            @Override
            public void handle(AsyncResult<NetSocket> socket) {

                System.out.println("Server Message Connection " + socket.succeeded());
                NetSocket socketResult = socket.result();
                mSocketConnection.setNativeSocket(socketResult);
                chatManager = new ChatManager(mSocketConnection);
                socketResult.handler(new Handler<Buffer>() {
                    @Override
                    public void handle(Buffer event) {
                        System.out.println(String.format("[CLIENT] ReceiveMessage ->  %s", event.toJsonObject()));
                        ObjectMapper packetMapper = new ObjectMapper();
                        try {
                            Class packetClass;
                            switch (event.toJsonObject().getString("type")) {
                                case "MESSAGE" : default :
                                    packetClass = MessagePacket.class;
                                    mPacketListener = new MessageListener();
                                    break;
                                case "JOIN"    :
                                    packetClass = JoinPacket.class;
                                    mPacketListener = new JoinListener();
                                    break;
                                case "INVITE"  :
                                    packetClass = InvitePacket.class;
                                    mPacketListener = new InviteListener();
                                    break;
                                case "READ"    :
                                    packetClass = ReadPacket.class;
                                    mPacketListener = new ReadListener();
                                    break;
                            }

                            receivedPacket = (Packet) packetMapper.readValue(event.toString(), packetClass);
                            mPacketListener.onReceived(receivedPacket);
                        }catch (ClassCastException cce) {
                            cce.printStackTrace();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                socketResult.write(
                    Buffer.buffer(String.format(
                        "{ " +
                                "\"from\" : \"%S\", " +
                                "\"to\" : \"%s\", " +
                                "\"title\" : \"%s\", " +
                                "\"body\" : \"%s\", " +
                                "\"type\" : \"%s\", " +
                                "\"receivedDate\" : \"%s\" " +
                                "}"
                        , "fromUser", "toUser", "title", "messsaaaaaggggggeeeee1", "MESSAGE", new SimpleDateFormat("yyyy-MM-dd").format(new Date()))));

            }

        });
        return this;
    }

    public Connection getSocketConnection(){
        return mSocketConnection;
    }

    public ChatManager getChatManager(){
        return chatManager;
    }

    public void addPacketListener(PacketListener listener) {
        mPacketListener = listener;
    }

}