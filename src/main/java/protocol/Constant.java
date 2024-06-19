package protocol;

public final class Constant {
    /** 表示消息类型的长度 单位字节 */
    public static final int TYPE_SIZE = 1;
    /** 表示数据类型的最大长度 单位字节 */
    public static final int DATA_LEN_SIZE = 4;

    /** 表示消息可描述最大总长度 */
    public static final int BODY_LEN = TYPE_SIZE + DATA_LEN_SIZE;



}
