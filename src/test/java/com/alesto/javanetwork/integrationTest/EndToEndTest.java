package com.alesto.javanetwork.integrationTest;

import com.alesto.javanetwork.configuration.RootConfiguration;
import com.alesto.javanetwork.configuration.WebConfiguration;
import com.alesto.javanetwork.web.controller.rest.v1.ApiEndpoints;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration(name = "root", classes = {RootConfiguration.class}),
        @ContextConfiguration(name = "web", classes = {WebConfiguration.class})
})
@ActiveProfiles("test")
public class EndToEndTest {

    @Inject
    private WebApplicationContext context;


    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context). //
                build();
    }

    /**
     * A combined test for posting messages, reading , following and wall
     *
     * @throws Exception
     */
    @Test
    public void testAll() throws Exception {

        //Alice posts a message
        mvc.perform(post(ApiEndpoints.TIMELINE_INDEX + ".json", "Alice").param("message", "I love the weather today")).andExpect(status().isCreated());

        //Bob posts messages
        mvc.perform(post(ApiEndpoints.TIMELINE_INDEX + ".json", "Bob").param("message", "Good game though.")).andExpect(status().isCreated());
        mvc.perform(post(ApiEndpoints.TIMELINE_INDEX + ".json", "Bob").param("message", "Damn! We lost!")).andExpect(status().isCreated());

        //Read Alice's timeline:
        ResultActions res = mvc.perform(get(ApiEndpoints.API_INDEX + "/Alice/timeline.json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username", is("Alice")))
                .andExpect(jsonPath("$.content[0].message", is("I love the weather today")));

        //Read Bob's timeline:
        mvc.perform(get(ApiEndpoints.API_INDEX + "/Bob/timeline.json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username", is("Bob")))
                .andExpect(jsonPath("$.content[0].message", is("Damn! We lost!")))
                .andExpect(jsonPath("$.content[1].message", is("Good game though.")));

        //Charlie posts a message
        mvc.perform(post(ApiEndpoints.TIMELINE_INDEX + ".json", "Charlie").param("message", "I am in New York today! Anyone wants to have coffee?"))
                .andExpect(status().isCreated());

        //Charlie follows Alice
        mvc.perform(post(ApiEndpoints.FOLLOWS_INDEX + ".json", "Charlie").param("userToFollow", "Alice")).andExpect(status().isCreated());


        //Charlie's wall
        mvc.perform(get(ApiEndpoints.API_INDEX + "/Charlie/wall.json").param("userId", "Charlie"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username", is("Charlie")))
                .andExpect(jsonPath("$.content[0].message", is("I am in New York today! Anyone wants to have coffee?")))
                .andExpect(jsonPath("$.content[1].username", is("Alice")))
                .andExpect(jsonPath("$.content[1].message", is("I love the weather today")));

        //Charlie follows Bob
        mvc.perform(post(ApiEndpoints.FOLLOWS_INDEX + ".json", "Charlie").param("userToFollow", "Bob")).andExpect(status().isCreated());

        //Charlie's wall
        mvc.perform(get(ApiEndpoints.API_INDEX + "/Charlie/wall.json").param("userId", "Charlie"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username", is("Charlie")))
                .andExpect(jsonPath("$.content[0].message", is("I am in New York today! Anyone wants to have coffee?")))
                .andExpect(jsonPath("$.content[1].username", is("Bob")))
                .andExpect(jsonPath("$.content[1].message", is("Damn! We lost!")))
                .andExpect(jsonPath("$.content[2].message", is("Good game though.")))
                .andExpect(jsonPath("$.content[3].username", is("Alice")))
                .andExpect(jsonPath("$.content[3].message", is("I love the weather today")));
    }

}
