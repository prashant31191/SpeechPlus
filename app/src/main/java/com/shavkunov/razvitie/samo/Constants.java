package com.shavkunov.razvitie.samo;

public class Constants {

    public static class Admob {
        public static final int AD_PER = 40;
        public static final int AD_START_INDEX = 6;
        public static final int AD_HEIGHT = 150;
    }

    public static class Url {
        public static final String RU = "https://api.myjson.com/bins/17rn9t";
        public static final String UK = "https://api.myjson.com/bins/p6phd";
        public static final String BE = "https://api.myjson.com/bins/jtt1d";
        public static final String KK = "https://api.myjson.com/bins/egwld";
        public static final String TR = "https://api.myjson.com/bins/9405d";
        public static final String PL = "https://api.myjson.com/bins/xiry9";
        public static final String PT = "https://api.myjson.com/bins/m7jgh";
        public static final String EN = "https://api.myjson.com/bins/bhqkh";
    }

    public static final class DbSchema {
        // Russian table
        public static final String NAME_RU = "ru";

        // Belorussian table
        public static final String NAME_BE = "be";

        // English table
        public static final String NAME_EN = "en";

        // Kazakh table
        public static final String NAME_KK = "kk";

        // Polish table
        public static final String NAME_PL = "pl";

        // Portuguese table
        public static final String NAME_PT = "pt";

        // Turkish table
        public static final String NAME_TR = "tr";

        // Ukraine table
        public static final String NAME_UK = "uk";

        public static final class Cols {
            public static final String ID = "id";
            public static final String URL = "url";
            public static final String TITLE = "title";
            public static final String FAVORITE = "favorite";
        }
    }
}
