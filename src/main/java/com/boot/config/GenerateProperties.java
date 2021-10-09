package com.boot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

//代码生成默认配置
@ConfigurationProperties(prefix = "yblog.generate")
@Configuration
public class GenerateProperties {

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
        return "GenerateProperties{" +
                "generateModel=" + generateModel +
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
