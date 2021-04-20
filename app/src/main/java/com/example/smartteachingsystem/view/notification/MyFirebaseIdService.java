package com.example.smartteachingsystem.view.notification;

import com.example.smartteachingsystem.view.repository.FirebaseDataRepository;
import com.example.smartteachingsystem.view.utils.Nodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseIdService extends FirebaseMessagingService {
   private final FirebaseFirestore fireStore= FirebaseFirestore.getInstance();


    @Override
    public void onNewToken(String s)
    {
        super.onNewToken(s);
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
       // String refreshToken= FirebaseInstanceId.getInstance().getToken();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        if(firebaseUser!=null){
            updateToken(refreshToken);
        }
    }
    private void updateToken(String refreshToken){


        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Map<String,String> tokenMap= new HashMap<>();
        tokenMap.put("token",refreshToken);
        fireStore.collection(Nodes.TOKENS).document(firebaseUser.getUid()).set(tokenMap);
    }

}
