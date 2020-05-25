package com.example.dbl.entity;


import lombok.Data;

import java.io.Serializable;

/**
 * @author: yijun
 * @Description: es 实体类  是图书和笔记的综合
 *  以book开头的是图书相关信息，以note开头的是笔记相关信息
 * @date: 2018/12/19
 */
@Data
public class EsNoteBook implements Serializable {
    /**
     * 图书ID
     */
    private Long bookId;
    /**
     * 图书名称
     */
    private String bookName;
    /**
     * 图书创建类型：0扫描生成；1自定义生成
     */
    private Integer bookType;
    /**
     * 图书背景图片url
     */
    private String bookPicUrl;
    /**
     * 图书出版方
     */
    private String bookPublisher;
    /**
     * 图书作者
     */
    private String bookAuthor;
    /**
     * 笔记总页数
     */
    private Integer bookNoteTotalPage;
    /**
     *图书记录创建者
     */
    private Long bookCreatorId;
    /**
     * 创建时间
     */
    private String bookCreateTime;
    /**
     * 图书更新时间
     */
    private String bookUpdateTime;
    /**
     * 笔记ID
     */
    private Long noteId;
    /**
     * 图书拍照地址
     */
    private String notePicUrl;
    /**
     * 页码
     */
    private Integer notePageNo;
    /**
     * 笔记心得（内容）
     */
    private String noteContent;
    /**
     * 笔记 书摘
     */
    private String noteDigest;
    /**
     * 笔记的书摘和心得，方便展示
     */
    private String noteDigestContent;
    /**
     * 书摘添加方式(0:自定义,1:文字识别)
     */
    private Integer noteDigestType;
    /**
     * 识别状态(0:未使用,1:识别成功,-1:识别失败)
     */
    private Integer noteIdentifyStatus;
    /**
     * 笔记创建者
     */
    private Long noteCreatorId;
    /**
     * 笔记创建时间
     */
    private String noteCreateTime;
    /**
     * 笔记更新时间
     */
    private String noteUpdateTime;
    /**
     * 图书关联ID
     */
    private Long noteBookId;
    /**
     * 笔记所属图书名称
     */
    private String noteBookName;
    /**
     * 笔记所属图书封面
     */
    private String noteBookPic;

    //数据类型 1为图书，2为笔记
    private Integer dataType;
    /**
     * 所属app
     */
    private Integer appType;

}
