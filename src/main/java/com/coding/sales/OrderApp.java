package com.coding.sales;

import com.coding.sales.ProductUtil.Member;
import com.coding.sales.ProductUtil.Product;
import com.coding.sales.ProductUtil.ProductUtil;
import com.coding.sales.input.OrderCommand;
import com.coding.sales.input.OrderItemCommand;
import com.coding.sales.input.PaymentCommand;
import com.coding.sales.output.DiscountItemRepresentation;
import com.coding.sales.output.OrderItemRepresentation;
import com.coding.sales.output.OrderRepresentation;
import com.coding.sales.output.PaymentRepresentation;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        //String jsonFileName = args[0];
        //String txtFileName = args[1];
        String orderCommand = FileUtils.readFromFile("F:/sample_command.json");
        OrderApp app = new OrderApp();
        String result = app.checkout(orderCommand);
        FileUtils.writeToFile(result, "F:/a.txt");
    }
    public String checkout(String orderCommand) throws Exception {
        OrderCommand command = OrderCommand.from(orderCommand);
        OrderRepresentation result = checkout(command);
        
        return result.toString();
    }

    OrderRepresentation checkout(OrderCommand command) throws Exception {



        OrderRepresentation result = null;

        //1.根据客户购买的贵金属计算订单金额
        String memberId = command.getMemberId();
        String orderId = command.getOrderId();
        String createTime = command.getCreateTime();

        int itemsize = command.getItems().size();
        List<OrderItemCommand> itemlist = command.getItems();
        BigDecimal total = BigDecimal.ZERO;
        for(OrderItemCommand orderItemCommand : itemlist){
        	String productId = orderItemCommand.getProduct();
        	BigDecimal amount = orderItemCommand.getAmount();
        	Product p = ProductUtil.getProductById(productId);
        	total = total.add(p.getPrice().multiply(amount));
        }
        
      //获取优惠明细和金额
    	Map<String,Object> discount = getDiscounts(command);
    	BigDecimal discountPrice = new BigDecimal(discount.get("totalDiscountPrice").toString());
    	List<DiscountItemRepresentation> DiscountItemRepresentations = (List<DiscountItemRepresentation>) discount.get("discounts");
    	 //2.客户可以使用账户余额购买贵金属，按客户等级计算积分，如果达到升级积分，则提升客户等级。
        Map scoreAndLevel = Member.getScoreAndLevel(memberId,total.subtract(discountPrice));
        Member m = Member.getMemberByMemberId(command.getMemberId());
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<PaymentCommand> paymentCommandlist = command.getPayments();
        List<PaymentRepresentation> paymentRepresentationlist = new ArrayList<PaymentRepresentation>();
        for (PaymentCommand paymentCommand : paymentCommandlist) {
        	PaymentRepresentation obj =  new PaymentRepresentation(paymentCommand.getType(),paymentCommand.getAmount());
        	paymentRepresentationlist.add(obj);
		}
        List<OrderItemCommand> orderItemCommandList = command.getItems();
        List<OrderItemRepresentation> orderItemRepresentationlist = new ArrayList<OrderItemRepresentation>();
        for (OrderItemCommand orderItemCommand : orderItemCommandList) {
        	Product product = ProductUtil.getProductById(orderItemCommand.getProduct());
        	OrderItemRepresentation obj =  new OrderItemRepresentation(product.getProductId(),product.getName(),product.getPrice(),orderItemCommand.getAmount(),product.getPrice().multiply(orderItemCommand.getAmount()));
        	orderItemRepresentationlist.add(obj);
		}

        result = new OrderRepresentation(command.getOrderId(),format.parse(command.getCreateTime()),command.getMemberId(),m.getName(),scoreAndLevel.get("oldlevel").toString(),scoreAndLevel.get("newlevel").toString(),Integer.parseInt(scoreAndLevel.get("increasescore").toString())  ,Integer.parseInt(scoreAndLevel.get("newscore").toString()),orderItemRepresentationlist,total,DiscountItemRepresentations,discountPrice,total.subtract(discountPrice),paymentRepresentationlist,command.getDiscounts());
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
		    List<String>  useDiscount1 =  command.getDiscounts();
		   
            StringBuffer useDiscount=new StringBuffer();
            for (String string : useDiscount1) {
            	useDiscount.append(string);
			}

		    for (OrderItemCommand item:items) {
		
		
		        //获取商品可以使用的优惠
		        Product product = ProductUtil.getProductById(item.getProduct());
		        String[] productDiscount1 =  product.getDiscounts();
		        StringBuffer productDiscount=new StringBuffer();
		        for (String string : productDiscount1) {
		        	productDiscount.append(string);
				}
		
		        //打折优惠
		        BigDecimal discount =  BigDecimal.ZERO;
		        if(useDiscount1.size()>0){
		            if(useDiscount.toString().contains("9折")&&productDiscount.toString().contains("9折")){
		                //计算9折券的优惠
		                discount=discount.add(product.getPrice().multiply(item.getAmount()).multiply(BigDecimal.ONE.subtract(new BigDecimal(0.9))));
		            }else if(useDiscount.toString().contains("95折券")&&productDiscount.toString().contains("95折券")){
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
		                tempTotalPrice=tempTotalPrice.subtract(new BigDecimal(3000));
		
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
		        if(totalDiscountPrice.compareTo(BigDecimal.ZERO)>0){
			        DiscountItemRepresentation representation = new DiscountItemRepresentation(product.getProductId(),product.getName(),largestDiscount);
					
			        discounts.add(representation);
		        }

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
		      largerDiscount = getLargerNum(thirdHalfDiscount,largerDiscount);
		      largerDiscount = getLargerNum(buyThreeFreeOneDiscount,largerDiscount);
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
