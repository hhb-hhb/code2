package com.heima.wemedia.controller;

import com.heima.apis.wemedia.LoginControllerApi;
import com.heima.apis.wemedia.WmUserControllerApi;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.media.dtos.WmUserDto;
import com.heima.model.media.pojos.WmUser;
import com.heima.wemedia.service.WmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController implements LoginControllerApi {

    @Autowired
    private WmUserService wmUserService;

    @PostMapping("/in")
    @Override
    public ResponseResult login(@RequestBody WmUserDto wmUserDto) {
        return wmUserService.login(wmUserDto);
    }
}
