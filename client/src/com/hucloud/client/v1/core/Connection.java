package com.hucloud.client.v1.core;

import com.hucloud.huchat.protocol.packet.MessagePacket;
import com.hucloud.huchat.protocol.packet.Packet;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 해당 파일은 소유권은 신휴창에게 있습니다.
 * 현재 오픈 소스로 공개중인 버전은 AGPL을 따르는 오픈 소스 프로젝트이며,
 * 소스 코드를 수정하셔서 사용하는 경우에는 반드시 동일한 라이센스로 소스 코드를 공개하여야 합니다.
 * 만약 HUCLOUD를 상업적으로 이용하실 경우에는 라이센스를 구매하여 사용하셔야 합니다.
 * email : huttchang@gmail.com
 * 프로젝트명    : vertXModule
 * 작성 및 소유자 : hucloud
 * 최초 생성일   : 2016. 7. 14.
 */
public class Connection {

    private NetSocket nativeSocket;


    NetSocket getNativeSocket() {
        return nativeSocket;
    }

    void setNativeSocket(NetSocket nativeSocket) {
        this.nativeSocket = nativeSocket;
    }

    public void sendPaket(Packet packet) {
        if ( nativeSocket == null ) {
            return;
        }
        nativeSocket.write(
                Buffer.buffer(String.format(
                        "{ " +
                                "\"from\" : \"%S\", " +
                                "\"to\" : \"%s\", " +
                                "\"title\" : \"%s\", " +
                                "\"body\" : \"%s\", " +
                                "\"type\" : \"%s\", " +
                                "\"receivedDate\" : \"%s\" " +
                                "}"
                        , "fromUser", "toUser", "title", packet.getBody(), packet.getType(), new SimpleDateFormat("yyyy-MM-dd").format(new Date()))));

    }


    public void sendMessage(MessagePacket packet) {
        if ( nativeSocket == null ) {
            return;
        }
        nativeSocket.write(
                Buffer.buffer(String.format(
                        "{ " +
                                "\"from\" : \"%S\", " +
                                "\"to\" : \"%s\", " +
                                "\"title\" : \"%s\", " +
                                "\"body\" : \"%s\", " +
                                "\"type\" : \"%s\", " +
                                "\"receivedDate\" : \"%s\" " +
                                "}"
                        , "fromUser", "toUser", "title", packet.getBody(), "MESSAGE", new SimpleDateFormat("yyyy-MM-dd").format(new Date()))));

    }
}
