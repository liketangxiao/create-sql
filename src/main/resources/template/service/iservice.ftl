package ${package};

<#list imports as import>
import ${import};
</#list>

/**
* ${comment}服务接口类
*
* @author ${author}
* @date ${date}
*/
public interface ${simpleName} {

    /**
    * 新增${comment}
    *
    * @param ${voName?uncap_first} ${comment}
    */
    int insert${voName}(${voName} ${voName?uncap_first});

    /**
    * 修改${comment}
    *
    * @param ${voName?uncap_first} ${comment}
    */
    int update${voName}(${voName} ${voName?uncap_first});

    /**
    * 批量插入${comment}
    *
    * @param ${voName?uncap_first}S ${comment}列表
    */
    int batchInsert${voName}(List<${voName}> ${voName?uncap_first}S);

    /**
    * 批量更新${comment}
    *
    * @param ${voName?uncap_first}S ${comment}列表
    */
    int batchUpdate${voName}(List<${voName}> ${voName?uncap_first}S);

    /**
    * 通过ID删除单个${comment}
    *
    * @param ${idName} ID
    */
    int deleteBy${idName?cap_first}(${idClass} ${idName});

    /**
    * 通过ID查询单个${comment}
    *
    * @param ${idName} ID
    * @return {@link ${voName}}
    */
    ${voName} findBy${idName?cap_first}(${idClass} ${idName});

    /**
    * 通过条件查询${comment}
    *
    * @param ${voName?uncap_first} ${comment}
    * @return {@link List<${voName}>}
    */
    List<${voName}> findBy${voName}(${voName} ${voName?uncap_first});

    /**
    * 查询所有${comment}
    *
    * @return {@link List<${voName}>}
    */
    List<${voName}> findAll${voName}();

}