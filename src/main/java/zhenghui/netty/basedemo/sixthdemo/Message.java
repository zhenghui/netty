package zhenghui.netty.basedemo.sixthdemo;

import java.io.Serializable;

/**
 * User: zhenghui
 * Date:2014/11/11
 * Time:21:41
 * Email:jingbo2759@163.com
 * 要被发送的对象。
 * 懒得写两个对象了，服务端和客户端都发送这个对象。
 */
public class Message implements Serializable{

    private static final long serialVersionUID = 3663916219982277840L;
    private String msg;

    private Long msgId;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "msg='" + msg + '\'' +
                ", msgId=" + msgId +
                '}';
    }
}
