package passtheaux.operationunthinkable.RequestModels;

import java.util.HashMap;
import java.util.UUID;

import passtheaux.operationunthinkable.ResponseModels.GetSongListResponse;
import passtheaux.operationunthinkable.ResponseModels.Response;

public class GetSongListRequest {

    public Response ResponseModel = new GetSongListResponse();

    public String url = Request.url + "GetSongList";

    public UUID sessionId;

    public HashMap<String, String> Serialize() {
        if (sessionId == null) {
            return null;
        }

        return new HashMap<String, String>() {{
            put("sessionId", sessionId.toString());
        }};
    }
}
