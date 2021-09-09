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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

public class LoginTest {
    @BeforeClass
    public void setup(){
        RestAssured.baseURI="http://api.lemonban.com/futureloan";
        RestAssured.config=RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //读取Excel中第一行的注册的用例
        File file=new File("src/test/resources/api_testcases_futureloan_v3.xls");
        ImportParams importParams=new ImportParams();
        importParams.setStartSheetIndex(1);
        importParams.setStartRows(0);
        importParams.setReadRows(1);
        List<ExcelPojo> preData=ExcelImportUtil.importExcel(file,ExcelPojo.class,importParams);

        Map requestHeaders= JSON.parseObject(preData.get(0).getRequestHeader());
        String phone=unregisterPhoneGenerate();
        System.out.println(phone);
        String inputParams=regexReplace(preData.get(0).getInputParams(),phone);
        Response response = given().headers(requestHeaders).body(inputParams).
                when().post(preData.get(0).getUrl()).then().log().all().extract().response();
        //从响应体中取得我们下一个接口需要用到的数据，如何保存---引入环境变量Environment.envData
        if(preData.get(0).getExtract()!=null) {
            Map extractMap = JSON.parseObject(preData.get(0).getExtract());
            for (Object key : extractMap.keySet()) {
                Object path = extractMap.get(key);
                Object value = response.jsonPath().get(path.toString());
                Environment.envData.put(key.toString(), value);
            }
        }
    }
    @Test(dataProvider = "getLoginData")
    public void testLogin(ExcelPojo excelPojo){
        //替换接口数据
        excelPojo.setInputParams(regexReplace(excelPojo.getInputParams()));
        //发包
        Map requestHeaders=JSON.parseObject(excelPojo.getRequestHeader());
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

        Response res=given().headers(requestHeaders).body(excelPojo.getInputParams()).when().post(excelPojo.getUrl()).then().log().all().extract().response();
        try {
            Allure.addAttachment("接口请求响应信息", new FileInputStream(logFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //断言
        Map expectedMap=JSON.parseObject(excelPojo.getExpectedResponse());
        for(Object key:expectedMap.keySet()){
            Object expectedValue=expectedMap.get(key);
            Object acturalValue=res.jsonPath().get(key.toString());
            Assert.assertEquals(acturalValue,expectedValue);
        }

    }
    @DataProvider
    public Object[] getLoginData(){
        File file=new File("src/test/resources/api_testcases_futureloan_v3.xls");
        ImportParams importParams=new ImportParams();
        importParams.setStartSheetIndex(1);
        importParams.setStartRows(1);
        List<ExcelPojo> dataList= ExcelImportUtil.importExcel(file,ExcelPojo.class,importParams);
        return dataList.toArray();
    }
    public String unregisterPhoneGenerate(){
        Random random=new Random();
        String phonePrefix="177";
        for(int i=0;i<8;i++){
            int number=random.nextInt(9);
            phonePrefix+=number;
        }
        String url="jdbc:mysql://api.lemonban.com/futureloan?useUnicode=true&characterEncoding=utf-8";
        String user="future";
        String password="123456";
        Connection conn=null;
        try {
            conn= DriverManager.getConnection(url,user,password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String querySql="select count(*) from member where phone="+phonePrefix;
        QueryRunner qr=new QueryRunner();
        long result=0;
        try {
            result=qr.query(conn,querySql,new ScalarHandler<>());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while(true){
            if (result==0){
                break;
            }else{
                unregisterPhoneGenerate();
            }
        }
        return phonePrefix;
    }
    public String regexReplace(String OriginStr,String replaceStr){
        Pattern pattern=Pattern.compile("\\{\\{(.*?)}}");
        Matcher matcher=pattern.matcher(OriginStr);
        while(matcher.find()){
            String outer=matcher.group(0);
            System.out.println(outer);
            String inner= matcher.group(1);
            System.out.println(inner);
            OriginStr=OriginStr.replace(outer,replaceStr);
        }
        return OriginStr;
    }
    public String regexReplace(String OriginStr){
        Pattern pattern=Pattern.compile("\\{\\{(.*?)}}");
        Matcher matcher=pattern.matcher(OriginStr);
        while(matcher.find()){
            String outer=matcher.group(0);
            System.out.println(outer);
            String inner= matcher.group(1);
            System.out.println(inner);
            Object replaceStr=Environment.envData.get(inner);
            OriginStr=OriginStr.replace(outer,replaceStr.toString());
        }
        return OriginStr;
    }
}
