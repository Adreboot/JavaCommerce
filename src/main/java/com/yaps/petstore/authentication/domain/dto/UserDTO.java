package com.yaps.petstore.authentication.domain.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.yaps.petstore.domain.dto.AddressDTO;
import com.yaps.petstore.domain.dto.CreditCardDTO;

/**
 * This class follows the Data Transfert Object design pattern.
 * It is a client view of a Customer. This class only
 * transfers data from a distant service to a client.
 */
@SuppressWarnings("serial")
public class UserDTO implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================
	@NotBlank(message = "username must be defined")
    private String username;
	@NotBlank(message = "Invalid customer first name")
    private String firstname;
	@NotBlank(message = "Invalid customer name")
    private String lastname;
	@Size(max=10, message="telephone nb's length exception (10 char.max)")
    private String telephone;
    private String email;
    @NotBlank(message = "password must be defined")
    @Size(min=4, message="password must have at least {min} char.")
    private String password;
    private AddressDTO addressDTO = new AddressDTO();
    private CreditCardDTO creditCardDTO = new CreditCardDTO();
    private String role;


	// ======================================
    // =            Constructors            =
    // ======================================
    public UserDTO() { }
    
    public UserDTO(final String username) {
    	this.username=username;
    }

    public UserDTO(final String username, final String firstname, final String lastname) {
        setUsername(username);
        setFirstname(firstname);
        setLastname(lastname);
    }

    // ======================================
    // =         Getters and Setters        =
    // ======================================
    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
    	this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(final String firstname) {
    	this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(final String lastname) {
    	this.lastname = lastname;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone( String telephone) {
    	this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email) {
    	this.email = email;
    }

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public AddressDTO getAddressDTO() {
		return addressDTO;
	}

	public void setAddressDTO(AddressDTO addressDTO) {
		this.addressDTO = addressDTO;
	}

	public String getStreet1() {
        return addressDTO.getStreet1();
    }

    public void setStreet1( String street1) {
        addressDTO.setStreet1(street1);
    }

    public String getStreet2() {
        return addressDTO.getStreet2();
    }

    public void setStreet2( String street2) {
        addressDTO.setStreet2(street2);
    }

    public String getCity() {
        return addressDTO.getCity();
    }

    public void setCity( String city) {
        addressDTO.setCity(city);
    }

    public String getState() {
        return addressDTO.getState();
    }

    public void setState( String state) {
        addressDTO.setState(state);
    }

    public String getZipcode() {
        return addressDTO.getZipcode();
    }

    public void setZipcode( String zipcode) {
        addressDTO.setZipcode(zipcode);
    }

    public String getCountry() {
        return addressDTO.getCountry();
    }

    public void setCountry( String country) {
        addressDTO.setCountry(country);
    }
    
    public CreditCardDTO getCreditCardDTO() {
		return creditCardDTO;
	}
    
    public void setCreditCardDTO(CreditCardDTO creditCardDTO) {
		this.creditCardDTO=creditCardDTO;
	}

    public String getCreditCardNumber() {
        return creditCardDTO.getCreditCardNumber();
    }

    public void setCreditCardNumber( String creditCardNumber) {
        creditCardDTO.setCreditCardNumber(creditCardNumber);
    }

    public String getCreditCardType() {
        return creditCardDTO.getCreditCardType();
    }

    public void setCreditCardType( String creditCardType) {
        creditCardDTO.setCreditCardType(creditCardType);
    }

    public String getCreditCardExpiryDate() {
        return creditCardDTO.getCreditCardExpiryDate();
    }

    public void setCreditCardExpiryDate( String creditCardExpiryDate) {
        creditCardDTO.setCreditCardExpiryDate(creditCardExpiryDate);
    }

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public void setNullFieldsToBlank() {
		if(getFirstname()==null)
			setFirstname("");
		if(getLastname()==null)
			setLastname("");
		if(getTelephone()==null)
			setTelephone("");
		if(getEmail()==null)
			setEmail("");
		if(getStreet1()==null)
			setStreet1("");
		if(getStreet2()==null)
			setStreet2("");
		if(getCity()==null)
			setCity("");
		if(getState()==null)
			setState("");
		if(getZipcode()==null)
			setZipcode("");
		if(getCountry()==null)
			setCountry("");
		if(getCreditCardNumber()==null)
			setCreditCardNumber("");
		if(getCreditCardType()==null)
			setCreditCardType("");
		if(getCreditCardExpiryDate()==null)
			setCreditCardExpiryDate("");
	}

    public String toString() {
         StringBuffer buf = new StringBuffer();
        buf.append("CustomerDTO{");
        buf.append("id=").append(getUsername());
        buf.append(",firstname=").append(getFirstname());
        buf.append(",lastname=").append(getLastname());
        buf.append(",telephone=").append(getTelephone());
        buf.append(",email=").append(getEmail());
        buf.append(",street1=").append(getStreet1());
        buf.append(",street2=").append(getStreet2());
        buf.append(",city=").append(getCity());
        buf.append(",state=").append(getState());
        buf.append(",zipcode=").append(getZipcode());
        buf.append(",country=").append(getCountry());
        buf.append(",creditCardNumber=").append(getCreditCardNumber());
        buf.append(",creditCardType=").append(getCreditCardType());
        buf.append(",creditCardExpiryDate=").append(getCreditCardExpiryDate());
        buf.append('}');
        return buf.toString();
    }
}
