package com.example.salescheckerspring.services;

import com.example.salescheckerspring.models.Product;
import com.example.salescheckerspring.models.ProductPast;
import com.example.salescheckerspring.repos.ProductPastRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductPastService {

    private ProductPastRepository productPastRepository;

    public ProductPastService(ProductPastRepository productPastRepository) {
        this.productPastRepository = productPastRepository;
    }

    public List<ProductPast> getProducts(){
        List<ProductPast> products = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader("datas.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] pieces = line.split(";");
                long articleNumber = Long.parseLong(pieces[0]);
                long EANCode = Long.parseLong(pieces[1]);
                String articleName = pieces[2];
                int quantity = Integer.parseInt(pieces[3].replaceAll(" ", ""));
                int value = Integer.parseInt(pieces[4].replaceAll(" ", ""));
                String supplier = pieces[5];
                Long year = Long.valueOf((pieces[6]));
                ProductPast product = new ProductPast(articleNumber, EANCode, articleName, quantity, value, supplier,year);
                products.add(product);
            }
            br.close();
        } catch (Exception exception) {
            System.out.println("Error" + exception);
        }
        return products;
    }

    public void saveProducts (){
        productPastRepository.saveAll(getProducts());
    }

    static public String customFormat(String pattern, long value ) {
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        return myFormatter.format(value);
    }

    public String totalCashFlow(Long year) {
        List<ProductPast> products = productPastRepository.findProductPastByYear(year);
        long sum = 0;
        for (ProductPast product : products) {
            sum += product.getValue();
        }
        String sumformat = customFormat("###,###.###", sum);
        return sumformat;
    }

    public ProductPast get(long id) {
        return productPastRepository.findById(id).get();
    }

    public List<ProductPast> listAll(String searching) {
        if (searching != null) {
            return productPastRepository.findAll(searching);
        }
        return productPastRepository.findAll();
    }

}
