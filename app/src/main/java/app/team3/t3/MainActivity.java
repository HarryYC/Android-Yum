package app.team3.t3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import app.team3.t3.yelp.Restaurant;
import app.team3.t3.yelp.RestaurantFinder;
import app.team3.t3.yelp.RestaurantSearchException;


public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    SoundPool mySound;
    int touchId, boomId;

    private ImageButton mShakeImageButton;
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private RestaurantFinder mySearch;
    private Restaurant restaurant;
    private boolean avoid_doubleShake = true; //use to avoid to get multiple searching results
    private double latitude;
    private double longitude;

    SharedPreferences firstRunprefs = null;

    final SensorEventListener mSensorListener = new SensorEventListener() {

        /* Sensor Listener  */
        @Override
        public void onSensorChanged(SensorEvent event) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 21 && avoid_doubleShake == true) {
                getResultPage();
                avoid_doubleShake = false;
                mySound.play(boomId, 1, 1, 1, 0, 1);
            }
            mAccelLast = mAccelCurrent;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button clearTextButton = (Button) findViewById(R.id.cleartext_button);
        clearTextButton.setVisibility(View.INVISIBLE);

        firstRunprefs = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        String gpsProvider = LocationManager.GPS_PROVIDER;
        String networkProvider = LocationManager.NETWORK_PROVIDER;
        String serviceAvailable;

        if (getIntent().getBooleanExtra("is_started", false)) {
            Intent locationSetting = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(locationSetting);
        }

        mySound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        touchId = mySound.load(this, R.raw.touch, 1);
        boomId = mySound.load(this, R.raw.boom, 1);

        mySearch = new RestaurantFinder(getApplicationContext());

        new Eula(this).show();

         /* Shake Sensor  */
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

         /* Components  */
        mShakeImageButton = (ImageButton) findViewById(R.id.shake_ImageButton);
        mShakeImageButton.requestFocus();
         /* Listeners  */
        mShakeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySound.play(boomId, 1, 1, 1, 0, 1);
                mySound.play(touchId, 1, 1, 1, 1, 1f);
                getResultPage();
            }
        });

        /* Location service, get current location */
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (locationManager.isProviderEnabled(gpsProvider)
//                || locationManager.isProviderEnabled(networkProvider)) {
//            if (!locationManager.isProviderEnabled(gpsProvider)) {
//                serviceAvailable = networkProvider;
//            } else {
//                serviceAvailable = gpsProvider;
//            }
//            locationManager.requestLocationUpdates(serviceAvailable, 5000, 0, this);
//            Location currentLocation = locationManager.getLastKnownLocation(serviceAvailable);
//            mySearch.setLatitude(currentLocation.getLatitude());
//            Log.e("####lati", String.valueOf(mySearch.getLatitude()));
//            mySearch.setLongitude(currentLocation.getLongitude());
//            Log.e("####longi", String.valueOf(mySearch.getLongitude()));
//            restaurants = mySearch.filteredSearch();
//            resDB.insertRestaurants(restaurants);
//        } else {
//            Log.e("####Location Err", "No location provider is not available. Does the device have location services enabled?");
//        }


        final AutoCompleteTextView changeLocation = (AutoCompleteTextView) findViewById(R.id.set_location_textView);

        String[] cityArray = {"ACTON", "ADELANTO", "AGOURA HILLS", "ALAMEDA", "ALAMO", "ALBANY", "ALHAMBRA", "ALISO VIEJO", "ALMANOR", "ALONDRA PARK", "ALPAUGH", "ALPINE", "ALPINE VILLAGE", "ALTADENA", "ALTA SIERRA", "ALTURAS", "ALUM ROCK", "AMADOR CITY", "AMERICAN CANYON", "AMESTI", "ANAHEIM", "ANDERSON", "ANGELS CITY", "ANGWIN", "ANTIOCH", "APPLE VALLEY", "APTOS", "APTOS HILLS-LARKIN VALLEY", "ARBUCKLE", "ARCADIA", "ARCATA", "ARDEN-ARCADE", "ARMONA", "ARNOLD", "AROMAS", "ARROYO GRANDE", "ARTESIA", "ARVIN", "ASHLAND", "ATASCADERO", "ATHERTON", "ATWATER", "AUBERRY", "AUBURN", "AUGUST", "AVALON", "AVENAL", "AVERY", "AVOCADO HEIGHTS", "AZUSA", "BAKERSFIELD", "BALDWIN PARK", "BANNING", "BARSTOW", "BAY POINT", "BAYVIEW", "BAYVIEW-MONTALVIN", "BAYWOOD-LOS OSOS", "BEALE AFB", "BEAR VALLEY", "BEAR VALLEY SPRINGS", "BEAUMONT", "BECKWOURTH", "BELDEN", "BELL", "BELLFLOWER", "BELL GARDENS", "BELMONT", "BELVEDERE", "BENICIA", "BEN LOMOND", "BERKELEY", "BERMUDA DUNES", "BERTSCH-OCEANVIEW", "BETHEL ISLAND", "BEVERLY HILLS", "BIG BEAR CITY", "BIG BEAR LAKE", "BIG BEND", "BIGGS", "BIG PINE", "BIG RIVER", "BIOLA", "BISHOP", "BLACKHAWK-CAMINO TASSAJARA", "BLACK POINT-GREEN POINT", "BLAIRSDEN", "BLOOMINGTON", "BLUE LAKE", "BLUEWATER", "BLYTHE", "BODEGA BAY", "BODFISH", "BOLINAS", "BOMBAY BEACH", "BONADELLE RANCHOS-MADERA RANCHOS", "BONITA", "BONSALL", "BOOTJACK", "BORON", "BORONDA", "BORREGO SPRINGS", "BOSTONIA", "BOULDER CREEK", "BOWLES", "BOYES HOT SPRINGS", "BRADBURY", "BRADLEY", "BRAWLEY", "BREA", "BRENTWOOD", "BRET HARTE", "BRISBANE", "BROADMOOR", "BUCKS LAKE", "BUELLTON", "BUENA PARK", "BUENA VISTA", "BURBANK", "BURBANK", "BURLINGAME", "BURNEY", "BUTTONWILLOW", "BYRON", "BYSTROM", "CABAZON", "CALABASAS", "CALEXICO", "CALIFORNIA CITY", "CALIMESA", "CALIPATRIA", "CALISTOGA", "CALWA", "CAMARILLO", "CAMBRIA", "CAMBRIAN PARK", "CAMERON PARK", "CAMPBELL", "CAMP PENDLETON NORTH", "CAMP PENDLETON SOUTH", "CANTUA CREEK", "CANYONDAM", "CANYON LAKE", "CAPITOLA", "CARIBOU", "CARLSBAD", "CARMEL-BY-THE-SEA", "CARMEL VALLEY VILLAGE", "CARMICHAEL", "CARPINTERIA", "CARRICK", "CARSON", "CARTAGO", "CARUTHERS", "CASA CONEJO", "CASA DE ORO-MOUNT HELIX", "CASTRO VALLEY", "CASTROVILLE", "CATHEDRAL CITY", "CAYUCOS", "CERES", "CERRITOS", "CHALLENGE-BROWNSVILLE", "CHANNEL ISLANDS BEACH", "CHARTER OAK", "CHERRYLAND", "CHERRY VALLEY", "CHESTER", "CHICO", "CHILCOOT-VINTON", "CHINA LAKE ACRES", "CHINESE CAMP", "CHINO", "CHINO HILLS", "CHOWCHILLA", "CHUALAR", "CHULA VISTA", "CITRUS", "CITRUS HEIGHTS", "CLAREMONT", "CLAYTON", "CLEARLAKE", "CLEARLAKE OAKS", "CLIO", "CLOVERDALE", "CLOVIS", "CLYDE", "COACHELLA", "COALINGA", "COBB", "COLFAX", "COLMA", "COLTON", "COLUMBIA", "COLUSA", "COMMERCE", "COMPTON", "CONCORD", "CONCOW", "COPPEROPOLIS", "CORCORAN", "CORNING", "CORONA", "CORONADO", "CORRALITOS", "CORTE MADERA", "COSTA MESA", "COTATI", "COTO DE CAZA", "COTTONWOOD", "COUNTRY CLUB", "COVELO", "COVINA", "CRESCENT CITY", "CRESCENT CITY NORTH", "CRESCENT MILLS", "CREST", "CRESTLINE", "C-ROAD", "CROCKETT", "CROMBERG", "CUDAHY", "CULVER CITY", "CUPERTINO", "CUTLER", "CUTTEN", "CYPRESS", "DALY CITY", "DANA POINT", "DANVILLE", "DARWIN", "DAVIS", "DAY VALLEY", "DEER PARK", "DEL AIRE", "DELANO", "DELHI", "DELLEKER", "DEL MAR", "DEL MONTE FOREST", "DEL REY", "DEL REY OAKS", "DEL RIO", "DENAIR", "DERBY ACRES", "DESERT HOT SPRINGS", "DESERT SHORES", "DESERT VIEW HIGHLANDS", "DIABLO", "DIAMOND BAR", "DIAMOND SPRINGS", "DILLON BEACH", "DINUBA", "DISCOVERY BAY", "DIXON", "DIXON LANE-MEADOW CREEK", "DOLLAR POINT", "DORRINGTON", "DORRIS", "DOS PALOS", "DOWNEY", "DUARTE", "DUBLIN", "DUCOR", "DUNSMUIR", "DURHAM", "DUSTIN ACRES", "EARLIMART", "EAST BLYTHE", "EAST COMPTON", "EAST FOOTHILLS", "EAST HEMET", "EAST LA MIRADA", "EAST LOS ANGELES", "EAST OAKDALE", "EASTON", "EAST OROSI", "EAST PALO ALTO", "EAST PASADENA", "EAST PORTERVILLE", "EAST QUINCY", "EAST RICHMOND HEIGHTS", "EAST SAN GABRIEL", "EAST SHORE", "EAST SONORA", "EDGEWOOD", "EDWARDS AFB", "EL CAJON", "EL CENTRO", "EL CERRITO", "EL CERRITO", "EL DORADO HILLS", "ELDRIDGE", "EL GRANADA", "ELK GROVE", "ELKHORN", "ELMIRA", "EL MONTE", "EL PASO DE ROBLES (PASO ROBLES)", "EL RIO", "EL SEGUNDO", "EL SOBRANTE", "EL VERANO", "EMERALD LAKE HILLS", "EMERYVILLE", "EMPIRE", "ENCINITAS", "ESCALON", "ESCONDIDO", "ESPARTO", "ETNA", "EUREKA", "EXETER", "FAIRBANKS RANCH", "FAIRFAX", "FAIRFIELD", "FAIR OAKS", "FAIRVIEW", "FALLBROOK", "FALL RIVER MILLS", "FARMERSVILLE", "FARMINGTON", "FELLOWS", "FELTON", "FERNDALE", "FETTERS HOT SPRINGS-AGUA CALIENTE", "FILLMORE", "FIREBAUGH", "FLORENCE-GRAHAM", "FLORIN", "FOLSOM", "FONTANA", "FOOTHILL FARMS", "FOOTHILL RANCH", "FORD CITY", "FORESTHILL", "FOREST MEADOWS", "FORESTVILLE", "FORT BRAGG", "FORT JONES", "FORTUNA", "FOSTER CITY", "FOUNTAIN VALLEY", "FOWLER", "FRAZIER PARK", "FREEDOM", "FREMONT", "FRENCH CAMP", "FRENCH GULCH", "FRESNO", "FRIANT", "FRUITDALE", "FULLERTON", "FURNACE CREEK", "GALT", "GARDENA", "GARDEN ACRES", "GARDEN GROVE", "GAZELLE", "GEORGETOWN", "GERBER-LAS FLORES", "GILROY", "GLEN AVON", "GLENDALE", "GLENDORA", "GLEN ELLEN", "GOLDEN HILLS", "GOLD RIVER", "GOLETA", "GONZALES", "GOSHEN", "GRAEAGLE", "GRAND TERRACE", "GRANITE BAY", "GRANITE HILLS", "GRASS VALLEY", "GRATON", "GRAYSON", "GREENFIELD", "GREENHORN", "GREEN VALLEY", "GREENVIEW", "GREENVILLE", "GRENADA", "GRIDLEY", "GROVELAND-BIG OAK FLAT", "GROVER BEACH", "GUADALUPE", "GUERNEVILLE", "GUSTINE", "HACIENDA HEIGHTS", "HALF MOON BAY", "HAMILTON BRANCH", "HAMILTON CITY", "HANFORD", "HARBISON CANYON", "HAWAIIAN GARDENS", "HAWTHORNE", "HAYFORK", "HAYWARD", "HEALDSBURG", "HEBER", "HEMET", "HERCULES", "HERMOSA BEACH", "HESPERIA", "HICKMAN", "HIDDEN HILLS", "HIDDEN MEADOWS", "HIDDEN VALLEY LAKE", "HIGHGROVE", "HIGHLAND", "HIGHLANDS-BAYWOOD PARK", "HILLSBOROUGH", "HILMAR-IRWIN", "HOLLISTER", "HOLTVILLE", "HOME GARDEN", "HOME GARDENS", "HOMELAND", "HOMEWOOD CANYON-VALLEY WELLS", "HORNBROOK", "HUGHSON", "HUMBOLDT HILL", "HUNTINGTON BEACH", "HUNTINGTON PARK", "HURON", "HYDESVILLE", "IDYLLWILD-PINE COVE", "IMPERIAL", "IMPERIAL BEACH", "INDEPENDENCE", "INDIAN FALLS", "INDIAN WELLS", "INDIO", "INDUSTRY", "INGLEWOOD", "INTERLAKEN", "INVERNESS", "INYOKERN", "IONE", "IRON HORSE", "IRVINE", "IRWINDALE", "ISLA VISTA", "ISLETON", "IVANHOE", "JACKSON", "JAMESTOWN", "JAMUL", "JOHANNESBURG", "JOHNSVILLE", "JOSHUA TREE", "JULIAN", "KEDDIE", "KEELER", "KEENE", "KELSEYVILLE", "KENNEDY", "KENSINGTON", "KENTFIELD", "KERMAN", "KERNVILLE", "KETTLEMAN CITY", "KEYES", "KING CITY", "KINGS BEACH", "KINGSBURG", "KIRKWOOD", "KLAMATH", "KNIGHTSEN", "LA CANADA FLINTRIDGE", "LA CRESCENTA-MONTROSE", "LADERA HEIGHTS", "LAFAYETTE", "LAGUNA", "LAGUNA BEACH", "LAGUNA HILLS", "LAGUNA NIGUEL", "LAGUNA WEST-LAKESIDE", "LAGUNA WOODS", "LAGUNITAS-FOREST KNOLLS", "LA HABRA", "LA HABRA HEIGHTS", "LAKE ALMANOR COUNTRY CLUB", "LAKE ALMANOR PENINSULA", "LAKE ALMANOR WEST", "LAKE ARROWHEAD", "LAKE DAVIS", "LAKE ELSINORE", "LAKE FOREST", "LAKEHEAD-LAKESHORE", "LAKE ISABELLA", "LAKELAND VILLAGE", "LAKE LOS ANGELES", "LAKE NACIMIENTO", "LAKE OF THE PINES", "LAKE OF THE WOODS", "LAKEPORT", "LAKE SAN MARCOS", "LAKESIDE", "LAKEVIEW", "LAKE WILDWOOD", "LAKEWOOD", "LA MESA", "LA MIRADA", "LAMONT", "LANARE", "LANCASTER", "LA PALMA", "LA PORTE", "LA PRESA", "LA PUENTE", "LA QUINTA", "LA RIVIERA", "LARKFIELD-WIKIUP", "LARKSPUR", "LAS FLORES", "LAS LOMAS", "LATHROP", "LATON", "LA VERNE", "LAWNDALE", "LAYTONVILLE", "LEBEC", "LE GRAND", "LEMON COVE", "LEMON GROVE", "LEMOORE", "LEMOORE STATION", "LENNOX", "LENWOOD", "LEWISTON", "LEXINGTON HILLS", "LINCOLN", "LINCOLN VILLAGE", "LINDA", "LINDEN", "LINDSAY", "LITTLE GRASS VALLEY", "LITTLEROCK", "LIVE OAK", "LIVE OAK", "LIVERMORE", "LIVINGSTON", "LOCKEFORD", "LODI", "LOMA LINDA", "LOMA RICA", "LOMITA", "LOMPOC", "LONDON", "LONE PINE", "LONG BEACH", "LOOMIS", "LOS ALAMITOS", "LOS ALAMOS", "LOS ALTOS", "LOS ALTOS HILLS", "LOS ANGELES", "LOS BANOS", "LOS GATOS", "LOS MOLINOS", "LOST HILLS", "LOWER LAKE", "LOYALTON", "LOYOLA", "LUCAS VALLEY-MARINWOOD", "LUCERNE", "LYNWOOD", "MCARTHUR", "MCCLOUD", "MACDOEL", "MCFARLAND", "MCKINLEYVILLE", "MCKITTRICK", "MADERA", "MADERA ACRES", "MAGALIA", "MALIBU", "MAMMOTH LAKES", "MANHATTAN BEACH", "MANTECA", "MANTON", "MARCH AFB", "MARICOPA", "MARINA", "MARINA DEL REY", "MARIPOSA", "MARKLEEVILLE", "MARTINEZ", "MARYSVILLE", "MAYFLOWER VILLAGE", "MAYWOOD", "MEADOW VALLEY", "MEADOW VISTA", "MECCA", "MEINERS OAKS", "MENDOCINO", "MENDOTA", "MENLO PARK", "MENTONE", "MERCED", "MESA", "MESA VISTA", "METTLER", "MIDDLETOWN", "MILLBRAE", "MILL VALLEY", "MILLVILLE", "MILPITAS", "MINERAL", "MIRA LOMA", "MIRA MONTE", "MISSION CANYON", "MISSION HILLS", "MISSION VIEJO", "MI-WUK VILLAGE", "MODESTO", "MOHAWK VISTA", "MOJAVE", "MOKELUMNE HILL", "MONO VISTA", "MONROVIA", "MONTAGUE", "MONTARA", "MONTCLAIR", "MONTEBELLO", "MONTECITO", "MONTEREY", "MONTEREY PARK", "MONTE RIO", "MONTE SERENO", "MONTGOMERY CREEK", "MOORPARK", "MORADA", "MORAGA", "MORENO VALLEY", "MORGAN HILL", "MORONGO VALLEY", "MORRO BAY", "MOSS BEACH", "MOSS LANDING", "MOUNTAIN MESA", "MOUNTAIN RANCH", "MOUNTAIN VIEW", "MOUNTAIN VIEW", "MOUNTAIN VIEW ACRES", "MOUNT HEBRON", "MOUNT SHASTA", "MUIR BEACH", "MURPHYS", "MURRIETA", "MURRIETA HOT SPRINGS", "MUSCOY", "MYRTLETOWN", "NAPA", "NATIONAL CITY", "NEBO CENTER", "NEEDLES", "NEVADA CITY", "NEWARK", "NEWMAN", "NEWPORT BEACH", "NEWPORT COAST", "NICE", "NILAND", "NIPOMO", "NORCO", "NORTH AUBURN", "NORTH EDWARDS", "NORTH EL MONTE", "NORTH FAIR OAKS", "NORTH HIGHLANDS", "NORTH LAKEPORT", "NORTH WOODBRIDGE", "NORWALK", "NOVATO", "NUEVO", "OAKDALE", "OAKHURST", "OAKLAND", "OAKLEY", "OAK PARK", "OAK VIEW", "OCCIDENTAL", "OCEANO", "OCEANSIDE", "OCOTILLO", "OILDALE", "OJAI", "OLANCHA", "OLIVEHURST", "ONTARIO", "ONYX", "OPAL CLIFFS", "ORANGE", "ORANGE COVE", "ORANGEVALE", "ORCUTT", "ORINDA", "ORLAND", "OROSI", "OROVILLE", "OROVILLE EAST", "OXNARD", "PACHECO", "PACIFICA", "PACIFIC GROVE", "PAJARO", "PALERMO", "PALMDALE", "PALM DESERT", "PALM SPRINGS", "PALO ALTO", "PALO CEDRO", "PALOS VERDES ESTATES", "PALO VERDE", "PARADISE", "PARAMOUNT", "PARKSDALE", "PARKWAY-SOUTH SACRAMENTO", "PARKWOOD", "PARLIER", "PASADENA", "PATTERSON", "PAXTON", "PEARSONVILLE", "PEDLEY", "PENN VALLEY", "PERRIS", "PETALUMA", "PHOENIX LAKE-CEDAR RIDGE", "PICO RIVERA", "PIEDMONT", "PINE HILLS", "PINE MOUNTAIN CLUB", "PINE VALLEY", "PINOLE", "PIRU", "PISMO BEACH", "PITTSBURG", "PIXLEY", "PLACENTIA", "PLACERVILLE", "PLANADA", "PLEASANT HILL", "PLEASANTON", "PLUMAS EUREKA", "PLYMOUTH", "POINT ARENA", "POINT REYES STATION", "POLLOCK PINES", "POMONA", "POPLAR-COTTON CENTER", "PORT COSTA", "PORTERVILLE", "PORT HUENEME", "PORTOLA", "PORTOLA HILLS", "PORTOLA VALLEY", "POWAY", "PRATTVILLE", "PRUNEDALE", "QUAIL VALLEY", "QUARTZ HILL", "QUINCY", "RAIL ROAD FLAT", "RAINBOW", "RAISIN CITY", "RAMONA", "RANCHO CALAVERAS", "RANCHO CORDOVA", "RANCHO CUCAMONGA", "RANCHO MIRAGE", "RANCHO MURIETA", "RANCHO PALOS VERDES", "RANCHO SAN DIEGO", "RANCHO SANTA FE", "RANCHO SANTA MARGARITA", "RANCHO TEHAMA RESERVE", "RANDSBURG", "RED BLUFF", "REDDING", "REDLANDS", "REDONDO BEACH", "REDWAY", "REDWOOD CITY", "REEDLEY", "RIALTO", "RICHGROVE", "RICHMOND", "RIDGECREST", "RIDGEMARK", "RIO DELL", "RIO DEL MAR", "RIO LINDA", "RIO VISTA", "RIPON", "RIVERBANK", "RIVERDALE", "RIVERDALE PARK", "RIVERSIDE", "ROCKLIN", "RODEO", "ROHNERT PARK", "ROLLING HILLS", "ROLLING HILLS ESTATES", "ROLLINGWOOD", "ROMOLAND", "ROSAMOND", "ROSEDALE", "ROSELAND", "ROSEMEAD", "ROSEMONT", "ROSEVILLE", "ROSS", "ROSSMOOR", "ROUND MOUNTAIN", "ROUND VALLEY", "ROWLAND HEIGHTS", "RUBIDOUX", "RUNNING SPRINGS", "SACRAMENTO", "ST. HELENA", "SALIDA", "SALINAS", "SALTON CITY", "SALTON SEA BEACH", "SAN ANDREAS", "SAN ANSELMO", "SAN ANTONIO HEIGHTS", "SAN ARDO", "SAN BERNARDINO", "SAN BRUNO", "SAN BUENAVENTURA (VENTURA)", "SAN CARLOS", "SAN CLEMENTE", "SAND CITY", "SAN DIEGO", "SAN DIEGO COUNTRY ESTATES", "SAN DIMAS", "SAN FERNANDO", "SAN FRANCISCO", "SAN GABRIEL", "SANGER", "SAN GERONIMO", "SAN JACINTO", "SAN JOAQUIN", "SAN JOAQUIN HILLS", "SAN JOSE", "SAN JUAN BAUTISTA", "SAN JUAN CAPISTRANO", "SAN LEANDRO", "SAN LORENZO", "SAN LUCAS", "SAN LUIS OBISPO", "SAN MARCOS", "SAN MARINO", "SAN MARTIN", "SAN MATEO", "SAN MIGUEL", "SAN PABLO", "SAN RAFAEL", "SAN RAMON", "SANTA ANA", "SANTA BARBARA", "SANTA CLARA", "SANTA CLARITA", "SANTA CRUZ", "SANTA FE SPRINGS", "SANTA MARIA", "SANTA MONICA", "SANTA PAULA", "SANTA ROSA", "SANTA VENETIA", "SANTA YNEZ", "SANTEE", "SARATOGA", "SAUSALITO", "SCOTTS VALLEY", "SEAL BEACH", "SEARLES VALLEY", "SEASIDE", "SEBASTOPOL", "SEDCO HILLS", "SEELEY", "SELMA", "SEVEN TREES", "SHACKELFORD", "SHAFTER", "SHANDON", "SHASTA LAKE", "SHAVER LAKE", "SHINGLE SPRINGS", "SHINGLETOWN", "SHOSHONE", "SIERRA MADRE", "SIGNAL HILL", "SIMI VALLEY", "SOLANA BEACH", "SOLEDAD", "SOLVANG", "SONOMA", "SONORA", "SOQUEL", "SOULSBYVILLE", "SOUTH DOS PALOS", "SOUTH EL MONTE", "SOUTH GATE", "SOUTH LAKE TAHOE", "SOUTH OROVILLE", "SOUTH PASADENA", "SOUTH SAN FRANCISCO", "SOUTH SAN GABRIEL", "SOUTH SAN JOSE HILLS", "SOUTH TAFT", "SOUTH WHITTIER", "SOUTH WOODBRIDGE", "SOUTH YUBA CITY", "SPRECKELS", "SPRING GARDEN", "SPRING VALLEY", "SPRINGVILLE", "SQUAW VALLEY", "SQUIRREL MOUNTAIN VALLEY", "STALLION SPRINGS", "STANFORD", "STANTON", "STINSON BEACH", "STOCKTON", "STORRIE", "STRATFORD", "STRATHMORE", "STRAWBERRY", "SUISUN CITY", "SUMMERLAND", "SUN CITY", "SUNNYSIDE-TAHOE CITY", "SUNNYSLOPE", "SUNNYVALE", "SUNOL", "SUNOL-MIDTOWN", "SUSANVILLE", "SUTTER", "SUTTER CREEK", "TAFT", "TAFT HEIGHTS", "TAFT MOSSWOOD", "TAHOE VISTA", "TALMAGE", "TAMALPAIS-HOMESTEAD VALLEY", "TARA HILLS", "TAYLORSVILLE", "TECOPA", "TEHACHAPI", "TEHAMA", "TEMECULA", "TEMELEC", "TEMPLE CITY", "TEMPLETON", "TENNANT", "TERRA BELLA", "THERMALITO", "THOUSAND OAKS", "THOUSAND PALMS", "THREE RIVERS", "TIBURON", "TIERRA BUENA", "TIPTON", "TOBIN", "TOMALES", "TORO CANYON", "TORRANCE", "TRACY", "TRANQUILLITY", "TRAVER", "TRINIDAD", "TRUCKEE", "TULARE", "TULELAKE", "TUOLUMNE CITY", "TUPMAN", "TURLOCK", "TUSTIN", "TUSTIN FOOTHILLS", "TWAIN", "TWAIN HARTE", "TWENTYNINE PALMS", "TWENTYNINE PALMS BASE", "TWIN LAKES", "UKIAH", "UNION CITY", "UPLAND", "UPPER LAKE", "VACAVILLE", "VALINDA", "VALLECITO", "VALLEJO", "VALLE VISTA", "VALLEY ACRES", "VALLEY CENTER", "VALLEY RANCH", "VALLEY SPRINGS", "VAL VERDE", "VANDENBERG AFB", "VANDENBERG VILLAGE", "VERNON", "VICTORVILLE", "VIEW PARK-WINDSOR HILLS", "VILLA PARK", "VINCENT", "VINE HILL", "VINEYARD", "VISALIA", "VISTA", "WALDON", "WALLACE", "WALNUT", "WALNUT CREEK", "WALNUT GROVE", "WALNUT PARK", "WASCO", "WATERFORD", "WATSONVILLE", "WEAVERVILLE", "WEED", "WEEDPATCH", "WELDON", "WEST ATHENS", "WEST BISHOP", "WEST CARSON", "WEST COMPTON", "WEST COVINA", "WESTHAVEN-MOONSTONE", "WEST HOLLYWOOD", "WESTLAKE VILLAGE", "WESTLEY", "WEST MENLO PARK", "WESTMINSTER", "WEST MODESTO", "WESTMONT", "WESTMORLAND", "WEST POINT", "WEST PUENTE VALLEY", "WEST SACRAMENTO", "WEST WHITTIER-LOS NIETOS", "WESTWOOD", "WHEATLAND", "WHITEHAWK", "WHITTIER", "WILDOMAR", "WILKERSON", "WILLIAMS", "WILLITS", "WILLOWBROOK", "WILLOW CREEK", "WILLOWS", "WILTON", "WINCHESTER", "WINDSOR", "WINTER GARDENS", "WINTERHAVEN", "WINTERS", "WINTON", "WOFFORD HEIGHTS", "WOODACRE", "WOODCREST", "WOODLAKE", "WOODLAND", "WOODSIDE", "WOODVILLE", "WRIGHTWOOD", "YORBA LINDA", "YOSEMITE LAKES", "YOSEMITE VALLEY", "YOUNTVILLE", "YREKA", "YUBA CITY", "YUCAIPA", "YUCCA VALLEY",};

        myArrayAdapter<String> mAdapter = new myArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, cityArray);
        changeLocation.setThreshold(1);
        changeLocation.setAdapter(mAdapter);
        
        clearTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLocation.setText("");
            }
        });
        changeLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!changeLocation.getText().toString().isEmpty()) {
                        clearTextButton.setVisibility(View.VISIBLE);
                        changeLocation.selectAll();
                    }
                } else {
                    findViewById(R.id.a1).requestFocus();
                    clearTextButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        changeLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!changeLocation.getText().toString().isEmpty()) {
                    clearTextButton.setVisibility(View.VISIBLE);
                } else {
                    clearTextButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        changeLocation.setOnEditorActionListener(new AutoCompleteTextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!changeLocation.getText().toString().isEmpty()) {
                        mySearch.setLongitude(0.0);
                        mySearch.setLatitude(0.0);
                        mySearch.setLocation(changeLocation.getText().toString());
                    } else {
                        changeLocation.setText("");
                        mySearch.setLocation(null);
                        getLocation();
                    }
                    try {
                        restaurant = mySearch.filteredSearch();
                    } catch (RestaurantSearchException rse) {
                        rse.printStackTrace();
                        Log.e("restaurantFinder", rse.toString());
                    }
                    Toast.makeText(getApplicationContext(),
                            "Results Updated. Read to Shake",
                            Toast.LENGTH_LONG).show();

                    final InputMethodManager inputMethodManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(changeLocation.getWindowToken(), 0);
                    findViewById(R.id.a1).requestFocus();
                    return true;
                }

                return false;
            }
        });

        getLocation();
    } //end onCreate

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        super.onResume();

        
            try {
                restaurant = mySearch.filteredSearch();
            } catch (RestaurantSearchException rse) {
                rse.printStackTrace();
                Log.e("restaurantFinder", rse.toString());
            }
      

        avoid_doubleShake = true;
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    /* pass a random restaurant object to result page */

    protected void getResultPage() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
        float distance = 0;

        // Calculate distance
        Location myLocation = new Location("my_location");
        myLocation.setLatitude(latitude);
        myLocation.setLongitude(longitude);
        Location restaurantLocation = new Location("restaurant_location");
        restaurantLocation.setLatitude(restaurant.getLatitude());
        restaurantLocation.setLongitude(restaurant.getLongitude());
        distance = Float.parseFloat(new DecimalFormat("##.####").format((myLocation.distanceTo(restaurantLocation) / 1609.34)));

        // Intent getResultIntent = new Intent(MainActivity.this, ActionBarTabsPager.class);
        Intent getResultIntent = new Intent(MainActivity.this, ActionBarTabsPagerActivity.class);
        getResultIntent.putExtra("restaurant_picked", restaurant);
        getResultIntent.putExtra("distance", distance);
        startActivity(getResultIntent);

    }

    /* fillter button */
    public void onFilterClick(View view) {

        LayoutInflater mLayoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View mView = mLayoutInflater.inflate(R.layout.fragment_filter, null);

        SharedPreferences sharedPref = getSharedPreferences("settings", 0);
        final SharedPreferences.Editor prefEditor = sharedPref.edit();
        Button filterButton = (Button) findViewById(R.id.filter_button);
        final PopupWindow mPopupWindow = new PopupWindow(mView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        final Button cancelFilterButton = (Button) mView.findViewById(R.id.cancel_filter_button);
        Button applyFilterButton = (Button) mView.findViewById(R.id.apply_options_button);

        final Spinner distanceSpinner = (Spinner) mView.findViewById(R.id.distance_spinner);
        final Spinner categorySpinner = (Spinner) mView.findViewById(R.id.category_spinner);


        cancelFilterButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        int categoryValue = sharedPref.getInt("categorySpinner", -1);
        Log.e("####CaValue:", String.valueOf(categoryValue));
        if (categoryValue != -1)
            categorySpinner.setSelection(categoryValue);
        int distanceValue = sharedPref.getInt("distanceSpinner", -1);
        Log.e("####DisValue:", String.valueOf(distanceValue));
        if (distanceValue != -1)
            distanceSpinner.setSelection(distanceValue);

        applyFilterButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {


                /* category parameters */
                if (categorySpinner.getSelectedItem().toString().equals("Select Category")) {
                    mySearch.setCategory("restaurants");
                } else if (categorySpinner.getSelectedItem().toString().equals("Japanese")) {
                    mySearch.setCategory("japanese");
                } else if (categorySpinner.getSelectedItem().toString().equals("American (Traditional)")) {
                    mySearch.setCategory("tradamerican");
                } else if (categorySpinner.getSelectedItem().toString().equals("Chinese")) {
                    mySearch.setCategory("chinese");
                } else if (categorySpinner.getSelectedItem().toString().equals("Indian")) {
                    mySearch.setCategory("indpak");
                } else if (categorySpinner.getSelectedItem().toString().equals("Pizza")) {
                    mySearch.setCategory("pizza");
                } else if (categorySpinner.getSelectedItem().toString().equals("American (New)")) {
                    mySearch.setCategory("newamerican");
                } else if (categorySpinner.getSelectedItem().toString().equals("Mexican")) {
                    mySearch.setCategory("mexican");
                } else if (categorySpinner.getSelectedItem().toString().equals("Middle Eastern")) {
                    mySearch.setCategory("mediterranean");
                } else if (categorySpinner.getSelectedItem().toString().equals("Modern European")) {
                    mySearch.setCategory("modern_european");
                } else if (categorySpinner.getSelectedItem().toString().equals("French")) {
                    mySearch.setCategory("french");
                } else if (categorySpinner.getSelectedItem().toString().equals("Thai")) {
                    mySearch.setCategory("thai");
                } else if (categorySpinner.getSelectedItem().toString().equals("Steakhouses")) {
                    mySearch.setCategory("steak");
                } else if (categorySpinner.getSelectedItem().toString().equals("Asian Fusion")) {
                    mySearch.setCategory("asianfusion");
                } else if (categorySpinner.getSelectedItem().toString().equals("Tapas Bars")) {
                    mySearch.setCategory("tapas");
                } else if (categorySpinner.getSelectedItem().toString().equals("Latin American")) {
                    mySearch.setCategory("latin");
                } else if (categorySpinner.getSelectedItem().toString().equals("Seafood")) {
                    mySearch.setCategory("seafood");
                } else if (categorySpinner.getSelectedItem().toString().equals("Italian")) {
                    mySearch.setCategory("italian");
                } else if (categorySpinner.getSelectedItem().toString().equals("Greek")) {
                    mySearch.setCategory("greek");
                }

                /* distance parameters */
                if (distanceSpinner.getSelectedItem().toString().equals("Select Distance")) {
                    mySearch.setRange(2000);
                } else if (distanceSpinner.getSelectedItem().toString().equals("2 blocks")) {
                    mySearch.setRange(161);
                } else if (distanceSpinner.getSelectedItem().toString().equals("6 blocks")) {
                    mySearch.setRange(483);
                } else if (distanceSpinner.getSelectedItem().toString().equals("1 mile")) {
                    mySearch.setRange(1609);
                } else if (distanceSpinner.getSelectedItem().toString().equals("5 mile")) {
                    mySearch.setRange(5 * 1609);
                } else if (distanceSpinner.getSelectedItem().toString().equals("10 mile")) {
                    mySearch.setRange(10000);
                }

                prefEditor.putInt("categorySpinner", categorySpinner.getSelectedItemPosition());
                prefEditor.putInt("distanceSpinner", distanceSpinner.getSelectedItemPosition());
                prefEditor.commit();


                try {
                    restaurant = mySearch.filteredSearch();
                } catch (RestaurantSearchException rse) {
                    rse.printStackTrace();
                    Log.e("restaurantFinder", rse.toString());
                }

                if (restaurant == null) {
                    AlertDialog.Builder mAlert = new AlertDialog.Builder(MainActivity.this);
                    mAlert.setTitle("No Result");
                    mAlert.setMessage("Please choose again!");
                    mAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    mAlert.create().show();
                } else {
                    mPopupWindow.dismiss();
                    Toast.makeText(getApplicationContext(),
                            "Results Updated. Read to Shake",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public void getLocation() {
        String gpsProvider = LocationManager.GPS_PROVIDER;
        String networkProvider = LocationManager.NETWORK_PROVIDER;
        String serviceAvailable;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(gpsProvider)
                || locationManager.isProviderEnabled(networkProvider)) {
            if (locationManager.isProviderEnabled(networkProvider)) {
                serviceAvailable = networkProvider;
            } else {
                serviceAvailable = gpsProvider;
            }
            locationManager.requestLocationUpdates(serviceAvailable, 3000, 0, this);
            Location currentLocation = locationManager.getLastKnownLocation(serviceAvailable);
            latitude = currentLocation.getLatitude();
            mySearch.setLatitude(latitude);
            Log.e("####lati", String.valueOf(mySearch.getLatitude()));
            longitude = currentLocation.getLongitude();
            mySearch.setLongitude(longitude);
            Log.e("####longi", String.valueOf(mySearch.getLongitude()));
            locationManager.removeUpdates(this);
            try {
                restaurant = mySearch.filteredSearch();
            } catch (RestaurantSearchException rse) {
                rse.printStackTrace();
                Log.e("restaurantFinder", rse.toString());
            }
        } else {
            Log.e("####Location Err", "No location provider is not available. Does the device have location services enabled?");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
