package com.heima.apis.wemedia;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.media.dtos.WmUserDto;

public interface LoginControllerApi {
    /**
     * 自媒体登录
     * @param wmUserDto
     * @return
     */
    public ResponseResult login(WmUserDto wmUserDto);
}
