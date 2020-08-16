
import static org.testng.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ParallelTest {
	WebDriver driver;

	@BeforeSuite
	public void disablelogs() {
		System.out.println("disabling logs");
		java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
		System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,
				"C:\\JENA\\RestAssured\\AWSWithDocker\\src\\test\\resources\\logs.txt");
		System.setProperty("webdriver.chrome.silentOutput", "true");
	}

	@BeforeMethod
	@Parameters("browser")
	public void setup(String browser) {
		if (browser.equalsIgnoreCase("chrome")) {
			//
			WebDriverManager.chromedriver().setup();
			// driver = new ChromeDriver();
			DesiredCapabilities cap = new DesiredCapabilities();
			cap.setCapability("browserName", "chrome");
			try {
				//aws url
				//driver = new RemoteWebDriver(new URL("http://13.126.160.225:4444/wd/hub"), cap);
			
				driver = new RemoteWebDriver(new URL("http://ec2-13-126-160-225.ap-south-1.compute.amazonaws.com:4444/wd/hub"), cap);
					
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		else if (browser.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			//driver = new FirefoxDriver();
			DesiredCapabilities cap = new DesiredCapabilities();
			cap.setCapability("browserName", "firefox");
			try {
				driver = new RemoteWebDriver(new URL("http://13.126.160.225:4444/wd/hub"), cap);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		}
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get("https://www.freshworks.com/");

	}

	@Test(priority = 1)
	public void freshWorkslogoTest() {
		boolean flag = false;
		flag = driver.findElement(By.cssSelector("a.logo.logo-fworks")).isDisplayed();
		Assert.assertTrue(flag);
	}

	@Test(priority = 2)
	public void freshWorksTitleTest() {
		System.out.println(driver.getTitle());
		assertEquals(driver.getTitle(), "A fresh approach to customer engagement");
	}

	@Test(priority = 3)
	public void getFooterLinksTest() {
		List<WebElement> footerLinksList = driver.findElements(By.cssSelector("ul.footer-nav li a"));
		footerLinksList.forEach(ele -> System.out.println(ele.getText()));
		assertEquals(footerLinksList.size(), 34);
	}

	@AfterMethod
	public void tearDown() {
		driver.quit();
	}

}