package controllers.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.SlackCommandRequest;
import play.mvc.Controller;
import play.mvc.Result;

import java.net.MalformedURLException;

/**
 * Created by scvalencia606 on 2/8/17.
 */
public class RequestController extends Controller {

    public Result create() {
        JsonNode jsonRequestBody = request().body().asJson();

        System.out.println(jsonRequestBody);

        if (true)
            return badRequest(request().body().asFormUrlEncoded().toString());
        else {

            /*
            * token=gIkuvaNzQIHg97ATvDxqgjtO
                team_id=T0001
                team_domain=example
                channel_id=C2147483705
                channel_name=test
                user_id=U2147483697
                user_name=Steve
                command=/weather
                text=94070
                response_url=https://hooks.slack.com/commands/1234/5678
             */

            SlackCommandRequest request = SlackCommandRequest.create("sdfdsfsdf", "", "", jsonRequestBody.toString());
            request.save();
            //Request specifiedEpisode = Episode.find.byId(episodeId);
            return ok("sa");
        }
    }
}
