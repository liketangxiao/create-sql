package ${package};

<#list imports as import>
import ${import};
</#list>

/**
* ${comment}数据访问对象接口
*
* @author ${author}
* @date ${date}
*/
@Mapper
public interface ${simpleName} {

<#list fields as field>
<#if field.id>
    /**
    * 新增${comment}
    *
    * @param ${entityName?uncap_first} ${comment}
    */
    int insert${entityName}(@Param("${entityName?uncap_first}") ${entityName} ${entityName?uncap_first});

    /**
    * 修改${comment}
    *
    * @param ${entityName?uncap_first} ${comment}
    */
    int update${entityName}(@Param("${entityName?uncap_first}") ${entityName} ${entityName?uncap_first});

    /**
    * 批量插入${comment}
    *
    * @param ${entityName?uncap_first}S ${comment}列表
    * @return int
    */
    int batchInsert${entityName}(@Param("${entityName?uncap_first}S") List<${entityName}> ${entityName?uncap_first}S);

    /**
    * 批量更新${comment}
    *
    * @param ${entityName?uncap_first}S ${comment}列表
    * @return int
    */
    int batchUpdate${entityName}(@Param("${entityName?uncap_first}S") List<${entityName}> ${entityName?uncap_first}S);

    /**
    * 通过ID删除单个${comment}
    *
    * @param ${idName} ID
    */
    int deleteBy${idName?cap_first}(@Param("${idName}") ${field.classSimpleName} ${idName});

    /**
    * 通过ID查询单个${comment}
    *
    * @param ${idName} ID
    * @return {@link ${voName}}
    */
    ${voName} findBy${idName?cap_first}(@Param("${idName}") ${field.classSimpleName} ${idName});

    /**
    * 通过条件查询${comment}
    *
    * @param ${voName?uncap_first} ${comment}
    * @return {@link List<${voName}>}
    */
    List<${voName}> findBy${voName}(@Param("${voName?uncap_first}") ${voName} ${voName?uncap_first});

    /**
    * 查询所有${comment}
    *
    * @return {@link List<${voName}>}
    */
    List<${voName}> findAll${voName}();
    </#if>
</#list>

}