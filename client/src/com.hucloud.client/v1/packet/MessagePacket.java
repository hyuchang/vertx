package com.hucloud.client.packet;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.impl.MessageImpl;
import io.vertx.core.json.JsonObject;

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
public class MessagePacket implements Packet {

    @Override
    public void handle(Object event) {
        MessageImpl message = (MessageImpl) event;
        System.out.println(String.format("[Client: %s] : %s", ((MessageImpl) event).address(), message.body())  );
    }

    @Override
    public Handler getEventHandler(Handler eventHandler) throws Exception {
        return null;
    }

    @Override
    public EventType getEventType() {
        return null;
    }

    @Override
    public JsonObject getPayload() throws Exception {
        return null;
    }

    @Override
    public String getFrom() throws Exception {
        return null;
    }

    @Override
    public String to() throws Exception {
        return null;
    }

    @Override
    public String body() throws Exception {
        return null;
    }

    @Override
    public String title() throws Exception {
        return null;
    }

    @Override
    public Date getReceivedTime() throws Exception {
        return null;
    }
}
