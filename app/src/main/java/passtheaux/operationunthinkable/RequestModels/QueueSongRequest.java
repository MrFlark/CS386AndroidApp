package passtheaux.operationunthinkable.RequestModels;

import java.util.HashMap;
import java.util.UUID;

import passtheaux.operationunthinkable.ResponseModels.QueueSongResponse;
import passtheaux.operationunthinkable.ResponseModels.Response;

public class QueueSongRequest implements Request {

    public Response ResponseModel = new QueueSongResponse();

    public String url = Request.url + "QueueSong";

    public String songUrl = "";
    public String source = "";
    public UUID clientId;

    public HashMap<String, String> Serialize() {

        if (songUrl == null || songUrl.equals("")) {
            return null;
        }

        if (source == null || source.equals("")) {
            return null;
        }

        if (clientId == null) {
            return null;
        }

        return new HashMap<String, String>() {{
            put("clientId", clientId.toString());
            put("source", source);
            put("url", songUrl);
        }};
    }
}
