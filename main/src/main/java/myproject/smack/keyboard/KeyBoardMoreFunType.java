package myproject.smack.keyboard;

/**
 * KeyBoard更多功能选项类型
 * Created by zby on 2018/12/3.
 */
public enum KeyBoardMoreFunType {
    NONE(-1),
    /**
     * 选择图片
     */
    FUN_TYPE_IMAGE(0),
    /**
     * 拍照
     */
    FUN_TYPE_TAKE_PHOTO(1);

    int value;

    KeyBoardMoreFunType(int value) {

        this.value = value;
    }

    public int value() {

        return value;
    }

    public static KeyBoardMoreFunType getFunType(int value) {

        if (value == FUN_TYPE_IMAGE.value()) {
            return FUN_TYPE_IMAGE;
        } else if(value == FUN_TYPE_TAKE_PHOTO.value()) {
            return FUN_TYPE_TAKE_PHOTO;
        } else {
            return NONE;
        }
    }
}
