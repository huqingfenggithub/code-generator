package io.github.jzdayz.controller;

import cn.hutool.core.bean.copier.BeanCopier;
import cn.hutool.core.bean.copier.CopyOptions;
import io.github.jzdayz.db.entity.CodeGeneratorEntity;
import io.github.jzdayz.util.GeneratorCommon;
import io.github.jzdayz.db.entity.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.BindException;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@org.springframework.stereotype.Controller
@RequestMapping("mbp")
@AllArgsConstructor
@Slf4j
public class Controller {

    private GeneratorCommon generatorCommon;

    private Validator validator;

    @ResponseBody
    @GetMapping("init-info")
    public Result<?> initInfo() {
        return Result.success(generatorCommon.list());
    }

    @GetMapping("generator")
    public Object generator(@RequestParam Map<String, Object> map) throws BindException{
        CodeGeneratorEntity entity = new CodeGeneratorEntity();
        BeanCopier.create(map, entity, CopyOptions.create()).copy();
        BindException bindException = new BindException(entity, "args");
        validator.validate(entity, bindException);
        if (bindException.hasErrors()){
            throw bindException;
        }
        log.info("{}", entity);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"mbg.zip\"");
        return new HttpEntity<>(generatorCommon.generator("mbp", entity), httpHeaders);
    }

}
