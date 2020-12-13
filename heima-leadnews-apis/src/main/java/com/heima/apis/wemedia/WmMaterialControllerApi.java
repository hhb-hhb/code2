package com.heima.apis.wemedia;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.media.dtos.WmMaterialDto;
import org.springframework.web.multipart.MultipartFile;

public interface WmMaterialControllerApi {
    /**
     * 图片上传
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
     * 取消收藏
     * @param dto
     * @return
     */
    ResponseResult cancleCollectionMaterial(Integer id);

    /**
     * 收藏图片
     * @param dto
     * @return
     */
    ResponseResult collectionMaterial(Integer id);
}
