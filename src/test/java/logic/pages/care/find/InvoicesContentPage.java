package logic.pages.care.find;

import framework.config.Config;
import logic.business.helper.MiscHelper;
import logic.pages.BasePage;
import logic.pages.TableControlBase;
import logic.utils.Common;
import framework.utils.Pdf;
import framework.utils.RandomCharacter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.HashMap;
import java.util.List;

public class InvoicesContentPage extends BasePage {

    private static final String invoiceNumber = "Invoice Number";
    private static final String status = "Status";
    private static final String amountOutstanding = "Amount Outstanding";
    private static final String dateIssued = "Date Issued";
    private static final String amount = "Amount";
    String pdfFile;

    private static InvoicesContentPage instance = new InvoicesContentPage();
    public static InvoicesContentPage getInstance() {
        return new InvoicesContentPage();
    }

    @FindBy(xpath = "//td[@class='informationBoxHeader' and contains(text(),'Invoices')]/../../..//following-sibling::div//table")
    WebElement invoicesGridTable;
    TableControlBase table = new TableControlBase(invoicesGridTable);

    public List<WebElement> getInvoices(List<HashMap<String, String>> invoice) {
        return table.findRowsByColumns(invoice);
    }

    public int getRowNumberOfInvoiceTable() {
        return table.getRowsCount();
    }

    public String getInvoiceNumber() {
        return table.getElementByColumnNameAndRowIndex(2, "Invoice Number").getText().split(" ")[0];
    }

    public void clickInvoiceNumberByIndex(int index) {
        table.getElementByColumnNameAndRowIndex(index + 1, invoiceNumber).findElement(By.tagName("a")).click();
    }

    public String getStatusByIndex(int index){
        return table.getElementByColumnNameAndRowIndex(index+1, "Status").getText();
    }

    public static class InvoiceDetailsContentPage extends InvoicesContentPage{

        private static InvoiceDetailsContentPage instance;
        public static InvoiceDetailsContentPage getInstance() {
            return new InvoiceDetailsContentPage();
        }

        @FindBy(xpath = "//td[contains(text(),'Invoice Details')]/following-sibling::td[1]//a")
        WebElement btnViewPdf;

        @FindBy(xpath = "//td[contains(text(),'Issued:')]/following-sibling::td[1]")
        WebElement issued;

        @FindBy(xpath = "//td[contains(text(),'Due Date:')]/following-sibling::td[1]")
        WebElement dueDate;

        @FindBy(xpath = "//td[contains(text(),'Status:')]/following-sibling::td[1]")
        List<WebElement> status;

        @FindBy(xpath = "//td[contains(text(),'End:')]/following-sibling::td[1]")
        WebElement end;

        public void saveFileFromWebRequest(String customerNumber){
            String [] param = btnViewPdf.getAttribute("href").split(",");
            String fileToDownloadLocation = String.format("%scustomer/CustomerInvoice.aspx?item=invoice&rootbuid=%s&invid=%s", Config.getProp("careUrl"), Common.stripNonDigits(param[0]), Common.stripNonDigits(param[1]));
            pdfFile = String.format("TC_%s_%s.pdf", customerNumber, RandomCharacter.getRandomNumericString(9));
            MiscHelper.saveFileFromWebRequest(btnViewPdf, fileToDownloadLocation, pdfFile);
        }

        public List<String> getListInvoiceContent(int startPage){
            return Pdf.getInstance().getText(System.getProperty("user.home")+"\\Desktop\\QA_Project\\" + pdfFile, startPage);
        }

        public String getPathOfPdfFile(){
            return System.getProperty("user.home")+"\\Desktop\\QA_Project\\" + pdfFile;
        }

        public String getIssued() {
            return getTextOfElement(issued);
        }

        public String getDueDate() {
            return getTextOfElement(dueDate);
        }

        public String getStatus(int index) {
            return getTextOfElement(status.get(1));
        }

        public String getEnd() {
            return getTextOfElement(end);
        }
    }

}
