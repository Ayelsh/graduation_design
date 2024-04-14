package com.ykj.graduation_design.module.liunxConect.eneity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

import java.util.Date;

/**
* 按键表
* @author Ayelsh.ye
 * @TableName button
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("button")
public class Button implements Serializable {

    @Serial
    private static final long serialVersionUID = -4066751666077248722L;
    /**
    * 主键
    */
    @TableId
    private Long id;
    /**
    * 按键对应命令
    */
    private String command;
    /**
    * 按键描述
    */
    private String description;
    /**
    * 创建时间
    */
    private Date createdTime;
    /**
    * 更新时间
    */
    private Date updatedTime;
    /**
     * 命令类型
     */
    private boolean commandType;
}
