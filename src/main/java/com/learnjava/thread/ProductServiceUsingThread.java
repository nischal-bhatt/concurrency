package com.learnjava.thread;

import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

import com.learnjava.domain.Product;
import com.learnjava.domain.ProductInfo;
import com.learnjava.domain.Review;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;

public class ProductServiceUsingThread {
    private ProductInfoService productInfoService;
    private ReviewService reviewService;

    public ProductServiceUsingThread(ProductInfoService productInfoService, ReviewService reviewService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
    }

    public Product retrieveProductDetails(String productId) throws InterruptedException {
        stopWatch.start();
        
        ProductInfoRunnable productInfoRunnable = new ProductInfoRunnable(productId);

        Thread productInfoThread = new Thread(productInfoRunnable);
        
        ReviewRunnable reviewRunnable = new ReviewRunnable(productId);
        Thread reviewThread = new Thread(reviewRunnable);
        
        //ProductInfo productInfo = productInfoService.retrieveProductInfo(productId); // blocking call
       // Review review = reviewService.retrieveReviews(productId); // blocking call
        //synchronous code
        // 
        // this kind of code is blocking nature
        // review service will only execute after product service is done although review service does not depend on product service
        
        productInfoThread.start();
        reviewThread.start();
        
        productInfoThread.join();
        reviewThread.join();
        
        ProductInfo productInfo = productInfoRunnable.getProductInfo();
        Review review = reviewRunnable.getReview();
        
        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
        return new Product(productId, productInfo, review);
    }

    public static void main(String[] args) throws InterruptedException {

        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        ProductServiceUsingThread productService = new ProductServiceUsingThread(productInfoService, reviewService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is " + product);

    }
    
    private class ProductInfoRunnable implements Runnable
    {
    	private ProductInfo productInfo;
    	private String productId;
    	
    	public ProductInfoRunnable(String productId)
    	{
    		this.productId = productId;
    	}
    	
    	

		public ProductInfo getProductInfo() {
			return productInfo;
		}



		public void setProductInfo(ProductInfo productInfo) {
			this.productInfo = productInfo;
		}



		public String getProductId() {
			return productId;
		}



		public void setProductId(String productId) {
			this.productId = productId;
		}



		@Override
		public void run() {
			this.productInfo = productInfoService.retrieveProductInfo(productId);
			
		}
    }
    
    private class ReviewRunnable implements Runnable {
    	
    	private String productId;
    	private Review review;
    	
    	
    	
    	public String getProductId() {
			return productId;
		}

		public void setProductId(String productId) {
			this.productId = productId;
		}

		public Review getReview() {
			return review;
		}

		public void setReview(Review review) {
			this.review = review;
		}

		public ReviewRunnable(String productId)
    	{
    		this.productId = productId;
    	}

		@Override
		public void run() {
			
			review = reviewService.retrieveReviews(productId);
		}
    }
    
}
