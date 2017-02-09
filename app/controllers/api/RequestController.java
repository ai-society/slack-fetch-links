package controllers.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.SlackCommandRequest;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.URLUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by scvalencia606 on 2/8/17.
 */
public class RequestController extends Controller {

    public Result create() {

        Map<String, String[]> postValues = request().body().asFormUrlEncoded();

        if (postValues == null)
            return badRequest("Invalid parameters");
        else {

            String channelName = postValues.get("channel_name")[0];
            String text = postValues.get("text")[0];
            String token = postValues.get("token")[0];
            String userName = postValues.get("user_name")[0];

            if(!token.equals("wygKn3F20sCyYhgfbLTkRW7I"))
                return unauthorized("Invalid Token");

            String link = URLUtils.extractLinks(text);

            SlackCommandRequest request = SlackCommandRequest.create(channelName, userName, link, text);
            request.save();

            ObjectNode jsonNode = Json.newObject();

            jsonNode.put("response_type", "in_channel");
            jsonNode.put("text", "Thanks " + userName + "!, Happy Hacking" + "\n" + text);

            return ok(jsonNode);
        }
    }


}
