package io.github.jzdayz;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.system.SystemUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.Entity;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import io.github.jzdayz.ex.TableNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Objects;

@Service
@Slf4j
public class MbpGenerator {

    private static final String TEMP_PATH = System.getProperty(SystemUtil.TMPDIR) + "code-generator" + File.separator;

    private static Field NAME_CONVERT;
    private static Field TABLE_FIELD_CONVERT;

    static {
        try {
            NAME_CONVERT = Entity.class.getDeclaredField("nameConvert");
            NAME_CONVERT.setAccessible(true);
            TABLE_FIELD_CONVERT = com.baomidou.mybatisplus.generator.config.po.TableField.class.getDeclaredField("convert");
            TABLE_FIELD_CONVERT.setAccessible(true);
        } catch (NoSuchFieldException e) {
            log.error("field", e);
            System.exit(0);
        }
    }

    public FileSystemResource generator(CodeGeneratorEntity codeGeneratorEntity) {
        String path = TEMP_PATH + IdUtil.fastSimpleUUID();
        FastAutoGenerator.create(codeGeneratorEntity.getJdbc(), codeGeneratorEntity.getUserName(), codeGeneratorEntity.getPwd())
                // 全局配置
                .globalConfig((scanner, builder) -> {
                    builder
                            .author(SystemUtil.get(SystemUtil.USER_NAME))
                            .outputDir(path)
                            .disableOpenDir();
                    if (codeGeneratorEntity.getSwagger()) {
                        builder.enableSwagger();
                    }
                })
                // 包配置
                .packageConfig((scanner, builder) ->
                        builder
                                .parent(codeGeneratorEntity.getMbpPackage())
                                .pathInfo(Collections.singletonMap(OutputFile.xml, path + File.separator + codeGeneratorEntity.getMbpPackage().replace(".", "/") + File.separator + "xml"))
                )
                .dataSourceConfig(builder -> {
                    if (StrUtil.isNotBlank(codeGeneratorEntity.getSchema())) {
                        builder.schema(codeGeneratorEntity.getSchema());
                    }
                })
                // 策略配置
                .strategyConfig(
                        (scanner, builder) -> {
                            builder
                                    .controllerBuilder()
                                    .enableRestStyle()
                                    .enableHyphenStyle()
                                    .mapperBuilder()
                                    .enableFileOverride()
                                    .enableBaseResultMap()
                                    .entityBuilder()
                                    .enableFileOverride()
                                    .naming(NamingStrategy.underline_to_camel)
                                    .enableTableFieldAnnotation();
                            if (codeGeneratorEntity.getLombok()) {
                                builder.entityBuilder().enableLombok();
                            }
                            if (Objects.equals("SQL", codeGeneratorEntity.getTfType())) {
                                builder.likeTable(new LikeTable(codeGeneratorEntity.getTableString()));
                            } else {
                                builder.disableSqlFilter();
                                builder.addInclude(codeGeneratorEntity.getTableString());
                            }
                            StrategyConfig strategyConfig = builder.entityBuilder().build();
                            tableNameHandler(strategyConfig, codeGeneratorEntity);
                        }

                )
                .templateConfig(builder -> builder.disable(TemplateType.CONTROLLER))
                .execute();
        if (!FileUtil.exist(path)) {
            throw new TableNotFoundException(path);
        }
        log.info("生成目录:{}", path);
        File zip = ZipUtil.zip(path);
        log.info("zip目录:{}", zip);
        return new FileSystemResource(zip);
    }

    private static void tableNameHandler(StrategyConfig strategyConfig, CodeGeneratorEntity cg) {
        INameConvert nameConvert = strategyConfig.entity().getNameConvert();
        try {
            NAME_CONVERT.set(strategyConfig.entity(), new INameConvert() {
                @Override
                public String entityNameConvert(TableInfo tableInfo) {
                    String name = nameConvert.entityNameConvert(tableInfo);
                    String tableNameFormat = cg.getTableNameFormat();
                    // 处理下字段的convert
                    tableInfo.getFields().forEach(tableField -> {
                        try {
                            TABLE_FIELD_CONVERT.set(tableField, true);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    if (StrUtil.isBlank(tableNameFormat)) {
                        return name;
                    }
                    return tableNameFormat.replace("${entity}", name);
                }

                @Override
                public String propertyNameConvert(com.baomidou.mybatisplus.generator.config.po.TableField field) {
                    return nameConvert.propertyNameConvert(field);
                }
            });
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}