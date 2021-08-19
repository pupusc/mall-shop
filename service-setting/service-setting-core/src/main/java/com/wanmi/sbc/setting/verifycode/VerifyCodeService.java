package com.wanmi.sbc.setting.verifycode;

import com.wanmi.sbc.setting.bean.enums.VerifyType;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.RandomStringUtils;
import org.patchca.background.SingleColorBackgroundFactory;
import org.patchca.color.SingleColorFactory;
import org.patchca.filter.predefined.CurvesRippleFilterFactory;
import org.patchca.font.RandomFontFactory;
import org.patchca.service.AbstractCaptchaService;
import org.patchca.text.renderer.BestFitTextRenderer;
import org.patchca.utils.encoder.EncoderHelper;
import org.patchca.word.RandomWordFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 验证码Service类
 */
@Service
public class VerifyCodeService {

    private static final String PASS_FLAG = "@PASS@";

    private static final String CAPTCHA = "CAPTCHA";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 生成图表验证码
     *
     * @param uuid 唯一key
     * @param os   输出流，一般来时response.getOutputStream()
     * @return 验证码字符串
     * @throws IOException {@link IOException}
     */
    public String generateCaptcha(String uuid, OutputStream os) throws IOException {
        String captcha = EncoderHelper.getChallangeAndWriteImage(new MyCaptchaService(), "png", os);
        // key like: CAPTCHA_{uuid}
        stringRedisTemplate.opsForValue().set(String.format("%s_%s", CAPTCHA, uuid), captcha, 10, TimeUnit.MINUTES);
        return captcha;
    }

    /**
     * 验证图片验证码
     *
     * @param uuid         唯一Key
     * @param enterCaptcha 输入的验证码
     * @param certTimeout  校验通过后证书有效时长
     * @param certTimeUnit 校验通过后证书有效时长时间单位
     * @return 通过/不通过
     */
    public boolean validateCaptcha(String uuid, String enterCaptcha, long certTimeout, TimeUnit certTimeUnit) {
        // key like: CAPTCHA_{uuid}
        return Optional.ofNullable(stringRedisTemplate.opsForValue().get(String.format("%s_%s", CAPTCHA, uuid))).map(captcha -> {
            if (captcha.equalsIgnoreCase(enterCaptcha)) {
                // key like: CAPTCHA_@PASS@_{uuid}
                stringRedisTemplate.opsForValue().set(String.format("%s_%s_%s", CAPTCHA, PASS_FLAG, uuid), "true", certTimeout, certTimeUnit);
                return true;
            } else {
                return false;
            }

        }).orElse(false);
    }

    /**
     * 验证是否拥有图片验证证书
     *
     * @param uuid 唯一Key
     * @return 通过/不通过
     */
    public boolean validateCaptchaCertificate(String uuid) {
        // key like: CAPTCHA_@PASS@_{uuid}
        return Optional.ofNullable(stringRedisTemplate.opsForValue().get(String.format("%s_%s_%s", CAPTCHA, PASS_FLAG, uuid))).isPresent();
    }

    /**
     * 删除手机找回密码凭证
     *
     * @param uuid 唯一Key
     */
    public void deleteCaptchaCertificate(String uuid) {
        // key like: CAPTCHA_@PASS@_{uuid}
        stringRedisTemplate.delete(String.format("%s_%s_%s", CAPTCHA, PASS_FLAG, uuid));
    }

    /**
     * 生成短信验证码
     *
     * @param phone      手机号码
     * @param verifyType 验证类型
     * @param timeout    有效时长
     * @param timeUnit   时间单位
     * @return 验证码
     */
    public String generateSmsVerifyCode(String phone, VerifyType verifyType, long timeout, TimeUnit timeUnit) {
        // step.1 生成6位数字验证码
        String verifyCode = String.format("%s", RandomStringUtils.randomNumeric(6));

        // step.2 放入缓存; key like: CUSTOMER_REGISTRY_13812345678
        stringRedisTemplate.opsForValue().set(String.format("%s_%s", verifyType, phone), verifyCode, timeout, timeUnit);

        return verifyCode;
    }


    /**
     * 生成
     *
     * @param phone      phone
     * @param verifyType verifyType
     * @param timeout    timeout
     * @param timeUnit   timeUnit
     */
    public void generateSmsCertificate(String phone, VerifyType verifyType, long timeout, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(String.format("%s_%s", verifyType, phone), Boolean.TRUE.toString(), timeout, timeUnit);
    }


    /**
     * 电话是否可以发短信
     *
     * @param phone      phone
     * @param verifyType verifyType
     * @return Boolean
     */
    public Boolean validSmsCertificate(String phone, VerifyType verifyType) {
        return Objects.isNull(stringRedisTemplate.opsForValue().get(String.format("%s_%s", verifyType, phone)));
    }

    /**
     * 检验短信验证码
     *
     * @param phone        手机号码
     * @param verifyCode   验证码
     * @param verifyType   验证类型
     * @param certTimeout  校验通过后证书有效时长
     * @param certTimeUnit 校验通过后证书有效时长时间单位
     * @return 通过/不通过
     */
    public boolean validateSmsVerifyCode(String phone, String verifyCode, VerifyType verifyType, long certTimeout, TimeUnit certTimeUnit) {
        return Optional.ofNullable(stringRedisTemplate.opsForValue().get(String.format("%s_%s", verifyType, phone))).map(cacheCode -> {
            if (cacheCode.equals(verifyCode)) {
                // 验证通过后，设置一个认证通过的凭证放置；凭借此凭证可以有效期内可以进行后续操作；key like: CUSTOMER_REGISTRY_@PASS@_1381234567
                stringRedisTemplate.opsForValue().set(String.format("%s_%s_%s", verifyType, PASS_FLAG, phone), "true", certTimeout, certTimeUnit);
                return true;
            } else {
                return false;
            }
        }).orElse(false);
    }


    /**
     * 检验短信验证码
     *
     * @param phone      手机号码
     * @param verifyCode 验证码
     * @param verifyType 验证类型
     * @return 通过/不通过
     */
    public boolean validateSmsVerifyCodeAgain(String phone, String verifyCode, VerifyType verifyType) {
        return Optional.ofNullable(stringRedisTemplate.opsForValue().get(String.format("%s_%s", verifyType, phone))).map(cacheCode -> {
            if (cacheCode.equals(verifyCode)) {
                return true;
            } else {
                return false;
            }
        }).orElse(false);
    }


    /**
     * 检验是否拥有凭证（即短信校验是否通过）
     *
     * @param phone      手机号码
     * @param verifyType 验证类型
     * @return 通过/不通过
     */
    public boolean validatePhoneCertificate(String phone, VerifyType verifyType) {
        // 判断手机号码是否已经拥有凭证; key like: CUSTOMER_REGISTRY_@PASS@_13812345678
        return Optional.ofNullable(stringRedisTemplate.opsForValue().get(String.format("%s_%s_%s", verifyType, PASS_FLAG, phone))).isPresent();
    }

    /**
     * 删除手机找回密码凭证
     *
     * @param phone 手机号码
     */
    public void deletePhoneCertificate(String phone, VerifyType verifyType) {
        // key like: CUSTOMER_REGISTRY_@PASS@_13812345678
        stringRedisTemplate.delete(String.format("%s_%s_%s", verifyType, PASS_FLAG, phone));
        stringRedisTemplate.delete(String.format("%s_%s", verifyType, phone));
    }


    /**
     * 验证码接口服务类
     *
     * @author NP-HEHU
     * @date 2015-8-28 17:01:58
     */
    private static class MyCaptchaService extends AbstractCaptchaService {

        /**
         * 验证码接口服务构造方法
         */
        public MyCaptchaService() {
            String[] fontOption = {"Verdana", "Tahoma"};
            wordFactory = new MyWordFactory();
            fontFactory = new RandomFontFactory(25, fontOption);
            textRenderer = new BestFitTextRenderer();
            backgroundFactory = new SingleColorBackgroundFactory();
            colorFactory = new SingleColorFactory(new Color(0xCC, 0x00, 0x00));
            filterFactory = new CurvesRippleFilterFactory(colorFactory);
            width = 90;
            height = 100;

        }
    }

    /**
     * 验证码生成器
     *
     * @author NP-HEHU
     */
    private static class MyWordFactory extends RandomWordFactory {
        private static final int FOUR = 4;
        private static final int SIX = 6;

        /**
         * 验证码生成器构造方法
         */
        public MyWordFactory() {
            // characters = "absdekmnowx23456789";
            characters = "123456789";
            minLength = FOUR;
            maxLength = SIX;
        }
    }
}
