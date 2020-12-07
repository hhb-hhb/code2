package com.heima.apis.admin;

import com.heima.model.admin.dtos.SensitiveDto;
import com.heima.model.admin.pojos.AdSensitive;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;

public interface AdSensitiveControllerApi {
    /**
     * 根据名字分页查询
     *
     * @param dto
     * @return
     */
    public ResponseResult list(SensitiveDto dto);

    /**
     * 新增
     *
     * @param adSensitive
     * @return
     */
    public ResponseResult save(AdSensitive adSensitive);


    /**
     * 根据id修改
     *
     * @param adSensitive
     * @return
     */
    public ResponseResult updateById(AdSensitive  adSensitive);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    public ResponseResult deleteById(Integer id);


}
