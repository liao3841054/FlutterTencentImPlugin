package top.huic.tencent_im_plugin.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMSoundElem;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.TIMVideoElem;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfoResult;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.RequiresApi;
import top.huic.tencent_im_plugin.TencentImPlugin;
import top.huic.tencent_im_plugin.entity.MessageEntity;
import top.huic.tencent_im_plugin.entity.SessionEntity;
import top.huic.tencent_im_plugin.enums.ListenerTypeEnum;
import top.huic.tencent_im_plugin.ValueCallBack;

/**
 * 腾讯云IM工具类
 */
public class TencentImUtils {

    /**
     * 获得会话信息
     *
     * @param callback      回调对象
     * @param conversations 原生会话列表
     */
    public static void getConversationInfo(final ValueCallBack<List<SessionEntity>> callback, final List<TIMConversation> conversations) {
        final List<SessionEntity> resultData = new ArrayList<>();

        // 需要获取用户信息的列表
        final Map<String, SessionEntity> userInfo = new HashMap<>();

        // 需要获取的群信息列表
        final Map<String, SessionEntity> groupInfo = new HashMap<>();

        // 获取会话列表
        for (final TIMConversation timConversation : conversations) {
            // 封装会话信息
            SessionEntity entity = new SessionEntity();
            entity.setId(timConversation.getPeer());
            entity.setNickname(timConversation.getGroupName());
            entity.setType(timConversation.getType());
            entity.setUnreadMessageNum(timConversation.getUnreadMessageNum());

            // 获取资料
            if (timConversation.getType() == TIMConversationType.C2C) {
                userInfo.put(timConversation.getPeer(), entity);
            } else if (timConversation.getType() == TIMConversationType.Group) {
                groupInfo.put(timConversation.getPeer(), entity);
            }

            // 获取最后一条消息
            TIMMessage lastMsg = timConversation.getLastMsg();
            if (lastMsg != null) {
                // 封装消息信息
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setId(lastMsg.getMsgId());
                messageEntity.setTimestamp(lastMsg.timestamp());
                messageEntity.setElemList(TencentImUtils.getArrrElement(lastMsg));
                entity.setMessage(messageEntity);
            }
            resultData.add(entity);
        }

        // 初始化计数器
        final int maxIndex = (userInfo.size() != 0 ? 1 : 0) + (groupInfo.size() != 0 ? 1 : 0);

        // 当前计数器
        final int[] currentIndex = {0};

        // 获取群资料
        if (groupInfo.size() != 0) {
            TIMGroupManager.getInstance().getGroupInfo(Arrays.asList(groupInfo.keySet().toArray(new String[0])), new ValueCallBack<List<TIMGroupDetailInfoResult>>() {
                @Override
                public void onError(int code, String desc) {
                    Log.d(TencentImPlugin.TAG, "getGroupInfo failed, code: " + code + "|descr: " + desc);
                    callback.onError(code, desc);
                }

                @Override
                public void onSuccess(List<TIMGroupDetailInfoResult> timGroupDetailInfoResults) {
                    // 设置群资料
                    for (TIMGroupDetailInfoResult timGroupDetailInfoResult : timGroupDetailInfoResults) {
                        SessionEntity sessionEntity = groupInfo.get(timGroupDetailInfoResult.getGroupId());
                        if (sessionEntity != null) {
                            sessionEntity.setNickname(timGroupDetailInfoResult.getGroupName());
                            sessionEntity.setFaceUrl(timGroupDetailInfoResult.getFaceUrl());
                        }
                    }

                    // 回调成功
                    if (++currentIndex[0] >= maxIndex) {
                        callback.onSuccess(resultData);
                    }
                }
            });
        }

        // 获取用户资料
        if (userInfo.size() != 0) {
            TIMFriendshipManager.getInstance().getUsersProfile(Arrays.asList(userInfo.keySet().toArray(new String[0])), false, new TIMValueCallBack<List<TIMUserProfile>>() {
                @Override
                public void onError(int code, String desc) {
                    Log.d(TencentImPlugin.TAG, "getUsersProfile failed, code: " + code + "|descr: " + desc);
                    callback.onError(code, desc);
                }

                @Override
                public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                    // 设置用户资料
                    for (TIMUserProfile timUserProfile : timUserProfiles) {
                        SessionEntity sessionEntity = userInfo.get(timUserProfile.getIdentifier());
                        if (sessionEntity != null) {
                            sessionEntity.setNickname(timUserProfile.getNickName());
                            sessionEntity.setFaceUrl(timUserProfile.getFaceUrl());
                        }
                    }

                    // 回调成功
                    if (++currentIndex[0] >= maxIndex) {
                        callback.onSuccess(resultData);
                    }
                }
            });
        }
    }

    /**
     * 根据Message对象获得所有节点
     *
     * @param message 消息对象
     * @return 所有节点对象
     */
    public static List<TIMElem> getArrrElement(TIMMessage message) {
        List<TIMElem> elems = new ArrayList<>();
        for (int i = 0; i < message.getElementCount(); i++) {
            TIMElem elem = message.getElement(i);
            String rootPath = getSystemFilePath(TencentImPlugin.context);
            // 如果是语音，就下载保存
            if (elem.getType() == TIMElemType.Sound) {
                final TIMSoundElem soundElem = (TIMSoundElem) elem;
                final File file = new File(rootPath + "/" + soundElem.getUuid());
                if (!file.exists()) {
                    // 通知参数
                    final Map<String, Object> params = new HashMap<>();
                    params.put("type", TIMElemType.Video);
                    params.put("uuid", soundElem.getUuid());
                    TencentImPlugin.invokeListener(ListenerTypeEnum.DownloadStart, params);

                    // 下载
                    soundElem.getSoundToFile(file.getPath(), new TIMCallBack() {
                        @Override
                        public void onError(int code, String desc) {
                            Log.d(TencentImPlugin.TAG, "login failed. code: " + code + " errmsg: " + desc);
                            TencentImPlugin.invokeListener(ListenerTypeEnum.DownloadFail, params);
                        }

                        @Override
                        public void onSuccess() {
                            Log.d(TencentImPlugin.TAG, "download success,path:" + file.getPath());
                            TencentImPlugin.invokeListener(ListenerTypeEnum.DownloadSuccess, params);
                        }
                    });
                }
                soundElem.setPath(file.getPath());
                // 如果是视频，就保存缩略图
            } else if (elem.getType() == TIMElemType.Video) {
                final TIMVideoElem videoElem = (TIMVideoElem) elem;
                // 缩略图文件
                final File snapshotFile = new File(rootPath + "/" + videoElem.getSnapshotInfo().getUuid());
                if (!snapshotFile.exists()) {
                    // 通知参数
                    final Map<String, Object> params = new HashMap<>();
                    params.put("type", TIMElemType.Video);
                    params.put("uuid", videoElem.getSnapshotInfo().getUuid());
                    TencentImPlugin.invokeListener(ListenerTypeEnum.DownloadStart, params);


                    // 下载
                    videoElem.getSnapshotInfo().getImage(snapshotFile.getPath(), new TIMCallBack() {
                        @Override
                        public void onError(int code, String desc) {
                            Log.d(TencentImPlugin.TAG, "login failed. code: " + code + " errmsg: " + desc);
                            Map<String, Object> params = new HashMap<>();
                            params.put("type", TIMElemType.Video);
                            params.put("uuid", videoElem.getSnapshotInfo().getUuid());
                            TencentImPlugin.invokeListener(ListenerTypeEnum.DownloadFail, params);
                        }

                        @Override
                        public void onSuccess() {
                            Log.d(TencentImPlugin.TAG, "download success,path:" + snapshotFile.getPath());
                            TencentImPlugin.invokeListener(ListenerTypeEnum.DownloadSuccess, params);
                        }
                    });
                }
                videoElem.setSnapshotPath(snapshotFile.getPath());


                // 短视频文件
                final File videoFile = new File(rootPath + "/" + videoElem.getVideoInfo().getUuid());
                if (!videoFile.exists()) {
                    // 通知参数
                    final Map<String, Object> params = new HashMap<>();
                    params.put("type", TIMElemType.Video);
                    params.put("uuid", videoElem.getVideoInfo().getUuid());
                    TencentImPlugin.invokeListener(ListenerTypeEnum.DownloadStart, params);

                    // 下载
                    videoElem.getVideoInfo().getVideo(videoFile.getPath(), new TIMCallBack() {
                        @Override
                        public void onError(int code, String desc) {
                            Log.d(TencentImPlugin.TAG, "login failed. code: " + code + " errmsg: " + desc);
                            TencentImPlugin.invokeListener(ListenerTypeEnum.DownloadFail, params);
                        }

                        @Override
                        public void onSuccess() {
                            Log.d(TencentImPlugin.TAG, "download success,path:" + snapshotFile.getPath());
                            TencentImPlugin.invokeListener(ListenerTypeEnum.DownloadSuccess, params);
                        }
                    });
                }
                videoElem.setVideoPath(videoFile.getPath());
            }
            elems.add(elem);
        }
        return elems;
    }

    /**
     * 根据会话ID和会话类型获得会话对象
     *
     * @param sessionId      会话ID
     * @param sessionTypeStr 会话类型字符串模式
     * @return 会话对象
     */
    public static TIMConversation getSession(String sessionId, String sessionTypeStr) {
        TIMConversationType sessionType = null;
        for (TIMConversationType value : TIMConversationType.values()) {
            if (sessionTypeStr.equals(value.name())) {
                sessionType = value;
                break;
            }
        }
        // 验证sessionType
        if (sessionType == null) {
            Log.w(TencentImPlugin.TAG, "init: Cannot find parameter `sessionType` or `sessionType` is null!");
            throw new RuntimeException("Cannot find parameter `sessionType` or `sessionType` is null!");
        }

        // 获得会话信息
        TIMConversation conversation = TIMManager.getInstance().getConversation(sessionType, sessionId);
        if (conversation == null) {
            throw new RuntimeException("Cannot find Conversation" + sessionId + "-" + sessionTypeStr);
        }
        return conversation;
    }

    /**
     * 获得完整的消息对象
     *
     * @param timMessages 消息列表
     * @param callBack    完成回调
     */
    public static void getMessageInfo(List<TIMMessage> timMessages, final ValueCallBack<List<MessageEntity>> callBack) {
        // 需要被获取用户信息的数据集
        final Map<String, List<MessageEntity>> userInfo = new HashMap<>();
        for (TIMMessage timMessage : timMessages) {
            List<MessageEntity> list = userInfo.get(timMessage.getSender());
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(new MessageEntity(timMessage));
            userInfo.put(timMessage.getSender(), list);
        }

        // 获取用户资料
        TIMFriendshipManager.getInstance().getUsersProfile(Arrays.asList(userInfo.keySet().toArray(new String[0])), false, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int code, String desc) {
                callBack.onError(code, desc);
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                // 赋值用户信息并封装返回集合
                List<MessageEntity> messageEntities = new ArrayList<>();
                for (TIMUserProfile timUserProfile : timUserProfiles) {
                    List<MessageEntity> list = userInfo.get(timUserProfile.getIdentifier());
                    if (list != null) {
                        for (MessageEntity messageEntity : list) {
                            messageEntity.setUserInfo(timUserProfile);
                            messageEntities.add(messageEntity);
                        }
                    }
                }
                messageEntities.sort(new Comparator<MessageEntity>() {
                    @Override
                    public int compare(MessageEntity o1, MessageEntity o2) {
                        return o1.getTimestamp().compareTo(o2.getTimestamp());
                    }
                });
                callBack.onSuccess(messageEntities);
            }
        });
    }

    /**
     * 获得系统目录
     *
     * @param context 全局上下文
     * @return 获得结果
     */
    private static String getSystemFilePath(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        } else {
            cachePath = context.getFilesDir().getAbsolutePath();
        }
        return cachePath;
    }
}