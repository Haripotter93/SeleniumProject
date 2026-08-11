package utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import testBase.BaseClass;

public class ExtentReportManager implements ITestListener {

    public ExtentSparkReporter sparkReporter;
    public ExtentReports extent;
    public ExtentTest test;

    String repName;

    @Override
    public void onStart(ITestContext testContext) {

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")
                .format(new Date());

        repName = "Test-Report-" + timeStamp + ".html";

        // Create reports directory
        Path reportsDirectory = Paths.get(
                System.getProperty("user.dir"),
                "reports"
        );

        try {
            Files.createDirectories(reportsDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create report path
        String reportPath = reportsDirectory
                .resolve(repName)
                .toString();

        System.out.println("==========================================");
        System.out.println("Extent Report Path:");
        System.out.println(reportPath);
        System.out.println("==========================================");

        sparkReporter = new ExtentSparkReporter(reportPath);

        sparkReporter.config().setDocumentTitle("Automation Report");
        sparkReporter.config().setReportName("Functional Testing");
        sparkReporter.config().setTheme(Theme.DARK);

        extent = new ExtentReports();

        extent.attachReporter(sparkReporter);

        extent.setSystemInfo("Computer Name", "localhost");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Tester Name", "Pavan");

        String os = testContext
                .getCurrentXmlTest()
                .getParameter("os");

        extent.setSystemInfo("Operating System", os);

        String browser = testContext
                .getCurrentXmlTest()
                .getParameter("browser");

        extent.setSystemInfo("Browser", browser);

        List<String> includedGroups =
                testContext.getCurrentXmlTest().getIncludedGroups();

        if (!includedGroups.isEmpty()) {
            extent.setSystemInfo(
                    "Groups",
                    includedGroups.toString()
            );
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        test = extent.createTest(
                result.getTestClass().getName()
        );

        test.assignCategory(
                result.getMethod().getGroups()
        );

        test.log(
                Status.PASS,
                result.getName() + " got successfully executed"
        );
    }

    @Override
    public void onTestFailure(ITestResult result) {

        test = extent.createTest(
                result.getTestClass().getName()
        );

        test.assignCategory(
                result.getMethod().getGroups()
        );

        test.log(
                Status.FAIL,
                result.getName() + " got failed"
        );

        if (result.getThrowable() != null) {
            test.log(
                    Status.INFO,
                    result.getThrowable().getMessage()
            );
        }

        try {

            String imgPath =
                    new BaseClass().captureScreen(result.getName());

            test.addScreenCaptureFromPath(imgPath);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        test = extent.createTest(
                result.getTestClass().getName()
        );

        test.assignCategory(
                result.getMethod().getGroups()
        );

        test.log(
                Status.SKIP,
                result.getName() + " got skipped"
        );

        if (result.getThrowable() != null) {
            test.log(
                    Status.INFO,
                    result.getThrowable().getMessage()
            );
        }
    }

    @Override
    public void onFinish(ITestContext context) {

        extent.flush();

        String reportPath = Paths.get(
                System.getProperty("user.dir"),
                "reports",
                repName
        ).toString();

        File extentReport = new File(reportPath);

        System.out.println("==========================================");
        System.out.println("Extent Report Generated");
        System.out.println("Path: " + extentReport.getAbsolutePath());
        System.out.println("Exists: " + extentReport.exists());
        System.out.println("==========================================");

        try {

            // Open report only when running locally
            if (System.getenv("GITHUB_ACTIONS") == null) {

                if (extentReport.exists()) {
                    Desktop.getDesktop()
                           .browse(extentReport.toURI());
                }
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}