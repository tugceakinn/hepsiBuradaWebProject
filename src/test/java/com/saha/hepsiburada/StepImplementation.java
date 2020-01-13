package com.saha.hepsiburada;


import com.saha.hepsiburada.helper.ElementHelper;
import com.saha.hepsiburada.helper.StoreHelper;
import com.saha.hepsiburada.model.ElementInfo;
import com.saha.slnarch.core.driver.DriverAction;
import com.saha.slnarch.core.element.Element;
import com.saha.slnarch.di.helper.InjectionHelper;
import com.thoughtworks.gauge.Step;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class StepImplementation {

    @Inject
    Element element;
    @Inject
    DriverAction driverAction;
    @Inject
    WebDriver driver;

    Logger logger = LoggerFactory.getLogger(getClass());

    public StepImplementation() {
        InjectionHelper.getInstance().getFeather().injectFields(this);
    }


    @Step("<key> li elemente tikla")
    public void GirisYap(String key) {
        ElementInfo elmntInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By by = ElementHelper.getElementInfoToBy(elmntInfo);

        driver.findElement(by).click();
    }

    @Step("sdasd li elemente tikla")
    public void butonTik() {


        WebElement as = driver.findElement(By.cssSelector("#login > div > a"));

        as.click();

        as.getText();
    }


    @Step("<seconds> saniye bekle")
    public void bekle(int seconds) throws Exception {
        Thread.sleep(seconds * 1000);
    }


    @Step("<key> li elemente <text> degerini yaz")
    public void degerYaz(String key, String text) {
        ElementInfo elmntInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By by = ElementHelper.getElementInfoToBy(elmntInfo);
        driver.findElement(by).sendKeys(text);
    }

    @Step("<key> kataloğundaki <index>. kitaba tıkla")
    public void indexeTikla(String key, int index) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By by = ElementHelper.getElementInfoToBy(elementInfo);
        List<WebElement> list = driver.findElements(by);
        list.get(index).click();
    }




    @Step({"Click element with key <key> with js",
            "js ile <key> li elemente tikla"})
    public void jsClickElement(String key) {

        ElementInfo elmntinfo = StoreHelper.INSTANCE.findElementInfoByKey(key);

        By by = ElementHelper.getElementInfoToBy(elmntinfo);

        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", by);

    }

    @Step({"<key> li elemente mousehover yap"})
    public void mousehoverWithKey(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                driver.findElement(ElementHelper.getElementInfoToBy(elementInfo)));
    }


    @Step("Login Kontrol")
    public void LoginKontrol() {
        WebElement webElement = driver.findElement(By.cssSelector("#cartItemCount > label"));

        String sepetKontrol = webElement.getText();
        System.out.println("Sepet => " + sepetKontrol);
        Assert.assertTrue("login başarısız", sepetKontrol.substring(0, 1).contains("0"));

    }

    public List<WebElement> findElementsByKey(String key) {
        return driver.findElements(ElementHelper.getElementInfoToBy(StoreHelper.INSTANCE.findElementInfoByKey(key)));
    }

    public WebElement findElementByKey(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        return findElementsByKey(key).get(elementInfo.getIndex());

    }

    @Step("GetText <text> Kontrol <key>")
    public void Kontrol(String text, String key) {
        WebElement webElement = findElementByKey(key);
        String elementText = webElement.getText();
        System.out.println("Text => " + elementText);
        Assert.assertTrue(text + " Kontrolu basarisiz!!", elementText.contains(text));
    }

    private void ClickElement(String key){
        WebElement webElement = findElementByKey(key);
        webElement.click();
    }



    @Step("Sepette <key> ürün var ise  <silButonu> tikla")
    public void Kontrol2( String key, String silButonu) throws InterruptedException {
        int miktar = Integer.parseInt(findElementByKey(key).getText());
        int sayi=0;
        while (miktar>sayi){
            if (miktar != 0) {
                Thread.sleep(5000);
                WebElement webElement1 = driver.findElement(By.id("CartButton"));
                webElement1.click();
                Thread.sleep(5000);
                ClickElement(silButonu);
                System.out.println("Sepetten ürün silindi ...");
                miktar--;
            }
        }
        findElementByKey(key).click();
        driver.findElement(By.xpath("/html//div[@id='page']//a[@href='/']/span[.='hepsiburada.com']")).click();

/*
        else if(miktar == 0) {
            Thread.sleep(5000);
            System.out.println("Sepette ürün kalmadığı için anasayfaya dönülüyor");
            driver.findElement(By.xpath("/html/body/div[3]/div[2]/div[2]/a")).click();
        }
*/
    }


    @Step("<key> alanının üstüne gel")
    public void mouseHoverElementStep(String key) {
        mouseHoverElement(key);
    }

    public void mouseHoverElement(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By byElement = ElementHelper.getElementInfoToBy(elementInfo);
        element.finds(byElement)
                .scrollTo();
        logger.info("Mouse " + key + "elementinin üstüne kaydırıldı.");
    }

    @Step({"<saniye> saniye bekle2"})
    public void waitBySeconds2(int seconds) throws InterruptedException {
        waitByMillis2(seconds * 1000);
    }

    @Step({"<milli> milisaniye bekle2"})
    public void waitByMillis2(int millis) throws InterruptedException {
        Thread.sleep(millis);
    }

    public static int randomNumber(int start, int end) {
        Random rn = new Random();
        int randomNumber = rn.nextInt(end - 1) + start;
        return randomNumber;
    }

    @Step("Hover random category <category> and click subcategory <subcategory>")
    public void randomClick(String category, String subCategory) throws InterruptedException {


        List<WebElement> categoryList = findElementsByKey(category);
        int randNum = randomNumber(0, categoryList.size());
        WebElement getCategory = categoryList.get(randNum);

        Actions builder = new Actions(driver);
        Actions hoverOverRegistrar = builder.moveToElement(getCategory);
        hoverOverRegistrar.perform();

        Thread.sleep(2000);
        List<WebElement> subCategoryList = findElementsByKey(subCategory);

        int randomSubCategory = randomNumber(0, subCategoryList.size());

        WebElement getSubCategory = subCategoryList.get(randomSubCategory);

        getSubCategory.click();
    }

    @Step("Rastgele Tıklama <marka>")
    public void randomMarka(String marka) {
        List<WebElement> markalar = findElementsByKey(marka);

        int randMarka = randomNumber(0, markalar.size());

        markalar.get(randMarka).click();
    }

    @Step("Urun tutarini <tutar> ve urun ismini <isim> csv dosyasına yazdirma")
    public void csvWriter(String tutar, String isim) {
        try (PrintWriter csvWriter = new PrintWriter("/Users/sahabt/Desktop/csvFile/urunDetay.csv")) {
            WebElement urunAdiElement = findElementByKey(isim);
            WebElement urunTutarElement = findElementByKey(tutar);
            String urunAdi = urunAdiElement.getText();
            String urunTutar = urunTutarElement.getText();
            csvWriter.append("\n");
            csvWriter.append(String.join(";", urunAdi));
            csvWriter.append(String.join(";", urunTutar));


           // csvWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Step("Urun toplam degeri <deger> ve kargo tutarini <kargotutari> csv dosyasına yazdirma")
    public void csvWriter2(String deger, String kargotutari) {
        try (PrintWriter csvWriter = new PrintWriter("/Users/sahabt/Desktop/csvFile/urunDetay.csv")) {
            WebElement urunAdiElement = findElementByKey(kargotutari);
            WebElement urunTutarElement = findElementByKey(deger);
            String urunAdi = urunAdiElement.getText();
            String urunTutar = urunTutarElement.getText();
            csvWriter.append("\n");
            csvWriter.append(String.join(";", urunAdi));
            csvWriter.append(String.join(";", urunTutar));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Step("<key> li elementi bul ve değerini <saveKey> saklanan değer ile karşılaştır")
    public void saveKeyKarsilastirma(String key, String saveKey) throws InterruptedException {

        //StoreHelper.INSTANCE.saveValue(saveKey, findElementByKey(key).getText());

        String saklanan=StoreHelper.INSTANCE.getValue(key);
        int miktar = Integer.parseInt(driver.findElement(By.cssSelector("span#cartItemCount > label")).getText());

        if(miktar>0) {
            for (int i = 0; i < miktar; i++) {
                driver.findElement(By.id("addToCart"));
                Thread.sleep(2000);
                Assert.assertEquals(StoreHelper.INSTANCE.getValue(saveKey), findElementByKey(saklanan).getText());
            }
        }

    }

    @Step("<key> li elementi sepetteki <text> karşılaştır")
    public void karsilastirma(String key, String text) {
        driver.findElement(By.cssSelector("button[title='Arttır']")).click();
        String saklanan=StoreHelper.INSTANCE.getValue(key);
        driver.findElement(By.id("addToCart")).click();
        String str=findElementByKey(text).getText();
        Assert.assertEquals("hata mesajı", text, str.trim());
    }

    @Step("Adet sayisi 2 arttirilir")
    public void adetArttirma(){
        driver.findElement(By.cssSelector("button[title='Arttır']")).click();
        driver.findElement(By.cssSelector("button[title='Arttır']")).click();
    }

    @Step("Adres Ekle")
    public void adresEkle() {
        driver.findElement(By.id("first-name")).sendKeys("test");
        driver.findElement(By.id("last-name")).sendKeys("test");
        driver.findElement(By.xpath("//*[@id=\"form-address\"]/div/div/section[2]/div[3]/div/div/button/span[2]")).click();
        driver.findElement(By.xpath("//*[@id=\"form-address\"]/div/div/section[2]/div[3]/div/div/button/span[2]")).sendKeys("üsküdar");
        driver.findElement(By.xpath("//*[@id=\"form-address\"]/div/div/section[2]/div[3]/div/div[1]/div/ul/li[39]/a/span")).click();
        driver.findElement(By.id("address")).sendKeys("test");
        driver.findElement(By.id("address-name")).sendKeys("test");
        driver.findElement(By.id("phone")).sendKeys("555 555 55 55");
        driver.findElement(By.xpath("//div[@id='short-summary']//button[@class='btn btn-primary full']/span[@class='text']")).click();
    }

}
