package com.heima.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.admin.dtos.SensitiveDto;
import com.heima.model.admin.pojos.AdSensitive;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;

public interface AdSensitiveService extends IService<AdSensitive> {
    /**
     * 根据名字分页查询
     *
     * @param dto
     * @return
     */
    public ResponseResult findByNameAndPage(SensitiveDto dto);

    /**
     * 新增
     *
     * @param adSensitive
     * @return
     */
    public ResponseResult insert(AdSensitive adSensitive);


    /**
     * 根据id修改
     *
     * @param adSensitive
     * @return
     */
    public ResponseResult update(AdSensitive adSensitive );

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    public ResponseResult deleteById(Integer id);
}
