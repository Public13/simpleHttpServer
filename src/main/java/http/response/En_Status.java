package http.response;

public enum En_Status
{
    OK ("200 OK"),
    BAD_REQUEST ("400 Bad Request"),
    NOT_FOUND ("404 Not Found");

    private String httpStatus;

    En_Status(String httpStatus)
    {
        this.httpStatus = httpStatus;
    }

    public String getHttpStatus()
    {
        return httpStatus;
    }
}
