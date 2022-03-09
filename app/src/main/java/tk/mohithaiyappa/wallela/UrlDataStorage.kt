package tk.mohithaiyappa.wallela;

class UrlDataStorage {


    private String midResUrl,hiResUrl,lowResUrl;


    UrlDataStorage(String midResUrl,String hiResUrl,String lowResUrl){
        this.midResUrl=midResUrl;
        this.hiResUrl=hiResUrl;
        this.lowResUrl=lowResUrl;
    }


    String getMidResUrl() {
        return midResUrl;
    }


    String getHiResUrl() {
        return hiResUrl;
    }


    String getLowResUrl() {
        return lowResUrl;
    }


}
