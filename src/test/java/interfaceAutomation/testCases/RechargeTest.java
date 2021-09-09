package interfaceAutomation.testCases;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
import interfaceAutomation.data.Environment;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

public class RechargeTest {
    @BeforeClass
    public void setup(){
        RestAssured.baseURI="http://api.lemonban.com/futureloan";
        RestAssured.config=RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //读取excel的前两行，注册和登录
        File file=new File("src/test/resources/api_testcases_futureloan_v3.xls");
        ImportParams importParams=new ImportParams();
        importParams.setStartSheetIndex(2);
        importParams.setStartRows(0);
        importParams.setReadRows(2);
        List<ExcelPojo> preDataList= ExcelImportUtil.importExcel(file,ExcelPojo.class,importParams);
        //获取未注册手机号码
        String phone=getUnregisterPhone();
        Environment.envData.put("phone",phone);
        //替换excelPOjo中的{{phone}}
        String inputParams=preDataList.get(0).getInputParams();
        preDataList.get(0).setInputParams(regexReplace(inputParams));
        //注册发包
        String requestHeader=preDataList.get(0).getRequestHeader();
        Map requestHeaderMap= JSON.parseObject(requestHeader);
        given().headers(requestHeaderMap).body(preDataList.get(0).getInputParams()).when().post(preDataList.get(0).getUrl()).then();

        //登录
        String inputParamsLogin=preDataList.get(1).getInputParams();
        preDataList.get(1).setInputParams(regexReplace(inputParams));
        String requestHeaderLogin=preDataList.get(1).getRequestHeader();
        Map requestHeaderLoginMap= JSON.parseObject(requestHeaderLogin);
        Response response = given().headers(requestHeaderLoginMap).body(preDataList.get(1).getInputParams()).
                when().post(preDataList.get(1).getUrl()).then().log().all().extract().response();
        //提取token和memberID到环境变量
        //String extract=preDataList.get(1).getExtract();
        //System.out.println(extract);
        String extract="{\"member_id\": \"data.id\",\"token\": \"data.token_info.token\"}";
        if(extract!=null){
        Map extractPathMap=JSON.parseObject(extract);
        for(Object key:extractPathMap.keySet()){
            Object path=extractPathMap.get(key.toString());
            System.out.println(path);
            Object value=response.jsonPath().get(path.toString());
            Environment.envData.put(key.toString(),value);
        }}
    }
    @DataProvider
    public Object[] rechargeData(){
        File file=new File("src/test/resources/api_testcases_futureloan_v3.xls");
        ImportParams importParams=new ImportParams();
        importParams.setStartSheetIndex(2);
        importParams.setStartRows(2);
        List<ExcelPojo> pojoList=ExcelImportUtil.importExcel(file,ExcelPojo.class,importParams);
        return pojoList.toArray();
    }
    @Test(dataProvider ="rechargeData" )
    public void testRecharge(ExcelPojo excelPojo){
        String requestHeader=excelPojo.getRequestHeader();
        String requestHeaderReplace=regexReplace(requestHeader);
        Map requestHeaderMap= JSON.parseObject(requestHeaderReplace);
        String inputParams=excelPojo.getInputParams();
        String inputParamsReplace=regexReplace(inputParams);
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
        Response response = given().headers(requestHeaderMap).body(inputParamsReplace).
                when().post(excelPojo.getUrl()).then().log().all().extract().response();
        try {
            Allure.addAttachment("接口请求响应信息", new FileInputStream(logFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //断言
        String expectedStr= excelPojo.getExpectedResponse();
        Map expectedMap=JSON.parseObject(expectedStr);
        for(Object key:expectedMap.keySet()){
            Object expectedValue=expectedMap.get(key.toString());
            Object actualValue=response.jsonPath().get(key.toString());
            Assert.assertEquals(actualValue,expectedValue);
        }
    }
    public void teardown(){

    }
    public String getUnregisterPhone(){
        Random random=new Random();
        String phonePrefix="177";
        for(int i=0;i<8;i++){
            int i1 = random.nextInt(9);
            phonePrefix+=i1;
        }
        String querySql="select count(*) from member where mobile_phone="+phonePrefix;
        //建立数据库连接
        String url="jdbc:mysql://api.lemonban.com/futureloan?useUnicode=true&characterEncoding=utf-8";
        String user="future";
        String password="123456";
        Connection conn=null;
        try {
            DriverManager.getConnection(url,user,password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        QueryRunner qr=new QueryRunner();
        long count=0;
        try {
         count=   qr.query(conn,querySql,new ScalarHandler<>());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while(true){
            if(count==0){
                break;
            }else{
                getUnregisterPhone() ;
            }
        }
        return phonePrefix;
    }
    public String regexReplace(String oriStr){
        Pattern pattern=Pattern.compile("\\{\\{(.*?)}}");
        Matcher matcher= pattern.matcher(oriStr);
        String result=oriStr;
        while(matcher.find()){
            String outer= matcher.group(0);
            String inner= matcher.group(1);
            String replaceStr= Environment.envData.get(inner).toString();
            result=result.replace(outer,replaceStr);
        }
        return result;
    }
}
