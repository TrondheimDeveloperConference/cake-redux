package no.javazone.cake.redux;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Configuration {

    private Map<String,String> properties = null;
    private static Configuration instance = new Configuration();

    private Configuration() {

    }

    private static String readConfigFile(String filename) {
        try (FileInputStream inputStream = new FileInputStream(filename)) {
            return CommunicatorHelper.toString(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getProperty(String key) {
        if (instance.properties == null) {
            instance.loadProps();
            if (instance.properties == null) {
                throw new IllegalStateException("Properties not initalized getting " +key);
            }
        }
        return instance.properties.computeIfAbsent(key, Configuration::tryEnvAndSysProp);
    }

    private static String tryEnvAndSysProp(String key) {
        String property = System.getProperty(key);
        if(isNull(property)){
            property = System.getenv(key);
        }
        return property;
    }

    private synchronized void loadProps() {
        Map<String,String> readProps = new HashMap<>();
        String config = readConfigFile(System.getProperty("cake-redux-config-file"));
        for (String line : config.split("\n")) {
            if (line.startsWith("#")) {
                continue;
            }
            int eqpos = line.indexOf("=");
            if (eqpos == -1) {
                throw new IllegalArgumentException("Illegal line : " + line);
            }
            String key = line.substring(0, eqpos);
            String value = line.substring(eqpos + 1);
            String valueFromEnv = tryEnvAndSysProp(key);
            if(nonNull(valueFromEnv)) {
                value = valueFromEnv;
            }
            readProps.put(key, value);
        }
        properties = readProps;
    }

    public static String getEmsUser() {
        return getProperty("emsUser");
    }

    public static String getEmsPassword() {
        return getProperty("emsPassword");
    }

    public static String getGoogleClientId() {
        return getProperty("googleClientId");
    }

    public static String getGoogleClientSecret() {
        return getProperty("googleClientSecret");
    }

    public static String getGoogleRedirectUrl() {
        return getProperty("googleRedirectUrl");
    }

    public static String getAutorizedUsers() {
        String authorizedUsers = getProperty("authorizedUsers");
        if (authorizedUsers == null) {
            return "";
        }
        return authorizedUsers;
    }

    public static String autorizedUserFile() {
        return getProperty("autorizedUserFile");
    }

    public static String fullUsers() {
        return getProperty("fullUsers");
    }

    public static boolean noAuthMode() {
        return "true".equals(getProperty("noAuthMode"));
    }

    public static String emsEventLocation() {
        return getProperty("emsEventLocation");
    }

    public static String submititLocation() {
        return getProperty("submititLocation");
    }

    public static Integer serverPort() {
        String serverPortStr = getProperty("serverPort");
        if (serverPortStr == null || serverPortStr.isEmpty()) {
            return null;
        }
        return Integer.parseInt(serverPortStr);
    }

    public static String mailSenderImplementation() {
        return readConf("mailSenderImplementation","smtp");
    }

    public static String smtpServer() {
        return getProperty("smthost");
    }

    public static int smtpPort() {
        return Integer.parseInt(getProperty("smtpport"));
    }

    public static boolean useMailSSL() {
        return "true".equals(getProperty("mailSsl"));
    }

    public static String mailUser() {
        return getProperty("mailUser");
    }

    public static String mailPassword() {
        return getProperty("mailPassword");
    }

    public static String cakeLocation() {
        return getProperty("cakeLocation");
    }

    private static String readConf(String prop,String defaultValue) {
        return Optional.ofNullable(getProperty(prop)).orElse(defaultValue);
    }

    public static boolean whydaSupported() {
        return "true".equals(readConf("supportWhyda","false"));
    }

    public static String logonRedirectUrl() {
        return readConf("logonRedirectUrl", "http://localhost:9997/sso/login?redirectURI=http://localhost:8088/admin/");
    }

    public static String tokenServiceUrl() {
        return readConf("tokenServiceUrl", "http://localhost:9998/tokenservice");
    }

    public static String applicationId() {
        return readConf("applicationId", "99");
    }

    public static String feedbackStoreFilename() {
        return readConf("feedbackStoreFilename",null);
    }

    public static String applicationSecret() { return readConf("applicationSecret", "33879936R6Jr47D4Hj5R6p9qT");}

    public static void setProps(Map<String,String> props) {
        instance.properties = props;
    }

    public static long emailSleepTime() {
        return Long.parseLong(readConf("emailSleepTime","5000"));
    }

    public static String sleepingPillBaseLocation() {
        return readConf("sleepingPillBaseLocation","http://localhost:8082");
    }

    public static String devnullLocation() {
        return readConf("devnullLocation","http://localhost:8084/devnull");
    }

    public static String sleepingpillUser() {
        return readConf("sleepingpillUser",null);
    }

    public static String sleepingpillPassword() {
        return readConf("sleepingpillPassword",null);
    }

    public static String feedbackDaoImpl() {
        return readConf("feedbackDaoImpl","sleepingpill");
    }

    public static String videoAdminPassword() {
        return readConf("videoAdminPassword","dummy:bingo");
    }

    public static String videoAdminConference() {
        return readConf("videoAdminConference","30d5c2f1cb214fc8b0649a44fdf3b4bf");
    }

    public static String mailFrom() {
        return readConf("mailFrom", "connect@trondheimdc.no");
    }
    public static String mailFromName() {
        return readConf("mailFromName", "Trondheim Developer Conference program commitee");
    }

    public static String slackAppId() {
        return readConf("slackAppId",null);
    }

    public static String slackClientSecret() {
        return readConf("slackClientSecret",null);
    }

    public static String slackAuthChannel() {
        return readConf("slackAuthChannel",null);
    }

    public static String slackApiToken() {
        return readConf("slackApiToken",null);
    }

    public static String bcc() {
        return readConf("bcc", "connect@trondheimdc.no");
    }
}
