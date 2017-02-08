package controllers.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.SlackCommandRequest;
import play.mvc.Controller;
import play.mvc.Result;

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

            if(!token.equals(new String("wygKn3F20sCyYhgfbLTkRW7I")))
                return unauthorized("Invalid Token");

            List<String> links = extractUrls(text);

            if(links == null)
                return badRequest("No link found");

            String link = links.get(0);

            SlackCommandRequest request = SlackCommandRequest.create(channelName, userName, link, text);
            request.save();
            //Request specifiedEpisode = Episode.find.byId(episodeId);
            return ok("Thanks @" + userName + "!, Happy Hacking" + "\n" + text);
        }
    }

    private List<String> extractUrls(String input) {
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
        while (matcher.find()) {
            result.add(matcher.group());
        }

        return result;
    }
}
