package com.ecommerce.admin.brand;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.admin.FileUploadUtil;
import com.ecommerce.admin.category.CategoryService;
import com.ecommerce.common.entity.Brand;
import com.ecommerce.common.entity.Category;

@Controller
public class BrandController {
	@Autowired
	public BrandService brandService;
	@Autowired
	public CategoryService catService;

	@GetMapping("/brands")
	public String listFirstPage(Model model) {
	 
		return listByPage(1 ,model, "name", "asc", null);
	}
	@GetMapping("/brands/page/{pageNum}")
	public String listByPage(@PathVariable("pageNum") Integer pageNum ,Model model,
							@Param("sortField") String sortField, @Param("sortDir") String sortDir, 
							@Param("keyword") String keyword) {
		Page<Brand> page = brandService.listByPage(pageNum, sortField, sortDir, keyword);
		List<Brand> listBrands = page.getContent();
		
		long startCount = (pageNum - 1) * BrandService.BRANDS_PER_PAGE + 1;
		long endCount = startCount + BrandService.BRANDS_PER_PAGE - 1;
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", sortField);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("moduleURL", "/brands");
		
		return "brands/brands";
	}

	@GetMapping("/brands/new")
	public String newBrand(Model model) {
		List<Category> listCategories = catService.listCategoriesUsedInForm();
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("brand", new Brand());
		model.addAttribute("pageTitle", "Create New Brand");

		return "brands/brand_form";
	}

	@PostMapping("/brands/save")
	public String saveBrand(Brand brand, @RequestParam("fileImage") MultipartFile multipartFile, 
			RedirectAttributes re) throws IOException {
		if(!multipartFile.isEmpty()){
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);
			
			Brand savedBrand = brandService.save(brand);
			String uploadDir = "../brand-logos/" + savedBrand.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}
		else {
			brandService.save(brand);
		}
			re.addFlashAttribute("message", "The brand has been saved successfully");
			return "redirect:/brands";
	}
	
	@GetMapping("/brands/edit/{id}")
	public String editBrand(@PathVariable("id") Integer id, Model model, RedirectAttributes re ) {
		
		try {
			Brand brand = brandService.get(id);
			List<Category> listCategories = catService.listCategoriesUsedInForm();
			
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("brand", brand);
			model.addAttribute("pageTitle", "Edit Brand (ID: "+ id + ")");
			
			return "brands/brand_form";
		}catch(BrandNotFoundException ex){
			re.addFlashAttribute("message", ex.getMessage());
			return "redirect:/brands";
		}
	}
		
		@GetMapping("/brands/delete/{id}")
		public String deleteBrand(@PathVariable("id") Integer id, Model model, RedirectAttributes re) {
			try {
				brandService.delete(id);
				String brandDir = "../brand-logos/" + id;
				FileUploadUtil.removeDir(brandDir);
				
				re.addFlashAttribute("message", "The brand ID " + id + " has been deleted successfully");
				
			}catch(BrandNotFoundException ex) {
				re.addFlashAttribute("message", ex.getMessage());
			}
			
			return "redirect:/brands";
		}
	
	


}
