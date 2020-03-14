# tencent_im_plugin
[![pub package](https://img.shields.io/pub/v/tencent_im_plugin.svg)](https://pub.dartlang.org/packages/tencent_im_plugin)

腾讯云IM插件

## Getting Started
集成腾讯云IM SDK，同时支持 Android 和 IOS  
**注意：`v0.1.x` 为测试版本，如果您要集成到正式项目，请保持关注新版本。稳定版本将大于等于 `v1.0.0`**

### v0.2.0 改版说明
v0.2.0 主要更改了发送消息和消息节点，使用上更符合逻辑。  
#### 更新步骤
1. 将发送消息统一更改为 sendMessage ,使用不同消息节点即可发送不同消息
2. MessageEntity 增加 note 属性，对非自定消息可直接用于展示

### 近期计划(已完成内容将会被移除)
[ ]【重大改变】整合发送消息为一个方法。**优势:使用更简单，学习成本更低。劣势：影响之前的代码**   
[ ]【BUG】打包APK后，RefreshConversation 中的会话信息没有id和type  
[ ]TIMLocationElem 节点  
[ ]IOS端语音、视频的下载功能  
[ ]视频上传回调功能


## 功能清单
[x]初始化  
[x]登录相关  
[x]消息收发    
[x]未读计数  
[x]群组相关  
[x]用户资料与关系链  
[ ]离线推送  

## 集成
### Flutter
```
tencent_im_plugin: ^[最新版本号]
```

### Android 端集成
无需额外配置，已内部打入混淆配置

### IOS
无需额外配置

## 使用
Demo截图:  
<img src="https://raw.githubusercontent.com/JiangJuHong/access-images/master/FlutterTencentImPlugin/1.png" height="300em" style="max-width:100%;display: inline-block;"/>
<img src="https://raw.githubusercontent.com/JiangJuHong/access-images/master/FlutterTencentImPlugin/2.png" height="300em" style="max-width:100%;display: inline-block;"/>
<img src="https://raw.githubusercontent.com/JiangJuHong/access-images/master/FlutterTencentImPlugin/3.png" height="300em" style="max-width:100%;display: inline-block;"/>
<img src="https://raw.githubusercontent.com/JiangJuHong/access-images/master/FlutterTencentImPlugin/4.png" height="300em" style="max-width:100%;display: inline-block;"/>
<img src="https://raw.githubusercontent.com/JiangJuHong/access-images/master/FlutterTencentImPlugin/5.png" height="300em" style="max-width:100%;display: inline-block;"/>
<img src="https://raw.githubusercontent.com/JiangJuHong/access-images/master/FlutterTencentImPlugin/6.png" height="300em" style="max-width:100%;display: inline-block;"/>
<img src="https://raw.githubusercontent.com/JiangJuHong/access-images/master/FlutterTencentImPlugin/7.png" height="300em" style="max-width:100%;display: inline-block;"/>
<img src="https://raw.githubusercontent.com/JiangJuHong/access-images/master/FlutterTencentImPlugin/8.png" height="300em" style="max-width:100%;display: inline-block;"/>

## 功能清单
### 消息发送节点
|  节点  |  进度  |  说明  |
|  ----  | ---- |  ---- |
|  TextMessageNode  |  √ |  文本消息 |
|  ImageMessageNode  |  √ |  图片消息 |
|  SoundMessageNode  |  √ |  语音消息 |
|  VideoMessageNode  |  √ |  视频消息 |
|  CustomMessageNode  |  √ | 自定义消息 |

### 消息接收节点
|  节点  |  进度  |  说明  |
|  ----  | ---- |  ---- |
|  TIMCustomElem  |  √ |  已完成 |
|  TIMFaceElem  |  - |  暂不考虑 |
|  TIMFileElem  |  - |  暂不考虑，建议使用 TIMCustomElem 代替 |
|  TIMGroupSystemElem  |  - |  暂不考虑 |
|  TIMGroupTipsElem  | √ |  已完成 |
|  TIMImageElem  |  √ |  已完成 |
|  TIMLocationElem  |  - |  计划内 |
|  TIMProfileSystemElem  |  - |  暂不考虑 |
|  TIMSNSSystemElem  |  - |  暂不考虑 |
|  TIMSoundElem  |  ⊙ |  有缺陷，但不影响使用(IOS端未实现下载功能) |
|  TIMTextElem  |  √ |  已完成 |
|  TIMVideoElem  |  ⊙ |  有缺陷，但不影响使用(IOS端未实现下载功能) |

### 接口
|  接口   | 说明  | 参数  | Android | IOS |
|  ----  | ----  | ----  | ----  | ----  |
| init  | 初始化 | {appid:"xxxxxx"} | √ | √
| login  | 登录 | {identifier:'用户ID',userSig:'用户签名'} | √ | √
| logout  | 登出 | - | √ | √
| getLoginUser  | 获得当前登录用户ID | - | √ | √
| initStorage  | 初始化本地存储 | {identifier: '用户ID'} | √ | √
| getConversationList  | 获得会话列表 | - | √ | √ 
| getConversation  | 获得单个会话 | {id:'会话ID',sessionType:'会话类型} | √ | √ 
| getGroupInfo  | 获得群信息(云端) | {id:'群ID'} | √ | √
| getUserInfo  | 获得用户信息 | {id:'用户ID',forceUpdate:"是否从云端拉取数据，默认为false"} | √ | √
| setRead  | 设置已读 | {sessionId:'会话ID',sessionType:'会话类型，枚举值:SessionType' } | √ | √
| getMessages  | 获得消息列表 | {sessionId:'会话ID',sessionType:'会话类型，枚举值:SessionType',number:"会话数量"} | √ | √
| getLocalMessages  | 获得本地消息列表 | {sessionId:'会话ID',sessionType:'会话类型，枚举值:SessionType',number:"会话数量"} | √ | √
| sendMessage  | 发送消息 | {sessionId:'会话ID',sessionType:'会话类型，枚举值:SessionType',ol:"是否是在线消息（无痕）",node:消息节点对象} | √ | √
| getFriendList  | 获得好友列表 | - | √ | √
| getGroupList  | 获得群组列表 | - | √ | √
| addFriend  | 添加好友 | {id:'用户ID',addType:'添加类型',remark:'备注',addWording:'请求说明',addSource:'添加来源',friendGroup:'分组名'} | √ | √
| checkSingleFriends  | 检测单个好友关系 | {id:'用户ID',type:'检测类型'} | √ | √
| getPendencyList  | 获得未决好友列表(申请中) | {type:'类型',seq:'未决列表序列号',timestamp:'翻页时间戳',numPerPage:'每页数量'} | √ | √
| pendencyReport  | 未决已读上报 | {timestamp:"时间戳"} | √ | √
| deletePendency  | 未决删除 | {id:'用户Id',type:'类型'}| √ | √
| examinePendency  | 未决审核 | {id:'用户ID',type:'类型',remark:'备注'} | √ | √ 
| deleteConversation  | 删除会话 | {sessionId:'会话ID',sessionType:'会话类型，枚举值:SessionType'} | √ | √
| deleteLocalMessage  | 删除会话内的本地聊天记录 | {sessionId:'会话ID',sessionType:'会话类型，枚举值:SessionType'} | √ | √
| createGroup  | 创建群组 | {groupId:'指定群ID',notification:'群公告',introduction:'描述',faceUrl:'头像',addOption:'入群类型',maxMemberNum:'最大成员数量',members:'成员集合',type:'类型',name:'群名'} | √ | √
| inviteGroupMember  | 邀请加入群组 | {groupId:'群ID',ids:'群成员ID'} | √ | √
| applyJoinGroup  | 申请加入群组 | {groupId:'群ID',reason:'申请说明'} | √ | √
| quitGroup  | 退出群组 | {groupId:'群ID'} | √ | √
| deleteGroupMember  | 删除群组成员 | {groupId:'群ID',ids:'用户ID集合',reason:"删除说明"} | √ | √
| getGroupMembers  | 获得群成员列表 | {groupId:'群ID'} | √ | √
| deleteGroup  | 解散群组 | {groupId:'群ID'} | √ | √
| modifyGroupOwner  | 转让群组 | {groupId:'群ID',identifier:'新群主ID'} | √ | √
| modifyGroupInfo  | 修改群组资料 | {groupId:'指定群ID',notification:'群公告',introduction:'描述',faceUrl:'头像',addOption:'入群类型',maxMemberNum:'最大成员数量(IOS不支持)',type:'类型',groupName:'群名',visable:'是否对外可见',silenceAll:'全员禁言',customInfo:'自定义信息'} | √ | √
| modifyMemberInfo  | 修改群成员资料 | {groupId:'群ID',identifier:'群成员ID',nameCard:'名片',receiveMessageOpt:'接收消息选项，注:IOS不支持',silence:'禁言时间',role:'角色',customInfo:'自定义数据'} | √ | √
| getGroupPendencyList  | 获得未决群列表 | {timestamp:'翻页时间戳',numPerPage:'每页的数量'} | √ | √
| reportGroupPendency  | 上报群未决已读 | {timestamp:'已读时间戳'} | √ | √
| groupPendencyAccept  | 群未决审核（同意）会遍历所有未决列表来获得未审核的列表，存在性能隐患 | {msg:'审核意见',groupId:'群ID',identifier:'申请人ID',addTime:'申请时间'} | √ | √
| groupPendencyRefuse  | 群未决审核（拒绝）会遍历所有未决列表来获得未审核的列表，存在性能隐患 | {msg:'审核意见',groupId:'群ID',identifier:'申请人ID',addTime:'申请时间'} | √ | √
| getSelfProfile  | 获取登录用户资料 | {forceUpdate:"是否强制走后台拉取"} | √ | √
| modifySelfProfile  | 修改登录用户资料 | {params:'修改参数'} | √ | √
| modifyFriend  | 修改好友资料 | {identifier:'好友ID',params:'修改参数'} | √ | √
| deleteFriends  | 删除好友 | {ids:"用户ID列表",delFriendType:'删除类型'} | √ | √
| addBlackList  | 添加到黑名单 | {ids:"用户ID列表"} | √ | √
| deleteBlackList  | 从黑名单删除 | {ids:"用户ID列表"} | √ | √
| getBlackList  | 获得黑名单列表 | - | √ | √
| createFriendGroup  | 创建好友分组 | {groupNames:'组名列表',ids:'用户列表'} | √ | √
| deleteFriendGroup  | 删除好友分组 | {groupNames:'组名列表'} | √ | √
| addFriendsToFriendGroup  | 添加好友到某个分组 | {groupName:'组名',ids:'ID列表'} | √ | √
| deleteFriendsFromFriendGroup  | 从分组删除好友 | {groupName:'组名',ids:'ID列表'} | √ | √
| renameFriendGroup  | 重命名分组 | {oldGroupName:'旧名称',newGroupName:'新名称'} | √ | √
| getFriendGroups  | 获得好友分组 | {groupNames:'组名'} | √ | √
| revokeMessage | 撤回一条发送成功的消息 | {sessionId:'会话ID',sessionType:'会话类型',rand:'消息随机码',seq:'消息序列号',timestamp:'消息时间戳'} | √ | √
| removeMessage | 删除一条消息(本地) | {sessionId:'会话ID',sessionType:'会话类型',rand:'消息随机码',seq:'消息序列号',timestamp:'消息时间戳',self:'是否是自己发送的'} | √ | √
| setMessageCustomInt | 设置自定义整数 | {sessionId:'会话ID',sessionType:'会话类型',rand:'消息随机码',seq:'消息序列号',timestamp:'消息时间戳',self:'是否是自己发送的',value:'自定义值'} | √ | √
| setMessageCustomStr | 设置自定义整数 | {sessionId:'会话ID',sessionType:'会话类型',rand:'消息随机码',seq:'消息序列号',timestamp:'消息时间戳',self:'是否是自己发送的',value:'自定义值'} | √ | √