package com.heima.model.user.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import com.heima.model.common.dtos.PageResponseResult;
import lombok.Data;

@Data
public class AuthDto extends PageRequestDto {
    private Short status;
    /**
     * 拒绝原因
     */
    private String msg;
    /**
     * 认证id
     */
    private Integer id;
}
