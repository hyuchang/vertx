package com.hucloud.client.v1.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hucloud.huchat.protocol.listener.*;
import com.hucloud.huchat.protocol.packet.*;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

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

    private static final String HOST = "localhost";

//    private static final String HOST = "vertx.hutt.co.kr";

    private static final int IP = 9313;

    private static Vertx vertx;

    public static void main(String [] ar ) {
        vertx = Vertx.vertx();
        VertxOptions options = new VertxOptions();
        options.setMaxEventLoopExecuteTime(Long.MAX_VALUE);
        vertx = Vertx.vertx(options);
        NetClient client = vertx.createNetClient().connect(IP, HOST, (event)-> {

            if ( event.succeeded() ) {
                System.out.println( "Server Message Connection " + event.succeeded() );
                NetSocket socket = event.result();

                socket.handler(event1 -> {
                    System.out.println(String.format("[CLIENT] ReceiveMessage ->  %s", event1.toJsonObject()));
                    ObjectMapper packetMapper = new ObjectMapper();
                    try {
                        Class packetClass;
                        PacketListener packetListener;
                        switch (event1.toJsonObject().getString("type")) {
                            case "MESSAGE" : default :
                                packetClass = MessagePacket.class;
                                packetListener = new MessageListener();
                                break;
                            case "JOIN"    :
                                packetClass = JoinPacket.class;
                                packetListener = new JoinListener();
                                break;
                            case "INVITE"  :
                                packetClass = InvitePacket.class;
                                packetListener = new InviteListener();
                                break;
                            case "READ"    :
                                packetClass = ReadPacket.class;
                                packetListener = new ReadListener();
                                break;
                        }

                        Packet receivedPacket = (Packet) packetMapper.readValue(event1.toString(), packetClass);
                        packetListener.onReceived(receivedPacket);
                    }catch (ClassCastException cce) {
                        cce.printStackTrace();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                socket.exceptionHandler(handler->{
                    handler.printStackTrace();
                });

                new Thread(()->{
                    Buffer buffer;
                    Scanner scanner;
                    String receivedMsg = "";

                    while ( true ) {
                        System.out.println( "서버에 전송할 메세지를 입력해주세요. \n");
                        scanner = new Scanner(System.in);
                        if ( scanner.hasNext() ) {
                            receivedMsg = scanner.nextLine();
                            if ( "{end}".equals(receivedMsg)) {
                                break;
                            }
                            buffer = Buffer.buffer(String.format(
                                "{ " +
                                    "\"from\" : \"%S\", " +
                                    "\"to\" : \"%s\", " +
                                    "\"title\" : \"%s\", " +
                                    "\"body\" : \"%s\", " +
                                    "\"type\" : \"%s\", " +
                                    "\"receivedDate\" : \"%s\" " +
                                "}"
                            , "fromUser", "toUser" , "title", receivedMsg, "MESSAGE", new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
                            socket.write(buffer);
                            buffer = Buffer.buffer(String.format(
                                "{ " +
                                    "\"from\" : \"%S\", " +
                                    "\"to\" : \"%s\", " +
                                    "\"title\" : \"%s\", " +
                                    "\"body\" : \"%s\", " +
                                    "\"type\" : \"%s\", " +
                                    "\"receivedDate\" : \"%s\" " +
                                "}"
                            , "fromUser", "toUser" , "title", receivedMsg, "JOIN", new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
                            socket.write(buffer);
                            buffer = Buffer.buffer(String.format(
                                "{ " +
                                    "\"from\" : \"%S\", " +
                                    "\"to\" : \"%s\", " +
                                    "\"title\" : \"%s\", " +
                                    "\"body\" : \"%s\", " +
                                    "\"type\" : \"%s\", " +
                                    "\"receivedDate\" : \"%s\" " +
                                "}"
                            , "fromUser", "toUser" , "title", receivedMsg, "INVITE", new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
                            socket.write(buffer);
                            buffer = Buffer.buffer(String.format(
                                "{ " +
                                    "\"from\" : \"%S\", " +
                                    "\"to\" : \"%s\", " +
                                    "\"title\" : \"%s\", " +
                                    "\"body\" : \"%s\", " +
                                    "\"type\" : \"%s\", " +
                                    "\"receivedDate\" : \"%s\" " +
                                "}"
                            , "fromUser", "toUser" , "title", receivedMsg, "READ", new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
                            socket.write(buffer);
                        }
                    }

                }).start();

            }
        });
    }
}