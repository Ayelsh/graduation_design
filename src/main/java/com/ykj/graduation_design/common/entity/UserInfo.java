package com.ykj.graduation_design.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @Date: 2024/03/14/14:39
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;


    /**
     * 账号状态（0正常 1停用）
     */
    private String status;


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
    private char sex;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户类型（0管理员，1普通用户）
     */
    private String userType;


    public UserInfo(SysUser sysUser) {
        this.userName = sysUser.getUserName();
        this.nickName = sysUser.getNickName();
        this.email = sysUser.getEmail();
        this.phoneNumber = sysUser.getPhoneNumber();

        if (Objects.isNull(sysUser.getStatus())) {
            this.status = "未知";
        } else {
            this.status = sysUser.getStatus() ? "正常" : "停用";
        }


        if (Objects.isNull(sysUser.getSex())) {
            this.sex = '?';
        } else {
            this.sex = sysUser.getSex() ? '男' : '女';
        }

        if (Objects.isNull(sysUser.getUserType())) {
            this.userType = "未知";
        } else {
            this.userType = sysUser.getUserType() ? "管理员" : "用户";
        }
        this.avatar = sysUser.getAvatar();

    }

}
