package activr.model;

/**
 * Created by Nic on 4/14/2015.
 */
public enum Activity {
    BASKETBALL {
        @Override
        public String toString() {
            return "BASKETBALL";
        }
    }, HANDBALL {
        @Override
        public String toString() {
            return "HANDBALL";
        }
    }, LIFTING {
        @Override
        public String toString() {
            return "LIFTING";
        }
    }, RACQUETBALL {
        @Override
        public String toString() {
            return "RACQUETBALL";
        }
    }, SQUASH {
        @Override
        public String toString() {
            return "SQUASH";
        }
    }, TENNIS {
        @Override
        public String toString() {
            return "TENNIS";
        }
    }
}
