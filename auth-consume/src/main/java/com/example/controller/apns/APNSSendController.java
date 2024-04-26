package com.example.controller.apns;


import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.auth.ApnsSigningKey;
import com.eatthepath.pushy.apns.util.ApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.eatthepath.pushy.apns.util.TokenUtil;
import com.eatthepath.pushy.apns.util.concurrent.PushNotificationFuture;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("apns")
public class APNSSendController {



    @PostMapping("/postApns")
    private boolean testFeign() throws IOException, NoSuchAlgorithmException, InvalidKeyException {

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("AuthKeyPush.p8").getFile());
        SimpleApnsPushNotification pushNotification;
        ApnsClient apnsClient = new ApnsClientBuilder()
                .setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
                .setSigningKey(ApnsSigningKey.loadFromPkcs8File(file,
//                .setSigningKey(ApnsSigningKey.loadFromPkcs8File(new File("classpath:AuthKeyPush.p8"),
                        "59HU657SU2", "5PV758Y2HY"))
                .build();

        final ApnsPayloadBuilder payloadBuilder = new SimpleApnsPayloadBuilder();
        payloadBuilder.setAlertBody("Example!");

        final String payload = payloadBuilder.build();
        final String token = TokenUtil.sanitizeTokenString("ac54f7fe850136252e3ad240c539683a48ff021638cff9774184594d86e4758f");

        pushNotification = new SimpleApnsPushNotification(token, "com.eastsoft.appPlatform.heat", payload);

        PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>>
                sendNotificationFuture = apnsClient.sendNotification(pushNotification);

        try {
           PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse =
                    sendNotificationFuture.get();

            if (pushNotificationResponse.isAccepted()) {
                System.out.println("Push notification accepted by APNs gateway.");
            } else {
                System.out.println("Notification rejected by the APNs gateway: " +
                        pushNotificationResponse.getRejectionReason());

                pushNotificationResponse.getTokenInvalidationTimestamp().ifPresent(timestamp -> {
                    System.out.println("\t…and the token is invalid as of " + timestamp);
                });
            }
        } catch (final ExecutionException e) {
            System.err.println("Failed to send push notification.");
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
