package testCases;
import org.testng.annotations.Test;
import testBase.BaseClass;
import utilities.DataProviders;

public class ExcelDataTest extends BaseClass {

	@Test(dataProvider = "TestData", dataProviderClass = DataProviders.class)
	public void excelDataTest(String email, String pswd, String exp) {
		logger.info("----Excel test start----");
			try {
				System.out.println("Email : " + email + "\t" + "Password : " + pswd + "\t" + "Valid/Invalid : " + exp);
			} catch(Exception e) {
				System.out.println(e);
			}
			logger.info("----Excel test end----");
	}
}
