package com.ykj.graduation_design.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @private Date: 2023/12/01/16:28
 * @Description:
 */
@Data
@NoArgsConstructor
@TableName(value = "sys_user")
public class User {


    @TableId
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 密码
     */
    private String password;

    /**
     * 账号状态（0正常 1停用）
     */
    private Boolean status;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 用户性别（0男，1女，2未知）
     */
    private Boolean sex;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户类型（0管理员，1普通用户）
     */
    private Boolean userType;

    /**
     * 创建人的用户id
     */
    private Long createBy;

    /**
     * 创建时间
     */
    @DateTimeFormat
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    @DateTimeFormat
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 删除标志（0代表未删除，1代表已删除)
     */
    private Boolean delFlag;
}
