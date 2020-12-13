package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.fastdfs.FastDFSClient;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.media.dtos.WmMaterialDto;
import com.heima.model.media.pojos.WmMaterial;
import com.heima.model.media.pojos.WmNewsMaterial;
import com.heima.model.media.pojos.WmUser;
import com.heima.utils.threadlocal.WmThreadLocalUtils;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.service.WmMaterialService;
import groovy.util.logging.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {

    @Autowired
    private FastDFSClient fastDFSClient;

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    @Value("${fdfs.url}")
    private String fileServerUrl;

    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        // 1：检查参数
        if (multipartFile == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 2：把图片上传到fasFdfs
        String file = null;
        try {
            file = fastDFSClient.uploadFile(multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
        }

        // 3：保存素材到表中wm_material
        WmUser user = WmThreadLocalUtils.getUser();

        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUrl(file);
        wmMaterial.setIsCollection((short) 0);
        wmMaterial.setUserId(user.getId());
        wmMaterial.setType((short) 0);
        wmMaterial.setCreatedTime(new Date());
        save(wmMaterial);
        //拼接图片路径
        wmMaterial.setUrl(fileServerUrl + file);
        return ResponseResult.okResult(wmMaterial);
    }

    @Override
    public ResponseResult findList(WmMaterialDto dto) {
        // 检查参数
        dto.checkParam();
        // 查询用户信息
        LambdaQueryWrapper<WmMaterial> lambdaQueryWrapper = new LambdaQueryWrapper();
        Integer UserId = WmThreadLocalUtils.getUser().getId();
        lambdaQueryWrapper.eq(WmMaterial::getUserId, UserId);
        // 判断是否收藏
        if (dto.getCollected() != null && dto.getCollected().shortValue() == 1) {
            lambdaQueryWrapper.eq(WmMaterial::getIsCollection, dto.getCollected());
        }
        //收藏按日期倒叙排序
        lambdaQueryWrapper.orderByDesc(WmMaterial::getCreatedTime);
        // 分页查询
        IPage page = new Page(dto.getSize(), dto.getSize());
        IPage result = page(page, lambdaQueryWrapper);

        // 返回数据
        PageResponseResult pageResponseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) result.getTotal());
        List<WmMaterial> datas = result.getRecords();
        // 为每个图片加上前缀
        datas = datas.stream().map(item -> {
            item.setUrl(fileServerUrl + item.getUrl());
            return item;
        }).collect(Collectors.toList());
        pageResponseResult.setData(datas);
        return pageResponseResult;
    }

    @Override
    public ResponseResult updateStatus(Integer id, Short type) {
        //1.检查参数
        if(id == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2.更新状态
        //获取当前用户信息
        Integer uid = WmThreadLocalUtils.getUser().getId();
        update(Wrappers.<WmMaterial>lambdaUpdate().set(WmMaterial::getIsCollection,type)
                .eq(WmMaterial::getId,id).eq(WmMaterial::getUserId,uid));

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult delPicture(Integer id) {
        // 检查参数
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 判断在新闻表中是否引用
        LambdaQueryWrapper<WmNewsMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmNewsMaterial::getMaterialId, id);
        Integer count = wmNewsMaterialMapper.selectCount(lambdaQueryWrapper);
        if (count > 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"当前图片被引用");
        }
        //  删除fastdfs中的图片
        WmMaterial wmMaterial = getById(id);
        String fileId = wmMaterial.getUrl().replace(fileServerUrl, "");
        try {
            fastDFSClient.delFile(fileId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 删除数据库中的图片
        removeById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
