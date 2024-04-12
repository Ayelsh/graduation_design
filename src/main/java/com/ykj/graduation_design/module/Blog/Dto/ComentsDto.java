package com.ykj.graduation_design.module.Blog.Dto;

import com.ykj.graduation_design.module.Blog.entity.Comments;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * @Author: Ayelsh
 * @Date: 2024/04/10/21:36
 * @Description: 
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentsDto extends Comments {
     private List<Comments> children;
}
