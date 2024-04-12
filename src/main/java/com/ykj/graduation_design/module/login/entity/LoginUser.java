package com.ykj.graduation_design.module.login.entity;

import com.ykj.graduation_design.common.entity.SysUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @Date: 2023/12/03/14:29
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class LoginUser implements UserDetails {

    private SysUser sysUser;

    private String ip;

    private Set<GrantedAuthority> authorities;

    public LoginUser(SysUser sysUser, String s) {
        this.ip = s;
        this.sysUser = sysUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }


//    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
//        return null;
//    }

    @Override
    public String getPassword() {
        return sysUser.getPassword();
    }

    @Override
    public String getUsername() {
        return sysUser.getUserName();
    }

    public void setUsername(String userName) {
        sysUser.setUserName(userName);
    }

    public Long getId(){return sysUser.getId();}

    public void setId(Long id) {
        sysUser.setId(id);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
