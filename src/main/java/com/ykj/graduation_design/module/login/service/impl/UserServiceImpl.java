package com.ykj.graduation_design.module.login.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykj.graduation_design.common.entity.SysUser;
import com.ykj.graduation_design.common.entity.UserInfo;
import com.ykj.graduation_design.common.utils.UserUtils;
import com.ykj.graduation_design.module.login.entity.LoginUser;
import com.ykj.graduation_design.module.login.mapper.UserMapper;
import com.ykj.graduation_design.module.login.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @Date: 2023/12/04/10:34
 * @Description:
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, SysUser> implements UserService {
    @Resource
    PasswordEncoder passwordEncoder;
    @Override
    public int insertUser(SysUser sysUser) {
        return baseMapper.insert(sysUser);
    }

    @Override
    public UserInfo getInfo() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser userDetails = (LoginUser) authentication.getPrincipal();
        SysUser sysUser = baseMapper.selectById(userDetails.getSysUser().getId());
        if (!Objects.isNull(sysUser)){
            return new UserInfo(sysUser);
        }
        else throw new RuntimeException("查无此人");

    }

    @Override
    public void updateUser(UserInfo userInfo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser userDetails = (LoginUser) authentication.getPrincipal();
        SysUser sysUser = baseMapper.selectById(userDetails.getSysUser().getId());

        sysUser.setNickName(userInfo.getNickName() != null ? userInfo.getNickName() : sysUser.getNickName());
        sysUser.setPhoneNumber(userInfo.getPhoneNumber() != null ? userInfo.getPhoneNumber() : sysUser.getPhoneNumber());
        sysUser.setAvatar(userInfo.getAvatar() != null ? userInfo.getAvatar() : sysUser.getAvatar());
        sysUser.setEmail(userInfo.getEmail() != null ? userInfo.getEmail() : sysUser.getEmail());

        baseMapper.updateById(sysUser);
    }

    @Override
    public List<UserInfo> listUserInfo() {
        List<SysUser> userList = this.list();
        List<UserInfo> userInfos;
        userInfos= userList.stream().map(UserInfo::new).toList();
        if (userInfos.isEmpty()){
            userInfos = new ArrayList<>();
        }
        return userInfos;
    }

    @Override
    public void updateUserByAdmin(SysUser sysUser) {
        SysUser oldSysUser = baseMapper.selectById(sysUser);
        LoginUser loginUser = UserUtils.getCurrentUser();
        sysUser.setUpdateBy(loginUser.getId());
        sysUser.setUpdateTime(new Date());
        sysUser.setNickName(sysUser.getNickName() != null ? sysUser.getNickName() : oldSysUser.getNickName());
        sysUser.setPhoneNumber(sysUser.getPhoneNumber() != null ? sysUser.getPhoneNumber() : oldSysUser.getPhoneNumber());
        sysUser.setAvatar(sysUser.getAvatar() != null ? sysUser.getAvatar() : oldSysUser.getAvatar());
        sysUser.setEmail(sysUser.getEmail() != null ? sysUser.getEmail() : oldSysUser.getEmail());
        sysUser.setUserName(sysUser.getUserName() != null ? sysUser.getUserName() : oldSysUser.getUserName());
        sysUser.setPassword(sysUser.getPassword() != null ? passwordEncoder.encode(sysUser.getPassword()) : oldSysUser.getPassword());
        sysUser.setStatus(sysUser.getStatus() != null ? sysUser.getStatus() : oldSysUser.getStatus());
        sysUser.setSex(sysUser.getSex() != null ? sysUser.getSex() : oldSysUser.getSex());

    }


}
