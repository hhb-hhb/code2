package com.heima.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.admin.dtos.ChannelDto;
import com.heima.model.admin.pojos.AdChannel;
import com.heima.model.common.dtos.ResponseResult;

public interface AdChannelService extends IService<AdChannel> {
    /**
     * 根据姓名分页查询
     * @param dto
     * @return
     */
    public ResponseResult findByNameAndPage(ChannelDto dto);
    /**
     * 新增
     * @param adChannel
     * @return
     */
    public ResponseResult insert(AdChannel adChannel);

    /**
     * 删除
     * @param id
     * @return
     */
    public ResponseResult deleteById(Integer id);

    /**
     * 修改
     * @param adChannel
     * @return
     */
    public ResponseResult update(AdChannel adChannel);


}
