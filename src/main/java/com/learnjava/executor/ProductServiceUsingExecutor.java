package com.learnjava.executor;

import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.learnjava.domain.Product;
import com.learnjava.domain.ProductInfo;
import com.learnjava.domain.Review;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ProductService;
import com.learnjava.service.ReviewService;

public class ProductServiceUsingExecutor {
	static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private ProductInfoService productInfoService;
    private ReviewService reviewService;

    public ProductServiceUsingExecutor(ProductInfoService productInfoService, ReviewService reviewService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
    }

    public Product retrieveProductDetails(String productId) throws InterruptedException, ExecutionException {
        stopWatch.start();
        
        Future<ProductInfo> productInfoFuture=executorService.submit(()->productInfoService.retrieveProductInfo(productId));
        Future<Review> reviewFuture=executorService.submit(()->reviewService.retrieveReviews(productId));
        
        
        ProductInfo productInfo = productInfoFuture.get(); //this is blocking - use timeout
        Review review = reviewFuture.get();

        //ProductInfo productInfo = productInfoService.retrieveProductInfo(productId); // blocking call
       // Review review = reviewService.retrieveReviews(productId); // blocking call
        //synchronous code
        // 
        // this kind of code is blocking nature
        // review service will only execute after product service is done although review service does not depend on product service
        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
        return new Product(productId, productInfo, review);
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        ProductServiceUsingExecutor productService = new ProductServiceUsingExecutor(productInfoService, reviewService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is " + product);
        executorService.shutdown();

    }
}
