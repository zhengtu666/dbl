package com.example.dbl.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.dbl.common.bean.ResultMsg;
import com.example.dbl.entity.EsNoteBook;
import com.example.dbl.util.ESUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: liuhuan
 * @Description: 首页跳转
 * @date: 2019/12/1
 */
@Controller
@Slf4j
public class IndexController {

    @Autowired
    private ESUtil esUtil;

    @GetMapping("/test")
    @ResponseBody
    public String test(){
        return "ok";
    }


    @GetMapping("/getQRCode")
    public void getQRCode(HttpServletRequest request, HttpServletResponse response, String url) throws Exception {
        //二维码中包含的信息
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        // 指定编码格式
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 指定纠错级别(L--7%,M--15%,Q--25%,H--30%)
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 编码内容,编码类型(这里指定为二维码),生成图片宽度,生成图片高度,设置参数
        BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 200, 200, hints);
        //设置请求头
        response.setHeader("Content-Type","image/jpg");
        response.setHeader("Content-Disposition", "inline;filename=" + "二维码.jpg");
        OutputStream outputStream = response.getOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "jpg", outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @GetMapping("/queryUseES")
    @ResponseBody
    public ResultMsg queryUseES(String keyword){
        try {
            List<JSONObject>list = esUtil.queryByKeyword(keyword);
            return ResultMsg.success(list);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            return ResultMsg.exception("查询失败");
        }

    }

    @PostMapping("/insertES")
    @ResponseBody
    public ResultMsg insertES(EsNoteBook esNoteBook){
        try {
            esUtil.insertES(esNoteBook);
            return ResultMsg.success();
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            return ResultMsg.exception("新增失败");
        }
        
    }

    @GetMapping("/deleteES")
    @ResponseBody
    public ResultMsg deleteES(Long noteId){
        try {
            esUtil.deleteES(noteId);
            return ResultMsg.success();
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            return ResultMsg.exception("删除失败");
        }

    }

    @GetMapping("/updateES")
    @ResponseBody
    public ResultMsg updateES(Long noteId,String noteContent){
        try {
            esUtil.updateES(noteId, noteContent);
            return ResultMsg.success();
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            return ResultMsg.exception("更新失败");
        }

    }

}
