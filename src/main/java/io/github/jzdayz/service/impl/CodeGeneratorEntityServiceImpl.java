package io.github.jzdayz.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.jzdayz.entity.CodeGeneratorEntity;
import io.github.jzdayz.mapper.CodeGeneratorEntityMapper;
import io.github.jzdayz.service.ICodeGeneratorEntityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author huqingfeng
 * @since 2021-08-25
 */
@Service
public class CodeGeneratorEntityServiceImpl extends ServiceImpl<CodeGeneratorEntityMapper, CodeGeneratorEntity> implements ICodeGeneratorEntityService {

    @Override
    public void saveOrUpdateByAlias(CodeGeneratorEntity entity) {
        String alias = entity.getAlias();
        boolean exists = baseMapper.selectList(
                Wrappers.<CodeGeneratorEntity>lambdaQuery().eq(CodeGeneratorEntity::getAlias, alias)
        ).size() > 0;
        if (exists) {
            baseMapper.update(entity, Wrappers.<CodeGeneratorEntity>lambdaUpdate().eq(CodeGeneratorEntity::getAlias, alias));
        } else {
            baseMapper.insert(entity);
        }
    }
}
