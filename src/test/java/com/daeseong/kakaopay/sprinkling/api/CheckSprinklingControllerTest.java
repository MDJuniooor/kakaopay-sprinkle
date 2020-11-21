package com.daeseong.kakaopay.sprinkling.api;

import com.daeseong.kakaopay.sprinkling.entity.Room;
import com.daeseong.kakaopay.sprinkling.entity.SprinklingMoney;
import com.daeseong.kakaopay.sprinkling.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CheckSprinklingControllerTest {

    @Autowired
    CheckSprinklingController checkSprinklingController;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(checkSprinklingController).build();
        List<User> users = new ArrayList<>();
        User user;
        Room room = new Room();

    }

    @Test
    public void test() {
        SprinklingMoney sprinklingMoney = mock(SprinklingMoney.class);

    }
}