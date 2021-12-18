package interfaceAutomation.excelPojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class ExcelPojo {
    //123456
    @Excel(name="序号(caseId)")
    private String caseId;
    @Excel(name="接口模块(interface)")
    private String interfaceName;
    @Excel(name="用例标题(title)")
    private String caseTitle;
    @Excel(name="请求头(requestHeader)")
    private String requestHeader;
    @Excel(name="请求方式(method)")
    private String method;
    @Excel(name="接口地址(url)")
    private String url;
    @Excel(name="参数输入(inputParams)")
    private String inputParams;
    @Excel(name="期望响应结果(expected)")
    private String expectedResponse;
    @Excel(name="提取返回数据(extract)")
    private String extract;
    @Excel(name="数据库校验")
    private String DBCheck;

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(String caseTitle) {
        this.caseTitle = caseTitle;
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInputParams() {
        return inputParams;
    }

    public void setInputParams(String inputParams) {
        this.inputParams = inputParams;
    }

    public String getExpectedResponse() {
        return expectedResponse;
    }

    public void setExpectedResponse(String expectedResponse) {
        this.expectedResponse = expectedResponse;
    }

    public String getExtract() {
        return extract;
    }

    public void setExtract(String extract) {
        this.extract = extract;
    }

    public String getDBCheck() {
        return DBCheck;
    }

    public void setDBCheck(String DBCheck) {
        this.DBCheck = DBCheck;
    }

    @Override
    public String toString() {
        return "ExcelPojo{" +
                "caseId='" + caseId + '\'' +
                ", interfaceName='" + interfaceName + '\'' +
                ", caseTitle='" + caseTitle + '\'' +
                ", requestHeader='" + requestHeader + '\'' +
                ", method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", inputParams='" + inputParams + '\'' +
                ", expectedResponse='" + expectedResponse + '\'' +
                ", extract='" + extract + '\'' +
                ", DBCheck='" + DBCheck + '\'' +
                '}';
    }
}
