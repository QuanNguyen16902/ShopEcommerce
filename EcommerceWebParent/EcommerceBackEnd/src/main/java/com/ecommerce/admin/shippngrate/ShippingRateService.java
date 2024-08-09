package com.ecommerce.admin.shippngrate;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.admin.paging.PagingAndSortingHelper;
import com.ecommerce.admin.product.ProductRepository;
import com.ecommerce.admin.setting.country.CountryRepository;
import com.ecommerce.common.entity.Country;
import com.ecommerce.common.entity.ShippingRate;
import com.ecommerce.common.entity.product.Product;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ShippingRateService {
	private static final int DIM_DIVISOR = 139;
	public static final int RATES_BY_PAGE = 10;
	@Autowired private ShippingRateRepository shipRepo;
	@Autowired private CountryRepository countryRepo;
	@Autowired private ProductRepository productRepo; 
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, RATES_BY_PAGE, shipRepo);
	}
	
	public List<Country> listAllCountries(){
		return countryRepo.findAllByOrderByNameAsc();
	}
	
	public void save(ShippingRate rateInForm) throws ShippingRateAlreadyExistsException {
		ShippingRate rateInDB = shipRepo.findByCountryAndState(rateInForm.getCountry().getId(), rateInForm.getState());
		boolean foundExistingRateInNewModel = rateInForm.getId() == null && rateInDB != null;
		boolean foundDifferentExistingRateInEditMode = rateInForm.getId() != null && rateInDB != null;
		
//		List<ShippingRate> rates = shipRepo.findAll();
//			for(ShippingRate rate : rates) {
//				if(rate.getCountry().getId().equals(rateInDB.getCountry().getId()) 
//						&& rate.getState().equals(rateInDB.getState()) && rate.getDays() == rateInDB.getDays() 
//						&& rate.getRate() == rateInDB.getRate()) {
//					throw new ShippingRateAlreadyExistsException("There's cs already a rate for  the destination "
//							+ rateInForm.getCountry().getName() + ", " + rateInForm.getState());
//				}
//			}
		if(foundExistingRateInNewModel){
					throw new ShippingRateAlreadyExistsException("There's already a rate for  the destination "
							+ rateInForm.getCountry().getName() + ", " + rateInForm.getState());
			}
		shipRepo.save(rateInForm);
	}
	
	public ShippingRate get(Integer id) throws ShippingRateNotFoundException {
		try {
			return shipRepo.findById(id).get();
		}catch(NoSuchElementException ex) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
		}
	}
	
	public void updateCODSupport(Integer id, boolean codSupported) throws ShippingRateNotFoundException {
		Long count = shipRepo.countById(id);
		if(count == null || count == 0) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
		}
		
		shipRepo.updateCODSupport(id, codSupported);
	}
	
	public void delete(Integer id) throws ShippingRateNotFoundException {
		Long count = shipRepo.countById(id);
		if(count == null || count == 0) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
		}
		shipRepo.deleteById(id);
	}
	
	public float calculateShippingCost(Integer productId, Integer countryId, String state) 
			throws ShippingRateNotFoundException {
		ShippingRate shippingRate = shipRepo.findByCountryAndState(countryId, state);
		
		if(shippingRate == null) {
			throw new ShippingRateNotFoundException("No shipping rate found for the given "
					+ "destination. You have to enter shipping cost manually.");
		}
		
		Product product = productRepo.findById(countryId).get();
		
		float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
		float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;
		
		
		return finalWeight * shippingRate.getRate();
	}
}
