<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${mapperNamePath}">

    <insert id="batchInsert${entityName}" parameterType="${entityNamePath}">
        INSERT INTO
        <include refid="table_name"/>
        (
        <include refid="base_column_list"/>
        )
        VALUES
        <foreach collection="${entityName?uncap_first}S" item="item" index="index" separator=",">
            (
        <#list fields as field>
        <#noparse>#{</#noparse>item.${field.name}, jdbcType=${field.jdbcType}}<#if field_index!=(fields?size-1)>,</#if>
        </#list>
            )
        </foreach>
    </insert>

    <update id="batchUpdate${entityName}" parameterType="${entityNamePath}">
        <foreach collection="${entityName?uncap_first}S" separator=";" item="item">
            UPDATE
            <include refid="table_name"/>
            SET
    <#list fields as field><#if !field.id>
        ${field.columnName} = <#noparse>#{</#noparse>item.${field.name}, jdbcType=${field.jdbcType}}<#if field_index!=(fields?size-1)>,</#if></#if>
    </#list>
            WHERE <#list fields as field><#if field.id>item.${field.columnName} = <#noparse>#{</#noparse>${field.name}, jdbcType=${field.jdbcType}}</#if></#list>
        </foreach>
    </update>

</mapper>