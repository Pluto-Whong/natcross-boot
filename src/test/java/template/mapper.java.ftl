package ${package.Mapper};

import ${package.Entity}.${entity};

<#if cfg.datasourceName??>
import com.baomidou.dynamic.datasource.annotation.DS;
</#if>
import org.apache.ibatis.annotations.Mapper;

import ${superMapperClassPackage};

/**
 * <p>
 * ${table.comment!} Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since ${.now?string["yyyy-MM-dd HH:mm:ss"]}
 */
<#if cfg.datasourceName??>
@DS("${cfg.datasourceName}")
</#if>
@Mapper
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {

}
