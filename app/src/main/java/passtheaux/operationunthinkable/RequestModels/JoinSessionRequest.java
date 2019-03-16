package passtheaux.operationunthinkable.RequestModels;

import java.util.HashMap;

import passtheaux.operationunthinkable.ResponseModels.JoinSessionResponse;
import passtheaux.operationunthinkable.ResponseModels.Response;

public class JoinSessionRequest implements Request {

    public Response ResponseModel = new JoinSessionResponse();

    public String url = Request.url + "JoinSession";

    public String password = "";
    public String displayName = "";

    public HashMap<String, String> Serialize() {

        if (displayName == null || displayName.equals("")) {
            return null;
        }

        if (password == null || password.equals("")) {
            return null;
        }

        return new HashMap<String, String>() {{
            put("DisplayName", displayName);
            put("Password", password);
        }};
    }
}
