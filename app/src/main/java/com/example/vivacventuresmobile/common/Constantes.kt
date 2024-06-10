package com.example.vivacventuresmobile.common

object Constantes {
    const val AUTHORIZATION = "Authorization"
    const val BEARER = "Bearer "
    const val DATA_STORE = "data_store"
    const val REFRESHTOKEN = "refresh_token"
    const val ACCESS_TOKEN = "access_token"
    const val REGISTER_PATH = "/vivacventures/auth/register"
    const val LOGIN_PATH = "/vivacventures/auth/login"
    const val REFRESH_PATH = "/vivacventures/auth/refreshToken"
    const val FORGOTPASSWORDPATH = "/vivacventures/auth/forgotPassword"
    const val RESETPASS = "/vivacventures/auth/resetPassword"
    const val TOKEN = "refreshToken"
    const val EMAIL = "email"
    const val PASSWORD = "password"
    const val NEWPASSWORD = "newPassword"
    const val TEMPORALPASSWORD = "temporalPassword"
    const val DEVELOPMENT = "development"

    const val USERNAME = "username"
    const val ID = "id"
    const val VIVACID = "vivacId"
    const val VIVACPLACEID = "vivacPlaceId"
    const val TYPE = "type"
    const val LATITUDE = "latitude"
    const val LONGITUDE = "longitude"
    const val CONTENT_TYPE = "Content-Type: application/json"

    const val FAVORITO = "/vivacventures/favorito"
    const val FAVORITOS = "/vivacventures/favoritos/{username}"
    const val DELETE_FAVORITO = "/vivacventures/favorito/delete"
    const val AMIGO = "/vivacventures/amigo"
    const val FRIENDS = "/vivacventures/friends"
    const val FRIENDS_SEND = "/vivacventures/friends/send"
    const val FRIENDS_ACCEPT = "/vivacventures/friends/accept"
    const val FRIENDS_REJECT = "/vivacventures/friends/reject/{id}"
    const val FRIENDS_DELETE = "/vivacventures/friends/delete/{id}"
    const val LISTS = "/vivacventures/lists"
    const val LIST = "/vivacventures/list/{id}"
    const val LIST_USER_VIVAC = "/vivacventures/list/userAndVivacPlace"
    const val LIST_SHARED = "/vivacventures/list/shared"
    const val LIST_SAVE = "/vivacventures/list"
    const val LIST_SHARE = "/vivacventures/list/share"
    const val LIST_DELETE_SHARED = "/vivacventures/list/delete/shared"
    const val LIST_DELETE = "/vivacventures/list/delete"
    const val LIST_FAVORITE = "/vivacventures/list/favorite"
    const val LIST_FAVORITE_DELETE = "/vivacventures/list/favorite/delete"
    const val REPORT = "/vivacventures/report"
    const val VALORATION = "/vivacventures/valoration"
    const val VALORATION_ID = "/vivacventures/valoration/{id}"
    const val VIVACPLACES = "/vivacventures/vivacplaces"
    const val VIVACPLACES_ID = "/vivacventures/vivacplaces/id/{id}"
    const val VIVACPLACES_USER = "/vivacventures/vivacplaces/user/{username}"
    const val VIVACPLACESMY = "/vivacventures/vivacplacesmy"
    const val VIVACPLACES_TYPE = "/vivacventures/vivacplaces/type/{type}"
    const val VIVACPLACE = "/vivacventures/vivacplace"
    const val NEARBY = "/vivacventures/nearby"
    const val DELETE = "/vivacventures/delete/{id}"
}
