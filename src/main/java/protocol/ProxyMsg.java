package protocol;

import lombok.Builder;
import lombok.experimental.Accessors;


public class ProxyMsg {
    /** 心跳 */
    public static final byte TYPE_HEARTBEAT = 0x00;

    /** 数据传输 */
    public static final byte TYPE_TRANSFER = 0x01;

    /** 连接 */
    public static final byte TYPE_CONNECT = 0x10;

    /** 连接断开 */
    public static final byte TYPE_DISCONNECT = 0x11;

//-----------------------------------------------------
    /** 消息类型 */
    private byte type;
    /** 消息携带数据 */
    private byte[] data;

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}