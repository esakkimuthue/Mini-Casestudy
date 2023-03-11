package Miniproject;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import io.github.bonigarcia.wdm.WebDriverManager;

public class demoblazeTest 
{
	WebDriver driver;
	Properties prop;
	Actions actions;
	ExtentReports reports;
	ExtentSparkReporter spark;
    ExtentTest extentTest;


@BeforeClass(groups="featureOne")
public void setupExtent() 
	{

	reports=new ExtentReports(); 
	
	spark=new ExtentSparkReporter("target\\Demoblaze.html");
	
	reports.attachReporter(spark);

	}

@BeforeTest(groups="featureOne")
public void CheomeBrowser() throws IOException
	{
		WebDriverManager.chromedriver().setup();
		driver=new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://www.demoblaze.com/");
		String path=System.getProperty("user.dir")+"\\src\\test\\resources\\ConfigFiles\\config.properties";
	    prop=new Properties();
		FileInputStream obtained = new FileInputStream(path);
		prop.load(obtained);
		driver.get(prop.getProperty("url"));
		driver.get("https://www.demoblaze.com/");
}
@Test(priority=1,groups="featureOne")
public void login() throws InterruptedException
{   
	extentTest=reports.createTest("login");
	driver.findElement(By.cssSelector("#login2")).click();
	Thread.sleep(2000);
	driver.findElement(By.cssSelector("#loginusername")).sendKeys(prop.getProperty("username"));
	driver.findElement(By.cssSelector("#loginpassword")).sendKeys(prop.getProperty("password"));
	Thread.sleep(3000);
	driver.findElement(By.cssSelector("button[onclick='logIn()']")).click();
}

@Test (priority=2)
public void selectitem() throws InterruptedException 
{
	extentTest=reports.createTest("selectitem");
    Thread.sleep(3000);
    driver.findElement(By.xpath("//h4/a[contains(text(),'Samsung galaxy s6')]")).click();
    Thread.sleep(2000);
    driver.findElement(By.xpath("//div/a[@onclick='addToCart(1)']")).click();
    Thread.sleep(1000);
    Alert alert=driver.switchTo().alert();
    alert.accept();

}

@Test(dataProvider="Register",priority=3)
public void ClickMultipleItems(String categories,String item1) throws InterruptedException
{
	extentTest=reports.createTest("ClickMultipleItems");
 //  String[] prodetails= {iteml, item2, item3, item4};
//   for (String product:prodetails)   	   
	driver.findElement(By.xpath("//li/a[contains(text(),'Home')]")).click();
	String strpath="//a[text()='"+categories+"']"; 
	driver.findElement(By.xpath(strpath)).click();
	Thread.sleep(2000);
	driver.findElement(By.partialLinkText(item1)).click();
	Thread.sleep (2000);
	driver.findElement(By.xpath("//div/a[@class='btn btn-success btn-lg']")).click();
	Thread.sleep (2000);
	Alert alert=driver.switchTo().alert();
	System.out.println(item1+","+alert.getText());
    alert.accept();
	

   
}

@Test (priority=4)
public void delteitem() throws InterruptedException 
{ 
   extentTest=reports.createTest("deleteitems");
   driver.findElement(By.partialLinkText("Home")).click();
   Thread.sleep (2000);
   driver.findElement(By.partialLinkText("Cart")).click(); 
   Thread.sleep (4000);
   driver.findElement(By.xpath("//td/a")).click();
   
}
@Test (priority=5)
public void palceorder () throws InterruptedException 
{ 
   extentTest=reports.createTest("placeorder");
   Thread.sleep (3000);
   
   driver.findElement(By.cssSelector("button[data-toggle='modal']")).click(); 
   Thread.sleep (3000);
   driver.findElement(By.cssSelector("#name")).sendKeys("name");
   driver.findElement(By.cssSelector("#country")).sendKeys("country"); 
   driver.findElement(By.cssSelector("#city")).sendKeys("city");
   driver.findElement(By.cssSelector("#card")).sendKeys("card");
   driver.findElement(By.cssSelector("#month")).sendKeys("month"); 
   driver.findElement(By.cssSelector("#year")).sendKeys("year"); 
   Thread.sleep (3000);
   driver.findElement(By.cssSelector("button[onclick='purchaseOrder()']")).click(); 
   Thread.sleep (1000);
   driver.findElement(By.cssSelector("button[tabindex='1']")).click();
   WebElement Postpuschase = driver.findElement(By.xpath("//h2[contains(text(),'Thank You')]"));
   Assert.assertEquals(Postpuschase.getText(),"Thank You for your Order");
   driver.findElement(By.cssSelector("button[tabindex='1']")).click();
   
}
@DataProvider(name="Register")
public Object[][] getData() throws CsvValidationException,IOException
{
	  String path=System.getProperty("user.dir")+"//src//test//resources//TestData//MultiItem.csv";
	  String[] cols;
	  CSVReader reader = new CSVReader(new FileReader(path));
	  ArrayList<Object> dataList=new ArrayList<Object>();
	  while((cols=reader.readNext())!=null)
	  {
		  Object[] record={cols[0], cols[1]};
		  dataList.add(record);
	  }
	  return dataList.toArray(new Object[dataList.size()][]);
	  
}
@AfterTest
public void close() 
{
   driver.close();
   reports.flush();
}
}