package io.github.jzdayz.db.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.jzdayz.db.entity.CodeGeneratorEntity;

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
