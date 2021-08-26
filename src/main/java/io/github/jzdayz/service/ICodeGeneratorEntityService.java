package io.github.jzdayz.service;

import io.github.jzdayz.entity.CodeGeneratorEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author huqingfeng
 * @since 2021-08-25
 */
public interface ICodeGeneratorEntityService extends IService<CodeGeneratorEntity> {

    void saveOrUpdateByAlias(CodeGeneratorEntity entity);

}
