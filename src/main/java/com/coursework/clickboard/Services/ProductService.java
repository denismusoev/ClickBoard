package com.coursework.clickboard.Services;

import com.coursework.clickboard.Models.DTO.ApiResponse;
import com.coursework.clickboard.Models.Database.Product.Category;
import com.coursework.clickboard.Models.Database.Product.Product;
import com.coursework.clickboard.Repositories.CategoryRepository;
import com.coursework.clickboard.Repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
@Transactional
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Category> getCategories(){
        return categoryRepository.findAll();
    }

    public Page<Product> filterByCategory(int categoryId, Pageable pageable){
        return productRepository.findByCategory_Id(categoryId, pageable);
    }

    public Page<Product> getAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product getById(int productId){
        return productRepository.findById(productId).orElseThrow();
    }

//    public void test(){
//
//        try {
//
//            List<Product> products = productRepository.findAll();
//            for (int i = 0; i < products.size(); i++){
//                Product product = products.get(i);
//                String imagePath = "";
//                switch (product.getId()){
//                    case 1:
//                        imagePath = "images/Gainward GeForce RTX 4090 Phantom GS.png";
//                        break;
//                    case 2:
//                        imagePath = "images/DEEPCOOL LS720 WH.png";
//                        break;
//                    case 3:
//                        imagePath = "images/Intel Core i9-14900K.png";
//                        break;
//                    case 4:
//                        imagePath = "images/MSI Katana B12VFK-463XRU.png";
//                        break;
//                    case 5:
//                        imagePath = "images/Razer Viper Ultimate.png";
//                        break;
//                }
//                ClassPathResource imageResource = new ClassPathResource(imagePath);
//                Path path = imageResource.getFile().toPath();
//                byte[] imageBytes = Files.readAllBytes(path);
//                product.setImage(imageBytes);
//                productRepository.save(product);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public Page<Product> searchByCategoryAndName(int categoryId, String name, Pageable pageable) {
        return productRepository.findByCategoryAndNameContainingIgnoreCase(categoryId, name, pageable);
    }
}
