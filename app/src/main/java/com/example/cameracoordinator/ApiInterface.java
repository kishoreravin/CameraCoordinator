package com.example.cameracoordinator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {
    @Headers({"Authorization: key=AAAAxtqQRtc:APA91bG77vKYy-e15slE2x6yMHZIKkml8XMLnC3ecHmHQlOTUKRPS4CzMVg8m7gFsLJmK4yDUzgFKS2a8TA2hkfyydT5o1ob9g_OYMKMdOGfCt1DBwgVirwkyaHRlThCEs3_xO_YoGno",
            "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendChatNotification(@Body RequestNotification requestNotificaton);
}