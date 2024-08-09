package com.ecommerce.admin.category;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.ecommerce.common.entity.Category;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CategoryController {
	@Autowired
	private CategoryService service;

	@GetMapping("/categories")
	public String listFirstPage(@Param("sortDir") String sortDir 
			,Model model) {
		model.addAttribute("pageNum", 1);
		return listByPage(1, sortDir, null, model);
	
	}
	@GetMapping("/categories/page/{pageNum}")
	public String listByPage(@PathVariable("pageNum") int pageNum,
			@Param("sortDir") String sortDir,
			@Param("keyword") String keyword,
			Model model) {
		if(sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		CategoryPageInfo pageInfo = new CategoryPageInfo();
		List<Category> categories = service.listByPage(pageInfo,pageNum,sortDir, keyword);
		
		long startCount = (pageNum - 1) * CategoryService.ROOT_CATEGORIES_PER_PAGE + 1;
		long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE - 1;
		if(endCount > pageInfo.getTotalElement()) {
			endCount = pageInfo.getTotalElement();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("totalPages", pageInfo.getTotalPages());
		model.addAttribute("totalItems", pageInfo.getTotalElement());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", "name");
		model.addAttribute("keyword", keyword);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		
		model.addAttribute("listCategories", categories);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("moduleURL", "/categories");
		
		return "categories/categories";
	}

	@GetMapping("/categories/new")
	public String newCategory(Model model) {
		List<Category> listCategories = service.listCategoriesUsedInForm();
		model.addAttribute("pageTitle", "Create new Category");
		model.addAttribute("category", new Category());
		model.addAttribute("listCategories", listCategories);

		return "categories/category_form";
	}

	@PostMapping("/categories/save")
	public String saveCategory(Category category, @RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes redirectAttribute) throws IOException {
		
		if(!multipartFile.isEmpty()) {
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		category.setImage(fileName);
		Category savedCategory = service.save(category);
		
		String uploadDir = "../category-images/" + savedCategory.getId();
		
		FileUploadUtil.cleanDir(uploadDir);
		
		FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		
		}else {
				service.save(category);
		}
		redirectAttribute.addFlashAttribute("message", "The category has been saved successfully. ");
		return "redirect:/categories";
	}

	@GetMapping("/categories/edit/{id}")
	public String editCategory(@PathVariable("id") Integer id, Model model, RedirectAttributes re){
		try {
			Category category = service.get(id);
			List<Category> listCategories = service.listCategoriesUsedInForm();
			
			model.addAttribute("pageTitle", "Edit category with ID: " + id);
			model.addAttribute("category",	category);
			model.addAttribute("listCategories", listCategories);
			
			return "categories/category_form";
			
		} catch (Exception ex) {
			re.addFlashAttribute("message", ex.getMessage());
			return "redirect:/categories";
		}
	}
	
	@GetMapping("/categories/{id}/enabled/{status}")
	public String updateCategoryEnableStatus(@PathVariable("id") Integer id, 
			@PathVariable("status") boolean enabled, RedirectAttributes re) {
		service.updateCategoryEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The category " + id +  " has been " + status;
		re.addFlashAttribute("message", message);
		return "redirect:/categories";
	}

	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(@PathVariable("id") Integer id, Model model, RedirectAttributes re) {
		
		try {
			service.delete(id);
			String categoryDir = "../category-images/" + id;
			FileUploadUtil.removeDir(categoryDir);
			
			re.addFlashAttribute("message", 
					"The category ID " + id + " has been deleted successfully");
		}catch(CategoryNotFoundException ex) {
			re.addFlashAttribute("message", ex.getMessage());
		}
		
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/export/csv")
	public void exportToCsv(HttpServletResponse response) throws IOException{
		List<Category> listCategories = service.listCategoriesUsedInForm();
		CategoryCsvExporter exporter = new CategoryCsvExporter();
		exporter.export(listCategories, response);
	}
	@GetMapping("/categories/export/pdf")
	public void exportToPdf(HttpServletResponse response) throws IOException{
		List<Category> listCategories = service.listCategoriesUsedInForm();
		CategoryPdfExporter exporter = new CategoryPdfExporter();
		exporter.export(listCategories, response);
	}
}
