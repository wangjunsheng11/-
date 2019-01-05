package com.kakacl.product_service.service;

import com.kakacl.product_service.domain.Product;

import java.util.List;

public interface ProductService {

    List<Product> listProduct();

    Product findById(int id);

    int selectCountTotal();


}
