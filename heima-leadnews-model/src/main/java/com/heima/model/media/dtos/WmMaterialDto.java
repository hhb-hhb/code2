package com.heima.model.media.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import com.heima.model.common.dtos.PageResponseResult;
import lombok.Data;

@Data
public class WmMaterialDto extends PageRequestDto {
    /**
     * 是否收藏
     */
    private Short Collected;
}
