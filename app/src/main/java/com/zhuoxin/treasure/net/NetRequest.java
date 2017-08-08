package com.zhuoxin.treasure.net;


import com.zhuoxin.treasure.User;
import com.zhuoxin.treasure.login.UserResult;
import com.zhuoxin.treasure.register.RegistResult;
import com.zhuoxin.treasure.treasure.Area;
import com.zhuoxin.treasure.treasure.Treasure;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by MACHENIKE on 2017/8/2.
 */

public interface NetRequest {

    @POST("/Handler/UserHandler.ashx?action=login")
    Call<UserResult> login(@Body User user);

    @POST("/Handler/UserHandler.ashx?action=register")
    Call<RegistResult> register(@Body User user);

    @POST("/Handler/\n" +
            "TreasureHandler.ashx?action=show")
    Call<List<Treasure>> getTreasure(@Body Area area);
}
