package com.example.dbl.common.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import static com.example.dbl.common.bean.ResultMsg.ResultCode.ERROR;
import static com.example.dbl.common.bean.ResultMsg.ResultCode.SUCCESS;

/**
 * @author: liuhuan
 * @Description: 结果响应对象
 * @date: 2020/05/22
 */
public class ResultMsg {

    /**
     * 响应状态
     */
    protected Integer state;

    @JsonIgnore
    protected Integer code;
    /**
     * 响应消息
     */
    protected String stateInfo;
    /**
     * 响应结果
     */
    protected Object resInfo;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getCode() {
        return code;
    }

    public ResultMsg setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public Object getResInfo() {
        return resInfo;
    }

    public void setResInfo(Object resInfo) {
        this.resInfo = resInfo;
    }

    public ResultMsg(Integer state, String stateInfo, Object resInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
        this.resInfo = resInfo;
    }

    public ResultMsg(Integer state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public ResultMsg() {
    }

    public static class ResultCode {
        //成功
        public final static Integer SUCCESS = 0;
        //正常失败(用于条件限制)
        public final static Integer ERROR = -2;
        //发生异常
        public final static Integer EXCEPTION = -1;
        //参数缺失
        public final static Integer PARAM_MISS = 201;
        //不在登录状态
        public final static Integer LOGIN_OUT = 202;
        //没有操作权限
        public final static Integer NO_PERMISSION = 203;
        //参数不合法
        public final static Integer PARAM_ERROR_CODE = 204;
        //没有登录
        public final static Integer NOT_LOGIN = 205;
        //用户被禁言的状态
        public final static Integer USER_STATUS_BLACK = 206;
        //云盾检测未通过
        public final static Integer CHECK_ERROR = -100;
        //验证码未通过
        public final static Integer CAPTCHA_ERROR = -101;

        public final static Integer NET_ERROR_CODE = 207;


    }
    public static ResultMsg success(Object resInfo){
        return new ResultMsg(SUCCESS,"success",resInfo);
    }

    public static ResultMsg success(){
        return new ResultMsg(SUCCESS,"success",null);
    }

    public static ResultMsg  failure(String stateInfo){
        return new ResultMsg(ERROR,stateInfo,null);
    }

    public static ResultMsg  exception(String stateInfo){
        return new ResultMsg(ResultCode.EXCEPTION,stateInfo,null);
    }

}