package com.ykj.graduation_design.module.login.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykj.graduation_design.common.entity.User;
import com.ykj.graduation_design.common.entity.UserInfo;
import com.ykj.graduation_design.module.login.entity.LoginUser;
import com.ykj.graduation_design.module.login.mapper.UserMapper;
import com.ykj.graduation_design.module.login.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @Date: 2023/12/04/10:34
 * @Description:
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public int insertUser(User user) {
        return baseMapper.insert(user);
    }

    @Override
    public UserInfo getInfo() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser userDetails = (LoginUser) authentication.getPrincipal();
        User user = baseMapper.selectById(userDetails.getUser().getId());
        if (!Objects.isNull(user)){
            return new UserInfo(user);
        }
        else throw new RuntimeException("查无此人");

    }

    @Override
    public void updateUser(UserInfo userInfo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser userDetails = (LoginUser) authentication.getPrincipal();
        User user = baseMapper.selectById(userDetails.getUser().getId());

        user.setNickName(userInfo.getNickName() != null ? userInfo.getNickName() : user.getNickName());
        user.setPhoneNumber(userInfo.getPhoneNumber() != null ? userInfo.getPhoneNumber() : user.getPhoneNumber());
        user.setAvatar(userInfo.getAvatar() != null ? userInfo.getAvatar() : user.getAvatar());
        user.setEmail(userInfo.getEmail() != null ? userInfo.getEmail() : user.getEmail());

        baseMapper.updateById(user);
    }


}
