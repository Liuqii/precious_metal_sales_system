package com.coding.sales;

import com.coding.sales.ProductUtil.Product;
import com.coding.sales.ProductUtil.ProductUtil;
import com.coding.sales.input.OrderCommand;
import com.coding.sales.input.OrderItemCommand;
import com.coding.sales.output.DiscountItemRepresentation;
import com.coding.sales.output.OrderRepresentation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 销售系统的主入口
 * 用于打印销售凭证
 */
public class OrderApp {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            throw new IllegalArgumentException("参数不正确。参数1为销售订单的JSON文件名，参数2为待打印销售凭证的文本文件名.");
        }

        String jsonFileName = args[0];
        String txtFileName = args[1];

        String orderCommand = FileUtils.readFromFile(jsonFileName);
        OrderApp app = new OrderApp();
        String result = app.checkout(orderCommand);
        FileUtils.writeToFile(result, txtFileName);
    }

    public String checkout(String orderCommand) throws Exception {
        OrderCommand command = OrderCommand.from(orderCommand);
        OrderRepresentation result = checkout(command);
        
        return result.toString();
    }

    OrderRepresentation checkout(OrderCommand command) throws Exception {



        OrderRepresentation result = null;

        //TODO: 请完成需求指定的功能扣。



        //获取优惠明细和金额
        Map<String,Object> discounts    = getDiscounts(command)  ;



        return result;
    }

    //获取优惠明细

    private Map<String,Object> getDiscounts(OrderCommand command) throws Exception {
        Map<String,Object> result = new HashMap<String, Object>();
        List<DiscountItemRepresentation> discounts = new ArrayList<DiscountItemRepresentation>();
        BigDecimal totalDiscountPrice = BigDecimal.ZERO;

        //获取订单条目
        List<OrderItemCommand> items = command.getItems();

        //获取使用的优惠(9折券、95折券两种)
        List<String>  useDiscount =  command.getDiscounts();
        for (OrderItemCommand item:items) {


            //获取商品可以使用的优惠
            Product product = ProductUtil.getProductById(item.getProduct());
            String[] productDiscount =  product.getDiscounts();

            //打折优惠
            BigDecimal discount =  BigDecimal.ZERO;
            if(useDiscount.size()>0){
                if(useDiscount.contains("9折券")&&productDiscount.toString().contains("9折券")){
                    //计算9折券的优惠
                    discount=discount.add(product.getPrice().multiply(item.getAmount()).multiply(BigDecimal.ONE.subtract(new BigDecimal(0.9))));
                }else if(useDiscount.contains("95折券")&&productDiscount.toString().contains("95折券")){
                    //计算95折券的优惠
                    discount=discount.add(product.getPrice().multiply(item.getAmount()).multiply(BigDecimal.ONE.subtract(new BigDecimal(0.95))));
                }


            }
            //满减优惠
            BigDecimal fullDiscount =BigDecimal.ZERO;
            //获取价格总额
            BigDecimal totalPrice = product.getPrice().multiply(item.getAmount());

            //3000满减优惠
            BigDecimal fullDiscount_3000=BigDecimal.ZERO;
            //2000满减优惠
            BigDecimal fullDiscount_2000=BigDecimal.ZERO;
            //1000满减优惠
            BigDecimal fullDiscount_1000=BigDecimal.ZERO;
            if(productDiscount.toString().contains("每满3000元减350")){
                BigDecimal tempTotalPrice = totalPrice;
                while (tempTotalPrice.compareTo(new BigDecimal(3000))>0){
                    fullDiscount_3000 = fullDiscount_3000.add(new BigDecimal(350));
                    tempTotalPrice.subtract(new BigDecimal(3000));

                }
            }
            if(productDiscount.toString().contains("每满2000元减30")){
                BigDecimal tempTotalPrice = totalPrice;
                while (tempTotalPrice.compareTo(new BigDecimal(2000))>0){
                    fullDiscount_2000 = fullDiscount_2000.add(new BigDecimal(30));
                    tempTotalPrice.subtract(new BigDecimal(2000));

                }
            }
            if(productDiscount.toString().contains("每满1000元减10")){
                BigDecimal tempTotalPrice = totalPrice;
                while (tempTotalPrice.compareTo(new BigDecimal(1000))>0){
                    fullDiscount_1000 = fullDiscount_1000.add(new BigDecimal(10));
                    tempTotalPrice.subtract(new BigDecimal(1000));

                }
            }
            //第三件减半优惠
            BigDecimal thirdHalfDiscount=BigDecimal.ZERO;
            if(productDiscount.toString().contains("第3件半价")&&item.getAmount().compareTo(new BigDecimal(3))>=0){
                thirdHalfDiscount = product.getPrice().divide(new BigDecimal(0.5));
            }
            //买三送一优惠
            BigDecimal buyThreeFreeOneDiscount = BigDecimal.ZERO;

            if(productDiscount.toString().contains("满3送1")&&item.getAmount().compareTo(new BigDecimal(4))>=0){
                buyThreeFreeOneDiscount = product.getPrice();
            }
            //获取最大优惠
            BigDecimal largestDiscount =  getLargestDiscount(discount,fullDiscount,fullDiscount_3000,fullDiscount_2000,fullDiscount_1000,thirdHalfDiscount,buyThreeFreeOneDiscount);

            totalDiscountPrice= totalDiscountPrice.add(largestDiscount);
            DiscountItemRepresentation representation = new DiscountItemRepresentation(product.getProductId(),product.getName(),largestDiscount);

            discounts.add(representation);
        }

        result.put("totalDiscountPrice",totalDiscountPrice);
        result.put("discounts",discounts);

        return  result;

    }
    //获取最大优惠
    private BigDecimal getLargestDiscount(BigDecimal discount, BigDecimal fullDiscount, BigDecimal fullDiscount_3000, BigDecimal fullDiscount_2000, BigDecimal fullDiscount_1000, BigDecimal thirdHalfDiscount, BigDecimal buyThreeFreeOneDiscount) {
          BigDecimal largerDiscount = getLargerNum(discount,fullDiscount);
          largerDiscount = getLargerNum(largerDiscount,fullDiscount_3000);
          largerDiscount = getLargerNum(largerDiscount,fullDiscount_2000);
          largerDiscount = getLargerNum(largerDiscount,fullDiscount_1000);
          largerDiscount = getLargerNum(thirdHalfDiscount,fullDiscount_2000);
          largerDiscount = getLargerNum(buyThreeFreeOneDiscount,fullDiscount_2000);
          return  largerDiscount;
    }

    private BigDecimal getLargerNum(BigDecimal discount, BigDecimal fullDiscount) {
        if(discount.compareTo(fullDiscount)>0){
            return discount;
        }else{
            return fullDiscount;
        }
    }
}
