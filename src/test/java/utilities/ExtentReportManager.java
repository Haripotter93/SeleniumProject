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
     * Create report only once for the complete Maven/TestNG execution.
     */
    @Override
    public synchronized void onStart(ITestContext testContext) {

        // Prevent creation of multiple reports
        if (extent != null) {
            return;
        }

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
         * One fixed report name.
         * This prevents multiple timestamped reports.
         */
        repName = "ExtentReport.html";

        Path reportPath = reportsDirectory.resolve(repName);

        System.out.println("==========================================");
        System.out.println("Creating Extent Report");
        System.out.println("Report Path: " + reportPath.toAbsolutePath());
        System.out.println("==========================================");

        sparkReporter = new ExtentSparkReporter(
                reportPath.toString()
        );

        sparkReporter.config().setDocumentTitle(
                "Automation Test Report"
        );

        sparkReporter.config().setReportName(
                "Selenium Automation Test Report"
        );

        sparkReporter.config().setTheme(
                Theme.DARK
        );

        extent = new ExtentReports();

        extent.attachReporter(sparkReporter);

        extent.setSystemInfo(
                "Computer Name",
                "GitHub Actions"
        );

        extent.setSystemInfo(
                "Environment",
                "QA"
        );

        extent.setSystemInfo(
                "Tester Name",
                "Pavan"
        );

        // XML parameter: OS
        if (testContext.getCurrentXmlTest() != null) {

            String os = testContext
                    .getCurrentXmlTest()
                    .getParameter("os");

            if (os != null) {
                extent.setSystemInfo(
                        "Operating System",
                        os
                );
            }

            // XML parameter: Browser
            String browser = testContext
                    .getCurrentXmlTest()
                    .getParameter("browser");

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

        /*
         * Flush the single report.
         */
        if (extent != null) {

            extent.flush();

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "Extent Report Generated"
            );

            System.out.println(
                    "Path: "
                            + Paths.get(
                                    System.getProperty("user.dir"),
                                    "reports",
                                    repName
                            ).toAbsolutePath()
            );

            System.out.println(
                    "=========================================="
            );
        }

        /*
         * Open report only on local machine.
         *
         * GitHub Actions does NOT have a desktop.
         */
        if (System.getenv("GITHUB_ACTIONS") == null) {

            try {

                Path reportPath = Paths.get(
                        System.getProperty("user.dir"),
                        "reports",
                        repName
                );

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
    }
}