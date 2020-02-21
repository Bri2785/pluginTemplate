package com.printnode.api.impl;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 *
 * This is the main class used in PrintNode-Java. Controls every request.
 * @author JakeTorrance
 * @author PrintNode
 * */
public class APIClient {

    /**
     * TypeAdapter for GSON. Converts ints of -1 to non-serialized values.
     *
     * @return a type adapter for GSON.
     * */
    private static final TypeAdapter<Integer> INT_ADAPTER = new TypeAdapter<Integer>() {
        @Override public void write(final JsonWriter out, final Integer value) throws IOException {
            if (value == -1) {
                out.nullValue();
            } else {
                out.value(value);
            }
        }
        @Override public Integer read(final JsonReader in) throws IOException {
            return in.nextInt();
        }

    };

    /**
     * TypeAdapter for GSON. Converts null Bools to non-serialized values.
     *
     * @return a type adapter for GSON.
     * */
    static final TypeAdapter<Boolean> BOOL_ADAPTER = new TypeAdapter<Boolean>() {
        @Override public void write(final JsonWriter out, final Boolean value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value);
            }
        }
        @Override public Boolean read(final JsonReader in) throws IOException {
            return in.nextBoolean();
        }
    };

    /**
     * The API-Url to be used with the Library.
     * */
    private static String apiUrl = "https://api.printnode.com";

    /**
     * The child headers we are authenticating with.
     * */
    private String[] childHeaders = new String[2];

    /**
     * The credentials we are authenticating with.
     * */
    private CredentialsProvider credentials;

    /**
     * default constructor for the APIClient.
     *
     * @param auth an Auth object which the APIClient will then save into a CredentialsProvider object.
     * @see Auth
     * */
    public APIClient(final Auth auth) {
        String[] credentialsArray = auth.getCredentials();
        credentials = new BasicCredentialsProvider();
        credentials.setCredentials(
                new AuthScope(null, -1),
                new UsernamePasswordCredentials(credentialsArray[0], credentialsArray[1])
                );
        childHeaders[0] = "";
        childHeaders[1] = "";

    }

    /**
     * Setter for API-Url if you don't want to use the default.
     *
     * @param url New url to be set for use with the library.
     * */
    public final void setApiUrl(final String url) {
        apiUrl = url;
    }

    /**
     * Sets the headers to be authenticated via X-Child-Account-By-Id.
     *
     * @param id id of child account.
     * */
    public final void setChildAccountById(final int id) {
        childHeaders[0] = "X-Child-Account-By-Id";
        childHeaders[1] = Integer.toString(id);
    }

    /**
     * Sets the headers to be authenticated via X-Child-Account-By-Email.
     *
     * @param email email of child account.
     * */
    public final void setChildAccountByEmail(final String email) {
        childHeaders[0] = "X-Child-Account-By-Email";
        childHeaders[1] = email;
    }

    /**
     * Sets the headers to be authenticated via X-Child-Account-By-CreatorRef.
     *
     * @param creatorRef creator refence of child account.
     * */
    public final void setChildAccountByCreatorRef(final String creatorRef) {
        childHeaders[0] = "X-Child-Account-By-CreatorRef";
        childHeaders[1] = creatorRef;
    }

    /**
     * Takes part of a response and creates a JsonElement from the response.
     *
     * @param response HTTP Response in JSON form.
     *
     * @return JsonElement of the response.
     * */
    public final JsonElement responseToJsonElement(final CloseableHttpResponse response) throws IOException {
        String responseString = EntityUtils.toString(response.getEntity());
        int statusCode = response.getStatusLine().getStatusCode();
        checkResponseForExceptions(statusCode,responseString);
        JsonElement jsonResponse = new JsonParser().parse(responseString);
        return jsonResponse;
    }

    /**
     * Creates a non-default Gson object.
     *
     * @return Gson object with type-adapters required for our objects.
     * */
    public final Gson gsonWithAdapters() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Integer.class, INT_ADAPTER)
                .registerTypeAdapter(int.class, INT_ADAPTER)
                .registerTypeAdapter(Boolean.class, BOOL_ADAPTER)
                .registerTypeAdapter(boolean.class, BOOL_ADAPTER)
                .create();
        return gson;
    }

    /**
     * Deletes apikey specified by the parameter.
     *
     * @param description apikey name to be deleted.
     * @return Boolean whether apikey was deleted or not.
     * @throws IOException if HTTP client is given bad values
     * */
    public final boolean deleteApiKey(final String description) throws IOException {
        CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credentials).build();
        boolean result;
        try {
            HttpDelete httpdelete = new HttpDelete(apiUrl + "/account/apikey/" + description);
            httpdelete.addHeader(childHeaders[0], childHeaders[1]);
            CloseableHttpResponse response = client.execute(httpdelete);
            try {
                JsonPrimitive responseParse = responseToJsonElement(response).getAsJsonPrimitive();
                result = responseParse.getAsBoolean();
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
        return result;
    }

    /**
     * Deletes tag specified by the parameter.
     *
     * @param tagname Tag to be deleted.
     * @return Boolean whether tag was deleted or not.
     * @throws IOException if HTTP client is given bad values
     * */
    public final boolean deleteTag(final String tagname) throws IOException {
        CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credentials).build();
        boolean result;
        try {
            HttpDelete httpdelete = new HttpDelete(apiUrl + "/account/tag/" + tagname);
            httpdelete.addHeader(childHeaders[0], childHeaders[1]);
            CloseableHttpResponse response = client.execute(httpdelete);
            try {
                JsonPrimitive responseParse = responseToJsonElement(response).getAsJsonPrimitive();
                result = responseParse.getAsBoolean();
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
        return result;
    }

    /**
     * Deletes account.
     * This can only be used when specified a child account is specified by email, id or creatorRef.
     * Otherwise, will throw an APIException as the response will throw a non 2xx status code.
     *
     * @return Boolean whether account was deleted or not.
     * @throws IOException if HTTP client is given bad values
     * @see APIException
     * */
    public final boolean deleteAccount() throws IOException {
        CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credentials).build();
        boolean result;
        try {
            HttpDelete httpdelete = new HttpDelete(apiUrl + "/account/");
            httpdelete.addHeader(childHeaders[0], childHeaders[1]);
            CloseableHttpResponse response = client.execute(httpdelete);
            try {
                JsonPrimitive responseParse = responseToJsonElement(response).getAsJsonPrimitive();
                result = responseParse.getAsBoolean();
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
        return result;
    }

    /**
     * Given a set of clients, such as "10-15" or "10,11,13" or just "10",
     * will set whether clients in the set are enabled.
     * If you are unsure what is possible for clientSet, check the PrintNode API docs on printnode.com.
     *
     * @param clientSet set of clients as a string.
     * @param enabled whether you are disabling or enabling the clients.
     * @return An array of ids relative to clients with changed values.
     * @throws IOException if HTTP client is given bad values
     * */
    public final int[] modifyClientDownloads(final String clientSet, final boolean enabled) throws IOException {
        CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credentials).build();
        int[] results;
        try {
            HttpPatch httppost = new HttpPatch(apiUrl + "/download/clients/" + clientSet);
            httppost.addHeader(childHeaders[0], childHeaders[1]);
            httppost.addHeader("Content-Type", "application/json");
            Gson gson = gsonWithAdapters();
            JsonObject jObject = new JsonObject();
            jObject.addProperty("enabled", enabled);
            String json = gson.toJson(jObject);
            StringEntity jsonEntity = new StringEntity(json);
            httppost.setEntity(jsonEntity);
            CloseableHttpResponse response = client.execute(httppost);
            try {
                JsonArray responseParse = responseToJsonElement(response).getAsJsonArray();
                results = new int[responseParse.size()];
                for (int i = 0; i < responseParse.size(); i++) {
                    results[i] = responseParse.get(i).getAsInt();
                }
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
        return results;
    }

    /**
     * Given an Account object, modifies an account.
     * This can only be used when a child account is specified by email, id or creatorRef.
     *
     * @param accountInfo account object. Requires atleast one value set.
     * Having id set will throw an exception, unless set to -1.
     * @return Whoami object of the modified account.
     * @throws IOException if HTTP client is given bad values
     * @see Account
     * @see Whoami
     * */
    public final Whoami modifyAccount(final Account accountInfo) throws IOException {
        CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credentials).build();
        Whoami account;
        try {
            HttpPatch httppost = new HttpPatch(apiUrl + "/account/");
            httppost.addHeader(childHeaders[0], childHeaders[1]);
            httppost.addHeader("Content-Type", "application/json");
            Gson gson = gsonWithAdapters();
            String json = gson.toJson(accountInfo);
            StringEntity jsonEntity = new StringEntity(json);
            httppost.setEntity(jsonEntity);
            CloseableHttpResponse response = client.execute(httppost);
            try {
                JsonObject responseParse = responseToJsonElement(response).getAsJsonObject();
                account = new Whoami(responseParse);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
        return account;
    }

    /**
     * Given a name and value for a tag, creates that tag.
     *
     * @param tagName name of tag.
     * @param tagValue value of tag.
     * @return String which will be "created" when no errors happen.
     * @throws IOException if HTTP client is given bad values
     * */
    public final String createTag(final String tagName, final String tagValue) throws IOException {
        CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credentials).build();
        String tag;
        try {
            HttpPost httppost = new HttpPost(apiUrl + "/account/tag/" + tagName);
            httppost.addHeader(childHeaders[0], childHeaders[1]);
            httppost.addHeader("Content-Type", "application/json");
            Gson gson = gsonWithAdapters();
            String json = gson.toJson(tagValue);
            StringEntity jsonEntity = new StringEntity(json);
            httppost.setEntity(jsonEntity);
            CloseableHttpResponse response = client.execute(httppost);
            try {
                String responseParse = responseToJsonElement(response).getAsString();
                tag = responseParse;
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
        return tag;
    }



    /**
     * Given a description for an apikey, creates that apikey.
     *
     * @param description reference for the apikey.
     * @return The value of the apikey.
     * @throws IOException if HTTP client is given bad values
     * */
    public final String createApiKey(final String description) throws IOException {
        CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credentials).build();
        String apikey;
        try {
            HttpPost httppost = new HttpPost(apiUrl + "/account/apikey/" + description);
            httppost.addHeader(childHeaders[0], childHeaders[1]);
            CloseableHttpResponse response = client.execute(httppost);
            try {
                String responseParse = responseToJsonElement(response).getAsString();
                apikey = responseParse;
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
        return apikey;
    }

    /**
     * Given a CreateAccountJson object, creates an account.
     *
     * @param accountInfo CreateAccountJson object with values set.
     * @return CreateAccountObject.
     * This is practically the same as the CreateAccountJson object with some other additions.
     * @throws IOException if HTTP client is given bad values
     * @see CreateAccountJson
     * @see CreateAccountObject
     * @see Account
     * */
    public final CreateAccountObject createAccount(final CreateAccountJson accountInfo) throws IOException {
        CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credentials).build();
        CreateAccountObject account;
        try {
            HttpPost httppost = new HttpPost(apiUrl + "/account/");
            httppost.addHeader(childHeaders[0], childHeaders[1]);
            httppost.addHeader("Content-Type", "application/json");
            Gson gson = gsonWithAdapters();
            String json = gson.toJson(accountInfo);
            StringEntity jsonEntity = new StringEntity(json);
            httppost.setEntity(jsonEntity);
            CloseableHttpResponse response = client.execute(httppost);
            try {
                JsonObject responseParse = responseToJsonElement(response).getAsJsonObject();
                account = new CreateAccountObject(responseParse);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
        return account;
    }

    /**
     * Given a PrintJobJson object, creates a PrintJob.
     *
     * @param printjobinfo PrintJobJson object with values set.
     * @return id of printjob that was just created.
     * @throws IOException if HTTP client is given bad values
     * @see PrintJobJson
     * */
    public final int createPrintJob(final PrintJobJson printjobinfo) throws IOException {
        CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credentials).build();
        int printjob;
        try {
            HttpPost httppost = new HttpPost(apiUrl + "/printjobs");
            httppost.addHeader(childHeaders[0], childHeaders[1]);
            httppost.addHeader("Content-Type", "application/json");
            Gson gson = gsonWithAdapters();
            String json = gson.toJson(printjobinfo);
            StringEntity jsonEntity = new StringEntity(json);
            httppost.setEntity(jsonEntity);
            CloseableHttpResponse response = client.execute(httppost);
            try {
                int responseParse = responseToJsonElement(response).getAsInt();
                printjob = responseParse;
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
        return printjob;
    }

    /**
     * Returns a Whoami object.
     *
     * @return object of a /whoami/ request.
     * @see Whoami
     * @throws IOException if HTTP client is given bad values
     * */
    public final Whoami getWhoami() throws IOException {
        CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credentials).build();
        Whoami whoami;
        try {
            HttpGet httpget = new HttpGet(apiUrl + "/whoami/");
            httpget.addHeader(childHeaders[0], childHeaders[1]);
            CloseableHttpResponse response = client.execute(httpget);
            try {
                JsonObject responseParse = responseToJsonElement(response).getAsJsonObject();
                whoami = new Whoami(responseParse);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
        return whoami;

    }

    /**
     * Given the OS of the client as a string, returns the latest available client.
     * The parameter can ONLY be "osx" or "windows". either can have any case, so "OSX" or "Windows" also works.
     *
     * @param os os of client requested as a string.
     * @return object of the request.
     * @throws IOException if HTTP client is given bad values
     * @throws APIException if OS is not 'osx' or 'windows'
     * @see Download
     * */
    public final Download getLatestClient(final String os) throws APIException, IOException {
        if (!(os.toLowerCase().equals("windows")) && !(os.toLowerCase().equals("osx"))) {
            throw new APIException("getLatestClient only takes the argment as 'osx' or 'windows'.");
        }
        CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credentials).build();
        Download download;
        try {
            HttpGet httpget = new HttpGet(apiUrl + "/download/client/" + os);
            httpget.addHeader(childHeaders[0], childHeaders[1]);
            CloseableHttpResponse response = client.execute(httpget);
            try {
                JsonObject responseParse = responseToJsonElement(response).getAsJsonObject();
                download = new Download(responseParse);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
        return download;

    }


    /**
     * Given a set of clients, returns an array of these clients.
     *
     *
     * @param clientSet set of clients.
     * @return Array of client objects.
     * @throws IOException if HTTP client is given bad values
     * @see Client
     * */
    public final Client[] getClients(final String clientSet) throws IOException {
        CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credentials).build();
        Client[] clients;
        try {
            HttpGet httpget = new HttpGet(apiUrl + "/download/clients/" + clientSet);
            httpget.addHeader(childHeaders[0], childHeaders[1]);
            CloseableHttpResponse response = client.execute(httpget);
            try {
                JsonArray responseParse = responseToJsonElement(response).getAsJsonArray();
                clients = new Client[responseParse.size()];
                for (int i = 0; i < responseParse.size(); i++) {
                    clients[i] = new Client(responseParse.get(i).getAsJsonObject());
                }
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
        return clients;

    }

    /**
     * Given a set of computers, return an array of these computers.
     *
     * @param computerSet set of computers.
     * @return Array of computer objects.
     * @throws IOException if HTTP client is given bad values
     * @see Computer
     * */
    public final Computer[] getComputers(final String computerSet) throws IOException {
        CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credentials).build();
        Computer[] computers;
        try {
            HttpGet httpget = new HttpGet(apiUrl + "/computers/" + computerSet);
            httpget.addHeader(childHeaders[0], childHeaders[1]);
            CloseableHttpResponse response = client.execute(httpget);
            try {
                JsonArray responseParse = responseToJsonElement(response).getAsJsonArray();
                computers = new Computer[responseParse.size()];
                for (int i = 0; i < responseParse.size(); i++) {
                    computers[i] = new Computer(responseParse.get(i).getAsJsonObject());
                }
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
        return computers;

    }

    /**
     * Given a set of states, return an array of states for each printjob in the set.
     *
     * @param printJobSet set of printjobs to find the states for.
     * @return an array of an array of state objects.
     * @throws IOException if HTTP client is given bad values
     * @see State
     * */
    public final State[][] getStates(final String printJobSet) throws IOException {
        CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credentials).build();
        State[][] states;
        try {
            String endPointUrl = apiUrl + "/printjobs/" + printJobSet;
            if (printJobSet == "") {
                endPointUrl = endPointUrl + "states/";
            } else {
                endPointUrl = endPointUrl + "/states/";
            }
            HttpGet httpget = new HttpGet(endPointUrl);
            httpget.addHeader(childHeaders[0], childHeaders[1]);
            CloseableHttpResponse response = client.execute(httpget);
            try {
                JsonArray responseParse = responseToJsonElement(response).getAsJsonArray();
                states = new State[responseParse.size()][];
                for (int i = 0; i < responseParse.size(); i++) {
                    JsonArray jsonSpecificPrinterStates = responseParse.get(i).getAsJsonArray();
                    State[] specificPrinterStates = new State[jsonSpecificPrinterStates.size()];
                    for (int j = 0; j < jsonSpecificPrinterStates.size(); j++) {
                        specificPrinterStates[j] = new State(jsonSpecificPrinterStates.get(j).getAsJsonObject());
                    }
                    states[i] = specificPrinterStates;
                }
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
        return states;

    }

    /**
     * Given a set of printers, and a set of printjobs, return an array of printjobs relative to the set of printers.
     *
     * @param printerSet set of printers.
     * @param printJobSet set of printjobs.
     * @return Array of printjobs.
     * @throws IOException if HTTP client is given bad values
     * @see PrintJob
     * */
    public final PrintJob[] getPrintJobsByPrinter(final String printerSet,
                                                  final String printJobSet) throws IOException {
        CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credentials).build();
        PrintJob[] printjobs;
        try {
            HttpGet httpget = new HttpGet(apiUrl + "/printers/" + printerSet + "/printjobs/" + printJobSet);
            httpget.addHeader(childHeaders[0], childHeaders[1]);
            CloseableHttpResponse response = client.execute(httpget);
            try {
                JsonArray responseParse = responseToJsonElement(response).getAsJsonArray();
                printjobs = new PrintJob[responseParse.size()];
                for (int i = 0; i < responseParse.size(); i++) {
                    printjobs[i] = new PrintJob(responseParse.get(i).getAsJsonObject());
                }
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
        return printjobs;

    }

       /**
     * Given a set of printjobs, return an array of printjobs.
     *
     * @param printJobSet set of printjobs.
     * @return Array of printjobs.
     * @throws IOException if HTTP client is given bad values
     * @see PrintJob
     * */
    public final PrintJob[] getPrintJobs(final String printJobSet) throws IOException {
        CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credentials).build();
        PrintJob[] printjobs;
        try {
            HttpGet httpget = new HttpGet(apiUrl + "/printjobs/" + printJobSet);
            httpget.addHeader(childHeaders[0], childHeaders[1]);
            CloseableHttpResponse response = client.execute(httpget);
            try {
                JsonArray responseParse = responseToJsonElement(response).getAsJsonArray();
                printjobs = new PrintJob[responseParse.size()];
                for (int i = 0; i < responseParse.size(); i++) {
                    printjobs[i] = new PrintJob(responseParse.get(i).getAsJsonObject());
                }
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
        return printjobs;

    }

    /**
     * Given a set of computers, and a set of printers, return an array of printers relative to the set of computers.
     * If computerSet is a blank string, this will throw an APIException.
     *
     * @param computerSet set of computers.
     * @param printerSet set of printers.
     * @return Array of Printers.
     * @throws IOException if HTTP client is given bad values
     * @see Printer
     * */
    public final Printer[] getPrintersByComputers(final String computerSet,
                                                  final String printerSet) throws IOException {
        CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credentials).build();
        Printer[] printers;
        try {
            HttpGet httpget = new HttpGet(apiUrl + "/computers/" + computerSet + "/printers/" + printerSet);
            httpget.addHeader(childHeaders[0], childHeaders[1]);
            CloseableHttpResponse response = client.execute(httpget);
            try {
                JsonArray responseParse = responseToJsonElement(response).getAsJsonArray();
                printers = new Printer[responseParse.size()];
                for (int i = 0; i < responseParse.size(); i++) {
                    printers[i] = new Printer(responseParse.get(i).getAsJsonObject());
                }
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
        return printers;

    }

    /**
     * Given a computer Id, return an array of scales for that commputer.
     *
     * @param computerId the computer id we want to find scales for
     * @return Array of scales.
     * @throws IOException if HTTP client is given bad values
     * @see Scale
     * */
    public final Scale[] getScales(final int computerId) throws IOException {
        CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credentials).build();
        Scale[] scales;
        try {
            HttpGet httpget = new HttpGet(apiUrl + "/computer/" + computerId + "/scales/");
            httpget.addHeader(childHeaders[0], childHeaders[1]);
            CloseableHttpResponse response = client.execute(httpget);
            try {
                JsonArray responseParse = responseToJsonElement(response).getAsJsonArray();
                scales = new Scale[responseParse.size()];
                for (int i = 0; i < responseParse.size(); i++) {
                    scales[i] = new Scale(responseParse.get(i).getAsJsonObject());
                }
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
        return scales;

    }
    /**
     * Given a set of printers, return an array of printers.
     *
     * @param printerSet set of printers.
     * @return Array of Printers.
     * @throws IOException if HTTP client is given bad values
     * @see Printer
     * */
    public final Printer[] getPrinters(final String printerSet) throws IOException {
        CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credentials).build();
        Printer[] printers;
        try {
            HttpGet httpget = new HttpGet(apiUrl + "/printers/" + printerSet);
            httpget.addHeader(childHeaders[0], childHeaders[1]);
            CloseableHttpResponse response = client.execute(httpget);
            try {
                JsonArray responseParse = responseToJsonElement(response).getAsJsonArray();
                printers = new Printer[responseParse.size()];
                for (int i = 0; i < responseParse.size(); i++) {
                    printers[i] = new Printer(responseParse.get(i).getAsJsonObject());
                }
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
        return printers;

    }

    /**
     * Given a UUID, a client edition, and a client version, returns a client-key.
     *
     * @param uuid the UUID.
     * @param edition The edition of the client the client-key refers to.
     * @param version the version of client being used.
     * @return a client key for the speicfied arguments.
     * @throws IOException if HTTP client is given bad values
     * */
    public final String getClientKey(final String uuid,
            final String edition,
            final String version) throws IOException {
        CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credentials).build();
        String clientKeyValue;
        try {
            HttpGet httpget = new HttpGet(apiUrl
                    + "/client/apikey/"
                    + uuid
                    + "?edition="
                    + edition
                    + "&version="
                    + version);
            httpget.addHeader(childHeaders[0], childHeaders[1]);
            CloseableHttpResponse response = client.execute(httpget);
            try {
                String responseParse = responseToJsonElement(response).getAsString();
                clientKeyValue = responseParse;
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
        return clientKeyValue;
    }

    /**
     * Given a reference to an apikey, return that apikey.
     *
     * @param apikey reference to the apikey, which can be found in a whoami request.
     * @return value of the apikey.
     * @throws IOException if HTTP client is given bad values
     * */
    public final String getApiKeys(final String apikey) throws IOException {
        CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credentials).build();
        String apikeyvalue;
        try {
            HttpGet httpget = new HttpGet(apiUrl + "/account/apikey/" + apikey);
            httpget.addHeader(childHeaders[0], childHeaders[1]);
            CloseableHttpResponse response = client.execute(httpget);
            try {
                String responseParse = responseToJsonElement(response).getAsString();
                apikeyvalue = responseParse;
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
        return apikeyvalue;
    }


    /**
     * Given a tagname, return the value of that tag.
     *
     * @param tagName name of tag.
     * @return value of the tag.
     * @throws IOException if HTTP client is given bad values
     * */
    public final String getTags(final String tagName) throws IOException {
        CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credentials).build();
        String tagValue;
        try {
            HttpGet httpget = new HttpGet(apiUrl + "/account/tag/" + tagName);
            httpget.addHeader(childHeaders[0], childHeaders[1]);
            CloseableHttpResponse response = client.execute(httpget);
            try {
                String responseParse = responseToJsonElement(response).getAsString();
                tagValue = responseParse;
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
        return tagValue;
    }

    /**
     * Given a status code and a response, if  we have an error, it will throw an APIException.
     *
     * @param statuscode Statuscode of the response.
     * @param response Body of the response.
     * */
    private void checkResponseForExceptions(final int statuscode, final String response) {
        if (!(Integer.toString(statuscode).startsWith("2"))) {
            throw new APIException("\nAPI response error found with status code:"
                    + statuscode
                    + "\nThe response content was this:"
                    + response);
        }
    }

}
