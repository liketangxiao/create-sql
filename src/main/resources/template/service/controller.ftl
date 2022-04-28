package ${package};

<#list imports as import>
import ${import};
</#list>

/**
* ${comment}控制层类
*
* @author ${author}
* @date ${date}
*/
@RestController
@RequestMapping("/${moduleName}/${name?lower_case}")
public class ${simpleName} {

    @Autowired
    ${iserviceName} ${serviceName?uncap_first};

    /**
    * 新增${comment}
    *
    * @param ${voName?uncap_first} ${comment}
    */
    @RequestMapping(method = RequestMethod.POST, value = "/save${name}")
    public Object save${voName}(@RequestBody ${voName} ${voName?uncap_first}) {
        return ${serviceName?uncap_first}.insert${voName}(${voName?uncap_first});
    }

    /**
    * 修改${comment}
    *
    * @param ${voName?uncap_first} ${comment}
    */
    @RequestMapping(method = RequestMethod.POST, value = "/update${name}")
    public Object update${voName}(@RequestBody ${voName} ${voName?uncap_first}) {
        return ${serviceName?uncap_first}.update${voName}(${voName?uncap_first});
    }

    /**
    * 通过ID删除单个${comment}
    *
    * @param ${idName} ID
    */
    @RequestMapping(method = RequestMethod.POST, value = "/deleteBy${idName?cap_first}")
    public Object deleteBy${idName?cap_first}(${idClass} ${idName}) {
        return ${serviceName?uncap_first}.deleteBy${idName?cap_first}(${idName});
    }

    /**
    * 通过ID查询单个${comment}
    *
    * @param ${idName} ID
    * @return {@link ${voName}}
    */
    @RequestMapping(method = RequestMethod.POST, value = "/findBy${idName?cap_first}")
    public Object findBy${idName?cap_first}(${idClass} ${idName}) {
        return ${serviceName?uncap_first}.findBy${idName?cap_first}(${idName});
    }

    /**
    * 通过条件查询${comment}
    *
    * @param ${voName?uncap_first} ${comment}
    * @return {@link List<${voName}>}
    */
    @RequestMapping(method = RequestMethod.POST, value = "/findBy${name}")
    public Object findBy${voName}(@RequestBody ${voName} ${voName?uncap_first}) {
        return ${serviceName?uncap_first}.findBy${voName}(${voName?uncap_first});
    }

    /**
    * 查询所有${comment}
    *
    * @return {@link List<${voName}>}
    */
    @RequestMapping(method = RequestMethod.GET, value = "/findAll${name}")
    public Object findAll${voName}() {
        return ${serviceName?uncap_first}.findAll${voName}();
    }

}