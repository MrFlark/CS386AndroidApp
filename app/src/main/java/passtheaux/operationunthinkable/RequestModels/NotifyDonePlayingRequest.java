package passtheaux.operationunthinkable.RequestModels;

import java.util.HashMap;
import java.util.UUID;

import passtheaux.operationunthinkable.ResponseModels.NotifyDonePlayingResponse;
import passtheaux.operationunthinkable.ResponseModels.Response;

public class NotifyDonePlayingRequest implements Request {

    public Response ResponseModel = new NotifyDonePlayingResponse();

    public String url = Request.url + "NotifyDonePlaying";

    public UUID sessionId;
    public UUID songId;

    public HashMap<String, String> Serialize() {
        if (sessionId == null) {
            return null;
        }

        if (songId == null) {
            return null;
        }

        return new HashMap<String, String>() {{
            put("sessionId", sessionId.toString());
            put("songId", songId.toString());
        }};
    }
}
