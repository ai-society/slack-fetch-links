package controllers.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.SlackCommandRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.URLUtils;

import java.io.IOException;
import java.util.List;
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

            new HttpPostToSlack(postValues).start();
            return ok();
        }
    }

    public Result test() {

        System.out.println(request().body().asJson());

        return ok(request().body().asJson());
    }

}

class HttpPostToSlack extends Thread {

    String channelName;
    String text;
    String userName;
    String responseURL;
    Map<String, String[]> postValues;

    public HttpPostToSlack(Map<String, String[]> postValues) {

        String channelName = postValues.get("channel_name")[0];
        String text = postValues.get("text")[0];
        String userName = postValues.get("user_name")[0];
        String responseURL = postValues.get("response_url")[0];

        this.channelName = channelName;
        this.text = text;
        this.userName = userName;
        this.responseURL = responseURL;
        this.postValues = postValues;

    }

    public void run() {

        String link = URLUtils.extractLinks(text);
        String responseMessage;

        SlackCommandRequest request = SlackCommandRequest.create(channelName, userName, link, text);
        List<SlackCommandRequest> sameLinkResources = SlackCommandRequest.searchByLink(link);

        ObjectNode jsonNode = Json.newObject();


        jsonNode.put("unfurl_links", true);

        if(sameLinkResources.size() == 0) {
            jsonNode.put("response_type", "in_channel");
            responseMessage = "Thanks " + userName + ", keep sharing AI knowledge!" + "\n" + text;
            request.save();
        } else {
            SlackCommandRequest sameLinkResource = sameLinkResources.get(0);

            responseMessage = userName + ":";
            responseMessage += "\n" + "The resource link was already in the server.";
            responseMessage += "\n" + "It was posted by " +
                ((userName.equals(sameLinkResource.getUserName())) ? " you " : sameLinkResource.getUserName());
            responseMessage += " on " + sameLinkResource.getDate();
        }

        jsonNode.put("text", responseMessage);
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
