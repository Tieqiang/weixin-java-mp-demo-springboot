package com.github.binarywang.demo.wx.mp.controller;

import com.github.binarywang.demo.wx.mp.config.WxMpConfiguration;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMenuService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpMenuServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/check")
public class CheckController {

    @GetMapping
    public String check() throws WxErrorException {
        String appid="wx42b8bf514df86fef";

        List<NameValuePair> params = new ArrayList<>();
        NameValuePair redirect_uri = new BasicNameValuePair("redirect_uri","http://myweixin.tunnel.qydev.com/regist.html");
        params.add(redirect_uri);

        String format = URLEncodedUtils.format(params,"utf-8");
        System.out.println(format);
        String path = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx42b8bf514df86fef" +
            "&response_type=code" +
            "&scope=snsapi_base&" +format+
            "&state=123#wechat_redirect";
        System.out.println(path);

        WxMpMenuService wxMpMenuService = new WxMpMenuServiceImpl(WxMpConfiguration.getMpServices().get(appid));

        WxMenu wxMenu = new WxMenu();

        WxMenuButton button3 = new WxMenuButton();
        button3.setName("测试菜单");

        wxMenu.getButtons().add(button3);

        WxMenuButton button31 = new WxMenuButton();
        button31.setType("view");
        button31.setName("测试");
        button31.setUrl(path);

        List<WxMenuButton> subButtons = new ArrayList<>();
        subButtons.add(button31);
        button3.setSubButtons(subButtons);



        String s = wxMpMenuService.menuCreate(wxMenu);
        return s ;

    }

    @GetMapping("delete")
    public void deleteMenu() throws WxErrorException {
        String appid="wx42b8bf514df86fef";
        WxMpMenuService wxMpMenuService = new WxMpMenuServiceImpl(WxMpConfiguration.getMpServices().get(appid));
        wxMpMenuService.menuDelete();
    }


    @PostMapping("acc")
    public WxJsapiSignature getAccessToken(@RequestBody(required = false) String url) throws WxErrorException, UnsupportedEncodingException {
        WxMpService mps = WxMpConfiguration.getMpServices().get("wx42b8bf514df86fef");


        url = URLDecoder.decode(url, "utf-8");
        String accessToken = mps.getAccessToken();
        System.out.println(accessToken);
        String jsapiTicket = mps.getJsapiTicket();
        System.out.println(jsapiTicket);
        System.out.println(url);
        if(!StringUtils.isNotEmpty(url)){
            url="http://myweixin.tunnel.qydev.com/regist.html";
        }
        WxJsapiSignature jsapiSignature = mps.createJsapiSignature(url);
        return jsapiSignature;
    }

}
