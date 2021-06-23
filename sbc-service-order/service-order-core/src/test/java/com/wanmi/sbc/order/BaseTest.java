package com.wanmi.sbc.order;

import com.wanmi.sbc.OrderTestApplication;
import com.wanmi.sbc.common.configure.CompositePropertySourceFactory;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by jinwei on 20/4/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OrderTestApplication.class)
public abstract class BaseTest {
}
