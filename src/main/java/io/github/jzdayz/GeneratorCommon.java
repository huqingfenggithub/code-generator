package io.github.jzdayz;

import io.github.jzdayz.ex.NotSupportException;
import io.github.jzdayz.service.ICodeGeneratorEntityService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class GeneratorCommon {

    private MbpGenerator mbpGenerator;

    private ICodeGeneratorEntityService iCodeGeneratorEntityService;

    public Resource generator(String type, CodeGeneratorEntity entity) {
        Resource resource;
        if (Objects.equals("mbp", type)) {
            resource =  mbpGenerator.generator(entity);
        }else{
            throw new NotSupportException();
        }
        if (StringUtils.isNotEmpty(entity.getAlias())){
            iCodeGeneratorEntityService.saveOrUpdateByAlias(entity);
        }
        return resource;
    }

    public List<CodeGeneratorEntity> list() {
        return iCodeGeneratorEntityService.list();
    }

}
