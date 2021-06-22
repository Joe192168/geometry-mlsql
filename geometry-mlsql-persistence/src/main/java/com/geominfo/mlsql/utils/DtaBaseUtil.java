package com.geominfo.mlsql.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import com.alibaba.fastjson.JSONArray;
import com.geominfo.mlsql.domain.dto.DataBaseDTO;

/**
   *
   * 数据库交换-工具类
   */
  public class DtaBaseUtil {

      /** 
       * 数据库类型,枚举 
       *  
       */  
      public static enum DATABASETYPE {  
          ORACLE, MYSQL, SQLSERVER, SQLSERVER2005, DB2, INFORMIX, SYBASE, OTHER, EMPTY  ,POSTGRESQL
      }  
    
      /** 
       * 根据字符串,判断数据库类型 
       *  
       * @param databasetype 
       * @return 
       */  
      public static DATABASETYPE parseDATABASETYPE(String databasetype) {  
          // 空类型  
          if (null == databasetype || databasetype.trim().length() < 1) {  
              return DATABASETYPE.EMPTY;
          }  
          // 截断首尾空格,转换为大写  
          databasetype = databasetype.trim().toUpperCase();  
          // Oracle数据库
          if (databasetype.contains("ORACLE")) {  
              //
              return DATABASETYPE.ORACLE;
          }  
          // MYSQL 数据库  
          if (databasetype.contains("MYSQL")) {  
              //  
              return DATABASETYPE.MYSQL;  
          }  
          // SQL SERVER 数据库  
          if (databasetype.contains("SQL") && databasetype.contains("SERVER")) {  
              //  
              if (databasetype.contains("2005") || databasetype.contains("2008") || databasetype.contains("2012")) {  
    
                  try {  
                      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
                  } catch (ClassNotFoundException e) {  
                      e.printStackTrace();  
                  }  
                    
                  return DATABASETYPE.SQLSERVER2005;  
              } else {  
                  try {  
                      // 注册 JTDS  
                      Class.forName("net.sourceforge.jtds.jdbc.Driver");  
                  } catch (ClassNotFoundException e) {  
                      e.printStackTrace();  
                  }  
                  return DATABASETYPE.SQLSERVER;  
              }  
          }  
          // 下面的这几个没有经过实践测试, 判断可能不准确  
          // DB2 数据库  
          if (databasetype.contains("DB2")) {  
              //  
              return DATABASETYPE.DB2;  
          }  
          // INFORMIX 数据库  
          if (databasetype.contains("INFORMIX")) {  
              //  
              return DATABASETYPE.INFORMIX;  
          }  
          // SYBASE 数据库  
          if (databasetype.contains("SYBASE")) {  
              //  
              return DATABASETYPE.SYBASE;  
          }

          if (databasetype.contains("POSTGRESQL")) {
              //
              return DATABASETYPE.POSTGRESQL;
          }



          // 默认,返回其他  
          return DATABASETYPE.OTHER;  
      }

    /**
     * 列出数据库的所有表
     * @param dataBaseDTO
     * @return
     */
    public static List<Map<String, Object>> listTables(DataBaseDTO dataBaseDTO) {
          // 去除首尾空格  
          String dataBaseType = trim(dataBaseDTO.getDataBaseType());
          String ip = trim(dataBaseDTO.getIp());
          String port = trim(dataBaseDTO.getPort());
          String dbname = trim(dataBaseDTO.getDbname());
          String username = trim(dataBaseDTO.getUsername());
          String password = trim(dataBaseDTO.getPassword());
          String tableName = trim(dataBaseDTO.getTableName());
          //  
          DATABASETYPE dbtype = parseDATABASETYPE(dataBaseType);
          //  
          List<Map<String, Object>> result = null;  
          String url = concatDBURL(dbtype, ip, port, dbname);  
          Connection conn = getConnection(url, username, password);  
          // Statement stmt = null;  
          ResultSet rs = null;  
          //  
          try {  
              conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
              // 获取Meta信息对象  
              DatabaseMetaData meta = conn.getMetaData();  
              // 数据库  
              String catalog = null;  
              // 数据库的用户  
              String schemaPattern = null;// meta.getUserName();  
              // 表名  
              String tableNamePattern = null;//  
              // types指的是table、view  
              String[] types = { "TABLE" };  
              // Oracle  
              if (DATABASETYPE.ORACLE.equals(dbtype)) {  
                  schemaPattern = username;  
                  if (null != schemaPattern) {  
                      schemaPattern = schemaPattern.toUpperCase();  
                  }  
                  // 查询  
                  rs = meta.getTables(catalog, schemaPattern, tableNamePattern, types);  
              } else if (DATABASETYPE.MYSQL.equals(dbtype)) {  
                  // Mysql查询  
                  // MySQL 的 table 这一级别查询不到备注信息  
                  schemaPattern = dbname;  
                  rs = meta.getTables(catalog, schemaPattern, tableNamePattern, types);  
              }  else if (DATABASETYPE.SQLSERVER.equals(dbtype) || DATABASETYPE.SQLSERVER2005.equals(dbtype)) {  
                  // SqlServer  
                  tableNamePattern = "%";  
                  rs = meta.getTables(catalog, schemaPattern, tableNamePattern, types);  
              }  else if (DATABASETYPE.DB2.equals(dbtype)) {  
                  // DB2查询  
                  schemaPattern = "jence_user";  
                  tableNamePattern = "%";  
                  rs = meta.getTables(catalog, schemaPattern, tableNamePattern, types);  
              } else if (DATABASETYPE.INFORMIX.equals(dbtype)) {  
                  // SqlServer  
                  tableNamePattern = "%";  
                  rs = meta.getTables(catalog, schemaPattern, tableNamePattern, types);  
              } else if (DATABASETYPE.SYBASE.equals(dbtype)) {  
                  // SqlServer  
                  tableNamePattern = "%";  
                  rs = meta.getTables(catalog, schemaPattern, tableNamePattern, types);  
              } else if (DATABASETYPE.POSTGRESQL.equals(dbtype)) {
                  // POSTGRESQL
                  tableNamePattern = "%";
                  rs = meta.getTables(catalog, schemaPattern, tableNamePattern, types);
              } else {
                  throw new RuntimeException("不认识的数据库类型!");  
              }  
              //  
              result = parseResultSetToMapList(rs);  
    
          } catch (Exception e) {  
              e.printStackTrace();  
          } finally {  
              close(rs);  
              close(conn);  
          }  
          //  
          return result;  
      }

    /**
     * 列出表的所有字段
     * @param dataBaseDTO
     * @return
     */
    public static List<Map<String, Object>> listColumns(DataBaseDTO dataBaseDTO) {
          // 去除首尾空格  
          String dataBaseType = trim(dataBaseDTO.getDataBaseType());
          String ip = trim(dataBaseDTO.getIp());
          String port = trim(dataBaseDTO.getPort());
          String dbname = trim(dataBaseDTO.getDbname());
          String username = trim(dataBaseDTO.getUsername());
          String password = trim(dataBaseDTO.getPassword());
          String tableName = trim(dataBaseDTO.getTableName());
          //  
          DATABASETYPE dbtype = parseDATABASETYPE(dataBaseType);
          //  
          List<Map<String, Object>> result = null;  
          String url = concatDBURL(dbtype, ip, port, dbname);  
          Connection conn = getConnection(url, username, password);  
          ResultSet rs = null;
          //  
          try {  
              // 获取Meta信息对象  
              DatabaseMetaData meta = conn.getMetaData();  
              // 数据库  
              String catalog = null;  
              // 数据库的用户  
              String schemaPattern = null;
              // 表名  
              String tableNamePattern = tableName;
              // 转换为大写  
              //
              String columnNamePattern = null;
              // Oracle
              if (DATABASETYPE.ORACLE.equals(dbtype)) {
                  if (null != tableNamePattern) {
                      tableNamePattern = tableNamePattern.toUpperCase();
                  }
                  // 查询
                  schemaPattern = username;  
                  if (null != schemaPattern) {  
                      schemaPattern = schemaPattern.toUpperCase();  
                  }  
              } else if (DATABASETYPE.POSTGRESQL.equals(dbtype)){
                  //其它数据库根据实际情况处理

              }  
    
              rs = meta.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);  
              // TODO 获取主键列,但还没使用  
              meta.getPrimaryKeys(catalog, schemaPattern, tableNamePattern);  
              //  
              result = parseResultSetToMapList(rs);  
          } catch (Exception e) {  
              e.printStackTrace();  
          } finally {  
              // 关闭资源  
              close(rs);  
              close(conn);  
          }  
          //  
          return result;  
      }  
    
      /** 
       * 根据IP,端口,以及数据库名字,拼接Oracle连接字符串 
       *  
       * @param ip 
       * @param port 
       * @param dbname 
       * @return 
       */  
      public static String concatDBURL(DATABASETYPE dbtype, String ip, String port, String dbname) {  
          //  
          String url = "";  
          // Oracle数据库  
          if (DATABASETYPE.ORACLE.equals(dbtype)) {  
              //  
              url += "jdbc:oracle:thin:@";
              url += ip.trim();  
              url += ":" + port.trim();  
              url += ":" + dbname;
          } else if (DATABASETYPE.MYSQL.equals(dbtype)) {  
              //  
              url += "jdbc:mysql://";  
              url += ip.trim();  
              url += ":" + port.trim();  
              url += "/" + dbname;  
          } else if (DATABASETYPE.SQLSERVER.equals(dbtype)) {  
              //  
              url += "jdbc:jtds:sqlserver://";  
              url += ip.trim();  
              url += ":" + port.trim();  
              url += "/" + dbname;  
              url += ";tds=8.0;lastupdatecount=true";  
          } else if (DATABASETYPE.SQLSERVER2005.equals(dbtype)) {  
              //  
              url += "jdbc:sqlserver://";  
              url += ip.trim();  
              url += ":" + port.trim();  
              url += "; DatabaseName=" + dbname;  
          } else if (DATABASETYPE.DB2.equals(dbtype)) {  
              url += "jdbc:db2://";  
              url += ip.trim();
              url += ":" + port.trim();  
              url += "/" + dbname;  
          } else if (DATABASETYPE.INFORMIX.equals(dbtype)) {  
              // Infox mix 可能有BUG  
              url += "jdbc:informix-sqli://";  
              url += ip.trim();
              url += ":" + port.trim();  
              url += "/" + dbname;
          } else if (DATABASETYPE.SYBASE.equals(dbtype)) {
              url += "jdbc:sybase:Tds:";  
              url += ip.trim();  
              url += ":" + port.trim();  
              url += "/" + dbname;  
          } else if (DATABASETYPE.POSTGRESQL.equals(dbtype)) {
              url += "jdbc:postgresql://";
              url += ip.trim();
              url += ":" + port.trim();
              url += "/" + dbname;
          }else {
              throw new RuntimeException("不认识的数据库类型!");  
          }  
          //  
          return url;  
      }  
    
      /** 
       * 获取JDBC连接 
       *  
       * @param url 
       * @param username 
       * @param password 
       * @return 
       */  
      public static Connection getConnection(String url, String username, String password) {  
          Connection conn = null;  
          try {  
              // 不需要加载Driver. Servlet 2.4规范开始容器会自动载入  
              // conn = DriverManager.getConnection(url, username, password);  
              //  
              Properties info =new Properties();  
              //  
              info.put("user", username);  
              info.put("password", password);  
              // !!! Oracle 如果想要获取元数据 REMARKS 信息,需要加此参数  
              info.put("remarksReporting","true");  
              // !!! MySQL 标志位, 获取TABLE元数据 REMARKS 信息  
              info.put("useInformationSchema","true");
              conn = DriverManager.getConnection(url, info);  
          } catch (SQLException e) {
              e.printStackTrace();
          }  
          return conn;
      }  
    
      /** 
       * 将一个未处理的ResultSet解析为Map列表. 
       *  
       * @param rs 
       * @return 
       */  
      public static List<Map<String, Object>> parseResultSetToMapList(ResultSet rs) {  
          //  
          List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();  
          //  
          if (null == rs) {  
              return null;  
          }  
          //  
          try {  
              while (rs.next()) {  
                  //   
                  Map<String, Object> map = parseResultSetToMap(rs);  
                  //  
                  if (null != map) {  
                      result.add(map);  
                  }  
              }  
          } catch (SQLException e) {  
              e.printStackTrace();  
          }  
          //  
          return result;  
      }  
    
      /** 
       * 解析ResultSet的单条记录,不进行 ResultSet 的next移动处理 
       *  
       * @param rs 
       * @return 
       */  
      private static Map<String, Object> parseResultSetToMap(ResultSet rs) {  
          //  
          if (null == rs) {  
              return null;  
          }  
          //  
          Map<String, Object> map = new HashMap<String, Object>();  
          //  
          try {  
              ResultSetMetaData meta = rs.getMetaData();  
              //  
              int colNum = meta.getColumnCount();  
              //  
              for (int i = 1; i <= colNum; i++) {  
                  // 列名  
                  String name = meta.getColumnLabel(i); // i+1  
                  Object value = rs.getObject(i);
                  //对data_type进行转换java类型
                  if ("DATA_TYPE".equals(name)){
                      value = getDatatype(Integer.parseInt(rs.getObject(i).toString()));
                  }
                  // 加入属性  
                  map.put(name, value);  
              }  
          } catch (SQLException e) {  
              e.printStackTrace();  
          }  
          //  
          return map;  
      }

    /**
     * 测试连接
     * @param dataBaseDTO
     * @return
     */
      public static boolean tryLink(DataBaseDTO dataBaseDTO) {
          //  
          DATABASETYPE dbtype = parseDATABASETYPE(dataBaseDTO.getDataBaseType());
          String url = concatDBURL(dbtype, dataBaseDTO.getIp(), dataBaseDTO.getPort(), dataBaseDTO.getDbname());
          Connection conn = null;  
          //  
          try {  
              conn = getConnection(url, dataBaseDTO.getUsername(), dataBaseDTO.getPassword());
              if(null == conn){  
                  return false;  
              }  
              DatabaseMetaData meta =  conn.getMetaData();  
              //  
              if(null == meta){  
                  return false;  
              } else {  
                  // 只有这里返回true  
                  return true;  
              }  
          } catch (Exception e) {  
              e.printStackTrace();  
          } finally{  
              close(conn);  
          }  
          //  
          return false;  
      }

    /**
     * 关闭conn
     * @param conn
     */
    public static void close(Connection conn) {
          if(conn!=null) {  
              try {  
                  conn.close();  
                  conn = null;  
              } catch (SQLException e) {  
                  e.printStackTrace();  
              }  
          }  
      }

    /**
     * 关闭stmt
     * @param stmt
     */
    public static void close(Statement stmt) {
          if(stmt!=null) {  
              try {  
                  stmt.close();  
                  stmt = null;  
              } catch (SQLException e) {  
                  e.printStackTrace();  
              }  
          }  
      }

    /**
     * 关闭rs
     * @param rs
     */
    public static void close(ResultSet rs) {
          if(rs!=null) {  
              try {  
                  rs.close();  
                  rs = null;  
              } catch (SQLException e) {  
                  e.printStackTrace();  
              }  
          }  
      }

    /**
     * 去除空格
     * @param str
     * @return
     */
    public static String trim(String str){
          if(null != str){  
              str = str.trim();  
          }  
          return str;  
    }

    /**
     * 数据库和java类型转换
     * @param javaType
     * @return
     */
    public static String getDatatype(int javaType) {
        switch (javaType) {
            case Types.TINYINT:
                return "Int";
            case Types.SMALLINT:
                return "Int";
            case Types.INTEGER:
                return "Int";
            case Types.BIGINT:
                return "Long";
            case Types.FLOAT:
                return "Double";
            case Types.REAL:
                return "Float";
            case Types.DOUBLE:
                return "Double";
            case Types.NUMERIC:
                return "BigDecimal";
            case Types.DECIMAL:
                return "BigDecimal";
            case Types.BOOLEAN:
                return "Boolean";
            case Types.DATE:
                return "Date";
            case Types.TIME:
                return "Time";
            case Types.TIMESTAMP:
                return "Timestamp";
            case Types.CHAR:
                return "String";
            case Types.VARCHAR:
                return "String";
            default:
                return "String";
        }
    }

    //测试
    public static void testOracle() {
        DataBaseDTO baseDTO = new DataBaseDTO();
        baseDTO.setDataBaseType("Oracle");
        baseDTO.setIp("192.168.0.223");
        baseDTO.setPort("1521");
        baseDTO.setDbname("edw");
        baseDTO.setUsername("inmon");
        baseDTO.setPassword("jhsz0603");
        baseDTO.setTableName("t_users");

        List<Map<String, Object>> tables = listTables(baseDTO);
        List<Map<String, Object>> columns = listColumns(baseDTO);
        //
        tables = MapUtil.convertKeyList2LowerCase(tables);
        columns = MapUtil.convertKeyList2LowerCase(columns);
        //
        String jsonT = JSONArray.toJSONString(tables, true);
        System.out.println(jsonT);
        System.out.println("tables.size()=" + tables.size());
        //
        System.out.println("-----------------------------------------" + "-----------------------------------------");
        System.out.println("-----------------------------------------" + "-----------------------------------------");
        //
        String jsonC = JSONArray.toJSONString(columns, true);
        System.out.println(jsonC);
        System.out.println("columns.size()=" + columns.size());
    }

    public static void main(String[] args) {
        testOracle();
    }

}
 