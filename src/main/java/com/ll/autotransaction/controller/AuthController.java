package com.ll.autotransaction.controller;

import com.ll.autotransaction.controller.model.CommonResult;
import com.ll.autotransaction.controller.model.vo.CaptchaVO;
import com.ll.autotransaction.controller.security.UserAuthenticationToken;
import com.ll.autotransaction.controller.security.UserPrincipal;
import com.ll.autotransaction.service.model.UserInfo;
import com.ll.autotransaction.util.LocalCacheUtil;
import com.ll.autotransaction.util.ValidateCode;
import com.ll.autotransaction.util.ValidateCodeUtil;
import lombok.var;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;


@RestController
@RequestMapping("/auth")
public class AuthController {



    private static final String imageCodeCacheKey = "login-image_code-";

    @GetMapping("/getImageCode")
    public CommonResult<CaptchaVO> getCaptcha() throws IOException {
        ValidateCode validateCode = ValidateCodeUtil.getValidateCode(120, 44);

        String uuid = UUID.randomUUID().toString();
        // 添加图形验证码到redis, 缓存5分钟
        LocalCacheUtil.save(imageCodeCacheKey + uuid, validateCode.codestr, 100*60*5L);

        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setUuid(uuid);

        // BufferedImage 转换 base64
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(validateCode.codeimg, "png", bos);
        byte[] imageBytes = bos.toByteArray();
        String imageBase64 = new String(Base64.getEncoder().encode(imageBytes));
        imageBase64 = imageBase64.replaceAll("\n", "").replaceAll("\r", "");
        captchaVO.setImageBase64(imageBase64);
        bos.close();

        return CommonResult.success(captchaVO);
    }




    @GetMapping("/login")
    public CommonResult<Boolean> login(){
        var userInfo = new UserInfo();
        userInfo.setId(1);
        userInfo.setAccountNumber("liuli");
        setResponseAuthInfo(userInfo);
        return CommonResult.success(true);
    }





    /**
     * 设置验证信息
     * @param userInfo
     */
    private void setResponseAuthInfo(UserInfo userInfo){
        UserPrincipal principal = new UserPrincipal();
        principal.setUserId(userInfo.getId().toString());
        principal.setUsername(userInfo.getAccountNumber());
        principal.setEnabled(true);
        principal.setAccountNonLocked(true);
        SecurityContextHolder.getContext().setAuthentication(new UserAuthenticationToken(principal));
    }



}
