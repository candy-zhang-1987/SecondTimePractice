package interfaceAutomation.testCases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
import interfaceAutomation.excelPojo.ExcelPojo;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class RegisterTest {
    @BeforeClass
    public void setup(){
        RestAssured.baseURI="http://api.lemonban.com/futureloan";
    }
    @Test(dataProvider = "getRegisterData")
    public void testRegister(ExcelPojo excelPojo){
        String requestHeader = excelPojo.getRequestHeader();
        Map requestHeaderMap = JSON.parseObject(requestHeader);
        String inputParams=excelPojo.getInputParams();
        //调用方法生成一个没有注册的手机号码，再使用正则表达式替换掉{{phone1}}
        String phone="";
        try {
            phone= generateUnregisterPhone();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String inputParamsReplace=regexReplace(inputParams,phone);
        String method=excelPojo.getMethod();
        String url= excelPojo.getUrl();
        String logFilePath;
        File dirPath = new File(System.getProperty("user.dir") + "\\log\\"+excelPojo.getInterfaceName());
        if (!dirPath.exists()) {
            //创建目录层级 log/接口模块名
            dirPath.mkdirs();
        }
        logFilePath = dirPath +"\\test"+ excelPojo.getCaseId() + ".log";
        PrintStream fileOutPutStream = null;
        try {
            fileOutPutStream = new PrintStream(new File(logFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(fileOutPutStream));
        Response res = given().headers(requestHeaderMap).body(inputParamsReplace).log().all().when().post(url).then().log().all().extract().response();
    //断言
        String expectedResponse = excelPojo.getExpectedResponse();
        String expectedResponseReplace=regexReplace(expectedResponse,phone);
        Map expectedResponseMap=JSON.parseObject(expectedResponseReplace);
        for(Object path:expectedResponseMap.keySet()){
            Object acturalValue=res.jsonPath().get(path.toString());
            Object expectedValue=expectedResponseMap.get(path);
            Assert.assertEquals(acturalValue,expectedValue);
        }
        try {
            Allure.addAttachment("接口请求响应信息", new FileInputStream(logFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    @DataProvider
    public Object[] getRegisterData(){
        File file=new File("src/test/resources/api_testcases_futureloan_v3.xls");
        ImportParams params = new ImportParams();
        params.setStartSheetIndex(0);
        List<ExcelPojo> excelDatas=ExcelImportUtil.importExcel(file,ExcelPojo.class,params);
        System.out.println(excelDatas.get(0));
        return excelDatas.toArray();
    }
    /*public void teardown(){

    }*/
   /* public static void main(String[] args) {
        *//*Random random = new Random();
        String phonePrefix="177";
        for(int i=0;i<8;i++){
            int number=random.nextInt(9);
            phonePrefix+=number;
        }
        //使用数据库查询号码是否已经存在
        String querySql="select count(*) from member where mobile_phone="+phonePrefix;
        //与数据库建立连接，提取为util里面的方法
        String url="jdbc:mysql://api.lemonban.com/futureloan?useUnicode=true&characterEncoding=utf-8";
        String user="future";
        String password="123456";
        Connection conn=null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        QueryRunner qr=new QueryRunner();
        long count= 0;
        try {
            count = qr.query(conn,querySql,new ScalarHandler<Long>());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println(count);
        System.out.println(phonePrefix);*//*
        String originStr="anbdvdhhf{{phone1}}ndbdvdv{{phone2}}";
        Pattern pattern=Pattern.compile("\\{\\{(.*?)\\}\\}");
        Matcher matcher= pattern.matcher(originStr);
        while(matcher.find()){
            String outer= matcher.group(0);
            String inner= matcher.group(1);
            originStr=   originStr.replace(outer,inner);
            System.out.println(originStr);
        }
    }*/
    public String generateUnregisterPhone() throws SQLException {
        Random random = new Random();
        String phonePrefix="177";
        for(int i=0;i<8;i++){
            int number=random.nextInt(9);
            phonePrefix+=number;
        }
       //使用数据库查询号码是否已经存在
        String querySql="select count(*) from member where mobile_phone="+phonePrefix;
        String url="jdbc:mysql://api.lemonban.com/futureloan?useUnicode=true&characterEncoding=utf-8";
        String user="future";
        String password="123456";
        Connection conn=null;
        conn = DriverManager.getConnection(url, user, password);
        QueryRunner qr=new QueryRunner();
        Long count=qr.query(conn,querySql,new ScalarHandler<Long>());
        System.out.println(count);
        System.out.println(phonePrefix);
        while(true){
            if(count==0){
                break;
            }else{
                generateUnregisterPhone();
            }
        }
        /*if(count==0){
            break;
        } else{
            generateUnregisterPhone();
        }*/
       return phonePrefix;
    }
    public String regexReplace(String originStr,String replaceStr){
        Pattern pattern=Pattern.compile("\\{\\{(.*?)\\}\\}");
        Matcher matcher= pattern.matcher(originStr);
        while(matcher.find()){
            String outer= matcher.group(0);
            String inner= matcher.group(1);
            originStr=originStr.replace(outer,replaceStr);
        }
        return originStr;
    }
}
