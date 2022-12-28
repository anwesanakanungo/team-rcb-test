package api.testcases;
import api.apiconfigreader.ConfigReader;
import api.requestbuilder.RequestBuilder;
import api.util.ApiUtil;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.List;
import java.util.function.Predicate;
import static api.frameworkconstants.FrameWorkConstants.WRITERESPONSETOJSON;
import static org.assertj.core.api.Assertions.assertThat;

public class TestRcbPlayers {
    private RequestBuilder requestBuilder;

    @Test
    public void testAtLeastOneWicketKeeper() {
        Response response = RequestBuilder.bulidRequestForGetCall()
                .get(ConfigReader.getValue("RCB_PLAYERS_URL"));
        assertThat(response.statusCode())
                .isEqualTo(200);
        JsonPath jsonPathEvaluator = response.jsonPath();
        List<String> wicketKeeperList = jsonPathEvaluator.getList("player.role");
        int countWicketKeeper = (int) wicketKeeperList.stream().filter("Wicket-keeper"::equals).count();
        assertThat(countWicketKeeper)
                .isEqualTo(1)
                .isGreaterThanOrEqualTo(1);
        ApiUtil.storeJsonResponse(WRITERESPONSETOJSON, response);
    }

    @Test
    public void testFourForeignPlayers() {

        Response response = RequestBuilder.bulidRequestForGetCall()
                .get(ConfigReader.getValue("RCB_PLAYERS_URL"));
        assertThat(response.statusCode())
                .isEqualTo(200);
        JsonPath jsonPathEvaluator = response.jsonPath();
        List<String> foreignPlayerList = jsonPathEvaluator.getList("player.country");
        Predicate<String> foreignPlayerListFist = name -> !name.contains("India");
        int countForeignPlayerList = (int) foreignPlayerList.stream().filter(foreignPlayerListFist)
                .count();
        assertThat(countForeignPlayerList)
                .isEqualTo(4)
                .isLessThanOrEqualTo(4);
        ApiUtil.storeJsonResponse(WRITERESPONSETOJSON, response);
    }
}