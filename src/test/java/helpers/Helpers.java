package helpers;
import java.util.*;

import io.restassured.response.Response;
import org.json.JSONObject;
public class Helpers {

    public static ArrayList<String> getDoneStatus(Response res, String doneStatus) {
        ArrayList<String> doneStatuses = new ArrayList<String>();

        int listSize = res.jsonPath().getList("todos.doneStatus").size();
        for (int i = 0; i < listSize; i++) {
            doneStatuses.add(res.jsonPath().getString("todos[" + i + "]" + "." + doneStatus));
        }

        return doneStatuses;
    }
}
