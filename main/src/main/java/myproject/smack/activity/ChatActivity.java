package myproject.smack.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.Utils;
import com.item.comm.util.ToastyUtil;
import com.item.comm.util.ToolbarUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import myproject.R;
import myproject.R2;
import myproject.presenter.DaoPresenter;
import myproject.smack.adapter.ChatAdapter;
import myproject.smack.bean.ChatUserBean;
import myproject.smack.enumclass.FileLoadState;
import myproject.smack.enumclass.MessageType;
import myproject.smack.greendao.ChatMessageDaoBean;
import myproject.smack.keyboard.ChatKeyboard;
import myproject.smack.keyboard.KeyBoardMoreFunType;
import myproject.smack.manager.SmackManager;
import myproject.smack.recyclerview.CommonRecyclerView;
import myproject.utils.AppFileHelper;
import myproject.utils.BitmapUtil;
import myproject.utils.CompressBitmapUtils;
import myproject.utils.DateUtil;
import myproject.utils.FileUtil;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 单聊窗口
 * <p>
 * Created by zby on 2018/12/3.
 */
public class ChatActivity extends IMBaseActivity implements ChatKeyboard.KeyboardOperateListener {

    @BindView(R2.id.chat_content)
    CommonRecyclerView mChatMessageRecyclerView;//聊天内容列表

    @BindView(R2.id.tool_bar)
    Toolbar mToolBar;

    @BindView(R2.id.ckb_chat_board)
    ChatKeyboard mChatKeyboard;// 聊天输入控件

    private ChatUserBean mChatUser;//聊天信息实体类
    private ChatAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private Chat mChat;//聊天窗口对象
    private FileTransferListener fileListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_layout);
        ButterKnife.bind(this);

        mChatUser = new ChatUserBean();
        mChatUser.setFileJid("18274635809@xydev/Smack");
        mChatUser.setChatJid("18274635809@xydev/Smack");
        mChatUser.setMeNickname("18274635808");
        mChatUser.setFriendNickname("18274635809");

        EventBus.getDefault().register(this);

        ToolbarUtils.with(this, mToolBar)
                .setSupportBack(true)
                .setTitle(mChatUser.getFriendNickname(), true)
                .build();
        initRecycleView();
        if (SmackManager.getInstance().isConnect()) {
            mChat = SmackManager.getInstance().createChat(mChatUser.getChatJid());
            addReceiveFileListener();
        } else {
            ToastyUtil.errorShort("创建聊天失败，服务器未连接");
        }
    }

    private void initRecycleView() {
        Intent intent = getIntent();
        final List<ChatMessageDaoBean> listData = (List<ChatMessageDaoBean>) intent.getSerializableExtra("listData");

        mChatKeyboard.setKeyboardOperateListener(this);
        mLayoutManager = new LinearLayoutManager(this);
        mChatMessageRecyclerView.setLayoutManager(mLayoutManager);
        mChatMessageRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mChatKeyboard.hideKeyBoardView();
                return false;
            }
        });

        mAdapter = new ChatAdapter(mActivity, listData);
        mChatMessageRecyclerView.setAdapter(mAdapter);
        //添加视图树监听，recyclerView加载完后再刷新，否则无法移动到最后
        mChatMessageRecyclerView.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                mChatMessageRecyclerView.getViewTreeObserver().removeOnGlobalFocusChangeListener(this);
                mChatMessageRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void chatMessageEvent(ChatMessageDaoBean message) {
        if (!message.getMIsMulti()) {
            mAdapter.add(message);
            mLayoutManager.scrollToPosition(mAdapter.getItemCount() - 1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        SmackManager.getInstance().removeFileTransferListener(fileListener);
    }

    /**
     * 发送消息
     *
     * @param message
     */
    @Override
    public void send(final String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        Observable.just(message)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(final String message) {
                        try {
                            JSONObject json = new JSONObject();
                            json.put(ChatMessageDaoBean.KEY_FROM_NICKNAME, mChatUser.getMeNickname());
                            json.put(ChatMessageDaoBean.KEY_MESSAGE_CONTENT, message);
                            mChat.sendMessage(json.toString());

                            //缓存本地
                            new DaoPresenter().saveSendChatMessage(mChatUser.getFriendNickname(),
                                    mChatUser.getFriendNickname(), message, null,
                                    true, MessageType.MESSAGE_TYPE_TEXT.value());

                        } catch (Exception e) {
                            ToastyUtil.errorShort("发送失败！");
                        }
                    }
                });
    }

    /**
     * 发送文件
     *
     * @param file
     */
    public void sendFile(final File file, int messageType) {
        File newFile;
        if (ImageUtils.isImage(file)) {
            Log.d("---压缩前----", String.format("Size : %s", CompressBitmapUtils.getReadableFileSize(file.length())));
            newFile = CompressBitmapUtils.getDefault(Utils.getApp()).compressToFile(file);
            Log.d("---压缩后----", String.format("Size : %s", CompressBitmapUtils.getReadableFileSize(newFile.length())));
        } else {
            newFile = file;
        }
        final OutgoingFileTransfer transfer = SmackManager.getInstance().getSendFileTransfer(mChatUser.getFileJid());
        try {
            transfer.sendFile(newFile, String.valueOf(messageType));
            checkTransferStatus(transfer, newFile, messageType, true);
        } catch (SmackException e) {
            Logger.e(e, "发送文件失败");
        }
    }

    /**
     * 接收文件
     */
    public void addReceiveFileListener() {

        fileListener = new FileTransferListener() {
            @Override
            public void fileTransferRequest(FileTransferRequest request) {
                IncomingFileTransfer transfer = request.accept();
                try {
                    String description = request.getDescription();
                    int messageType = Integer.parseInt(request.getDescription());

                    File dir = AppFileHelper.getAppChatMessageDir(messageType);
                    File file = new File(dir, request.getFileName());
                    transfer.recieveFile(file);
                    checkTransferStatus(transfer, file, messageType, false);
                } catch (SmackException | IOException e) {
                    Logger.e(e, "接收文件失败");
                }
            }
        };
        SmackManager.getInstance().addFileTransferListener(fileListener);
    }


    /**
     * 检查发送文件、接收文件的状态
     *
     * @param transfer
     * @param file        发送或接收的文件
     * @param messageType 文件类型，语音或图片
     * @param isMeSend    是否为发送
     */
    private void checkTransferStatus(final FileTransfer transfer, final File file, final int messageType, final boolean isMeSend) {
        //缓存本地
        final ChatMessageDaoBean messageDaoBean = new DaoPresenter().saveSendChatMessage(mChatUser.getFriendNickname(), mChatUser.getFriendNickname(),
                null, file.getAbsolutePath(), isMeSend, messageType);


        Observable.create(new Observable.OnSubscribe<ChatMessageDaoBean>() {
            @Override
            public void call(Subscriber<? super ChatMessageDaoBean> subscriber) {
                subscriber.onNext(messageDaoBean);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map(new Func1<ChatMessageDaoBean, ChatMessageDaoBean>() {
                    @Override
                    public ChatMessageDaoBean call(ChatMessageDaoBean chatMessage) {
                        long startTime = -1;
                        while (!transfer.isDone()) {
                            try {
                                if (transfer.getStatus().toString().equals(FileTransfer.Status.error)) {
                                    ToastyUtil.errorShort("发送错误");
                                } else {
                                    double progress = transfer.getProgress();
                                    if (progress > 0.0 && startTime == -1) {
                                        startTime = System.currentTimeMillis();
                                    }
                                    progress *= 100;
                                    System.out.println("status=" + transfer.getStatus());
                                    System.out.println("下载或上传=" + progress);
                                }
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        return chatMessage;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ChatMessageDaoBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("文件错误", e.getMessage());
                    }

                    @Override
                    public void onNext(ChatMessageDaoBean chatMessage) {
                        Log.e("---发送和接收是否成功--", FileTransfer.Status.complete.toString()
                                + "----" + transfer.getStatus());
                        if (FileTransfer.Status.complete.toString().equals(transfer.getStatus().toString())) {//传输完成
                            chatMessage.setMFileLoadState(FileLoadState.STATE_LOAD_SUCCESS.value());
                            mAdapter.update(chatMessage);
                        } else {
                            ToastyUtil.errorShort("发送错误");
                            chatMessage.setMFileLoadState(FileLoadState.STATE_LOAD_ERROR.value());
                            mAdapter.update(chatMessage);
                        }
                    }
                });
    }

    /**
     * 发送语音消息
     *
     * @param audioFile
     */
    @Override
    public void sendVoice(File audioFile) {
        sendFile(audioFile, MessageType.MESSAGE_TYPE_VOICE.value());
    }

    private static final int REQUEST_CODE_GET_IMAGE = 1;//图片
    private static final int REQUEST_CODE_TAKE_PHOTO = 2;//拍照

    @Override
    public void functionClick(KeyBoardMoreFunType funType) {
        switch (funType) {
            case FUN_TYPE_IMAGE://选择图片
                selectImage();
                break;
            case FUN_TYPE_TAKE_PHOTO://拍照
                takePhoto();
                break;
        }
    }

    /**
     * 从图库选择图片
     */
    public void selectImage() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"), REQUEST_CODE_GET_IMAGE);
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"), REQUEST_CODE_GET_IMAGE);
        }
    }

    private String mPicPath = "";

    /**
     * 拍照
     */
    public void takePhoto() {
        String dir = AppFileHelper.getAppChatMessageDir(MessageType.MESSAGE_TYPE_IMAGE.value()).getAbsolutePath();
        mPicPath = dir + "/" + DateUtil.formatDatetime(new Date(), "yyyyMMddHHmmss") + ".png";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mPicPath)));
        startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_TAKE_PHOTO) {//拍照成功
                takePhotoSuccess();
            } else if (requestCode == REQUEST_CODE_GET_IMAGE) {//图片选择成功
                Uri dataUri = data.getData();
                if (dataUri != null) {
                    File file = FileUtil.uri2File(this, dataUri);
                    sendFile(file, MessageType.MESSAGE_TYPE_IMAGE.value());
                }
            }
        }
    }

    /**
     * 照片拍摄成功
     */
    public void takePhotoSuccess() {
        Bitmap bitmap = BitmapUtil.createBitmapWithFile(mPicPath, 640);
        BitmapUtil.createPictureWithBitmap(mPicPath, bitmap, 80);
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        sendFile(new File(mPicPath), MessageType.MESSAGE_TYPE_IMAGE.value());
    }


    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

}
