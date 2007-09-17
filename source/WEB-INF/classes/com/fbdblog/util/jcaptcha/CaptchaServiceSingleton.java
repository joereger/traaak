package com.fbdblog.util.jcaptcha;


import com.octo.captcha.service.image.ImageCaptchaService;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;

public class CaptchaServiceSingleton {

    private static ImageCaptchaService instance = new DefaultManageableImageCaptchaService();

    public static ImageCaptchaService getInstance(){
        return instance;
    }
}


//import com.octo.captcha.service.image.ImageCaptchaService;
//import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
//
///**
// * jcaptcha service singleton
// */
//
//public class CaptchaServiceSingleton {
//
//    private static DefaultManageableImageCaptchaService instance = new DefaultManageableImageCaptchaService();
//    private static boolean configured = false;
//
//    public static ImageCaptchaService getInstance(){
//        if (!configured){
//            configure();
//        }
//        return instance;
//    }
//
//    private static void configure(){
//        instance.setMinGuarantedStorageDelayInSeconds(1000);
//    }
//}
