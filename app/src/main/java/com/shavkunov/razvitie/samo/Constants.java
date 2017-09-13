package com.shavkunov.razvitie.samo;

public class Constants {

    public static class Admob {
        public static final int AD_PER = 20;
        public static final int AD_START_INDEX = 6;
        public static final int AD_HEIGHT = 150;
    }

    public static class Url {
        public static final String RU = "https://api.myjson.com/bins/1g835d";
        public static final String UK = "https://api.myjson.com/bins/19bo8x";
        public static final String BE = "https://api.myjson.com/bins/1ag5t5";
        public static final String KK = "https://api.myjson.com/bins/c61kh";
        public static final String TR = "https://api.myjson.com/bins/1e4em1";
        public static final String PL = "https://api.myjson.com/bins/fgz7d";
        public static final String PT = "https://api.myjson.com/bins/15b6ux";
        public static final String EN = "https://api.myjson.com/bins/kw0t5";
    }

    public static final class DbSchema {
        public static final String NAME_RU = "ru";
        public static final String NAME_UK = "uk";
        public static final String NAME_BE = "be";
        public static final String NAME_KK = "kk";
        public static final String NAME_TR = "tr";
        public static final String NAME_PL = "pl";
        public static final String NAME_PT = "pt";
        public static final String NAME_EN = "en";

        public static final String MY_TWISTERS = "myTwisters";

        public static final class Cols {
            public static final String ID = "id";
            public static final String URL = "url";
            public static final String TITLE = "title";
            public static final String FAVORITE = "favorite";
        }
    }
}
