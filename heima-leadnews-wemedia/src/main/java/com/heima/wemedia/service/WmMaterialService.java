package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.media.dtos.WmMaterialDto;
import com.heima.model.media.pojos.WmMaterial;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public interface WmMaterialService extends IService<WmMaterial> {
    /**
     * 上传图片接口
     * @param multipartFile
     * @return
     */
    ResponseResult uploadPicture(MultipartFile multipartFile);

    /**
     * 素材列表
     * @return
     */
    ResponseResult findList(WmMaterialDto dto);

    /**
     * 删除图片
     * @param id
     * @return
     */
    ResponseResult delPicture(Integer id);

    /**
     * 收藏与取消收藏
     * @param id
     * @param type
     * @return
     */
    ResponseResult updateStatus(Integer id, Short type);
}
