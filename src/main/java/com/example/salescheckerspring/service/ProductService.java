package com.example.salescheckerspring.service;

import com.example.salescheckerspring.form.ProductSerachForm;
import com.example.salescheckerspring.model.Product;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

@Component
public class ProductService {


    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();

        //Beolvasás
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader("data.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] pieces = line.split(";");
                long articleNumber = Long.parseLong(pieces[0]);
                long EANCode = Long.parseLong(pieces[1]);
                String articleName = pieces[2];
                int quantity = Integer.parseInt(pieces[3].replaceAll(" ", ""));
                int value = Integer.parseInt(pieces[4].replaceAll(" ", ""));
                String supplier = pieces[5];
                Product product = new Product(articleNumber, EANCode, articleName, quantity, value, supplier);
                products.add(product);
            }
            br.close();
        } catch (Exception exception) {
            System.out.println("Error" + exception);
        }
        return products;
    }


    public long totalCashFlow(List<Product> products) {
        long sum = 0;
        for (Product product : products) {
            sum += product.getValue();
        }
        return sum;
    }


    public String worstSellingProduct(List<Product> products) {
        int min = Integer.MAX_VALUE;
        for (Product product : products) {
            if (product.getQuantity() < min) {
                min = product.getQuantity();
            }
        }
        for (Product product : products) {
            if (product.getQuantity() == min) {
                return (product.getArticleName()) + "    Darabszám: " + product.getQuantity();
            }
        }
        return null;
    }

    public String bestSellingProduct(List<Product> products) {
        int max = Integer.MIN_VALUE;
        for (Product product : products) {
            if (product.getQuantity() > max) {
                max = product.getQuantity();
            }
        }
        for (Product product : products) {
            if (product.getQuantity() == max) {
                return (product.getArticleName()) + "    Darabszám: " + product.getQuantity();
            }
        }
        return null;
    }

    public static long totalSoldProduct(List<Product> products) {
        long sum = 0;
        for (Product product : products) {
            sum += product.getQuantity();
        }
        return sum;
    }

    public List<Product> getByForm(ProductSerachForm form) {
        List<Product> result = new ArrayList<>();

        for (Product question : getProducts()) {
            if (form.getId() != null && !form.getId().equals(question.getEANCode())) {
                continue;
            }
            if (form.getArticleName() != null && !question.getArticleName().toLowerCase().contains(form.getArticleName().toLowerCase())) {
                continue;
            }
            if (form.getArticleNumber() != null && !form.getArticleNumber().equals(question.getArticleNumber())) {
                continue;
            }
            if (form.getSupplier() != null && !question.getSupplier().toLowerCase().contains(form.getSupplier().toLowerCase())) {
                continue;
            }

            result.add(question);
        }

        return result;
    }

    public List<String> getSuppliers(List<Product> products) {
        Set<String> suppliers = new HashSet<>();
        for (Product product : products) {
            suppliers.add(product.getSupplier());
        }


        return new ArrayList<>(suppliers);

    }
}
