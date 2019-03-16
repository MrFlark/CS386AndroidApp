package passtheaux.operationunthinkable.RequestModels;

import java.util.HashMap;

import passtheaux.operationunthinkable.ResponseModels.GetSessionListResponse;
import passtheaux.operationunthinkable.ResponseModels.Response;

public class GetSessionListRequest implements Request {

    public Response ResponseModel = new GetSessionListResponse();

    public String url = Request.url + "GetSessionList";

    public HashMap<String, String> Serialize() {
        return new HashMap<String, String>();
    }
}
