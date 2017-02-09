package controllers.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.SlackCommandRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.URLUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

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
            String responseURL = postValues.get("response_url")[0];

            if(!token.equals("wygKn3F20sCyYhgfbLTkRW7I"))
                return unauthorized("Invalid Token");

            new HttpPostToSlack(channelName, text, userName, responseURL).start();
            return ok();

            /*

            String link = URLUtils.extractLinks(text);

            SlackCommandRequest request = SlackCommandRequest.create(channelName, userName, link, text);
            request.save();

            ObjectNode jsonNode = Json.newObject();

            jsonNode.put("response_type", "in_channel");
            jsonNode.put("text", "Thanks " + userName + "!, Happy Hacking" + "\n" + text);
            jsonNode.put("unfurl_links", true);
            jsonNode.put("response_url", responseURL);

            return ok(jsonNode);
            */
        }
    }

}

class HttpPostToSlack extends Thread {

    String channelName;
    String text;
    String token;
    String userName;
    String responseURL;

    public HttpPostToSlack(String channelName, String text, String userName, String responseURL) {

        this.channelName = channelName;
        this.text = text;
        this.userName = userName;
        this.responseURL = responseURL;

    }

    public void run() {

        String link = URLUtils.extractLinks(text);

        SlackCommandRequest request = SlackCommandRequest.create(channelName, userName, link, text);
        request.save();

        ObjectNode jsonNode = Json.newObject();

        jsonNode.put("response_type", "in_channel");
        jsonNode.put("text", "Thanks " + userName + "!, Happy Hacking" + "\n" + text);
        jsonNode.put("unfurl_links", true);
        jsonNode.put("response_url", responseURL);

        HttpClient httpClient    = HttpClientBuilder.create().build();
        HttpPost post          = new HttpPost(responseURL);
        try {
            StringEntity postingString = new StringEntity(jsonNode.toString());
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            httpClient.execute(post);
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

}
