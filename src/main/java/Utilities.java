import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class Utilities {

    private WebDriver driver;

    public Utilities(WebDriver driver) {
    }

    //Method to java scroll with xpath
    public void javaXpathScroll(String xpath) throws InterruptedException
    {
        JavascriptExecutor jse2 = (JavascriptExecutor)driver;
        jse2.executeScript("arguments[0].scrollIntoView()", driver.findElement(By.xpath(xpath)));
    }

}
