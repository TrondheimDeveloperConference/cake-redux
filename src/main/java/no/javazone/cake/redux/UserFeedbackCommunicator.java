package no.javazone.cake.redux;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Optional;

public class UserFeedbackCommunicator {

    public Optional<String> feedback(String conferenceId, String talkId) {
        String urlToDevNull = convertFromEmsToDevNullUrl(conferenceId, talkId);
        URLConnection urlConnection = CommunicatorHelper.openConnection(urlToDevNull, false);
        try (InputStream is = CommunicatorHelper.openStream(urlConnection)){
            return Optional.of(CommunicatorHelper.toString(is));
        } catch (IOException ignore) {
            return Optional.empty();
        }
    }

    private String convertFromEmsToDevNullUrl(String conferenceId, String sessionId) {
        return Configuration.devnullLocation() + "/events/" + conferenceId + "/sessions/" + sessionId + "/feedbacks";
    }

}

