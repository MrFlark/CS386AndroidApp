package passtheaux.operationunthinkable.RequestModels;

import java.util.HashMap;

import passtheaux.operationunthinkable.ResponseModels.CreateSessionResponse;
import passtheaux.operationunthinkable.ResponseModels.Response;

public class CreateSessionRequest implements Request {

    public Response ResponseModel = new CreateSessionResponse();

    public String url = Request.url + "CreateSession";

    public String name = "";
    public String displayName = "";
    public String password = "";

    public HashMap<String, String> Serialize() {

        //check for valid (non-null, non-blank) values
        if (name == null || name.equals("")) {
            return null;
        }

        if (displayName == null || displayName.equals("")) {
            return null;
        }

        return new HashMap<String, String>() {{
            put("Name", name);
            put("DisplayName", displayName);

            if (password != null && !password.equals("")) {
                put("Password", password);
            }
        }};
    }
}
