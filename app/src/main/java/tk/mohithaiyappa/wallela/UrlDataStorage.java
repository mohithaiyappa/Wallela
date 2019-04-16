package tk.mohithaiyappa.wallela;

class UrlDataStorage {
    private String midResUrl,hiResUrl,lowResUrl;
    UrlDataStorage(String midResUrl,String hiResUrl,String lowResUrl){
        this.midResUrl=midResUrl;
        this.hiResUrl=hiResUrl;
        this.lowResUrl=lowResUrl;
    }

    public String getMidResUrl() {
        return midResUrl;
    }
    public String getHiResUrl() {
        return hiResUrl;
    }
    public String getLowResUrl() {
        return lowResUrl;
    }
}
