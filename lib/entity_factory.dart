import 'package:tencent_im_plugin/entity/add_friend_result_entity.dart';
import 'package:tencent_im_plugin/entity/node_video_entity.dart';
import 'package:tencent_im_plugin/entity/group_info_entity.dart';
import 'package:tencent_im_plugin/entity/node_sound_entity.dart';
import 'package:tencent_im_plugin/entity/session_entity.dart';
import 'package:tencent_im_plugin/entity/check_friend_result_entity.dart';
import 'package:tencent_im_plugin/entity/node_entity.dart';
import 'package:tencent_im_plugin/entity/node_text_entity.dart';
import 'package:tencent_im_plugin/entity/message_entity.dart';
import 'package:tencent_im_plugin/entity/user_info_entity.dart';
import 'package:tencent_im_plugin/entity/friend_entity.dart';
import 'package:tencent_im_plugin/entity/node_image_entity.dart';

class EntityFactory {
  static T generateOBJ<T>(json) {
    if (1 == 0) {
      return null;
    } else if (T.toString() == "AddFriendResultEntity") {
      return AddFriendResultEntity.fromJson(json) as T;
    } else if (T.toString() == "NodeVideoEntity") {
      return NodeVideoEntity.fromJson(json) as T;
    } else if (T.toString() == "GroupInfoEntity") {
      return GroupInfoEntity.fromJson(json) as T;
    } else if (T.toString() == "NodeSoundEntity") {
      return NodeSoundEntity.fromJson(json) as T;
    } else if (T.toString() == "SessionEntity") {
      return SessionEntity.fromJson(json) as T;
    } else if (T.toString() == "CheckFriendResultEntity") {
      return CheckFriendResultEntity.fromJson(json) as T;
    } else if (T.toString() == "NodeEntity") {
      return NodeEntity.fromJson(json) as T;
    } else if (T.toString() == "NodeTextEntity") {
      return NodeTextEntity.fromJson(json) as T;
    } else if (T.toString() == "MessageEntity") {
      return MessageEntity.fromJson(json) as T;
    } else if (T.toString() == "UserInfoEntity") {
      return UserInfoEntity.fromJson(json) as T;
    } else if (T.toString() == "FriendEntity") {
      return FriendEntity.fromJson(json) as T;
    } else if (T.toString() == "NodeImageEntity") {
      return NodeImageEntity.fromJson(json) as T;
    } else {
      return null;
    }
  }
}