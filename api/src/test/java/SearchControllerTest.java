import com.Jinhyy.api.controller.search.SearchController;
import com.Jinhyy.search.request.SearchRequest;
import com.Jinhyy.search.type.SortType;
import com.Jinhyy.service.search.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;


@RunWith(SpringRunner.class)
public class SearchControllerTest {

    @Mock
    private SearchService searchService;
    @Test
    public void searchBlog() {
        SearchRequest searchRequest = new SearchRequest()
                .setQuery("test")
                .setPage(1)
                .setPageSize(10)
                .setSortType(SortType.ACCURACY)
                ;

        WebTestClient webClient = WebTestClient.bindToController(new SearchController(searchService)).configureClient().build();
        webClient.post()
                .uri("/search/blog")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(searchRequest), SearchRequest.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void searchHot() {
        WebTestClient webClient = WebTestClient.bindToController(new SearchController(searchService)).configureClient().build();
        webClient.get()
                .uri("/search/hot")
                .exchange()
                .expectStatus().isOk();
    }
}
