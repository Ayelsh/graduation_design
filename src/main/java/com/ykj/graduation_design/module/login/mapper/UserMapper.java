package com.ykj.graduation_design.module.login.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ykj.graduation_design.common.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @Date: 2023/12/01/16:45
 * @Description:
 */
@Mapper
public interface UserMapper extends BaseMapper<SysUser> {
    @Select("Select exists (select id from sys_user where ${columnName} = '${Value}' limit 1)")
    boolean checkExist(@Param("Value") String value, @Param("columnName") String columnName);
}
