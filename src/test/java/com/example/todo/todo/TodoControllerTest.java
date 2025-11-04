package com.example.todo.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TodoControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private FakeService service;

    @BeforeEach
    void setup() {
    objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    service = new FakeService();
        TodoController controller = new TodoController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setValidator(new LocalValidatorFactoryBean())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void listReturnsTodos() throws Exception {
    service.setList(List.of(
        new Todo(1, "Write tests", false, null),
        new Todo(2, "Wire CI", true, null)
    ));

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Write tests")))
                .andExpect(jsonPath("$[1].completed", is(true)));
    }

    @Test
    void createValidatesTitle() throws Exception {
        CreateTodoRequest req = new CreateTodoRequest("");
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createReturnsCreatedWithBody() throws Exception {
    service.setAddReturn(new Todo(10, "New task", false, null));
    CreateTodoRequest req = new CreateTodoRequest("New task");

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/todos/10"))
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.title", is("New task")));
    }

    @Test
    void toggleNotFoundReturns404() throws Exception {
    service.setThrowOnToggle(true);
    mockMvc.perform(put("/api/todos/123/toggle"))
                .andExpect(status().isNotFound());
    }

    // Simple fake without Mockito/Byte Buddy to be Java 24 friendly
    static class FakeService extends TodoService {
        private List<Todo> list = new ArrayList<>();
        private Todo addReturn;
        private boolean throwOnToggle;

        public FakeService() { super(new TodoRepository()); }

        void setList(List<Todo> l) { this.list = new ArrayList<>(l); }
        void setAddReturn(Todo t) { this.addReturn = t; }
        void setThrowOnToggle(boolean v) { this.throwOnToggle = v; }

        // Backwards-compatible: called by controller when no user header is provided
        @Override
        public List<Todo> list() { return List.copyOf(list); }

        // New per-user signature used by controller
        @Override
        public List<Todo> list(String userName) { return List.copyOf(list); }

        @Override
        public Todo add(String title) { return addReturn != null ? addReturn : new Todo(999, title, false, null); }

        @Override
        public Todo add(CreateTodoRequest req) { return addReturn != null ? addReturn : new Todo(999, req.getTitle(), false, null); }

        @Override
        public Todo add(String userName, CreateTodoRequest req) { return addReturn != null ? addReturn : new Todo(999, req.getTitle(), false, null); }

        @Override
        public Todo toggle(long id) {
            if (throwOnToggle) throw new TodoNotFoundException(id);
            return new Todo(id, "t", true, null);
        }

        @Override
        public Todo toggle(String userName, long id) {
            if (throwOnToggle) throw new TodoNotFoundException(id);
            return new Todo(id, "t", true, null);
        }
    }
}
