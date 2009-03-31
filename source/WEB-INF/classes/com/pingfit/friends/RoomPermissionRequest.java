package com.pingfit.friends;

/**
 * User: Joe Reger Jr
 * Date: Mar 30, 2009
 * Time: 8:17:56 PM
 */
public class RoomPermissionRequest {

    private int useridofrequestor;
    private String nicknameofrequestor;
    private int roomid;
    private String roomname;

    public int getUseridofrequestor() {
        return useridofrequestor;
    }

    public void setUseridofrequestor(int useridofrequestor) {
        this.useridofrequestor=useridofrequestor;
    }

    public String getNicknameofrequestor() {
        return nicknameofrequestor;
    }

    public void setNicknameofrequestor(String nicknameofrequestor) {
        this.nicknameofrequestor=nicknameofrequestor;
    }

    public int getRoomid() {
        return roomid;
    }

    public void setRoomid(int roomid) {
        this.roomid=roomid;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname=roomname;
    }
}
