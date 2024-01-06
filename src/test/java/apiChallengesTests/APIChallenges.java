package apiChallengesTests;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import java.util.Random;

import org.json.JSONObject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class APIChallenges {
    public static String challengerId;
    public static String validId;
    public static String validTodoBody;

    @BeforeSuite
    void setUp() {
        RestAssured.baseURI = "https://apichallenges.herokuapp.com";
        Response response = RestAssured.post("/challenger");
        given().log().all().
                post("/challenger").
                then().log().all().
                header("X-Challenger", matchesRegex(".*"));

        Headers headers = response.getHeaders();
        challengerId = headers.get("X-Challenger").getValue();
    }

    //GET Tests
    @Test
    void getChallenges() {
        given().log().all().
                header("X-CHALLENGER", challengerId).
                when().
                get("/challenges").
                then().
                statusCode(200).
                header("content-type", "application/json").
                time(lessThan(2000L));


    }

    @Test
    void getTodos() {
        Response res = given().log().all().
                header("X-CHALLENGER", challengerId).
                when().log().all().
                get("/todos");

        Assert.assertEquals(res.getStatusCode(), 200);
        validId = res.jsonPath().getString("todos[0].id");
        validTodoBody = res.jsonPath().getString("todos[0]");

    }

    @Test
    void getTodo() {
        given().log().all().
                header("X-CHALLENGER", challengerId).
                when().
                get("/todo").
                then().
                statusCode(404);
    }

    @Test
    void getTodosValidId() {
        given().log().all().
                header("X-CHALLENGER", challengerId).
                when().
                get("/todos/" + validId).
                then().
                statusCode(200);
    }

    @Test
    void getTodosInvalidId() {
        given().log().all().
                header("X-CHALLENGER", challengerId).
                when().
                get("/todos/0999217").
                then().
                statusCode(404);
    }

    //HEAD Tests
    @Test
    void headTodos() {
        given().log().all().
                header("X-CHALLENGER", challengerId).
                when().
                head("/todos").
                then().
                statusCode(200);
    }

    //POST Tests
    @Test
    void postTodos() {
        Random random = new Random();

        JSONObject postBody = new JSONObject()
                .put("title", Faker.instance().lordOfTheRings().character())
                .put("doneStatus", random.nextBoolean())
                .put("description", Faker.instance().lordOfTheRings().location());


        given().log().all().
                header("X-CHALLENGER", challengerId).
                body(postBody.toString()).
                contentType("application/json").
                when().
                post("/todos").
                then().log().all().
                statusCode(201);
    }

    @Test
    void postTodosInvalidDoneStatus() {
        Random random = new Random();

        JSONObject postBody = new JSONObject()
                .put("title", Faker.instance().lordOfTheRings().character())
                .put("doneStatus", Faker.instance().beer())
                .put("description", Faker.instance().lordOfTheRings().location());


        given().log().all().
                header("X-CHALLENGER", challengerId).
                body(postBody.toString()).
                contentType("application/json").
                when().
                post("/todos").
                then().log().all().
                statusCode(400);


    }

}
