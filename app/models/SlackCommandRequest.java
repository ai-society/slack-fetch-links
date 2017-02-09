package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by scvalencia606 on 2/8/17.
 */
@Entity
public class SlackCommandRequest extends Model {

    @Id
    @GeneratedValue
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Constraints.Required
    private String channelName;

    @Constraints.Required
    private String userName;

    private String link;

    @Constraints.Required
    @Column(columnDefinition="TEXT")
    private String text;

    public static Finder<Long, SlackCommandRequest> find = new Finder(
        Long.class, SlackCommandRequest.class
    );

    public SlackCommandRequest() {}

    public static SlackCommandRequest create(String channelName, String userName, String link, String text) {

        SlackCommandRequest request = new SlackCommandRequest();

        request.channelName = channelName;
        request.userName = userName;
        request.link = link;
        request.text = text;

        request.date = new Date();

        return request;
    }

    public static List<SlackCommandRequest> searchByLink(String link){
        return find.where().ilike("link", "%" + link + "%").findList();
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getUserName() {
        return userName;
    }

    public String getLink() {
        return link;
    }

    public String getText() {
        return text;
    }
}
