package person.pluto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MpGenerator {
    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    @SuppressWarnings("resource")
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入").append(tip).append("：");
        System.out.println(help.toString());
        String ipt = scanner.nextLine();
        if (StringUtils.isNotEmpty(ipt)) {
            return ipt;
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        // 获取项目路径
        String projectPath = System.getProperty("user.dir");

        // 代码生成器
        AutoGenerator autoGenerator = new AutoGenerator();

        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(projectPath + "/src/main/java");
        globalConfig.setAuthor(System.getProperty("user.name"));

        globalConfig.setOpen(false);
        globalConfig.setBaseResultMap(false);
        globalConfig.setBaseColumnList(false);
        globalConfig.setEnableCache(false);

        autoGenerator.setGlobalConfig(globalConfig);

        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDriverName("com.mysql.jdbc.Driver");
        dataSourceConfig.setUrl("jdbc:mysql://127.0.0.1/natcross?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("root");
        autoGenerator.setDataSource(dataSourceConfig);

        // 包配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("person.pluto");
        packageConfig.setModuleName(scanner("模块名"));
        autoGenerator.setPackageInfo(packageConfig);

        String datasourceName = scanner("多源库名");
        // 自定义配置
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> newMap = new HashMap<>();
                newMap.put("datasourceName", datasourceName);
                setMap(newMap);
            }
        };
        autoGenerator.setCfg(injectionConfig);

        String xmlTemplate = "template/mapper.xml.ftl";

        // 自定义输出配置
        List<FileOutConfig> fileOutConfigList = new ArrayList<>();
        // 自定义配置会被优先输出
        fileOutConfigList.add(new FileOutConfig(xmlTemplate) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/"
//                        + (StringUtils.isBlank(packageConfig.getModuleName()) ? "" : packageConfig.getModuleName())
                        + (StringUtils.isBlank(datasourceName) ? "master" : datasourceName)
                //
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        // 指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        String entityTemplate = null;
        String mapperTemplate = null;
        String serviceTemplate = null;
        String serviceImplTemplate = null;
        String controllerTemplate = null;

        // 若只想重新生成某几个，则屏蔽掉其他的，覆盖设置为true
        globalConfig.setFileOverride(true);

        // XML
        injectionConfig.setFileOutConfigList(fileOutConfigList);
        entityTemplate = "template/entity.java";
        mapperTemplate = "template/mapper.java";
        serviceTemplate = "template/service.java";
        serviceImplTemplate = "template/serviceImpl.java";
        controllerTemplate = "template/controller.java";

        templateConfig.setEntity(entityTemplate);
        templateConfig.setMapper(mapperTemplate);
        templateConfig.setService(serviceTemplate);
        templateConfig.setServiceImpl(serviceImplTemplate);
        templateConfig.setController(controllerTemplate);

        templateConfig.setXml(null);
        autoGenerator.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setCapitalMode(false);

        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategyConfig.setSuperEntityClass("person.demo.entity");
//        strategyConfig.setSuperEntityColumns("id");
        strategyConfig.setLogicDeleteFieldName("is_delete");
        strategyConfig.setVersionFieldName("version");

        strategyConfig.setTableFillList(
                // 自动填充设置列表
                Arrays.asList(
                        // 插入创建时间
                        new TableFill("gmt_create", FieldFill.INSERT),
                        // 插入、更新时都要填充时间
                        new TableFill("gmt_modify", FieldFill.INSERT_UPDATE),
                        // 逻辑删除 封装的方法必会注入，若有特殊需求需要查出已删除的，则需要手写sql
                        new TableFill("is_delete", FieldFill.INSERT),
                        // 版本
                        new TableFill("version", FieldFill.INSERT)

                // 自动填充设置完毕
                ));

        strategyConfig.setEntityTableFieldAnnotationEnable(true);
        strategyConfig.setEntityLombokModel(true);

//        strategyConfig.setSuperControllerClass("person.demo.controller");
        strategyConfig.setRestControllerStyle(true);
        strategyConfig.setControllerMappingHyphenStyle(false);

        strategyConfig.setTablePrefix("t_");
        strategyConfig.setInclude(scanner("表名"));

        autoGenerator.setStrategy(strategyConfig);

        // 设置模板引擎
        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());

        autoGenerator.execute();
    }
}
