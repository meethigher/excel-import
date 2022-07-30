package top.meethigher.domain.request;

import top.meethigher.domain.request.base.BasePageRequest;

public class DocRequest extends BasePageRequest {

    private String id;

    private String loc;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
