package com.coding.sales.ProductUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductUtil {


    public  static List<Product> getProducts(){





        List<Product> products  =new ArrayList<Product>();

        //* 世园会五十国钱币册
        //		* 编号：001001
        //		* 单位：册
        //		* 价格：998.00元
        Product product = new Product("001001", "世园会五十国钱币册", "册",new BigDecimal(998.00) , new String[]{});
        products.add(product);

        product = new Product("001002", "2019北京世园会纪念银章大全40g", "盒",new BigDecimal(1380.00) , new String[]{"可使用9折打折券"});
        products.add(product);

        product = new Product("003001", "招财进宝", "条",new BigDecimal(1580.00) , new String[]{"可使用95折打折券"});
        products.add(product);


        product = new Product("003002", "水晶之恋", "条",new BigDecimal(980.00) , new String[]{"第3件半价","满3送1"});
        products.add(product);

        product = new Product("002002", "中国经典钱币套装", "套",new BigDecimal(998.00) , new String[]{"每满2000减30","每满1000减10"});
        products.add(product);

        product = new Product("002001", "守扩之羽比翼双飞4.8g", "条",new BigDecimal(1080.00) , new String[]{"第3件半价","满3送1","可使用95折打折券"});
        products.add(product);


        product = new Product("002003", "中国银象棋12g", "套",new BigDecimal(698.00) , new String[]{"每满3000元减350","每满2000减30","每满1000减10","可使用9折打折券"});
        products.add(product);
        return products;
    }


    public static Product  getProductById(String productId) throws Exception {

        List<Product> products = getProducts();

        for (Product product:products) {
            if (productId.equals( product.getProductId()))
                return product;
        }
        throw new Exception("编号："+productId+"的商品不存在");

    }

    public static void main(String[] args){
        try {
            Product product = getProductById("001009");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
