package com.example.salescheckerspring.services;


import com.example.salescheckerspring.models.Product;
import com.example.salescheckerspring.models.ProductPast;
import com.example.salescheckerspring.repos.ProductRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader("testdata.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] pieces = line.split(";");
                long articleNumber = Long.parseLong(pieces[0]);
                long EANCode = Long.parseLong(pieces[1]);
                String articleName = pieces[2];
                int quantity = Integer.parseInt(pieces[3].replaceAll(" ", ""));
                int price = Integer.parseInt(pieces[4].replaceAll(" ", ""));
                String supplier = pieces[5];
                int year = Integer.parseInt(pieces[6]);
                Product product = new Product(articleNumber, EANCode, articleName, quantity, price, supplier, year);
                products.add(product);
            }
            br.close();
        } catch (Exception exception) {
            System.out.println("Error" + exception);
        }
        return products;
    }

    public void loadProducts() {
        productRepository.saveAll(getProducts());
    }

    public void saveProducts(Product product) {
        productRepository.save(product);
    }

    public Product findProduct (long EANcode){
        List<Product> products = productRepository.findAll();
        for (Product product : products){
            if (product.getEANCode()==EANcode){
                return product;
            }
        }
        return null;
    }

}
