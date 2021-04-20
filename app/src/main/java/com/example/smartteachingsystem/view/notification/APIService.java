package com.example.smartteachingsystem.view.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAIMc9UkE:APA91bEHgdkMgWBHe9LSp3aR5kS2kzAoFPVHL9BcyC95oXA02coQbSdvrI9WByrDU6Bhgvqx9gJjDkfUCsxngwFBzdgh23DoTjaUhXKNJi1kW861F6bBi2dHIXnwGnYK8N3tLzSLYJ8u"
                    // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);
}
