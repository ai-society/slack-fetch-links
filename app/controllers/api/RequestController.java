package controllers.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import models.SlackCommandRequest;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.IOException;
import java.net.MalformedURLException;
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

            List<String> links = extractLinks(text);
            String link = "";

            if(links.size() != 0)
                link = links.get(0);

            SlackCommandRequest request = SlackCommandRequest.create(channelName, userName, link, text);
            request.save();

            ObjectNode jsonNode = Json.newObject();

            jsonNode.put("response_type", "in_channel");
            jsonNode.put("text", "Thanks " + userName + "!, Happy Hacking" + "\n" + text);

            //Request specifiedEpisode = Episode.find.byId(episodeId);
            return ok(jsonNode);
        }
    }

    private List<String> extractLinks(String input) {
        List<String> result = new ArrayList<String>();

        Pattern pattern = Pattern.compile(
            "\\b(((ht|f)tp(s?)\\:\\/\\/|~\\/|\\/)|www.)" +
                "(\\w+:\\w+@)?(([-\\w]+\\.)+(com|org|net|gov" +
                "|mil|biz|info|mobi|name|aero|jobs|museum" +
                "|travel|[a-z]{2}))(:[\\d]{1,5})?" +
                "(((\\/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?" +
                "((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" +
                "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)" +
                "(&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" +
                "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*" +
                "(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?\\b");

        Matcher matcher = pattern.matcher(input);

        while (matcher.find())
            result.add(matcher.group());

        return result;
    }
}
