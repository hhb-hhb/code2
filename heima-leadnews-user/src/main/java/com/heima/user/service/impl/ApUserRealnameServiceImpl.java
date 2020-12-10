package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.common.constants.userconstants.UserConstants;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.media.pojos.WmUser;
import com.heima.model.user.dtos.AuthDto;
import com.heima.model.user.pojos.ApUser;
import com.heima.model.user.pojos.ApUserRealname;
import com.heima.user.feign.ArticleFeign;
import com.heima.user.feign.WmUserFeign;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.mapper.ApUserRealnameMapper;
import com.heima.user.service.ApUserRealnameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ApUserRealnameServiceImpl extends ServiceImpl<ApUserRealnameMapper, ApUserRealname> implements ApUserRealnameService {
    @Override
    public ResponseResult loadListByStatus(AuthDto dto) {
        // 1.检查参数
        if (null == dto) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 2.分页检查
        dto.checkParam();
        // 3.分页查询
        LambdaQueryWrapper<ApUserRealname> lambdaQueryWrapper = new LambdaQueryWrapper();
        if (dto.getStatus() != null) {
            // 4.根据状态查询
            lambdaQueryWrapper.eq(ApUserRealname::getStatus, dto.getStatus());
        }
        IPage page = new Page(dto.getPage(), dto.getSize());
        IPage result = page(page, lambdaQueryWrapper);
        // 返回数据
        PageResponseResult pageResponseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) result.getTotal());
        pageResponseResult.setData(result.getRecords());
        return pageResponseResult;
    }

    @Override
    public ResponseResult updateStatusById(AuthDto dto, Short stutas) {
        // 1.检查参数
        if (null == dto || dto.getId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 2.检查状态
        if (checkOutStutas(stutas)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);

        }
        // 3.修改状态
        ApUserRealname apUserRealname = new ApUserRealname();
        apUserRealname.setId(dto.getId());
        apUserRealname.setStatus(stutas);
        if (dto.getMsg() == null) {
            apUserRealname.setReason(dto.getMsg());
        }
        updateById(apUserRealname);


        // 4.如果审核状态通过，创建媒体用户，作者用户
        if (stutas.equals(UserConstants.PASS_AUTH)) {
            ResponseResult result = createWmUserAndAuthor(dto);
            if (result != null) {
                return result;
            }
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Autowired
    private ApUserMapper apUserMapper;
    @Autowired
    private WmUserFeign wmUserFeign;
    @Autowired
    private ArticleFeign articleFeign;

    /**
     * 创建自媒体用户和作者用户
     *
     * @param dto
     * @return
     */
    private ResponseResult createWmUserAndAuthor(AuthDto dto) {
        // 根据id查询出用户的认证表
        Integer apUserRealnameId = dto.getId();
        ApUserRealname apUserRealname = getById(apUserRealnameId);
        // 查询该用户的user表
        Integer userId = apUserRealname.getUserId();
        ApUser apUser = apUserMapper.selectById(userId);
        if (apUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmUser wmUser = wmUserFeign.findByName(apUser.getName());
        if (wmUser == null) {
            wmUser = new WmUser();
            wmUser.setApUserId(apUser.getId());
            wmUser.setCreatedTime(new Date());
            wmUser.setName(apUser.getName());
            wmUser.setPassword(apUser.getPassword());
            wmUser.setSalt(apUser.getSalt());
            wmUser.setPhone(apUser.getPhone());
            wmUser.setStatus(9);
            wmUserFeign.save(wmUser);
        }

        // 创建作者用户
        createAuthor(wmUser);
        // 设置普通标签
        apUser.setFlag((short) 1);
        // 更新用户表
        apUserMapper.updateById(apUser);
        return null;
    }

    /**
     * 创建作者
     * @param wmUser
     */
    private void createAuthor(WmUser wmUser) {
        Integer apUserId = wmUser.getApUserId();
        ApAuthor apAuthor = articleFeign.findByUserId(apUserId);
        if (apAuthor == null) {
            apAuthor = new ApAuthor();
            apAuthor.setName(wmUser.getName());
            apAuthor.setCreatedTime(new Date());
            apAuthor.setUserId(apUserId);
            apAuthor.setType(UserConstants.AUTH_TYPE);
            articleFeign.save(apAuthor);
        }
    }


    private boolean checkOutStutas(Short stutas) {
        if (stutas == null || !stutas.equals(UserConstants.FAIL_AUTH) && !stutas.equals(UserConstants.PASS_AUTH)) {
            return true;
        }
        return false;
    }
}
