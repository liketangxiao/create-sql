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
    @RequestMapping(method = RequestMethod.POST, value = "/save${name?cap_first}")
    public ${return}<#noparse><</#noparse>${voName}> save${voName}(@RequestBody ${voName} ${voName?uncap_first}) {
        int i = ${serviceName?uncap_first}.insert${voName}(${voName?uncap_first});
        return new ${return}(${voName?uncap_first});
    }

    /**
    * 修改${comment}
    *
    * @param ${voName?uncap_first} ${comment}
    */
    @RequestMapping(method = RequestMethod.POST, value = "/update${name?cap_first}")
    public ${return}<#noparse><</#noparse>${voName}> update${voName}(@RequestBody ${voName} ${voName?uncap_first}) {
        int i = ${serviceName?uncap_first}.update${voName}(${voName?uncap_first});
        return new ${return}(${voName?uncap_first});
    }

    /**
    * 批量插入${comment}
    *
    * @param ${voName?uncap_first}S ${comment}列表
    */
    @RequestMapping(method = RequestMethod.POST, value = "/batchInsert${name?cap_first}")
    public ${return}<#noparse><</#noparse>List<#noparse><</#noparse>${voName}>> batchInsert${voName}(List<${voName}> ${voName?uncap_first}S){
        int i = ${serviceName?uncap_first}.batchInsert${voName?cap_first}(${voName?uncap_first}S);
        return new ${return}(${voName?uncap_first}S);
    }

    /**
    * 批量更新${comment}
    *
    * @param ${voName?uncap_first}S ${comment}列表
    */
    @RequestMapping(method = RequestMethod.POST, value = "/batchUpdate${name?cap_first}")
    public ${return}<#noparse><</#noparse>List<#noparse><</#noparse>${voName}>> batchUpdate${voName}(List<${voName}> ${voName?uncap_first}S){
        int i = ${serviceName?uncap_first}.batchUpdate${voName?cap_first}(${voName?uncap_first}S);
        return new ${return}(${voName?uncap_first}S);
    }

    /**
    * 通过ID删除单个${comment}
    *
    * @param ${idName} ID
    */
    @RequestMapping(method = RequestMethod.POST, value = "/deleteBy${idName?cap_first}")
    public ${return}<#noparse><</#noparse>${voName}> deleteBy${idName?cap_first}(${idClass} ${idName}) {
        int i = ${serviceName?uncap_first}.deleteBy${idName?cap_first}(${idName});
        return new ${return}(${idName});
    }

    /**
    * 通过ID查询单个${comment}
    *
    * @param ${idName} ID
    */
    @RequestMapping(method = RequestMethod.POST, value = "/findBy${idName?cap_first}")
    public ${return}<#noparse><</#noparse>${voName}> findBy${idName?cap_first}(${idClass} ${idName}) {
        ${voName} ${voName?uncap_first} = ${serviceName?uncap_first}.findBy${idName?cap_first}(${idName});
        return new ${return}(${voName?uncap_first});
    }

    /**
    * 通过条件查询${comment}
    *
    * @param ${voName?uncap_first} ${comment}
    */
    @RequestMapping(method = RequestMethod.POST, value = "/findBy${name?cap_first}")
    public ${return}<#noparse><</#noparse>List<#noparse><</#noparse>${voName}>> findBy${voName}(@RequestBody ${voName} ${voName?uncap_first}) {
        List<${voName}> ${voName?uncap_first}S = ${serviceName?uncap_first}.findBy${voName}(${voName?uncap_first});
        return new ${return}(${voName?uncap_first}S);
    }

    /**
    * 查询所有${comment}
    *
    * @return {@link List<${voName}>}
    */
    @RequestMapping(method = RequestMethod.GET, value = "/findAll${name?cap_first}")
    public ${return}<#noparse><</#noparse>List<#noparse><</#noparse>${voName}>> findAll${voName}() {
        List<${voName}> ${voName?uncap_first}S = ${serviceName?uncap_first}.findAll${voName}();
        return new ${return}(${voName?uncap_first}S);
    }

}