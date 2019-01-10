package myproject.smack.enumclass;

/**
 * 消息接收过程中文件的加载状态
 *
 * Created by zby on 2018/12/3.
 */
public enum FileLoadState {
    STATE_LOAD_START(0),//加载开始
    STATE_LOAD_SUCCESS(1),//加载成功
    STATE_LOAD_ERROR(2);//加载失败

    int value;
    FileLoadState(int value) {

        this.value = value;
    }

    public int value() {

        return value;
    }
}
