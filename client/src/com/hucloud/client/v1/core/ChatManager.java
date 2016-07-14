package com.hucloud.client.v1.core;

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
public class ChatManager {

    private static ChatManager instance;
    private Connection mConn;

    public ChatManager(Connection con){
        mConn = con;
    }

    public ChatRoom getRoom(String roomName){
        return new ChatRoom(roomName, mConn);
    }


}
