package com.alesto.javanetwork.web.controller.rest.v1;

import com.alesto.javanetwork.configuration.TestWebConfiguration;
import com.alesto.javanetwork.domain.entity.Follows;
import com.alesto.javanetwork.domain.entity.Timeline;
import com.alesto.javanetwork.service.DataExchangeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration(name = "web", classes = {TestWebConfiguration.class})
})
public class RestFollowsControllerTest {

    @Inject
    private WebApplicationContext context;

    private MockMvc mvc;

    @Inject
    private DataExchangeService mockDataService;


    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context). //
                build();

    }

    /**
     * Tests the posting and reading of one timeline message
     *
     * @throws Exception
     */
    @Test
    public void testFollowUser() throws Exception {
        final Follows followUser = new Follows();
        followUser.setUsername("Alice");
        followUser.setFollowsUser("Bob");

        when(mockDataService.followUser(followUser)).thenReturn(followUser);


        mvc.perform(post(ApiEndpoints.FOLLOWS_INDEX + ".json", "Alice").param("userToFollow", "Bob")).andExpect(status().isCreated());
        verify(mockDataService, times(1)).followUser(any(Follows.class));

    }


    /**
     * Tests the posting and reading of one timeline message
     *
     * @throws Exception
     */
    @Test
    public void testReadTimelineMessage() throws Exception {
        final Follows followUser = new Follows();
        followUser.setUsername("Alice");
        followUser.setFollowsUser("Bob");
        followUser.setId("" + System.currentTimeMillis());
        followUser.setCreated(new Date());
        final List<Follows> followUserList = Arrays.asList(followUser);

        when(mockDataService.getFollowedUsers("Alice")).thenReturn(followUserList);

        mvc.perform(get(ApiEndpoints.API_INDEX + "/" + followUser.getUsername() + "/follows.json").param("userId", "Alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username", is("Alice")))
                .andExpect(jsonPath("$.content[0].followsUser", is("Bob")));

    }

}
