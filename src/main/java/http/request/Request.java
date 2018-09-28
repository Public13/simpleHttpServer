package http.request;

import java.util.List;

public class Request
{
    private String method;
    private String query;
    private String protocol;
    private List<Header> headers;

    public String getMethod()
    {
        return method;
    }

    public void setMethod(String method)
    {
        this.method = method;
    }

    public String getQuery()
    {
        return query;
    }

    public void setQuery(String query)
    {
        this.query = query;
    }

    public String getProtocol()
    {
        return protocol;
    }

    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }

    public List<Header> getHeaders()
    {
        return headers;
    }

    public void setHeaders(List<Header> headers)
    {
        this.headers = headers;
    }
}
