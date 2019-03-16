package passtheaux.operationunthinkable.RequestModels;

import java.util.HashMap;

import passtheaux.operationunthinkable.ResponseModels.Response;

public interface Request {
    String url = "http://josephsirna.org:81/dev/Data/";

    HashMap<String, String> Serialize();

    Response ResponseModel = null;
}
