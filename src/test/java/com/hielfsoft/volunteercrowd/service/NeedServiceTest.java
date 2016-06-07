package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.VolunteercrowdApp;
import com.hielfsoft.volunteercrowd.domain.Need;
import com.hielfsoft.volunteercrowd.repository.NeedRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel Sánchez on 06/06/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VolunteercrowdApp.class)
@Transactional
@WebAppConfiguration
@Rollback
public class NeedServiceTest {

    @Inject
    private NeedService needService;

    @Inject
    private NeedRepository needRepository;

    private static final Long appUserId = 10l;

    @Test
    public void FindNeedById(){
        List<Need> needs = new ArrayList<Need>(needRepository.findByAppUserId(appUserId));

        Assert.isTrue(needs.size()==3);
    }


}
