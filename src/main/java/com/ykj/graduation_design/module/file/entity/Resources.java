package com.ykj.graduation_design.module.file.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

import java.util.Date;

/**
 * 按键表
 * @TableName resources
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("resources")
public class Resources implements Serializable {

    @Serial
    private static final long serialVersionUID = 8984223764698333363L;
    /**
     * 主键
     */
    @TableId
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    /**
     * 资源文件名
     */
    private String filename;
    /**
     * 资源描述
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
     * 资源URL
     */
    private String fileUrl;

    public Resources( String filename,String description){
        this.filename = filename;
        this.description = description;
    }

}
