package com.hucloud.client;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

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

    static Vertx vertx;

    public static void main(String [] ar ) {
        vertx = Vertx.vertx();
        VertxOptions options = new VertxOptions();
        options.setMaxEventLoopExecuteTime(Long.MAX_VALUE);
        vertx = Vertx.vertx(options);
        NetClient client = vertx.createNetClient().connect(9313, "vertx.hutt.co.kr", (event)-> {
            Buffer buffer;
            Scanner scanner;
            String receivedMsg = "";

            if ( event.succeeded() ) {
                System.out.println( "Server Message Connection " + event.succeeded() );
                NetSocket socket = event.result();

                socket.handler(event1 -> {
                    System.out.println(String.format("[CLIENT] ReceiveMessage ->  %s", event1.toJsonObject()));
                });

                socket.exceptionHandler((handler)->{
                    handler.printStackTrace();
                });

                while ( true ) {
                    System.out.println( "서버에 전송할 메세지를 입력해주세요. \n");
                    scanner = new Scanner(System.in);
                    if ( scanner.hasNext() ) {
                        receivedMsg = scanner.nextLine();
                        if ( "{end}".equals(receivedMsg)) {
                            break;
                        }
                        buffer = Buffer.buffer(String.format("{ \"packetTp\" : \"MESSAGE\", \"body\" : \"%s\" }", receivedMsg));
                        socket.write(buffer);
                    }
                }
            }
        });
    }
}