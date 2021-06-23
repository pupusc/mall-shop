package com.wanmi.sbc.payorder;

//import com.wanmi.sbc.account.AccountApp;
import com.wanmi.sbc.order.payorder.service.PayOrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 付款单服务
 * Created by zhangjin on 2017/4/20.
 */

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = AccountApp.class)
public class PayOrderServiceTest {

    @Autowired
    private PayOrderService payOrderService;

//    @Autowired
//    private CustomerDetailRepository customerDetailRepository;
//
//    @Autowired
//    private CustomerRepository customerRepository;


    @Test
    public void testAdd() {
//        Optional<payorder> payOrderOptional = payOrderService.generatePayOrderByOrderCode("O12312312");
//        assertThat(payOrderOptional.isPresent() , is(true));
    }


    @Before
    public void generateData(){
//        //case
//        CustomerDetail customerDetail = new CustomerDetail();
//        Customer customer = new Customer();
//
//        customerDetail.setCustomerName("zhangsan");
//        customerDetail.setCustomer(customer);
//        CustomerDetail customerDetail1 = customerDetailRepository.save(customerDetail);
//        System.out.println(customerDetail1);
    }
}
