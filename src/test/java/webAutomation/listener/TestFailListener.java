package webAutomation.listener;

import io.qameta.allure.Attachment;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import webAutomation.base.BaseTest;

public class TestFailListener implements IHookable {
    public void run(IHookCallBack callBack, ITestResult testResult){
        System.out.println("class TestFailListener implements IHookable的run方法执行");
        callBack.runTestMethod(testResult);
        if(testResult.getThrowable()!=null){
            byte[] screenShot=BaseTest.screenshotas();
            displayScreenShotToAllure(screenShot);
        }
    }
    @Attachment(value="screenshot",type="image/png")
    public byte[] displayScreenShotToAllure(byte[] screenShot){
        return screenShot;
    }
}