package io.github.jzdayz;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author huqingfeng
 * @since 2021-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("CODE_GENERATOR_ENTITY")
public class CodeGeneratorEntity implements Serializable {


    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @NotBlank
    private String jdbc;

    private String alias;

    private String userName;

    private String schema;

    private String pwd;

    @TableField("TABLE_STRING")
    private String tableString;

    @TableField("TF_TYPE")
    private String tfType;

    @TableField("MBP_PACKAGE")
    private String mbpPackage;

    private Boolean swagger;

    private Boolean lombok;

    @TableField("TABLE_NAME_FORMAT")
    private String tableNameFormat;


}
