package com.ecommerce.admin.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.common.entity.Country;
import com.ecommerce.common.entity.Customer;
import com.ecommerce.common.exception.CustomerNotFoundException;
@Controller
public class CustomerController {
	@Autowired @Qualifier("adminCustomerService")
	 CustomerService customerService;
	
	
	@GetMapping("/customers")
	public String listFirstPage(Model model) {
		
		return listByPage(1,model, "firstName", "asc", null);
	}
	@GetMapping("/customers/page/{pageNum}")
	public String listByPage(@PathVariable("pageNum") Integer pageNum ,Model model,
							@Param("sortField") String sortField, @Param("sortDir") String sortDir, 
							@Param("keyword") String keyword) {
		
		Page<Customer> page = customerService.listByPage(pageNum, sortField, sortDir, keyword);
		List<Customer> listCustomers = page.getContent();
		
		
		long startCount = (pageNum - 1) * CustomerService.CUSTOMERS_PER_PAGE + 1;
		long endCount = startCount + CustomerService.CUSTOMERS_PER_PAGE - 1;
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listCustomers", listCustomers);
		model.addAttribute("moduleURL", "/customers");
		
		return "customers/customers";
	}

	@PostMapping("/customers/save")
	public String savecustomer(Customer customer, Model model, RedirectAttributes ra) {
		
		customerService.save(customer);
		ra.addFlashAttribute("message", "The customer has been saved successfully");
		return "redirect:/customers";
		
	}
	

	@GetMapping("/customers/{id}/enabled/{status}")
	public String updateStatusEnabled(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes re) throws CustomerNotFoundException {
		
		customerService.updateCustomerEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Customer ID " + id + " has been " + status;
		re.addFlashAttribute("message", message);
		
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/delete/{id}")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes re) throws CustomerNotFoundException {
		try {
		customerService.delete(id);
		
		re.addFlashAttribute("message", "The customer with ID " + id + " has been deleted successfully");
		}catch(CustomerNotFoundException e) {
			re.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/edit/{id}")
	public String editcustomer(@PathVariable("id") Integer id, Model model
			, RedirectAttributes re) {
		try {
		Customer customer =  customerService.get(id);
		List<Country> listCountries = customerService.listAllCountries();
		
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("customer", customer);
		model.addAttribute("pageTitle", String.format("Edit Customer (ID: %d)", id));
		
		return "customers/customer_form";
		}catch(CustomerNotFoundException e) {
			re.addFlashAttribute("message", e.getMessage());
			return "redirect:/customers";
		}
	}
	
	@GetMapping("/customers/detail/{id}")
	public String viewcustomerDetails(@PathVariable("id") Integer id, Model model
			, RedirectAttributes re) {
		try {
		Customer customer =  customerService.get(id);
		
		model.addAttribute("customer", customer);
		
		return "customers/customer_detail_modal";
		}catch(CustomerNotFoundException e) {
			re.addFlashAttribute("message", e.getMessage());
			return "redirect:/customers";
		}
		}
}
