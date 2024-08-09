package com.ecommerce.admin.shippngrate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.admin.paging.PagingAndSortingHelper;
import com.ecommerce.admin.paging.PagingAndSortingParam;
import com.ecommerce.common.entity.Country;
import com.ecommerce.common.entity.ShippingRate;

@Controller
public class ShippingRateController {
	private String defaultRedirectURL = "redirect:/shipping_rates/page/1?sortField=country&sortDir=asc";
	
	@Autowired private ShippingRateService service;
	
	@GetMapping("/shipping_rates")
	public String listFirstPage() {
		return defaultRedirectURL;
	}
	
	@GetMapping("/shipping_rates/page/{pageNum}")
	public String listByPage(@PagingAndSortingParam(listName = "shippingRates",
					moduleURL = "/shipping_rates") PagingAndSortingHelper helper,
			@PathVariable("pageNum") Integer pageNum) {
		service.listByPage(pageNum , helper);
		return "shipping_rates/shipping_rates";
	}
	
	@GetMapping("/shipping_rates/new")
	public String newRate(Model model) {
		List<Country> listCountries = service.listAllCountries();
		
		model.addAttribute("rate",new ShippingRate());
		model.addAttribute("listCountries",listCountries);
		model.addAttribute("pageTitle", "New Rate");
		
		return "shipping_rates/shipping_rate_form";
	}
	
	@PostMapping("/shipping_rates/save")
	public String saveRate(ShippingRate rate, RedirectAttributes ra) {
		try {
			service.save(rate);
			ra.addFlashAttribute("message", "The shipping rate has been saved successfully.");
		}catch(ShippingRateAlreadyExistsException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		return defaultRedirectURL;
	}
	
	@GetMapping("/shipping_rates/edit/{id}")
	public String editRate(@PathVariable(name = "id") Integer id,
				Model model, RedirectAttributes ra) {
		try {
			ShippingRate rate = service.get(id);
			List<Country> listCountries = service.listAllCountries();
			model.addAttribute("listCountries",listCountries);
			model.addAttribute("rate",rate);
			model.addAttribute("pageTitle","Edit Rate (ID: " + id + ")");
			
			return "shipping_rates/shipping_rate_form";
			
		}catch(ShippingRateNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return defaultRedirectURL;
		}
	}
	
	@GetMapping("/shipping_rates/cod/{id}/enabled/{supported}")
	public String updateCODSupport(@PathVariable(name = "id") Integer id,
			@PathVariable(name = "supported") Boolean supported,
			Model model, RedirectAttributes ra) {
		try {
			service.updateCODSupport(id, supported);
			String enabled = supported == true ? "enabled" : "disabled";
			ra.addFlashAttribute("message", "COD supported for shipping rate ID " + id + " has been " + enabled);
			
		}catch(ShippingRateNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		return defaultRedirectURL;
	}
	
	@GetMapping("/shipping_rates/delete/{id}")
	public String deleteRate(@PathVariable(name = "id") Integer id,
			Model model, RedirectAttributes ra) {
		try {
			service.delete(id);
			ra.addFlashAttribute("message", "The shipping rate ID " + id + " has been deleted");
			
		}catch(ShippingRateNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		return defaultRedirectURL;
	}
}
