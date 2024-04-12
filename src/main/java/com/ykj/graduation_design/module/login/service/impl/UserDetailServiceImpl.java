package com.ykj.graduation_design.module.login.service.impl;

import cn.hutool.core.text.StrSplitter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.ykj.graduation_design.common.entity.SysUser;
import com.ykj.graduation_design.common.utils.RoleConverter;
import com.ykj.graduation_design.module.login.entity.LoginUser;
import com.ykj.graduation_design.module.login.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

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

        LambdaQueryWrapper<SysUser> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(SysUser::getUserName, username);


        SysUser sysUser = userMapper.selectOne(userLambdaQueryWrapper);

        if (Objects.isNull(sysUser)){
            throw new UsernameNotFoundException("查无该用户,请重试");
        }
        Set<GrantedAuthority> authorities = RoleConverter.convertRoles(sysUser.getRoles());
        return new LoginUser(sysUser,"",authorities);
    }


}
