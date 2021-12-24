package basePractice.restAssured;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class RestAssuredDemo {
    public static void main(String[] args) {
        //get request，first way to pass parameter
        Response res =
                given().log().all().
                when().get("http://www.httpbin.org/get?phone=13323234545&password=123456").
                then().log().all().extract().response();
        System.out.println(res);
        //get request,second way to pass parameter
        /*Response res1 =
                given().log().all().queryParam("mobilephone","13323234545").
                        queryParam("password","123456").
                        when().get("http://www.httpbin.org/get").
                        then().log().all().extract().response();
        System.out.println(res1);*/
        //post request：form表单传参
       /* given().formParam("mobilephone","13323231234").log().all().
                when().post("http://www.httpbin.org/post").
                then().log().body();*/
        //json传参gg
      /*  String jsonData="{\"mobile_phone\":\"13212121212\",\"pwd\":\"12345678\"}";
        given().contentType(ContentType.JSON).body(jsonData).
                when().post("http://www.httpbin.org/post").then().log().body();*/
        //System.out.println(res.body());
        //System.out.println(res.jsonPath().get("headers.Host")+"");

//xml传参
       /* String xmlData="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "    <groupId>org.example</groupId>\n" +
                "    <artifactId>SecondTimePracticeTestAutomation</artifactId>\n" +
                "    <version>1.0-SNAPSHOT</version>";
        Response res1 = given().contentType(ContentType.XML).body(xmlData).
                when().post("http://www.httpbin.org/post").then().extract().response();
        //System.out.println(res1);
        System.out.println(res1.jsonPath().get("headers.Host")+"");*/
        //Gpath HTML路径解析
        //Response res = given().when().get("https://www.baidu.com/").then().log().body().extract().response();
       // System.out.println(res.htmlPath().get("html.head.title")+"");
        //System.out.println(res.htmlPath().get("html.head.meta[0].@http-equiv")+"");
    }
}
