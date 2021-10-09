package com.boot.pojo;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.List;

@ApiModel("自动生成代码实体类")
public class Code implements Serializable {

    private String className; //类名
    private List<String> attributes; //格式为:属性,对象
    private String databaseName; //数据库名
    private String primaryKey; //主键
    private boolean generateModel; //是否生成实体类
    private boolean modelSerialize; //实体类是否序列化
    private boolean modelGetterAndSetter; //是否生成get/set方法
    private boolean modelConstructor; //是否生成构造方法
    private String generatePackage; //存放生成的实体类的包名（后面会和generateModelPath+generatePackage进行拼接作为路径）
    private String generateModelPath; //生成实体类的路径（**默认使用这个路径**）
    private boolean generateDatabase; //是否生成数据库
    private String databaseHost; //数据库主机
    private int databasePort; //数据库端口号
    private String databaseUser; //数据库用户名
    private String databasePassword; //数据库密码
    private String databaseDriver; //数据库驱动
    private boolean generateTable; //是否生成数据库表
    private boolean generateMapper; //是否生成Mapper接口
    private boolean generateServiceAndImpl; //是否生成Service接口和Service的实现类

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }


    public void setDatabasePort(int databasePort) {
        this.databasePort = databasePort;
    }

    public int getDatabasePort() {
        return databasePort;
    }
    public boolean getGenerateModel() {
        return generateModel;
    }

    public void setGenerateModel(boolean generateModel) {
        this.generateModel = generateModel;
    }

    public boolean getModelSerialize() {
        return modelSerialize;
    }

    public void setModelSerialize(boolean modelSerialize) {
        this.modelSerialize = modelSerialize;
    }

    public boolean getModelGetterAndSetter() {
        return modelGetterAndSetter;
    }

    public void setModelGetterAndSetter(boolean modelGetterAndSetter) {
        this.modelGetterAndSetter = modelGetterAndSetter;
    }

    public boolean getModelConstructor() {
        return modelConstructor;
    }

    public void setModelConstructor(boolean modelConstructor) {
        this.modelConstructor = modelConstructor;
    }

    public String getGeneratePackage() {
        return generatePackage;
    }

    public void setGeneratePackage(String generatePackage) {
        this.generatePackage = generatePackage;
    }

    public String getGenerateModelPath() {
        return generateModelPath;
    }

    public void setGenerateModelPath(String generateModelPath) {
        this.generateModelPath = generateModelPath;
    }

    public boolean getGenerateDatabase() {
        return generateDatabase;
    }

    public void setGenerateDatabase(boolean generateDatabase) {
        this.generateDatabase = generateDatabase;
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public void setDatabaseHost(String databaseHost) {
        this.databaseHost = databaseHost;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public String getDatabaseDriver() {
        return databaseDriver;
    }

    public void setDatabaseDriver(String databaseDriver) {
        this.databaseDriver = databaseDriver;
    }

    public boolean getGenerateTable() {
        return generateTable;
    }

    public void setGenerateTable(boolean generateTable) {
        this.generateTable = generateTable;
    }

    public boolean getGenerateMapper() {
        return generateMapper;
    }

    public void setGenerateMapper(boolean generateMapper) {
        this.generateMapper = generateMapper;
    }

    public boolean getGenerateServiceAndImpl() {
        return generateServiceAndImpl;
    }

    public void setGenerateServiceAndImpl(boolean generateServiceAndImpl) {
        this.generateServiceAndImpl = generateServiceAndImpl;
    }

    @Override
    public String toString() {
        return "Code{" +
                "className='" + className + '\'' +
                ", attributes=" + attributes +
                ", databaseName='" + databaseName + '\'' +
                ", primaryKey='" + primaryKey + '\'' +
                ", generateModel=" + generateModel +
                ", modelSerialize=" + modelSerialize +
                ", modelGetterAndSetter=" + modelGetterAndSetter +
                ", modelConstructor=" + modelConstructor +
                ", generatePackage='" + generatePackage + '\'' +
                ", generateModelPath='" + generateModelPath + '\'' +
                ", generateDatabase=" + generateDatabase +
                ", databaseHost='" + databaseHost + '\'' +
                ", databasePort=" + databasePort +
                ", databaseUser='" + databaseUser + '\'' +
                ", databasePassword='" + databasePassword + '\'' +
                ", databaseDriver='" + databaseDriver + '\'' +
                ", generateTable=" + generateTable +
                ", generateMapper=" + generateMapper +
                ", generateServiceAndImpl=" + generateServiceAndImpl +
                '}';
    }
}
