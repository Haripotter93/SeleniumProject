package utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
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

    public ExtentSparkReporter sparkReporter;  // UI of the report
    public ExtentReports extent; //populate common info on the report
    public ExtentTest test;  // creating test case entries in the report and update status of the test methods

    String repName;

    @Override
    public void onStart(ITestContext testContext) {


		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		repName = "Test-Report-" + timeStamp + ".html";
		sparkReporter = new ExtentSparkReporter (".\\reports\\" + repName);

        sparkReporter.config().setDocumentTitle("Automation Report");
        sparkReporter.config().setReportName("Functional Testing");
        sparkReporter.config().setTheme(Theme.DARK);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        extent.setSystemInfo("Computer Name", "localhost");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Tester Name", "Pavan");

		String os = testContext.getCurrentXmlTest().getParameter("os"); 
		extent.setSystemInfo("Operating System", os);
		String browser = testContext.getCurrentXmlTest().getParameter("browser"); 
		extent.setSystemInfo("Browser", browser);
		List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups(); 
		if(!includedGroups.isEmpty()) {
			extent.setSystemInfo("Groups", includedGroups.toString());
		}
    }

	@Override
	public void onTestSuccess(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups()); // to display groups in report
		test.log(Status.PASS, result.getName() + " got successfully executed");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());
		test.log(Status.FAIL, result.getName() + " got failed");
		test.log(Status.INFO, result.getThrowable().getMessage());
		try {
			String imgPath = new BaseClass().captureScreen(result.getName());
			test.addScreenCaptureFromPath(imgPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());
		test.log(Status.SKIP, result.getName() + " got skipped");
		test.log(Status.INFO, result.getThrowable().getMessage());
	}

	@Override
	public void onFinish(ITestContext context) {

	    extent.flush();

	    String pathOfExtentReport = System.getProperty("user.dir")
	            + File.separator + "reports"
	            + File.separator + repName;

	    File extentReport = new File(pathOfExtentReport);

	    try {
	        // Open report automatically only when running locally
	        if (System.getenv("GITHUB_ACTIONS") == null) {
	            Desktop.getDesktop().browse(extentReport.toURI());
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


	/*
	* try { 
	* URL url = new	* URL("file:///"+System.getProperty("user.dir")+"\\reports\\"+repName);
	* |
	* // Create the email message
	ImageHtmlEmail email = new ImageHtmlEmail();
	* email.setDataSourceResolver (new DataSourceUrlResolver (url));
	* email.setHostName("smtp.googlemail.com");
	* email.setSmtpPort(465);
	* email.setAuthenticator (new DefaultAuthenticator("hariharana.ja@gmail.com","password")); * email.setSSLOnConnect(true);
	* email.setFrom("hariharana.ja@gmail.com"); //Sender
	* email.setSubject("Test Results");
	* email.setMsg("Please find Attached Report....");
	* email.addTo("pavankumar.busyqa@gmail.com"); //Receiver
	* email.attach (url, "extent report", "please check report...");
	* email.send(); // send the email
	* }
	* catch (Exception e) { e.printStackTrace(); }
	*/
}