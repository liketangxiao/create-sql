package ${package};

<#list imports as import>
import ${import};
</#list>

/**
* ${comment}服务层类
*
* @author ${author}
* @date ${date}
*/
@Service
public class ${simpleName} implements ${iserviceName} {

    @Autowired
    private ${mapperName} ${mapperName?uncap_first};

    @Override
    public int insert${voName}(${voName} ${voName?uncap_first}) {
        return ${mapperName?uncap_first}.insert${entityName}(${voName?uncap_first});
    }

    @Override
    public int update${voName}(${voName} ${voName?uncap_first}) {
        return ${mapperName?uncap_first}.update${entityName}(${voName?uncap_first});
    }

    @Override
    public int deleteBy${idName?cap_first}(${idClass} ${idName}) {
        return ${mapperName?uncap_first}.deleteBy${idName?cap_first}(${idName});
    }

    @Override
    public ${voName} findBy${idName?cap_first}(${idClass} ${idName}) {
        return ${mapperName?uncap_first}.findBy${idName?cap_first}(${idName});
    }

    @Override
    public List<${voName}> findBy${voName}(${voName} ${voName?uncap_first}) {
        return ${mapperName?uncap_first}.findBy${voName}(${voName?uncap_first});
    }

    @Override
    public List<${voName}> findAll${voName}() {
        return ${mapperName?uncap_first}.findAll${voName}();
    }
}