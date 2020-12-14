package com.heima.apis.article;

import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.web.bind.annotation.ResponseBody;

public interface AuthorControllerApi {
    /**
     * 根据用户id查询作者信息
     *
     * @param id
     * @return
     */
    public ApAuthor findByUserId(Integer id);

    /**
     *保存作者信息
     * @param apAuthor
     * @return
     */
    public ResponseResult save(ApAuthor apAuthor);
}
