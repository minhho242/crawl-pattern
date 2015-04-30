package hosocongty;

import model.Item;
import model.Webbot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import util.HoSoCongTyUtils;
import util.Utils;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Description
 *
 * @author minhho242 on 3/23/15.
 */
public class Company {

    public static Company extractCompany(String URL) throws IOException {
        Webbot webbot = new Webbot(URL);
        String htmlData = webbot.crawl(Webbot.METHOD_GET);

        Company company = new Company();

        Document document = Jsoup.parse(htmlData);
        Field[] fields = company.getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                Object temp = field.get(company);
                if ( temp instanceof Item) {
                    Item item = (Item)temp;
                    String value = item.extractValue(document);
                    item.setValue(value);
                }
            } catch (Exception e) {
                System.out.println("Debug");
            }
        }
        return company;
    }

    public Item getName() {
        return name;
    }

    public void setName(Item name) {
        this.name = name;
    }

    public Item getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(Item taxCode) {
        this.taxCode = taxCode;
    }

    public Item getAddress() {
        return address;
    }

    public void setAddress(Item address) {
        this.address = address;
    }

    public Item getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(Item businessLicense) {
        this.businessLicense = businessLicense;
    }

    public Item getBusinessLicenseIssueDate() {
        return businessLicenseIssueDate;
    }

    public void setBusinessLicenseIssueDate(Item businessLicenseIssueDate) {
        this.businessLicenseIssueDate = businessLicenseIssueDate;
    }

    public Item getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Item operationDate) {
        this.operationDate = operationDate;
    }

    public Item getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Item phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Item getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(Item transactionName) {
        this.transactionName = transactionName;
    }

    public Item getDirector() {
        return director;
    }

    public void setDirector(Item director) {
        this.director = director;
    }

    public Item getBusinesses() {
        return businesses;
    }

    public void setBusinesses(Item businesses) {
        this.businesses = businesses;
    }

    public Item name = new Item("name") {
        @Override
        public String extractValue(Document document) {
            return Utils.toTitleCase(HoSoCongTyUtils.extractByCSSQuery(document, "h1"));
        }
    };

    public Item taxCode = new Item("taxCode") {
        @Override
        public String extractValue(Document document) {
            return HoSoCongTyUtils.extractValueFromImageAndMatchingText(document, "Mã số thuế");
        }
    };

    public Item address = new Item("address") {
        @Override
        public String extractValue(Document document) {
            return HoSoCongTyUtils.extractValueMatchingText(document, "Địa chỉ");
        }
    };

    public Item businessLicense = new Item("businessLicense") {
        @Override
        public String extractValue(Document document) {
            return HoSoCongTyUtils.extractBusinessLicense(document);
        }
    };

    public Item businessLicenseIssueDate = new Item("businessLicenseIssueDate") {
        @Override
        public String extractValue(Document document) {
            return HoSoCongTyUtils.extractBusinessLicenseIssueDate(document);
        }
    };

    public Item operationDate = new Item("operationDate") {
        @Override
        public String extractValue(Document document) {
            return HoSoCongTyUtils.extractValueFromImageAndMatchingText(document, "Ngày hoạt động");
        }
    };

    public Item phoneNumber = new Item("phoneNumber") {
        @Override
        public String extractValue(Document document) {
            return HoSoCongTyUtils.extractValueFromImageAndMatchingText(document, "Điện thoại");
        }
    };

    public Item transactionName = new Item("transactionName") {
        @Override
        public String extractValue(Document document) {
            return HoSoCongTyUtils.extractValueMatchingText(document, "Tên giao dịch");
        }
    };

    public Item director = new Item("director") {
        @Override
        public String extractValue(Document document) {
            HoSoCongTyUtils.extractBusinesses(document);
            return HoSoCongTyUtils.extractValueMatchingText(document, "Giám đốc");
        }
    };

    public Item businesses = new Item("businesses") {
        @Override
        public String extractValue(Document document) {
            return HoSoCongTyUtils.extractBusinesses(document);
        }
    };

    @Override
    public String toString() {
        String newLineChar = System.getProperty("line.separator");
        Field[] fields = this.getClass().getDeclaredFields();
        StringBuilder resultBuidler = new StringBuilder();
        for (Field field : fields ) {
            try {
                Object temp = field.get(this);
                if ( temp instanceof Item ) {
                    Item item = (Item) temp;
                    resultBuidler.append(item.getName()).append(": ").append(item.getValue()).append(newLineChar);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return resultBuidler.toString();
    }
}
