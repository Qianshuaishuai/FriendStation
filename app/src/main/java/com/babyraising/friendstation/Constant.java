package com.babyraising.friendstation;

public class Constant {
    public static final Boolean DEBUG = true;

    public static final String WX_APPID = "wxfeda27e75bee03f3";

    public static final int GIFT_CHAT_CODE = 9997;
    public static final int RESULT_CHAT_ROOM_CODE = 9998;
    public static final int INVITE_CHAT_ROOM_CODE = 9999;

    public static final int CODE_VOICE_TIP_REQUEST = 11111;

    public static final int OFFICIAL_INTO_CHAT_CODE = 999;
    public static final int REQUEST_PERMISSION_CODE = 101;
    public static final int REQUEST_GIFT_CODE = 666;

    public static final int TIM_SDK_APPID = 1400457648;
    public static final int RTC_SDK_APPID = 1400462920;
    public static final int TIM_RTC_CLOUD_ROOM_PREFIX = 20200000;
    public static final String TIM_SDK_PREFIX = "FriendStation";

    public static final int ACTIVITY_COMMON_REQUEST = 10001;

    public static final String BASE_URL = "http://8.129.108.2/api/";

    //user部分
    public static final String URL_GET_CODE = "ums/auth/verifyCode";
    public static final String URL_IS_FIRSTLOGIN = "ums/auth/checkMobileExist";
    public static final String URL_LOGINOUT = "ums/auth/loginOut";
    public static final String URL_LOGINBYPASSWORD = "ums/auth/loginByPassword";
    public static final String URL_LOGINBYMOBILE = "ums/auth/loginByMobile";

    public static final String URL_UMS_USER = "ums/user";
    public static final String URL_UMS_USER_ALBUM_PAGE = "ums/user/album/page";
    public static final String URL_UMS_USER_ALBUM_PAGEBYID = "ums/user/album/pageById";
    public static final String URL_UMS_USER_FULL = "ums/user/full";
    public static final String URL_UMS_GET_FULL_BYID = "ums/user/getFullById";
    public static final String URL_UMS_USER_SAVE_ALBUM = "ums/user/save/album";
    public static final String URL_UMS_USER_DELETE_ALBUM = "ums/user/delete/album";
    public static final String URL_UMS_UPDATE = "ums/user/update";
    public static final String URL_UMS_USER_UPDATE_PASSWORD = "ums/user/update/password";
    public static final String URL_UMS_USER_UPDATE_VERIFY = "ums/user/update/verify";
    public static final String URL_USER_GET_USERINTIMACYLIST = "ums/user/get/userIntimacyList";
    public static final String URL_USER_GET_USERRICHLIST = "ums/user/get/userRichList";
    public static final String URL_USER_UPDATE_VERIFY = "ums/user/update/verify";
    public static final String URL_UMS_USER_GET_USERMAINPAGELIST = "ums/user/get/userMainPageList";
    public static final String URL_UMS_USER_USER_USERMAINPAGELIST = "ums/user/user/userMainPageList";
    public static final String URL_UMS_USER_USER_USERMESSAGELIST = "ums/user/user/userMessageList";
    public static final String URL_UMS_USER_USER_USERRECOMMENDLIST = "ums/user/user/userRecommendList";
    public static final String URL_UMS_USER_UPDATE_RECORDSIGN = "ums/user/update/recordSign";
    public static final String URL_USER_UPDATE_COORDINATE = "ums/user/update/coordinate";

    //friend部分
    public static final String URL_FRIENDS_MOMENT = "friends/moments/getMomentsInfo";
    public static final String URL_FRIENDS_MOMENT_SAVE = "friends/moments/save";
    public static final String URL_FRIENDS_MOMENTLIKE_SAVE = "friends/momentLike/save";
    public static final String URL_FRIENDS_MOMENTLIKE_DELETE = "friends/momentLike/delete/";
    public static final String URL_FRIENDS = "friends";
    public static final String URL_FRIENDS_GIFT = "friends/gift";
    public static final String URL_FRIENDS_TASK = "friends/task";
    public static final String URL_FRIENDS_GIFT_ORDER = "friends/giftOrder";
    public static final String URL_FRIENDS_GIFT_ORDER_SAVE = "friends/giftOrder/save";
    public static final String URL_FRIENDS_GIFT_PRESENT = "friends/giftPresent";
    public static final String URL_FRIENDS_GIFT_PRESENT_SAVE = "friends/giftPresent/save";
    public static final String URL_FRIENDS_SCORE = "friends/score";
    public static final String URL_FRIENDS_SCORE_ORDER = "friends/scoreOrder";
    public static final String URL_FRIENDS_COIN = "friends/coin";
    public static final String URL_FRIENDS_COIN_ORDER = "friends/coinOrder";
    public static final String URL_FRIENDS_COIN_RECORD = "friends/coinRecord";
    public static final String URL_FRIENDS_COIN_RECORD_SAVE = "friends/coinRecord/save";
    public static final String URL_FRIENDS_UPLOAD = "friends/upload";
    public static final String URL_FRIENDS_CONFIGAVATAR = "friends/configAvatar";
    public static final String URL_FRIENDS_USERFOLLOW_DELETE = "friends/userFollow/delete";
    public static final String URL_FRIENDS_USERFOLLOW_SAVE = "friends/userFollow/save";
    public static final String URL_FRIENDS_NOTICE = "friends/notice";
    public static final String URL_FRIENDS_USERVIEW_SAVE = "friends/userView/save";
    public static final String URL_FRIENDS_USERVIEW = "friends/userView";


    //腾讯云im相关
    public static final String URL_GET_IIM_OFFINE = "https://console.tim.qq.com/v4/openim/querystate";
    public static final String TIM_ADMIN_ACCOUNT = "administrator";


}
