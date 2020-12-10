package com.heima.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.AuthDto;
import com.heima.model.user.pojos.ApUserRealname;
import com.heima.user.mapper.ApUserRealnameMapper;
import org.springframework.stereotype.Service;


public interface ApUserRealnameService extends IService< ApUserRealname> {
    /**
     * 根据状态分页查询
     * @param dto
     * @return
     */
    public ResponseResult loadListByStatus(AuthDto dto);

    /**
     * 审核修改认证状态
     * @param dto
     * @param stutas
     * @return
     */
    public ResponseResult updateStatusById(AuthDto dto,Short stutas);

}
