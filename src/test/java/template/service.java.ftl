package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};

/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${.now?string["yyyy-MM-dd HH:mm:ss"]}
 */
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

<#list table.fields as field>
	<#if "uuid" == field.name || "UUID" == field.name>
	/**
	 * 根据id找出数据，若uuid不相等，则返回null
	 *
	 * @param id
	 * @param uuid
	 * @return
	 */
	${entity} getByIdAndUUID(Integer id, String uuid);
		<#break>
	</#if>
</#list>

}
