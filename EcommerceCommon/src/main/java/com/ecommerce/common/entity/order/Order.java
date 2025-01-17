package com.ecommerce.common.entity.order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ecommerce.common.entity.Address;
import com.ecommerce.common.entity.Customer;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "first_name", nullable = false, length = 45)
	private String firstName;
	
	@Column(name = "last_name", nullable = false, length = 45)
	private String lastName;
	
	@Column(name = "phone_number", nullable = false, length = 15)
	private String phoneNumber;
	
	@Column(length = 64, nullable = false, name = "address_line_1")
	private String addressLine1;
	
	@Column(length = 64, name = "address_line_2")
	private String addressLine2;
	
	@Column(length = 45, nullable = false)
	private String city;
	
	@Column(length = 45, nullable = false)
	private String state;
	
	@Column(name = "postal_code",length = 10, nullable = false)
	private String postalCode;
	
	@Column(length = 45, nullable = false)
	private String country;
	
	private Date orderTime;
	
	private float shippingCost;
	private float productCost;
	private float subtotal;
	private float tax;
	private float total;
	
	private int deliverDays;
	
	private Date deliverDate;
	
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	
	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	@Transient
	private String status1;	
	
	public String getStatus1() {
		return status1;
	}

	public void setStatus1(String status1) {
		this.status1 = status1;
	}
	@ManyToOne
	@JoinColumn(name = "customer_id")
	public Customer customer;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<OrderDetail> orderDetails = new HashSet<>();

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("updatedTime ASC")
	private List<OrderTrack> orderTracks = new ArrayList<>();
	
	
	
	public Order() {
	}

	public Order(Integer id, Date orderTime, float productCost, float subtotal, float total) {
		this.id = id;
		this.orderTime = orderTime;
		this.productCost = productCost;
		this.subtotal = subtotal;
		this.total = total;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public float getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(float shippingCost) {
		this.shippingCost = shippingCost;
	}

	public float getProductCost() {
		return productCost;
	}

	public void setProductCost(float productCost) {
		this.productCost = productCost;
	}

	public float getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(float subtotal) {
		this.subtotal = subtotal;
	}

	public float getTax() {
		return tax;
	}

	public void setTax(float tax) {
		this.tax = tax;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public int getDeliverDays() {
		return deliverDays;
	}

	public void setDeliverDays(int deliverDays) {
		this.deliverDays = deliverDays;
	}

	public Date getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(Set<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}
	
	public void copyAddressFromCustomer() {
		setFirstName(customer.getFirstName());
		setLastName(customer.getLastName());
		setPhoneNumber(customer.getPhoneNumber());
		setAddressLine1(customer.getAddressLine1());
		setAddressLine2(customer.getAddressLine2());
		setCity(customer.getCity());
		setCountry(customer.getCountry().getName());
		setPostalCode(customer.getPostalCode());
		setState(customer.getState());
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", subtotal=" + subtotal + ", paymentMethod=" + paymentMethod + ", status=" + status
				+ ", customer=" + customer.getFullName() + "]";
	}
	
	@Transient
	public String getDestination() {
		String destination = city + ", ";
		if(state != null && !state.isEmpty()) destination += state + ", ";
		destination += country;
		return destination; 
	}

	public void copyShippingAddress(Address address) {
		setFirstName(address.getFirstName());
		setLastName(address.getLastName());
		setPhoneNumber(address.getPhoneNumber());
		setAddressLine1(address.getAddressLine1());
		setAddressLine2(address.getAddressLine2());
		setCity(address.getCity());
		setCountry(address.getCountry().getName());
		setPostalCode(address.getPostalCode());
		setState(address.getState());
	}
	
	@Transient
	public String getShippingAddress() {
	String address = firstName;
		
		if(lastName != null && !lastName.isEmpty()) address += " " + lastName;
		
		if(!addressLine1.isEmpty()) address += ", " + addressLine1;
		
		if(addressLine2 != null && !addressLine2.isEmpty()) address += ", " + addressLine2;
		
		if(!city.isEmpty()) address += ", " + city;
		
		if(state != null && !state.isEmpty()) address += ", " + state;
		
		address += ", " + country;
		
		if(!postalCode.isEmpty()) address += ". Postal Code: " + postalCode;
		
		if(!phoneNumber.isEmpty()) address += ". Phone Number: " + phoneNumber;
		
		return address;
	}

	public List<OrderTrack> getOrderTracks() {
		return orderTracks;
	}

	public void setOrderTracks(List<OrderTrack> orderTracks) {
		this.orderTracks = orderTracks;
	}
	
	@Transient
	public String getDeliverDateOnForm() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormatter.format(this.deliverDate);
	}

	public void setDeliverDateOnForm(String dateString) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			this.deliverDate = dateFormatter.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getRecipientName() {
		String name = firstName;
		
		if(lastName != null && !lastName.isEmpty()) name += " " + lastName;
		return name;
	}
	
	@Transient
	public String getRecipientAddress() {
	String address = addressLine1 ;
		
		
		if(addressLine2 != null && !addressLine2.isEmpty()) address += ", " + addressLine2;
		
		if(!city.isEmpty()) address += ", " + city;
		
		if(state != null && !state.isEmpty()) address += ", " + state;
		
		address += ", " + country;
		
		if(!postalCode.isEmpty()) address += ". " + postalCode;
		
		return address;
	}
	
	@Transient
	public boolean isCOD() {
		return paymentMethod.equals(PaymentMethod.COD);
	}
	
	@Transient
	public boolean isPicked() {
		return hasStatus(OrderStatus.PICKED);
	}
	
	@Transient
	public boolean isShipping() {
		return hasStatus(OrderStatus.SHIPPING);
	}
	@Transient
	public boolean isProcessing() {
		return hasStatus(OrderStatus.PROCESSING);
	}
	@Transient
	public boolean isDelivered() {
		return hasStatus(OrderStatus.DELIVERED);
	}
	@Transient
	public boolean isReturned() {
		return hasStatus(OrderStatus.RETURNED);
	}
	@Transient
	public boolean isReturnRequested() {
		return hasStatus(OrderStatus.RETURN_REQUESTED);
	}
	public boolean hasStatus(OrderStatus status) {
		for(OrderTrack aTrack : orderTracks) {
			if(aTrack.getStatus().equals(status)) {
				return true;
			}
		}
		return false;
	}
	
	@Transient
	public String getProductNames() {
		String productNames = "";
		productNames = "<ul>";
		
		for(OrderDetail detail : orderDetails) {
			productNames += "<li>" + detail.getProduct().getShortName() + "</li>";
		}
		
		productNames += "</ul>";
	
		return productNames;
	}
}
