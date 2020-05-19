package com.example.dbl.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: liuhuan
 * @Description: 首页跳转
 * @date: 2019/12/1
 */
@Controller
public class IndexController {

    @RequestMapping("/test")
    public String test(){
        return "ok";
    }

    @RequestMapping("/test1")
    public String test(ModelAndView model,String ct){
        System.out.println(ct);
        return "test";
    }


    @RequestMapping("/getQRCode/{categoryId}")
    public void getQRCode(HttpServletRequest request, HttpServletResponse response,@PathVariable Long categoryId) throws Exception {
        //二维码中包含的信息
        String content = "https://bang.rd.duia.com/wap/jc/"+categoryId;
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        // 指定编码格式
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 指定纠错级别(L--7%,M--15%,Q--25%,H--30%)
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 编码内容,编码类型(这里指定为二维码),生成图片宽度,生成图片高度,设置参数
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 200, 200, hints);
        //设置请求头
        response.setHeader("Content-Type","image/jpg");
        response.setHeader("Content-Disposition", "inline;filename=" + "二维码.jpg");
        OutputStream outputStream = response.getOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "jpg", outputStream);
        outputStream.flush();
        outputStream.close();
    }
}
