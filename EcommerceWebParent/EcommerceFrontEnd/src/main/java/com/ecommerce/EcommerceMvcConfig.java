package com.ecommerce;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class EcommerceMvcConfig implements WebMvcConfigurer {

//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		String dirName = "user-photos";
//		Path userPhotosDir = Paths.get(dirName);
//		
//		String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();
//		
//		registry.addResourceHandler("/" + dirName + "/**")
//				.addResourceLocations("file:/" + userPhotosPath + "/");
//		
//		String categoryImageName = "../category-images";
//		Path categoryImageDir = Paths.get(categoryImageName);
//		
//		String categoryImagePath = categoryImageDir.toFile().getAbsolutePath();
//		
//		registry.addResourceHandler("/category-images/**")
//				.addResourceLocations("file:/" + categoryImagePath + "/");
//		
//		
//		String brandLogoDirName = "../brand-logos";
//		Path brandLogoDir = Paths.get(brandLogoDirName);
//		
//		String brandLogoPath = brandLogoDir.toFile().getAbsolutePath();
//		
//		registry.addResourceHandler("/brand-logos/**") 
//				.addResourceLocations("file:/" + brandLogoPath + "/");
//	}
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		exposeDirectory("../category-images", registry);
		exposeDirectory("../brand-logos", registry);
		exposeDirectory("../product-images", registry);
		exposeDirectory("../site-logo", registry);
	}
	private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {
		Path path = Paths.get(pathPattern);
		
		String absolutePath = path.toFile().getAbsolutePath();
		String logicalPath = pathPattern.replace("../", "") + "/**";
		
		registry.addResourceHandler(logicalPath)
		.addResourceLocations("file:/" + absolutePath + "/");
	}
	
	
}
