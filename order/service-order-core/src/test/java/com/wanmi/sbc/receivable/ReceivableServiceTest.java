package com.wanmi.sbc.receivable;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.order.receivables.model.root.Receivable;
import com.wanmi.sbc.order.receivables.service.ReceivableService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


/**
 * ReceivableServiceTest
 * Created by zhangjin on 2017/3/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(AccountApp.class)
public class ReceivableServiceTest {

    @Autowired
    private ReceivableService receivableService;

    @Test
    public void testAdd() {
        Optional<Receivable> addReceivable = addReceivable();
        System.out.println(addReceivable.get().toString());
        assertThat(addReceivable.get().getReceivableNo() , is("B123123232"));
    }

    @Test
    public void testFindOne(){
        Optional<Receivable> addReceivable = addReceivable();
//        Optional<Receivable> receivable = receivableService.findReceivableById(addReceivable.get().getReceivableId());
//        assertThat(receivable.get().getReceivableNo() , is("B123123232"));
    }


    private Optional<Receivable> addReceivable() {
        Receivable receivable = new Receivable();
        receivable.setReceivableNo("B123123232");
        receivable.setComment("xxx");
        receivable.setCreateTime(LocalDateTime.now());
        receivable.setOnlineAccountId(123L);
        receivable.setDelFlag(DeleteFlag.NO);
        return null;
    }
}
