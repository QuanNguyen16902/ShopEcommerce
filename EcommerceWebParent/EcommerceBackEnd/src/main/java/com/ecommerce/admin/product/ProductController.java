package com.ecommerce.admin.product;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.admin.FileUploadUtil;
import com.ecommerce.admin.brand.BrandService;
import com.ecommerce.admin.category.CategoryService;
import com.ecommerce.admin.security.EcommerceUserDetails;
import com.ecommerce.common.entity.Brand;
import com.ecommerce.common.entity.Category;
import com.ecommerce.common.entity.product.Product;
import com.ecommerce.common.exception.ProductNotFoundException;
@Controller
public class ProductController {
	@Autowired
	private ProductService productService;
	@Autowired
	private BrandService brandService;
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/products")
	public String listFirstPage(Model model) {
		
		return listByPage(1,model, "name", "asc", null,null);
	}
	@GetMapping("/products/page/{pageNum}")
	public String listByPage(@PathVariable("pageNum") Integer pageNum ,Model model,
							@Param("sortField") String sortField, @Param("sortDir") String sortDir, 
							@Param("keyword") String keyword,
							@Param("categoryId") Integer categoryId) {
		
		Page<Product> page = productService.listByPage(pageNum, sortField, sortDir, keyword, categoryId);
		List<Product> listProducts = page.getContent();
		
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		
		long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		if(categoryId != null) model.addAttribute("categoryId", categoryId);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("moduleURL", "/products");
		return "products/products";
	}
	@GetMapping("/products/new")
	public String newProduct(Model model) {
		
		List<Brand> listBrands = brandService.listAll();
		
		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);
		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTitle", "Create New Product");
		model.addAttribute("numberOfExistingExtraImages", 0);
		return "products/product_form";
	}
	
	@PostMapping("/products/save")
	public String saveProduct(Product product,RedirectAttributes ra,
			@RequestParam(value = "fileImage" , required = false) MultipartFile mainImageMultipart, 
			@RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts, 
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailValues", required = false) String[] detailValues,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,
			@RequestParam(name = "imageNames", required = false) String[] imageNames,
			@AuthenticationPrincipal EcommerceUserDetails loggedUser) throws IOException {
		if(!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
			if(loggedUser.hasRole("Salesperson")) {
				productService.saveProductPrice(product);
				ra.addFlashAttribute("message", "The product has been saved successfully");
				return "redirect:/products";
			}
		}
		ProductSavedHelper.setMainImageName(mainImageMultipart, product);
		ProductSavedHelper.setExistingExtraImageNames(imageIDs, imageNames, product);
		ProductSavedHelper.setNewExtraImageName(extraImageMultiparts, product);
		ProductSavedHelper.setProductDetails(detailIDs ,detailNames, detailValues, product);
		
		Product savedProduct = productService.save(product);
		
		ProductSavedHelper.saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);
		
		ProductSavedHelper.deleteExtraImagesWereRemovedOnForm(product);
		
		ra.addFlashAttribute("message", "The product has been saved successfully");
		return "redirect:/products";
		
	}
	

	@GetMapping("/products/{id}/enabled/{status}")
	public String updateStatusEnabled(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes re) throws ProductNotFoundException {
		
		productService.updateStatusEnabled(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The product ID " + id + " has been " + status;
		re.addFlashAttribute("message", message);
		
		return "redirect:/products";
	}
	
	@GetMapping("/products/delete/{id}")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes re) throws ProductNotFoundException {
		try {
		productService.delete(id);
		String productExtraImageDir = "../product-images/" + id + "/extras";
		String productImageDir = "../product-images/" + id;
		
		FileUploadUtil.removeDir(productExtraImageDir);
		FileUploadUtil.removeDir(productImageDir);
		
		re.addFlashAttribute("message", "The product with ID " + id + " has been deleted successfully");
		}catch(ProductNotFoundException e) {
			re.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/products";
	}
	
	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable("id") Integer id, Model model
			, RedirectAttributes ra, @AuthenticationPrincipal EcommerceUserDetails loggedUser) {
		try {
		Product product =  productService.get(id);
		List<Brand> listBrands = brandService.listAll();
		Integer numberOfExistingExtraImages = product.getImages().size();
		
		boolean isReadOnlyForSalesperson = false;
		if(!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
			if(loggedUser.hasRole("Salesperson")) {
				isReadOnlyForSalesperson = true;
			}
		}
		model.addAttribute("isReadOnlyForSalesperson", isReadOnlyForSalesperson);
		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
		model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
		
		return "products/product_form";
		}catch(ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
		}
	}
	
	@GetMapping("/products/detail/{id}")
	public String viewProductDetails(@PathVariable("id") Integer id, Model model
			, RedirectAttributes re) {
		try {
		Product product =  productService.get(id);
		
		model.addAttribute("product", product);
		
		return "products/product_detail_modal";
		}catch(ProductNotFoundException e) {
			re.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
		}
		}
}
