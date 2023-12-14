package com.mcy.product.controller;


import com.mcy.product.service.ITProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mcy
 * @since 2020-06-06
 */
@RestController
@RequestMapping("product")
public class TProductController {

    @Autowired
    private ITProductService productService;

    @PostMapping("deduct")
    public String deduct(String productId,int count)  {
        productService.deduct(productId,count);
        return "success";
    }

}
