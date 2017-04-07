package test.adlib.project;

/**
 * Created by Adlib on 2017. 3. 30..
 */

public enum AdlibTestListItem {

    BANNER_HEADER("Banner", true),
    BANNER_MEDIATION("Mediation"),
    BANNER_DYNAMIC("Dynamic"),
    INTERSTITIAL_HEADER("Interstitial", true),
    INTERSTITIAL_MEDIATION("Mediation"),
    INTERSTITIAL_DYNAMIC("Dynamic"),
    NATIVE_HEADER("Native", true),
    NATIVE_LIST_1("List : Feed"),
    NATIVE_LIST_2("List : Song"),
    NATIVE_RECYCLER("RecyclerView"),
    ETC_HEADER("ETC", true),
    ADLIB_ACTIVITY("AdlibActivity"),
    ICON_AD("Icon AD");

    private String value;
    private boolean isHeader = false;

    AdlibTestListItem(String value) {
        this.value = value;
    }

    AdlibTestListItem(String value, boolean isHeader) {
        this.value = value;
        this.isHeader = isHeader;
    }

    public String getValue() {
        return value;
    }

    public boolean isHeader() {
        return isHeader;
    }
}
