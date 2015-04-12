package hosocongty;

/**
 * Description
 *
 * @author minhho242 on 3/26/15.
 */
public class CompanyDTO {
    
    public CompanyDTO(Company company) {
        director = company.getDirector().getValue();
        name = company.getName().getValue();
        address = company.getAddress().getValue();
        transactionName = company.getTransactionName().getValue();
        taxCode = company.getTaxCode().getValue();
        businesses = company.getBusinesses().getValue();
        businessLicense = company.getBusinessLicense().getValue();
        businessLicenseIssueDate = company.getBusinessLicenseIssueDate().getValue();
        phone = company.getPhoneNumber().getValue();
        operationDate = company.getOperationDate().getValue();
        type = 1;
    }
    
    private Long id;

    public CompanyDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(String operationDate) {
        this.operationDate = operationDate;
    }

    public String getBusinesses() {
        return businesses;
    }

    public void setBusinesses(String businesses) {
        this.businesses = businesses;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getBusinessLicenseIssueDate() {
        return businessLicenseIssueDate;
    }

    public void setBusinessLicenseIssueDate(String businessLicenseIssueDate) {
        this.businessLicenseIssueDate = businessLicenseIssueDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String director;
    private String taxCode;
    private String operationDate;
    private String businesses;
    private String businessLicense;
    private String businessLicenseIssueDate;
    private String phone;
    private String address;
    private String transactionName;
    private String name;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;
    
}
