package tests;

import io.qameta.allure.*;
import models.User;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.RequestSpec;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Epic("User Management API")
@Feature("User Profile CRUD Operations")
public class UserTest {

    private final String USER_ENDPOINT = "/users";

    @Test(priority = 1, description = "TC01: Create a new User")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that a new user can be successfully created with data.")
    public void createUser() {

        User user = new User(
                "Dung Nhan",
                "nhantester",
                "nhan.tester@bank.com",
                "Seven", "Ho Chi Minh",
                "090-123-4567",
                "nhantester.com");

        User responseUser =
        given()
                .spec(RequestSpec.getCommonSpec())
                .body(user)
        .when()
                .post(USER_ENDPOINT)
        .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(User.class);
        validateUserInReport(responseUser, "Dung Nhan", "nhantester", "nhan.tester@bank.com");
        Assert.assertEquals(responseUser.getName(), "Dung Nhan");
        Assert.assertEquals(responseUser.getUsername(), "nhantester");
        Assert.assertEquals(responseUser.getEmail(), "nhan.tester@bank.com");
        Assert.assertEquals(responseUser.getPhone(), "090-123-4567");
        Assert.assertEquals(responseUser.getWebsite(), "nhantester.com");
        Assert.assertEquals(responseUser.getAddress().getCity(), "Ho Chi Minh");
        Assert.assertEquals(responseUser.getAddress().getStreet(), "Seven");
    }

    @Test(priority = 2, dependsOnMethods = "createUser", description = "TC02: Get details of User")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that user details can be retrieved using valid User ID.")
    public void getUserById() {

        User responseUser =
                given()
                        .spec(RequestSpec.getCommonSpec())
                        .when()
                        .get(USER_ENDPOINT + "/" + 1)
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .as(User.class);
        validateUserInReport(responseUser, "Leanne Graham", "Bret", "Sincere@april.biz");

        Assert.assertEquals(responseUser.getName(), "Leanne Graham");
        Assert.assertEquals(responseUser.getUsername(), "Bret");
        Assert.assertEquals(responseUser.getEmail(), "Sincere@april.biz");
        Assert.assertEquals(responseUser.getPhone(), "1-770-736-8031 x56442");
        Assert.assertEquals(responseUser.getWebsite(), "hildegard.org");
        Assert.assertEquals(responseUser.getAddress().getCity(), "Gwenborough");
        Assert.assertEquals(responseUser.getAddress().getStreet(), "Kulas Light");

    }

    @Test(priority = 3, dependsOnMethods = "createUser", description = "TC03: Update details of User")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that an existing user's information can be updated successfully with valid data.")
    public void updateUser() {

        User updatedUser = new User(
                "Dung Nhan Updated",
                "nhantester_pro",
                "nhan.updated@bank.com",
                "Eight", "Da Nang",
                "090-888-9999",
                "nhantester.pro");

        User responseUser =
        given()
                .spec(RequestSpec.getCommonSpec())
                .body(updatedUser)
        .when()
                .put(USER_ENDPOINT + "/" + 1)
        .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(User.class);
        validateUserInReport(responseUser, "Dung Nhan Updated", "nhantester_pro", "nhan.updated@bank.com");

        Assert.assertEquals(responseUser.getName(), "Dung Nhan Updated");
        Assert.assertEquals(responseUser.getUsername(), "nhantester_pro");
        Assert.assertEquals(responseUser.getEmail(), "nhan.updated@bank.com");
        Assert.assertEquals(responseUser.getPhone(), "090-888-9999");
        Assert.assertEquals(responseUser.getWebsite(), "nhantester.pro");
        Assert.assertEquals(responseUser.getAddress().getCity(), "Da Nang");
        Assert.assertEquals(responseUser.getAddress().getStreet(), "Eight");
    }

    @Test(priority = 4, dependsOnMethods = "updateUser", description = "TC04: Delete an existing User by ID")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that an existing user can be deleted from the system using their ID.")
    public void deleteUser() {

        given()
                .spec(RequestSpec.getCommonSpec())
                .log().all()
                .when()
                .delete(USER_ENDPOINT + '/' + 1)
                .then()
                .log().all()
                .statusCode(200)
                .body(equalTo("{}"));

    }

    @Test(priority = 5, description = "TC05: Get User with non-existing ID (404)")
    @Severity(SeverityLevel.MINOR)
    @Description("Ensure the system returns a 404 Not Found status when searching for a non-existent User ID.")
    public void getUserByInvalidId() {
        given()
                .spec(RequestSpec.getCommonSpec())
                .when()
                .get(USER_ENDPOINT + "/9999")
                .then()
                .log().status()
                .statusCode(404);

    }

    @Test(priority = 6, description = "TC06: Create User with invalid data format (400)")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validate that the system rejects requests with malformed JSON or invalid data types.")
    public void createUserWithInvalidData() {
        String invalidBody = "{name: Missing quotes, email: not-an-email}";
        given()
                .spec(RequestSpec.getCommonSpec())
                .body(invalidBody)
                .when()
                .post(USER_ENDPOINT)
                .then()
                .log().status()
                .statusCode(400);
    }

    @Test(priority = 7, description = "TC07: Access protected resource without token")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that unauthorized access to protected endpoints is strictly prohibited (401 Unauthorized).")
    public void getProtectedResource() {
        given()
                .baseUri("https://jsonplaceholder.typicode.com")
                .when()
                .get("/posts/1")
                .then()
                .log().status()
                .statusCode(401);
    }

    @Step("Validate user details: Name={1}, Username={2}, Email={3}")
    private void validateUserInReport(User user, String expectedName, String expectedUsername, String expectedEmail){
        Assert.assertEquals(user.getName(), expectedName);
        Assert.assertEquals(user.getUsername(), expectedUsername);
        Assert.assertEquals(user.getEmail(), expectedEmail);
    }
}
