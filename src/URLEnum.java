public enum URLEnum {
    API_URL("https://api.currencyapi.com/v3/latest?apikey=cur_live_fhAY37FpswY6oyQFGMS5QfYzvPQ3i1ZRZkGYr8u6");
    private final String value;

    /**
    * @param value
    */
    URLEnum(String value) {
        this.value = value;
    }

    /**
     * @return
     */
    public String getValue() {
        return value;
    }
}
