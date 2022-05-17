package nl.frankkie.dmvideos2.network

import com.google.gson.annotations.SerializedName

class DmUserInfo {
    @SerializedName("error")
    var error: DmUserInfoError? = null
    @SerializedName("id")
    var id: String? = null
    @SerializedName("screenname")
    var screenname: String? = null
}

class DmUserInfoError {
    var code: Int? = null
}

/*
//Example JSON
//https://api.dailymotion.com/user/PioRuN_PL
{
id: "x1gzy6k",
screenname: "PioRuN_PL"
}
 */