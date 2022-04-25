<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${mapperNamePath}">

    <resultMap id="BaseResultMap" type="${entityNamePath}">
<#list fields as field>
    <#if field.id>
        <id column="${field.columnName}" jdbcType="${field.jdbcType}" property="${field.name}"${field.javaType}/>
    <#else>
        <result column="${field.columnName}" jdbcType="${field.jdbcType}" property="${field.name}"${field.javaType}/>
    </#if>
</#list>
    </resultMap>

    <sql id="base_column_list">
    <#list fields as field>
        ${field.columnName}<#if field_index!=fields?size-1>,</#if>
    </#list>
    </sql>

    <sql id="table_name">
        ${tableName}
    </sql>

    <!--新增${comment}-->
    <insert id="insert${entityName}">
        INSERT INTO
        <include refid="table_name"/>
        (<include refid="base_column_list"/>)
        VALUES
        (
        <#list fields as field>
        <#noparse>#{</#noparse>${field.name}, jdbcType=${field.jdbcType}}<#if field_index!=(fields?size-1)>,</#if>
        </#list>
        )
    </insert>

    <!--修改${comment}-->
    <update id="update${entityName}">
        UPDATE
        <include refid="table_name"/>
        SET
        <#list fields as field><#if !field.id>
        ${field.columnName} = <#noparse>#{</#noparse>${field.name}, jdbcType=${field.jdbcType}}<#if field_index!=(fields?size-1)>,</#if></#if>
        </#list>
        WHERE <#list fields as field><#if field.id>${field.columnName} = <#noparse>#{</#noparse>${field.name}, jdbcType=${field.jdbcType}}</#if></#list>
    </update>

    <!--通过ID删除单个${comment}-->
    <delete id="deleteBy${idName?cap_first}">
        DELETE FROM
        <include refid="table_name"/>
        WHERE <#list fields as field><#if field.id>${field.columnName}</#if></#list> = <#noparse>#{</#noparse>${idName}, jdbcType=${idType}}
    </delete>

    <!--通过ID查询单个${comment}-->
    <select id="findBy${idName?cap_first}" resultMap="BaseResultMap">
        SELECT
        <include refid="base_column_list"/>
        FROM
        <include refid="table_name"/>
        WHERE <#list fields as field><#if field.id>${field.columnName}</#if></#list> = <#noparse>#{</#noparse>${idName}, jdbcType=${idType}}
    </select>


    <!--通过条件查询${comment}-->
    <select id="findBy${voName?cap_first}" resultMap="BaseResultMap">
        SELECT
        <include refid="base_column_list"/>
        FROM
        <include refid="table_name"/>
        <where>
<#list fields as field>
    <#switch field.classSimpleName>
        <#case "String">
            <if test="${field.name} != null and ${field.name} !=''">
                AND ${field.columnName} = <#noparse>#{</#noparse>${field.name}, jdbcType=${field.jdbcType}}
            </if>
            <#break>
        <#default>
            <if test="${field.name} != null">
                AND ${field.columnName} = <#noparse>#{</#noparse>${field.name}, jdbcType=${field.jdbcType}}
            </if>
    </#switch>
</#list>
        </where>
    </select>


    <!--查询所有${comment}-->
    <select id="findAll${voName}" resultMap="BaseResultMap">
        SELECT
        <include refid="base_column_list"/>
        FROM
        <include refid="table_name"/>
    </select>

</mapper>