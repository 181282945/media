package com.sunjung.test.controller;

import com.sunjung.base.sysmgr.aclresource.annotation.AclResc;
import com.sunjung.base.sysmgr.aclresource.common.AclResourceType;
import com.sunjung.core.controller.BaseController;
import com.sunjung.core.util.SpringUtils;
import com.sunjung.test.service.TestEntityService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;


/**
 * Created by ZhenWeiLai on 2016/12/28.
 */
@RestController
@RequestMapping("/test")
@AclResc(code = "test",name = "测试模块",homePage = TestController.HOME_PAGE)
public class TestController extends BaseController {

    final static String HOME_PAGE = "/test/list";

//    private static final UserService userService;
//    private static final User user;
//    static {
//        userService = (UserService)SpringUtils.getBean("userService");
//        user = userService.findEntityById("1");
//    }

//    @Resource
//    private WechatProperties wechatProperties;

    @Resource
    private TestEntityService testEntityService;


//    @PreAuthorize("#oauth2.hasScope('read')")
//    @RequestMapping( value = "/test", method = RequestMethod.GET)
//    public void test(){
//        System.out.println(wechatProperties.getAppid());
//        System.out.println("in");
//    }

    public TestController(){
//        System.out.println(wechatProperties.getAppid());
    }

    @RequestMapping("/helloThymeleaf")
    @AclResc(code = "helloThymeleaf",name = "测试Thymeleaf")
    public ModelAndView testThymeleaf(){
        ModelAndView mav = new ModelAndView("footer");
        mav.addObject("hello", "Thymeleaf");
        return mav;
    }

    @RequestMapping(value = "/testService",method = RequestMethod.GET)
    @AclResc(code = "testService",name = "测试Service")
    public void testService(){
        System.out.println(testEntityService.findEntityById(1));
//        System.out.println(userSignInfoService.findEntityById("1"));
    }

//    @RequestMapping(value = "/listennerTest",method = RequestMethod.GET)
//    public void listennerTest(){
//        System.out.println(user.getUsername());
//    }


    @RequestMapping(value = "/getUrlMapping")
    @AclResc(code = "getUrlMapping",name = "获取UrlMappling")
    public Object getUrlMapping() {
//        SpringUtils.getBean(RestController.class);
        RequestMappingHandlerMapping rmhp = SpringUtils.getBean(RequestMappingHandlerMapping.class);
//        System.out.println(rmhp.getClass().getSimpleName());
        Map<RequestMappingInfo, HandlerMethod> map = rmhp.getHandlerMethods();
        for(RequestMappingInfo info : map.keySet()){
            AclResc resc = map.get(info).getMethod().getAnnotation(AclResc.class);
            RequestMapping requestMapping = map.get(info).getBeanType().getAnnotation(RequestMapping.class);
            if(requestMapping != null){
                System.out.println(info.getPatternsCondition().toString()
                        + "," + requestMapping.value()[0]);
            }
//            if(alias != null){
//                System.out.println(info.getPatternsCondition().toString()
//                        + "," + map.get(info).getBean().toString()+"," + alias.value());
//            }
//            System.out.println(info.getPatternsCondition().toString()
//                    + "," + map.get(info).getBeanType());
        }
        return null;
    }

    /**
     * 验证url和token
     */
    @RequestMapping( value = "/tks", method = RequestMethod.GET)
    public void tokenService(HttpServletRequest request,HttpServletResponse response) throws IOException {

        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戮
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");

        PrintWriter out = response.getWriter();
        // 通过检验 signature 对请求进行校验，若校验成功则原样返回 echostr，表示接入成功，否则接入失败
        if(new SignUtil().checkSignature(signature, timestamp, nonce)){
            out.print(echostr);
        }

        out.close();
    }


    class SignUtil {
        /**
         * 与接口配置信息中的 token 要一致，这里赋予什么值，在接口配置信息中的Token就要填写什么值，
         * 两边保持一致即可，建议用项目名称、公司名称缩写等，我在这里用的是项目名称weixinface
         */
        private String token = "weixintest";

        /**
         * 验证签名
         * @param signature
         * @param timestamp
         * @param nonce
         * @return
         */
        public boolean checkSignature(String signature, String timestamp, String nonce){
            String[] arr = new String[]{token, timestamp, nonce};
            // 将 token, timestamp, nonce 三个参数进行字典排序
            Arrays.sort(arr);
            StringBuilder content = new StringBuilder();
            for(int i = 0; i < arr.length; i++){
                content.append(arr[i]);
            }
            MessageDigest md = null;
            String tmpStr = null;

            try {
                md = MessageDigest.getInstance("SHA-1");
                // 将三个参数字符串拼接成一个字符串进行 shal 加密
                byte[] digest = md.digest(content.toString().getBytes());
                tmpStr = byteToStr(digest);
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            content = null;
            // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
            return tmpStr != null ? tmpStr.equals(signature.toUpperCase()): false;
        }

        /**
         * 将字节数组转换为十六进制字符串
         * @param digest
         * @return
         */
        private String byteToStr(byte[] digest) {
            // TODO Auto-generated method stub
            String strDigest = "";
            for(int i = 0; i < digest.length; i++){
                strDigest += byteToHexStr(digest[i]);
            }
            return strDigest;
        }

        /**
         * 将字节转换为十六进制字符串
         * @param b
         * @return
         */
        private String byteToHexStr(byte b) {
            // TODO Auto-generated method stub
            char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            char[] tempArr = new char[2];
            tempArr[0] = Digit[(b >>> 4) & 0X0F];
            tempArr[1] = Digit[b & 0X0F];
            String s = new String(tempArr);
            return s;
        }
    }
}
