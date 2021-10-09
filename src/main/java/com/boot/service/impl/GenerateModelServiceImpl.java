package com.boot.service.impl;

import com.boot.pojo.Code;
import com.boot.service.GenerateModelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/** @author 游政杰 */
@Service
@Slf4j
public class GenerateModelServiceImpl implements GenerateModelService {


  private FileOutputStream fileOutputStream = null;
  private BufferedOutputStream bufferedOutputStream = null;
  private Connection connection = null;
  private Statement statement = null;

  @Override
  public boolean generate(Code code) {

    try {

      boolean generateModel = code.getGenerateModel();
      boolean modelSerialize = code.getModelSerialize();
      boolean modelGetterAndSetter = code.getModelGetterAndSetter();
      boolean modelConstructor = code.getModelConstructor();
      String generatePackage = code.getGeneratePackage();
      String generateModelPath = code.getGenerateModelPath();
      boolean generateDatabase = code.getGenerateDatabase();
      String databaseHost = code.getDatabaseHost();
      int databasePort = code.getDatabasePort();
      String databaseUser = code.getDatabaseUser();
      String databasePassword = code.getDatabasePassword();
      String databaseDriver = code.getDatabaseDriver();
      boolean generateTable = code.getGenerateTable();
      boolean generateMapper = code.getGenerateMapper();
      boolean generateServiceAndImpl = code.getGenerateServiceAndImpl();

      // 如果需要生成实体类
      if (generateModel) {
        log.info("正在自动生成实体类......");

        generateModel(
            code,
            modelSerialize,
            modelGetterAndSetter,
            modelConstructor,
            generatePackage,
            generateModelPath);
        log.info("生成实体类成功......");
      }

      // 如果需要生成数据库
      if (generateDatabase) {

        log.info("正在生成数据库......");
        generateDatabase(
            code, databaseHost, databasePort, databaseUser, databasePassword, databaseDriver);

        log.info("生成数据库成功......");
      }

      // 如果需要生成数据库表
      if (generateTable) {

        log.info("正在生成数据库表......");
        generateTable(
            code, databaseHost, databasePort, databaseUser, databasePassword, databaseDriver);
        log.info("生成数据库表成功......");
      }

      // 如果需要生成Mapper接口
      if (generateMapper) {
        log.info("正在生成Mapper接口......");

        generateMapper(code, generatePackage, generateModelPath);

        log.info("生成Mapper接口成功......");
      }

      // 如果需要生成service和Impl
      if (generateServiceAndImpl) {
        log.info("正在生成Service接口......");
        generateService(code, generatePackage, generateModelPath);

        log.info("生成Service接口成功......");

        log.info("正在生成Service Impl......");
        generateServiceImpl(code, generatePackage, generateModelPath);
        log.info("生成Service Impl成功......");

      }

 


      return true;
    } catch (Exception e) {
      log.info("自动生成代码出现异常,生成失败......");

      return false;
    }
  }

  //生成service实现类Impl
  private void generateServiceImpl(Code code, String generatePackage, String generateModelPath) throws IOException {

    try{
      String primaryKey = code.getPrimaryKey();
      List<String> attributes = code.getAttributes(); // 获取属性,对象
      StringBuffer codeString = new StringBuffer();
      String[] split = generatePackage.split("\\.");
      String packageName = split[split.length - 1];

      String servicePackagePath = generateModelPath + "\\\\" + packageName + "\\\\service"; // Service的包路径

      File pg = new File(servicePackagePath);

      // 创建包的操作
      if (!pg.exists()) { // 如果Service的包不存在，则创建一个

        boolean mkdir = pg.mkdirs(); // 这里不能用mkdir，因为防止父目录有其中某个没有被创建也会失败

      } else {
        if (pg.isFile()) {
          boolean delete = pg.delete();
        }
      }

      String classname = parseBig(code.getClassName()); // 接口名

      String serviceClassPath =
              servicePackagePath + "\\\\" + classname + "Service.java"; // Service接口的路径**

      String serviceImplClassPath =
              servicePackagePath + "\\\\" + classname + "ServiceImpl.java"; // Service impl的路径**

      // 写代码生成文件
      codeString.append("package " + generatePackage + ".service;\n\n");

      // 写导入类
      codeString.append("import java.util.*;\n");
      codeString.append("import " + generatePackage + ".pojo." + classname + ";\n");
      codeString.append("import org.springframework.beans.factory.annotation.Autowired;\n");
      codeString.append("import org.springframework.stereotype.Service;\n");
      codeString.append("import " + generatePackage + ".dao." + classname + "Mapper;\n");

      // 写入代码生成注释信息
      codeString.append("\n\n/**\n");
      codeString.append(" * 该ServiceImpl由yblog内置的代码器生成\n");
      Date date = new Date();
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String dateTime = simpleDateFormat.format(date);
      codeString.append(" * @date " + dateTime + "\n */\n");

      //写入注解
      codeString.append("@Service\n");
      // 写入注释完成
      String ServiceImplName = classname + "ServiceImpl";
      String ServiceImplObject=parseSmall(classname)+"Mapper";
      codeString.append("public class " + ServiceImplName + " implements "+classname+"Service"+" {\n\n");

      codeString.append("\t@Autowired\n");
      codeString.append("\tprivate "+classname+"Mapper "+ServiceImplObject+";\n\n");

      codeString.append("\t@Override\n");
      codeString.append(
              "\tpublic int insert" + classname + "(" + classname + " " + classname.toLowerCase() + "){\n");

      codeString.append("\t\treturn "+ServiceImplObject+".insert"+classname + "(" +classname.toLowerCase()+ ");\n\t}");

      codeString.append("\n\t@Override\n");
      codeString.append(
              "\tpublic int update" + classname + "(" + classname + " " + classname.toLowerCase() + "){\n");


      codeString.append("\t\treturn "+ServiceImplObject+".update"+classname + "(" +classname.toLowerCase()+ ");\n\t}");

      String[] data = parseData(attributes.get(0)); // 默认是实体类第一个属性为主键
      String dataType = data[0];
      String dataObject = data[1];
      codeString.append("\n\t//根据主键查询单个数据\n");
      codeString.append("\t@Override\n");
      codeString.append(
              "\tpublic "
                      + classname
                      + " select"
                      + classname
                      + "By"
                      + parseBig(primaryKey)
                      + "("
                      + dataType
                      + " "
                      + dataObject
                      + "){\n");


      codeString.append("\t\treturn "+ServiceImplObject+".select"+classname+"By"+parseBig(primaryKey)+"("+dataObject+");\n\t}");

      codeString.append("\n\t@Override\n");
      codeString.append("\tpublic List<" + classname + "> selectAll" + classname + "(){\n");


      codeString.append("\t\treturn "+ServiceImplObject+".selectAll"+classname+"("+");\n\t}");

      codeString.append("\n\t@Override\n");
      codeString.append(
              "\tpublic int delete"
                      + classname
                      + "By"
                      + parseBig(primaryKey)
                      + "("
                      + dataType
                      + " "
                      + dataObject
                      + "){\n");

      codeString.append("\t\treturn "+ServiceImplObject+".delete"+classname+"By"+parseBig(primaryKey)+"("+dataObject+");\n\t}");

      codeString.append("\n}");

      // 生成文件
      fileOutputStream = new FileOutputStream(serviceImplClassPath);
      bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

      String codeStr = codeString.toString();
      byte[] classToBytes = codeStr.getBytes();

      bufferedOutputStream.write(classToBytes);

      bufferedOutputStream.flush(); // 刷新缓冲区


    } catch (Exception e) {
      log.error("Service Impl代码生成失败...");
      throw new RuntimeException("Service Impl代码生成失败...");

    } finally {

      if (bufferedOutputStream != null) {
        bufferedOutputStream.close();
      }
      if (fileOutputStream != null) {
        fileOutputStream.close();
      }
    }

  }

  // 生成service接口
  private void generateService(Code code, String generatePackage, String generateModelPath) throws IOException {

    try{
      String primaryKey = code.getPrimaryKey();
      List<String> attributes = code.getAttributes(); // 获取属性,对象
      StringBuffer codeString = new StringBuffer();
      String[] split = generatePackage.split("\\.");
      String packageName = split[split.length - 1];

      String servicePackagePath = generateModelPath + "\\\\" + packageName + "\\\\service"; // Service的包路径

      File pg = new File(servicePackagePath);

      // 创建包的操作
      if (!pg.exists()) { // 如果Service的包不存在，则创建一个

        boolean mkdir = pg.mkdirs(); // 这里不能用mkdir，因为防止父目录有其中某个没有被创建也会失败

      } else {
        if (pg.isFile()) {
          boolean delete = pg.delete();
        }
      }

      String classname = parseBig(code.getClassName()); // 接口名

      String serviceClassPath =
              servicePackagePath + "\\\\" + classname + "Service.java"; // Service接口的路径**

      // 写代码生成文件
      codeString.append("package " + generatePackage + ".service;\n\n");

      // 写导入类
      codeString.append("import java.util.*;\n");
      codeString.append("import " + generatePackage + ".pojo." + classname + ";\n");

      // 写入代码生成注释信息
      codeString.append("\n\n/**\n");
      codeString.append(" * 该Service接口由yblog内置的代码器生成\n");
      Date date = new Date();
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String dateTime = simpleDateFormat.format(date);
      codeString.append(" * @date " + dateTime + "\n */\n");
      // 写入注释完成
      String ServiceName = classname + "Service";
      codeString.append("public interface " + ServiceName + " {\n\n");

      codeString.append(
              "\tint insert" + classname + "(" + classname + " " + classname.toLowerCase() + ");\n\n");

      codeString.append(
              "\tint update" + classname + "(" + classname + " " + classname.toLowerCase() + ");\n\n");

      String[] data = parseData(attributes.get(0)); // 默认是实体类第一个属性为主键
      String dataType = data[0];
      String dataObject = data[1];
      codeString.append("\t//根据主键查询单个数据\n");
      codeString.append(
              "\t"
                      + classname
                      + " select"
                      + classname
                      + "By"
                      + parseBig(primaryKey)
                      + "("
                      + dataType
                      + " "
                      + dataObject
                      + ");\n\n");


      codeString.append("\tList<" + classname + "> selectAll" + classname + "();\n\n");

      codeString.append(
              "\tint delete"
                      + classname
                      + "By"
                      + parseBig(primaryKey)
                      + "("
                      + dataType
                      + " "
                      + dataObject
                      + ");\n\n}");

      // 生成文件
      fileOutputStream = new FileOutputStream(serviceClassPath);
      bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

      String codeStr = codeString.toString();
      byte[] classToBytes = codeStr.getBytes();

      bufferedOutputStream.write(classToBytes);

      bufferedOutputStream.flush(); // 刷新缓冲区


    } catch (Exception e) {
      log.error("Service接口代码生成失败...");
      throw new RuntimeException("Service接口代码生成失败...");

    } finally {

      if (bufferedOutputStream != null) {
        bufferedOutputStream.close();
      }
      if (fileOutputStream != null) {
        fileOutputStream.close();
      }
    }


  }

  // 自动生成Mapper接口
  private void generateMapper(Code code, String generatePackage, String generateModelPath)
      throws IOException {

    try {
      String primaryKey = code.getPrimaryKey();
      StringBuffer codeString = new StringBuffer();
      String[] split = generatePackage.split("\\.");
      String packageName = split[split.length - 1];

      String MapperPackagePath = generateModelPath + "\\\\" + packageName + "\\\\dao"; // Mapper的包路径

      File pg = new File(MapperPackagePath);

      // 创建包的操作
      if (!pg.exists()) { // 如果Mapper的包不存在，则创建一个

        boolean mkdir = pg.mkdirs(); // 这里不能用mkdir，因为防止父目录有其中某个没有被创建也会失败

      } else {

        if (pg.isFile()) {
          boolean delete = pg.delete();
        }
      }

      String classname = parseBig(code.getClassName()); // 接口名

      String mapperClassPath =
          MapperPackagePath + "\\\\" + classname + "Mapper.java"; // Mapper接口的路径**

      List<String> attributes = code.getAttributes(); // 获取属性,对象

      // 写代码生成文件
      codeString.append("package " + generatePackage + ".dao;\n\n");

      // 写导入类
      codeString.append("import org.apache.ibatis.annotations.*;\n");
      codeString.append("import org.springframework.stereotype.Repository;\n");
      codeString.append("import java.util.*;\n");
      codeString.append("import " + generatePackage + ".pojo." + classname + ";\n");

      // 写入代码生成注释信息
      codeString.append("\n\n/**\n");
      codeString.append(" * 该Mapper接口由yblog内置的代码器生成\n");
      Date date = new Date();
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String dateTime = simpleDateFormat.format(date);
      codeString.append(" * @date " + dateTime + "\n */\n");
      // 写入注释完成

      // 写注解
      codeString.append("@Mapper //表示当前接口是Mapper接口\n");
      codeString.append("@Repository //该接口作为组件添加到Spring容器中\n");

      String MapperName = classname + "Mapper";
      String tableName = "t_" + code.getClassName(); // 数据库表名（默认）

      // 插入sql
      StringBuffer insertSql = new StringBuffer();
      insertSql.append("insert into " + tableName + "(");
      for (int i = 0; i < attributes.size() - 1; i++) {
        String[] data = parseData(attributes.get(i));
        String dataObject = data[1];

        insertSql.append(dataObject + ",");
      }
      String[] d1 = parseData(attributes.get(attributes.size() - 1));
      String lastdataObject = d1[1];

      insertSql.append(lastdataObject + ")");

      insertSql.append("values(");
      for (int i = 0; i < attributes.size() - 1; i++) {
        String[] data = parseData(attributes.get(i));
        String dataObject = data[1];
        insertSql.append("#{" + dataObject + "},");
      }

      insertSql.append("#{" + lastdataObject + "})");

      // 修改sql ----默认是实体类第一个属性为主键
      StringBuffer updateSql = new StringBuffer();
      updateSql.append("update " + tableName + " set ");
      for (int i = 1; i < attributes.size() - 1; i++) { // 从下标1开始遍历，下标0是主键
        String[] data = parseData(attributes.get(i));
        String dataObject = data[1];

        updateSql.append(dataObject + "=#{" + dataObject + "},");
      }
      updateSql.append(
          lastdataObject
              + "=#{"
              + lastdataObject
              + "} where "
              + primaryKey
              + "=#{"
              + primaryKey
              + "}");

      // 查询一个结果的sql

      StringBuffer selectOneSql = new StringBuffer();
      selectOneSql.append(
          "select * from " + tableName + " where " + primaryKey + "=#{" + primaryKey + "}");

      // 查询多个结果的sql

      StringBuffer selectAllSql = new StringBuffer();
      selectAllSql.append("select * from " + tableName);

      // 删除sql
      StringBuffer deleteSql = new StringBuffer();
      deleteSql.append(
          "delete from " + tableName + " where " + primaryKey + "=#{" + primaryKey + "}");

      codeString.append("public interface " + MapperName + " {\n\n");

      codeString.append("\t//插入" + classname + "\n");
      codeString.append("\t@Insert(\"" + insertSql.toString() + "\")\n");
      codeString.append(
          "\tint insert" + classname + "(" + classname + " " + classname.toLowerCase() + ");\n\n");

      codeString.append("\t//修改" + classname + "\n");
      codeString.append("\t@Update(\"" + updateSql.toString() + "\")\n");
      codeString.append(
          "\tint update" + classname + "(" + classname + " " + classname.toLowerCase() + ");\n\n");

      String[] data = parseData(attributes.get(0)); // 默认是实体类第一个属性为主键
      String dataType = data[0];
      String dataObject = data[1];
      codeString.append("\t//根据主键查询单个数据\n");
      codeString.append("\t@Select(\"" + selectOneSql.toString() + "\")\n");
      codeString.append(
          "\t"
              + classname
              + " select"
              + classname
              + "By"
              + parseBig(primaryKey)
              + "("
              + dataType
              + " "
              + dataObject
              + ");\n\n");

      codeString.append("\t//查询表的所有数据\n");
      codeString.append("\t@Select(\"" + selectAllSql.toString() + "\")\n");
      codeString.append("\tList<" + classname + "> selectAll" + classname + "();\n\n");

      codeString.append("\t//根据主键删除表数据\n");
      codeString.append("\t@Delete(\"" + deleteSql.toString() + "\")\n");
      codeString.append(
          "\tint delete"
              + classname
              + "By"
              + parseBig(primaryKey)
              + "("
              + dataType
              + " "
              + dataObject
              + ");\n\n}");

      // 生成文件
      fileOutputStream = new FileOutputStream(mapperClassPath);
      bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

      String codeStr = codeString.toString();
      byte[] classToBytes = codeStr.getBytes();

      bufferedOutputStream.write(classToBytes);

      bufferedOutputStream.flush(); // 刷新缓冲区

    } catch (Exception e) {
      log.error("Mapper接口代码生成失败...");
      throw new RuntimeException("Mapper接口代码生成失败...");

    } finally {

      if (bufferedOutputStream != null) {
        bufferedOutputStream.close();
      }
      if (fileOutputStream != null) {
        fileOutputStream.close();
      }
    }
  }

  // 自动生成数据库表
  private void generateTable(
      Code code,
      String databaseHost,
      int databasePort,
      String databaseUser,
      String databasePassword,
      String databaseDriver)
      throws ClassNotFoundException, SQLException {

    try {

      ResourceBundle mysqlDataType = ResourceBundle.getBundle("mysqlDataType");

      Class.forName(databaseDriver);

      String urlArgs =
          "?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=UTF-8"; // url参数

      String databaseName = code.getDatabaseName();
      String url =
          "jdbc:mysql://" + databaseHost + ":" + databasePort + "/" + databaseName + urlArgs;

      connection = DriverManager.getConnection(url, databaseUser, databasePassword);
      statement = connection.createStatement();

      String tableName = "t_" + code.getClassName(); // 数据库表名（默认）

      StringBuffer stringBuffer = new StringBuffer(); // 拼接创建表的sql

      stringBuffer.append("CREATE TABLE IF NOT EXISTS `" + tableName + "` (");

      List<String> attributes = code.getAttributes();

      for (int i = 0; i < attributes.size(); i++) {
        String[] data = parseData(attributes.get(i));
        String dataType = data[0];
        String dataObject = data[1];
        String type = dataType.toLowerCase(); // 变小写
        String sqlDataType = mysqlDataType.getString(type); // 获取配置文件中的javaType对应的sqlType
        if (StringUtils.isBlank(sqlDataType)) { // 如果查询不到类型，直接抛异常
          log.error("无法从配置文件中查询到字段的类型,生成数据库表失败......");
          throw new RuntimeException("无法从配置文件中查询到字段的类型,生成数据库表失败......");
        }
        stringBuffer.append("`" + dataObject + "` " + sqlDataType + " ,");
      }
      stringBuffer.append("PRIMARY KEY (`" + code.getPrimaryKey() + "`)"); // 添加主键
      stringBuffer.append(")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");
      String sql = stringBuffer.toString();

      int res = statement.executeUpdate(sql);

    } catch (Exception e) {

      log.error("自动生成数据库表失败......");
      throw new RuntimeException("自动生成数据库表失败......");
    } finally {

      if (statement != null) {
        statement.close();
      }
      if (connection != null) {
        connection.close();
      }
    }
  }

  // 自动生成数据库
  private void generateDatabase(
      Code code,
      String databaseHost,
      int databasePort,
      String databaseUser,
      String databasePassword,
      String databaseDriver)
      throws ClassNotFoundException, SQLException {

    try {

      // 加载数据库驱动
      Class.forName(databaseDriver);

      String databaseName = code.getDatabaseName(); // 获取需要生成的数据库名

      String sql = "create database if not exists " + databaseName + ";";

      String urlArgs =
          "?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=UTF-8"; // url参数

      String url =
          "jdbc:mysql://" + databaseHost + ":" + databasePort + "/blog_system" + urlArgs;
      connection = DriverManager.getConnection(url, databaseUser, databasePassword);

      statement = connection.createStatement();

      int res = statement.executeUpdate(sql); // 创建数据库

    } catch (Exception e) {
      log.error("自动生成数据库失败......");
      throw new RuntimeException("自动生成数据库失败");

    } finally {

      if (statement != null) {
        statement.close();
      }
      if (connection != null) {
        connection.close();
      }
    }
  }

  // 自动生成实体类
  private void generateModel(
      Code code,
      boolean modelSerialize,
      boolean modelGetterAndSetter,
      boolean modelConstructor,
      String generatePackage,
      String generateModelPath)
      throws IOException {

    try {

      // 使用StringBuffer来写自动生成的代码
      // StringBuilder也行，但是StringBuffer的优势是在多线程的情况下使用的，刚好我们现在的场景符合。
      StringBuffer codeString = new StringBuffer();

      String[] split = generatePackage.split("\\."); // ***split之间分隔'.'是没有用的,要"\\."

      String packageName = split[split.length - 1];

      String modelPackagePath = generateModelPath + "\\\\" + packageName + "\\\\pojo"; // 实体类的包路径

      File pg = new File(modelPackagePath);

      // 创建包的操作
      if (!pg.exists()) { // 如果实体类的包不存在，则创建一个
        boolean mkdir = pg.mkdirs(); // 这里不能用mkdir，因为防止父目录有其中某个没有被创建也会失败

      } else {

        if (pg.isFile()) {
          boolean delete = pg.delete();
        }
      }
      String className = parseBig(code.getClassName()); // 类名

      String modelClassPath = modelPackagePath + "\\\\" + className + ".java"; // 类的路径**

      List<String> attributes = code.getAttributes(); // 获取属性,对象

      // 写代码生成文件
      codeString.append("package " + generatePackage + ".pojo;\n\n");

      // 写导入类
      codeString.append("import lombok.ToString;\n");
      codeString.append("import java.util.*;\n"); // 导入java.util包

      if (modelSerialize) { // 如果需要序列化
        codeString.append("import java.io.Serializable;\n");
      }

      // 写入代码生成注释信息
      codeString.append("\n\n/**\n");
      codeString.append(" * 该类由yblog内置的代码器生成\n");

      Date date = new Date();
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String dateTime = simpleDateFormat.format(date);

      codeString.append(" * @date " + dateTime + "\n */\n");

      // 写入注释完成

      // 开始写入类

      // 生成lombok的@ToString注解
      codeString.append("@ToString //lombok生成toString方法\n");

      if (modelSerialize) {

        codeString.append("public class " + className + " implements Serializable {");

      } else {

        codeString.append("public class " + className + " {");
      }

      // 生成字段
      for (int i = 0; i < attributes.size(); i++) {
        String[] data = parseData(attributes.get(i));
        String dataType = data[0];
        String dataObject = data[1];

        codeString.append("\n\tprivate " + dataType + " " + dataObject + ";");
      }

      codeString.append("\n\n");

      if (modelConstructor) { // 如果生成构造方法

        // 写入无參构造
        codeString.append("\t//无參构造方法\n");
        codeString.append("\tpublic " + className + "(){\n\t}");

        // 写入有参构造
        codeString.append("\n\n\tpublic " + className + "(");

        for (int i = 0; i < attributes.size() - 1; i++) {
          String[] data = parseData(attributes.get(i));
          String dataType = data[0];
          String dataObject = data[1];
          codeString.append(dataType + " " + dataObject + ", ");
        }

        String[] data1 = parseData(attributes.get(attributes.size() - 1));
        String dataType1 = data1[0];
        String dataObject1 = data1[1];
        codeString.append(dataType1 + " " + dataObject1 + ") {\n");

        for (int i = 0; i < attributes.size(); i++) {
          String[] data = parseData(attributes.get(i));
          String dataType = data[0];
          String dataObject = data[1];
          codeString.append("\t\tthis." + dataObject + " = " + dataObject + ";\n");
        }
        codeString.append("\t}\n\n\n");
      }

      if (modelGetterAndSetter) { // 如果生成getter/setter方法

        for (int i = 0; i < attributes.size(); i++) {
          String[] data = parseData(attributes.get(i));
          String dataType = data[0];
          String dataObject = data[1];

          // dataObject首字母大写
          String parseDataObject = "";

          // 如果首字母是大写则不用parse

          char c = dataObject.charAt(0); // 获取首字母字符
          if (c >= 97 && c <= 122) { // 说明首字母是小写，要parse

            for (int j = 0; j < dataObject.length(); j++) {
              if (j == 0) {
                parseDataObject += (char) (c - 32);
              } else {
                parseDataObject += dataObject.charAt(j);
              }
            }
            // get方法
            codeString.append("\tpublic " + dataType + " get" + parseDataObject + "() {\n");
            codeString.append("\t\treturn " + dataObject + ";\n\t}\n\n");
            // set方法
            codeString.append(
                "\tpublic void set"
                    + parseDataObject
                    + "("
                    + dataType
                    + " "
                    + dataObject
                    + ") {\n");
            codeString.append("\t\tthis." + dataObject + " = " + dataObject + ";\n\t}\n\n");

          } else { // 不用转换
            // get方法
            codeString.append("\tpublic " + dataType + " get" + dataObject + "() {\n");
            codeString.append("\t\treturn " + dataObject + ";\n\t}\n\n");
            // set方法
            codeString.append(
                "\tpublic void set" + dataObject + "(" + dataType + " " + dataObject + ") {\n");
            codeString.append("\t\tthis." + dataObject + " = " + dataObject + ";\n\t}\n\n");
          }
        }
      }

      // 类的结束
      codeString.append("\n}");

      // 生成文件
      fileOutputStream = new FileOutputStream(modelClassPath);

      bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

      String codeStr = codeString.toString();

      byte[] classToBytes = codeStr.getBytes();

      bufferedOutputStream.write(classToBytes);

      bufferedOutputStream.flush(); // 刷新缓冲区

    } catch (Exception e) {
      log.error("实体类代码生成失败...");
      throw new RuntimeException("实体类代码生成失败...");

    } finally {

      if (bufferedOutputStream != null) {
        bufferedOutputStream.close();
      }
      if (fileOutputStream != null) {
        fileOutputStream.close();
      }
    }
  }

  public String[] parseData(String data) {

    String[] split = data.split(",");

    return split;
  }

  // 首字母变大写
  private String parseBig(String dataObject) {

    // dataObject首字母大写
    String parseDataObject = "";

    // 如果首字母是大写则不用parse
    char c = dataObject.charAt(0); // 获取首字母字符
    if (c >= 97 && c <= 122) { // 说明首字母是小写，要parse

      for (int j = 0; j < dataObject.length(); j++) {
        if (j == 0) {
          parseDataObject += (char) (c - 32);
        } else {
          parseDataObject += dataObject.charAt(j);
        }
      }
    }
    return parseDataObject;
  }
  // 首字母变小写
  private String parseSmall(String dataObject) {

    // dataObject首字母大写
    String parseDataObject = "";

    // 如果首字母是大写则不用parse
    char c = dataObject.charAt(0); // 获取首字母字符
    if (c >= 65 && c <= 90) { // 说明首字母是大写，则要parse

      for (int j = 0; j < dataObject.length(); j++) {
        if (j == 0) {
          parseDataObject += (char) (c + 32);
        } else {
          parseDataObject += dataObject.charAt(j);
        }
      }
    }
    return parseDataObject;
  }
}
