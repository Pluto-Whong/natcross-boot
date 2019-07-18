package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
<#if cfg.datasourceName??>
import com.baomidou.dynamic.datasource.annotation.DS;
</#if>
import org.springframework.stereotype.Service;

<#list table.fields as field>
	<#if "uuid" == field.name || "UUID" == field.name>
import org.apache.commons.lang3.StringUtils;
		<#break>
	</#if>
</#list>

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${.now?string["yyyy-MM-dd HH:mm:ss"]}
 */
 <#if cfg.datasourceName??>
@DS("${cfg.datasourceName}")
</#if>
@Service
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

<#list table.fields as field>
	<#if "uuid" == field.name || "UUID" == field.name>
	@Override
	public ${entity} getByIdAndUUID(Integer id, String uuid) {
		${entity} entity = this.getById(id);
		if (entity == null || !StringUtils.equals(entity.getUuid(), uuid)) {
			return null;
		}
		return entity;
	}
		<#break>
	</#if>
</#list>

}
