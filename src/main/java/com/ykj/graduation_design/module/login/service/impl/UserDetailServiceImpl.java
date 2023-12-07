package com.ykj.graduation_design.module.login.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ykj.graduation_design.common.entity.User;
import com.ykj.graduation_design.module.login.entity.LoginUser;
import com.ykj.graduation_design.module.login.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @Date: 2023/12/01/16:25
 * @Description:
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(userLambdaQueryWrapper);

        if (Objects.isNull(user)){
            throw new UsernameNotFoundException("查无该用户,请重试");
        }

        return new LoginUser(user);
    }
}
