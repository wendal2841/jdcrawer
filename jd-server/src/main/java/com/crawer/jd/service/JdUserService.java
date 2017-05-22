package com.crawer.jd.service;

import com.crawer.jd.JdUser;
import com.crawer.jd.UserLoginCallBack;
import com.crawer.jd.domain.items.JdItem;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Author     : zh_zhou@Ctrip.com
 * Copyright  : Ctrip Copyright (c) 2017
 * Company    : Ctrip
 * Create at  : 2017/5/21 12:34
 * Description:
 */
@Service
public class JdUserService {

    Map<String, JdUser> userMap = new HashMap<>();

    public JdUser getNewUser() {
        JdUser user = new JdUser();
        userMap.put(user.getIdentity(), user);
        return user;
    }

    public JdUser getByKey(String key) {
        return userMap.get(key);
    }

    public JdUser loginByQrCode(String key) throws IOException {
        final JdUser user = getByKey(key);
        user.loadLogInQrCode();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    user.waitToScanQrCode(new UserLoginCallBack() {
                        @Override
                        public void logInResult(boolean success) {
                            if (!success) {
                                userMap.remove(user.getIdentity());
                                System.out.println("登录失败");
                            } else {
                                System.out.println("登录成功");
                            }
                        }
                    });
                } catch (IOException e) {
                    userMap.remove(user.getIdentity());
                }
            }
        }).start();
        return user;
    }


    public JdItem buy(JdItem item) throws IOException {
//        if(!JdItemService.canBuyWithMiaoShaPrice(item)){
//            return null;
//        }
        for (Map.Entry<String, JdUser> entry : userMap.entrySet()) {
            JdUser user=entry.getValue();
            if(user.isLoginSuccess()){
                user.buy(item);
            }
        }
        return item;
    }
}