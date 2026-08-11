package utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private static ExtentSparkReporter sparkReporter;
    private static ExtentReports extent;
    private static String repName;

    /*
     * Create one Extent report for each TestNG <test>.
     *
     * Example:
     *
     * Linux-Chrome  -> reports/Linux-Chrome.html
     * Linux-Firefox -> reports/Linux-Firefox.html
     */
    @Override
    public synchronized void onStart(ITestContext testContext) {

        Path reportsDirectory = Paths.get(
                System.getProperty("user.dir"),
                "reports"
        );

        try {
            Files.createDirectories(reportsDirectory);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Unable to create reports directory",
                    e
            );
        }

        /*
         * Get the TestNG <test> name.
         */
        String testName = testContext.getName();

        /*
         * Make the test name safe for use as a file name.
         */
        repName = testName.replaceAll(
                "[\\\\/:*?\"<>|]",
                "_"
        ) + ".html";

        Path reportPath = reportsDirectory.resolve(repName);

        System.out.println("==========================================");
        System.out.println("Creating Extent Report");
        System.out.println("Test Name  : " + testName);
        System.out.println("Report Path: " + reportPath.toAbsolutePath());
        System.out.println("==========================================");

        sparkReporter = new ExtentSparkReporter(
                reportPath.toString()
        );

        sparkReporter.config().setDocumentTitle(
                "Automation Test Report - " + testName
        );

        sparkReporter.config().setReportName(
                "Selenium Automation Test Report - " + testName
        );

        sparkReporter.config().setTheme(
                Theme.DARK
        );

        extent = new ExtentReports();

        extent.attachReporter(sparkReporter);

        extent.setSystemInfo(
                "Computer Name",
                "LPT1079"
        );

        extent.setSystemInfo(
                "Environment",
                "QA"
        );

        extent.setSystemInfo(
                "Tester Name",
                "Pavan"
        );

        /*
         * Read OS and browser parameters from TestNG XML.
         */
        if (testContext.getCurrentXmlTest() != null) {

            String os = testContext
                    .getCurrentXmlTest()
                    .getParameter("os");

            String browser = testContext
                    .getCurrentXmlTest()
                    .getParameter("browser");

            if (os != null) {
                extent.setSystemInfo(
                        "Operating System",
                        os
                );
            }

            if (browser != null) {
                extent.setSystemInfo(
                        "Browser",
                        browser
                );
            }

            List<String> includedGroups =
                    testContext
                            .getCurrentXmlTest()
                            .getIncludedGroups();

            if (includedGroups != null &&
                    !includedGroups.isEmpty()) {

                extent.setSystemInfo(
                        "Groups",
                        includedGroups.toString()
                );
            }
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        ExtentTest test = extent.createTest(
                result.getTestClass().getName()
                        + " - "
                        + result.getName()
        );

        test.assignCategory(
                result.getMethod().getGroups()
        );

        test.log(
                Status.PASS,
                result.getName()
                        + " got successfully executed"
        );
    }

    @Override
    public void onTestFailure(ITestResult result) {

        ExtentTest test = extent.createTest(
                result.getTestClass().getName()
                        + " - "
                        + result.getName()
        );

        test.assignCategory(
                result.getMethod().getGroups()
        );

        test.log(
                Status.FAIL,
                result.getName()
                        + " got failed"
        );

        if (result.getThrowable() != null) {

            test.log(
                    Status.FAIL,
                    result.getThrowable().toString()
            );
        }

        try {

            String imgPath =
                    new BaseClass()
                            .captureScreen(result.getName());

            if (imgPath != null) {

                test.addScreenCaptureFromPath(
                        imgPath
                );
            }

        } catch (Exception e) {

            test.log(
                    Status.WARNING,
                    "Unable to attach screenshot: "
                            + e.getMessage()
            );
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        ExtentTest test = extent.createTest(
                result.getTestClass().getName()
                        + " - "
                        + result.getName()
        );

        test.assignCategory(
                result.getMethod().getGroups()
        );

        test.log(
                Status.SKIP,
                result.getName()
                        + " got skipped"
        );

        if (result.getThrowable() != null) {

            test.log(
                    Status.SKIP,
                    result.getThrowable().toString()
            );
        }
    }

    @Override
    public synchronized void onFinish(ITestContext context) {

        if (extent != null) {

            extent.flush();

            Path reportPath = Paths.get(
                    System.getProperty("user.dir"),
                    "reports",
                    repName
            );

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "Extent Report Generated"
            );

            System.out.println(
                    "Test Name : " + context.getName()
            );

            System.out.println(
                    "Path      : "
                            + reportPath.toAbsolutePath()
            );

            System.out.println(
                    "=========================================="
            );

            /*
             * Open report only on local machine.
             *
             * GitHub Actions does not have a desktop.
             */
            if (System.getenv("GITHUB_ACTIONS") == null) {

                try {

                    File report = reportPath.toFile();

                    if (report.exists() &&
                            Desktop.isDesktopSupported()) {

                        Desktop.getDesktop()
                                .browse(report.toURI());
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            /*
             * IMPORTANT:
             *
             * Reset these so the next TestNG <test>
             * creates its own report.
             */
            extent = null;
            sparkReporter = null;
        }
    }
}

