package com.babyraising.friendstation;

public class Constant {
    public static final Boolean DEBUG = true;

    public static final int OFFICIAL_INTO_CHAT_CODE = 999;
    public static final int REQUEST_PERMISSION_CODE = 101;

    public static final String BASE_URL = "http://8.129.108.2/api/";

    //user部分
    public static final String URL_GET_CODE = "ums/auth/verifyCode";
    public static final String URL_IS_FIRSTLOGIN = "ums/auth/checkMobileExist";
    public static final String URL_LOGINOUT = "ums/auth/loginOut";
    public static final String URL_LOGINBYPASSWORD = "ums/auth/loginByPassword";
    public static final String URL_LOGINBYMOBILE = "ums/auth/loginByMobile";

    public static final String URL_UMS_USER = "ums/user";
    public static final String URL_UMS_USER_ALBUM_PAGE = "ums/user/album/page";
    public static final String URL_UMS_USER_DELETE_ALBUM = "ums/user/album";
    public static final String URL_UMS_USER_FULL = "ums/user/full";
    public static final String URL_UMS_USER_SAVE_ALBUM = "ums/user/save/album";
    public static final String URL_UMS_UPDATE = "ums/user/update";
    public static final String URL_UMS_USER_UPDATE_PASSWORD = "ums/user/update/password";
    public static final String URL_UMS_USER_UPDATE_VERIFY = "ums/user/update/verify";

    //friend部分
    public static final String URL_FRIENDS_MOMENT = "friends/moments";
    public static final String URL_FRIENDS_MOMENT_SAVE = "friends/moments/save";
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
    public static final String URL_FRIENDS_UPLOAD = "friends/upload";
}
