package com.geominfo.mlsql.domain.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @title: TransferDataSource
 * @desc: 数据源接收类
 * @date 2021/6/7 16:08
 */
@Data
@ApiModel(value = "TransferDataSource", description = "TransferDataSource实体")
public class TransferDataSource {

    /**
     * 显示名称
     */
    @ApiModelProperty(value = "显示名称", name = "name", required = true)
    private String name;

    /**
     * 数据库地址
     */
    @ApiModelProperty(value = "数据库地址", name = "address", required = true)
    private String address;

    /**
     * 端口号
     */
    @ApiModelProperty(value = "端口号", name = "port", required = true)
    private String port;

    /**
     * 连接名称
     */
    @ApiModelProperty(value = "连接名称", name = "connectionName", required = true)
    private String connectionName;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", name = "username", required = true)
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", name = "password", required = true)
    private String password;

    /**
     * 数据库名称(ORACLE,MYSQL,POSTGRESQL,SQLSERVER)
     */
    @ApiModelProperty(value = "数据库类型", name = "databaseName", required = true)
    private String databaseName;

    /**
     * Schema
     */
    @ApiModelProperty(value = "schema", name = "schema", required = true)
    private String schema;

    /**
     * 驱动类
     */
    @ApiModelProperty(value = "驱动类", name = "driver")
    private String driver;

    @ApiModelProperty(value = "表所有者", name = "owner")
    private String tableOwner;

    @ApiModelProperty(value = "是否为同义词", name = "isSynonyms")
    private boolean isSynonyms;

    /**
     * 地址
     */
    @ApiModelProperty(value = "地址", name = "url")
    private String url;



    /**
     * 重写toString(),保存数据源对应的信息
     */
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();

        str.append("{\"name\":\""+this.name+"\",");
        str.append("\"username\":\""+this.username+"\",");
        str.append("\"password\":\""+this.password+"\",");
        str.append("\"address\":\""+this.address+"\",");
        str.append("\"schema\":\""+this.schema+"\",");
        str.append("\"url\":\""+this.url+"\",");
        str.append("\"port\":\""+this.port+"\",");
        str.append("\"connectionName\":\""+this.connectionName+"\",");
        str.append("\"driver\":\""+this.driver+"\",");

        str.append("\"databaseName\":\""+this.databaseName+"\",");
        str.append("\"tableOwner\":\""+this.tableOwner+"\",");
        str.append("\"isSynonyms\":\""+this.isSynonyms+"\"}");

        return str.toString();
    }







    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if(o instanceof TransferDataSource){
            TransferDataSource that = (TransferDataSource) o;

            if (address != null ? !address.equals(that.address) : that.address != null) return false;
            if (port != null ? !port.equals(that.port) : that.port != null) return false;
            if (connectionName != null ? !connectionName.equals(that.connectionName) : that.connectionName != null)
                return false;
            if (username != null ? !username.equals(that.username) : that.username != null) return false;
            if (password != null ? !password.equals(that.password) : that.password != null) return false;
            if (databaseName != null ? !databaseName.equals(that.databaseName) : that.databaseName != null) return false;
            if (schema != null ? !schema.equals(that.schema) : that.schema != null) return false;
            if (driver != null ? !driver.equals(that.driver) : that.driver != null) return false;
            return url != null ? url.equals(that.url) : that.url == null;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (port != null ? port.hashCode() : 0);
        result = 31 * result + (connectionName != null ? connectionName.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (databaseName != null ? databaseName.hashCode() : 0);
        result = 31 * result + (schema != null ? schema.hashCode() : 0);
        result = 31 * result + (driver != null ? driver.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
