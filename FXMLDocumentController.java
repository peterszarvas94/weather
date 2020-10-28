package weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONObject;

public class FXMLDocumentController implements Initializable {
    
    
    @FXML AnchorPane anchPane;
    @FXML Pane bgPane;
    @FXML Button moreBtn;
    @FXML Button currentBtn;
    @FXML Button refresh;
    @FXML Pane currentPage;
    @FXML Pane morePage;
    @FXML Label city;
    @FXML Label currentC;
    @FXML Label currentW;
    @FXML Pane currentIcon;
    @FXML Label updated;
    @FXML Label feels;
    @FXML Label wind;
    @FXML Pane windDeg;
    @FXML Label visibility;
    @FXML Label pressure;
    @FXML Label clouds;
    @FXML Label humidity;
    @FXML Label dewpoint;
    @FXML Label uvindex;
    @FXML Label sunrise;
    @FXML Pane sunUp;
    @FXML Label sunset;
    @FXML Pane sunDown;
    @FXML Button slideButtonForward;
    @FXML Button slideButtonBack;
    @FXML Pane slider;
    @FXML Pane sliderTemps;
    @FXML Pane sliderLines;
    @FXML Pane sliderIcons;
    @FXML Pane sliderWind;
    @FXML Pane sliderTimes;
    @FXML Button slideButton;
    @FXML Pane dailyDays;
    @FXML Pane dailyIcons;
    @FXML Pane dailyDesc;
    @FXML Pane dailyWind;
    @FXML Pane dailyTemp;
    
    @FXML private void goToMorePage(ActionEvent event) { //go to 2nd page
        currentPage.setVisible(false);
        morePage.setVisible(true);
    }
    @FXML private void goToCurrentPage(ActionEvent event) { //go to 1st page
        morePage.setVisible(false);
        currentPage.setVisible(true);
    }
    @FXML private void refreshApp(ActionEvent event) {
        currentIcon.getChildren().clear();
        windDeg.getChildren().clear();
        sliderTemps.getChildren().clear();
        sliderLines.getChildren().clear();
        sliderIcons.getChildren().clear();
        sliderWind.getChildren().clear();
        sliderTimes.getChildren().clear();
        if(slided = true) {
            slideEvent(new ActionEvent());
        }
        sliderLines.getChildren().clear();
        bgPane.getChildren().clear();
        startApp();
    }
    
    @FXML private void slideEvent(ActionEvent event) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(500), slider);
        if(slided == false) {
            transition.setToX(-1200);
            slideButton.setText("<");
            slideButton.setLayoutX(1210);
            slided = true;
        } else {
            transition.setToX(0);
            slideButton.setText(">");
            slideButton.setLayoutX(1120);
            slided = false;
        }
        transition.play();
    }
    
    private static HttpURLConnection connection;
    private static JSONObject json;
    private static char deg = '\u00B0';
    private static char reload = '\u21BB';
    private static ArrayList<String> icons = new ArrayList<String>(Arrays.asList(
        "icons8-sun-100.png",
        "icons8-moon-and-stars-100.png",
        "icons8-partly-cloudy-day-100.png",
        "icons8-night-100.png",
        "icons8-cloud-100.png",
        "icons8-rain-cloud-100.png",
        "icons8-rainfall-100.png",
        "icons8-storm-100.png",
        "icons8-snow-100.png",
        "icons8-haze-100.png",
        "icons8-fog-100.png",
        //"icons8-gps-100.png",
        "icons8-near-me-100.png",
        "icons8-water-100.png",
        "icons8-wind-100.png",
        "icons8-sunrise-100.png",
        "icons8-sunset-100.png"
    ));
    
    private static ArrayList<String> pictures = new ArrayList<String>(Arrays.asList(
        "clear_day.jpg",
        "clear_night.jpg",
        "cloud_day.jpg",
        "cloud_night.jpg",
        "rain_day.jpg",
        "rain_night.jpg",
        "thunder_day.jpg",
        "thunder_night.jpg",
        "snow_day.jpg",
        "snow_night.jpg",
        "fog_day.jpeg",
        "fog_night.jpg"
    ));
    
    Boolean slided = false;
    Line lineTemp1;
    Line lineTemp2;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startApp();
    }
    
    private void startApp() {
        city.setText("Budapest");
        refresh.setText(String.valueOf(reload));
        getData("47.5000", "19.0833", "7c80f570e5255fa7e8ab6ee9b041e827");
        WeatherData weatherdata = new WeatherData(json); //create object from json
        setCurrentData(weatherdata); //set first page
        setForecastData(weatherdata);
    }
    
    private static void getData(String lat, String lon, String appid) { //get the weather data from openweathermap API
        BufferedReader reader;
        String line;
        StringBuilder responseContent = new StringBuilder();
        URL myurl;
        
        try {
            myurl = new URL("https://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon=" + lon + "&units=metric&lang=en&appid=" + appid);
            
            System.out.println(myurl);
            
            connection = (HttpURLConnection) myurl.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            
            int status = connection.getResponseCode();
            System.out.println("HTTP response code: " + status);
            
            if(status > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            }
         
        } catch(MalformedURLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        
        json = parse(responseContent.toString());
        
        /*java 11: java.net.http.HttpClient
        
        HttpClient client = HttpClient.newHttpClient();
        HttpsRequest request = HttpRequest.newBuilder(.uri(URI.create("https://...")).build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            .thenApply(HttpResponse::body)
            .thenApply(Main::parse)
            .thenAccept(System.out::println)
            .join();
        */
    }
    
    private void setCurrentData(WeatherData data) {
        
        loadBackground(bgPane, pickBg(data.getCurrent().getJSONArray("weather").getJSONObject(0).getString("icon")));
        //loadBackground(bgPane, 5);
        currentC.setText(Math.round(data.getCurrent().getDouble("temp")) + " " + deg + "C");
        currentW.setText(data.getCurrent().getJSONArray("weather").getJSONObject(0).getString("description"));
        currentW.setWrapText(true);
        loadIcon(currentIcon, pickIcon(data.getCurrent().getJSONArray("weather").getJSONObject(0).getString("icon")), 150);
        
        updated.setText(formatTimeFull(data.getCurrent().getInt("dt")));
        feels.setText(Math.round(data.getCurrent().getDouble("feels_like")) + " " + deg + "C");
        
        wind.setText(data.getCurrent().getDouble("wind_speed")+ " km/h");
        loadIcon(windDeg, 11, 25);
        windDeg.getChildren().get(0).setRotate(data.getCurrent().getInt("wind_deg")-225);
        
        int vDistance = data.getCurrent().getInt("visibility");
        if (vDistance < 1000) visibility.setText(vDistance + " m");
        else visibility.setText(vDistance/1000 + " km");
        
        pressure.setText(data.getCurrent().getInt("pressure") + " hPa");
        clouds.setText(data.getCurrent().getInt("clouds") + " %");
        humidity.setText(data.getCurrent().getInt("humidity") + " %");
        dewpoint.setText(Math.round(data.getCurrent().getDouble("dew_point")) + " " + deg + "C");
        uvindex.setText(String.valueOf(data.getCurrent().getDouble("uvi")));
        
        loadIcon(sunUp, 14, 30);
        loadIcon(sunDown, 15, 30);
        
        sunrise.setText(formatTimeFull(data.getCurrent().getInt("sunrise")));
        sunset.setText(formatTimeFull(data.getCurrent().getInt("sunset")));
        
        ArrayList<Long> temperatures = new ArrayList<>();
        for (int i = 1; i <= 24; i++) {
            temperatures.add(Math.round(data.getHourly().getJSONObject(i).getDouble("temp")));
        }
        int maxTemp = Collections.max(temperatures).intValue();
        int minTemp = Collections.min(temperatures).intValue(); //bring out
        
        for (int i = 0; i < 23; i++) {
            int actualTemp = (int) Math.round(data.getHourly().getJSONObject(i+1).getDouble("temp"));
            int previousTemp;
            Line line;
            if (i == 0) {
                previousTemp = actualTemp;
                line = new Line(i*100+10, 100/(maxTemp-minTemp)*(maxTemp-previousTemp), i*100+10, 100/(maxTemp-minTemp)*(maxTemp-actualTemp));
            } else if(i < 12) {
                previousTemp = (int) Math.round(data.getHourly().getJSONObject(i).getDouble("temp"));
                line = new Line((i-1)*100+10, 100/(maxTemp-minTemp)*(maxTemp-previousTemp), i*100+10, 100/(maxTemp-minTemp)*(maxTemp-actualTemp));
            } else {
                previousTemp = (int) Math.round(data.getHourly().getJSONObject(i).getDouble("temp"));
                line = new Line((i+1)*100-10, 100/(maxTemp-minTemp)*(maxTemp-previousTemp), (i+2)*100-10, 100/(maxTemp-minTemp)*(maxTemp-actualTemp));
            }
            
            line.setStroke(Color.WHITE);
            sliderLines.getChildren().add(line);
            
            if (i < 22) {
                String actualWeather = data.getHourly().getJSONObject(i+1).getJSONArray("weather").getJSONObject(0).getString("icon");

                Pane actualWeatherPane = new Pane();
                actualWeatherPane.setPrefSize(50, 50);
                if(i < 11) {
                    actualWeatherPane.setLayoutX(i*100+10);
                } else {
                    actualWeatherPane.setLayoutX((i+2)*100-10);
                }
                actualWeatherPane.setLayoutY(0);
                loadIcon(actualWeatherPane, pickIcon(actualWeather), 50);
                sliderIcons.getChildren().add(actualWeatherPane);


                Label actualTempLabel = new Label(String.valueOf(actualTemp) + " " + deg + "C");
                if(i < 11) {
                   actualTempLabel.setLayoutX(i*100+10);
                } else {
                   actualTempLabel.setLayoutX((i+2)*100-10);
                }
                actualTempLabel.setPrefSize(100, 50);
                actualTempLabel.setFont(Font.font(18));
                actualTempLabel.setLayoutY(line.getEndY() - 50);
                actualTempLabel.setTextFill(Color.WHITE);

                if(/*previousTemp != actualTemp || i == 0*/ true) {
                    sliderTemps.getChildren().add(actualTempLabel);
                }

                Label actualWindLabel = new Label(String.valueOf(Math.round(data.getHourly().getJSONObject(i+1).getDouble("wind_speed"))) + " km/h");
                if(i < 11) {
                    actualWindLabel.setLayoutX(i*100+10);
                } else {
                    actualWindLabel.setLayoutX((i+2)*100-10);
                }
                actualWindLabel.setPrefSize(100, 50);
                actualWindLabel.setFont(Font.font(18));
                actualWindLabel.setLayoutY(0);
                actualWindLabel.setTextFill(Color.WHITE);

                sliderWind.getChildren().add(actualWindLabel);


                Label actualTimeLabel = new Label(formatTimeShort(data.getHourly().getJSONObject(i+1).getInt("dt")));
                if( i < 11) {
                    actualTimeLabel.setLayoutX(i*100+10);
                } else {
                    actualTimeLabel.setLayoutX((i+2)*100-10);
                }
                actualTimeLabel.setPrefSize(100, 50);
                actualTimeLabel.setFont(Font.font(18));
                actualTimeLabel.setLayoutY(0);
                actualTimeLabel.setTextFill(Color.WHITE);

                sliderTimes.getChildren().add(actualTimeLabel);
            }
            
        }
    }
    
    private void setForecastData(WeatherData data) {
        ArrayList<Long> temperatures = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            temperatures.add(Math.round(data.getDaily().getJSONObject(i).getJSONObject("temp").getDouble("min")));
            temperatures.add(Math.round(data.getDaily().getJSONObject(i).getJSONObject("temp").getDouble("max")));
        }
        int maxTemp = Collections.max(temperatures).intValue();
        int minTemp = Collections.min(temperatures).intValue();
        
        for (int i = 1; i <= 7; i++) {
            Label dayLabel = new Label(formatTimeDay(data.getDaily().getJSONObject(i).getInt("dt")));
            dayLabel.setLayoutX((i-1)*183);
            dayLabel.setLayoutY(0);
            dayLabel.setTextFill(Color.WHITE);
            dayLabel.setStyle("-fx-font-size: 18px");
            dailyDays.getChildren().add(dayLabel);
            
            Pane iconPane = new Pane();
            iconPane.setPrefSize(100, 100);
            iconPane.setLayoutX((i-1)*183);
            iconPane.setLayoutY(0);
            loadIcon(iconPane, pickIcon(data.getDaily().getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon")), 100);
            dailyIcons.getChildren().add(iconPane);
            
            Label descLabel = new Label(data.getDaily().getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description"));
            descLabel.setLayoutX((i-1)*183);
            descLabel.setLayoutY(0);
            descLabel.setTextFill(Color.WHITE);
            descLabel.setStyle("-fx-font-size: 16px");
            dailyDesc.getChildren().add(descLabel);
            
            Pane windPane = new Pane();
            windPane.setLayoutX((i-1)*183);
            windPane.setLayoutY(0);
            windPane.setPrefSize(100, 50);
            
            Label windText = new Label(Math.round(data.getDaily().getJSONObject(i).getDouble("wind_speed")) + " km/h");
            windText.setStyle("-fx-font-size: 16px");
            windText.setTextFill(Color.WHITE);
            windText.setLayoutX(25);
            windText.setLayoutY(0);
            
            Pane windIcon = new Pane();
            windIcon.setPrefSize(10, 10);
            windIcon.setLayoutX(0);
            windIcon.setLayoutY(7);
            loadIcon(windIcon, 11, 10);
            windIcon.getChildren().get(0).setRotate(data.getDaily().getJSONObject(i).getInt("wind_deg"));
            
            windPane.getChildren().add(windText);
            windPane.getChildren().add(windIcon);
            
            dailyWind.getChildren().add(windPane);
            
            int actualMin = (int) Math.round(data.getDaily().getJSONObject(i).getJSONObject("temp").getDouble("min"));
            int actualMinHeight = 300/(maxTemp-minTemp) * (maxTemp-actualMin);
            Label minLabel = new Label(actualMin + " " + deg + "C");
            minLabel.setLayoutX((i-1)*183);
            minLabel.setLayoutY(actualMinHeight);
            minLabel.setTextFill(Color.WHITE);
            minLabel.setStyle("-fx-font-size: 16px");
            
            int actualMax = (int) Math.round(data.getDaily().getJSONObject(i).getJSONObject("temp").getDouble("max"));
            int actualMaxHeight = 300/(maxTemp-minTemp) * (maxTemp-actualMax);
            Label maxLabel = new Label(actualMax + " " + deg + "C");
            maxLabel.setLayoutX((i-1)*183);
            maxLabel.setLayoutY(actualMaxHeight);
            maxLabel.setTextFill(Color.WHITE);
            maxLabel.setStyle("-fx-font-size: 16px");
            
            dailyTemp.getChildren().add(minLabel);
            dailyTemp.getChildren().add(maxLabel);
            
            if(i<7) {
                
                int nextMin = (int) Math.round(data.getDaily().getJSONObject(i+1).getJSONObject("temp").getDouble("min"));
                int nextMinHeight = 300/(maxTemp-minTemp) * (maxTemp-nextMin);
                int nextMax = (int) Math.round(data.getDaily().getJSONObject(i+1).getJSONObject("temp").getDouble("max"));
                int nextMaxHeight = 300/(maxTemp-minTemp) * (maxTemp-nextMax);
            
                Line minLine = new Line((i-1)*183, actualMinHeight+50, (i)*183, nextMinHeight+50);
                minLine.setStroke(Color.WHITE);
                
                Line maxLine = new Line((i-1)*183, actualMaxHeight+50,(i)*183 , nextMaxHeight+50);
                maxLine.setStroke(Color.WHITE);
                
                dailyTemp.getChildren().add(minLine);
                dailyTemp.getChildren().add(maxLine);
            }
        }
    }
    
    private void loadIcon(Pane pane, int i, int size) { //load pictures in specific size
        String icon = icons.get(i);
        ImageView picture = new ImageView(new Image(getClass().getResourceAsStream("icons/" + icon)));
        picture.setFitHeight(size);
        picture.setFitWidth(size);
        pane.getChildren().add(picture);
    }
    
    private void loadBackground(Pane pane, int i) {
        String background = pictures.get(i);
        ImageView picture = new ImageView(new Image(getClass().getResourceAsStream("backgrounds/" + background)));
        picture.setFitWidth(1300);
        picture.setOpacity(1);
        pane.getChildren().add(picture);
    }
        
    private int pickIcon(String iconString) { //pick the corresponding icon for the API's icon code
        int iconNumber = 0;
        switch(iconString) {
            case "01d": iconNumber = 0; break;
            case "01n": iconNumber = 1; break;
            case "02d": iconNumber = 2; break;
            case "02n": iconNumber = 3; break;
            case "03d": iconNumber = 4; break;
            case "03n": iconNumber = 4; break;
            case "04d": iconNumber = 4; break;
            case "04n": iconNumber = 4; break;
            case "09d": iconNumber = 5; break;
            case "09n": iconNumber = 5; break;
            case "10d": iconNumber = 6; break;
            case "10n": iconNumber = 6; break;
            case "11d": iconNumber = 7; break;
            case "11n": iconNumber = 8; break;
            case "13d": iconNumber = 8; break;
            case "13n": iconNumber = 8; break;
            case "50d": iconNumber = 9; break;
            case "50n": iconNumber = 10; break;
            default: iconNumber = 100; break;
        }
        return iconNumber;
    }
    
    private int pickBg(String bgString) {
        int bgNumber;
        switch(bgString) {
            case "01d": bgNumber = 0; break;
            case "01n": bgNumber = 1; break;
            case "02d": bgNumber = 2; break;
            case "02n": bgNumber = 3; break;
            case "03d": bgNumber = 2; break;
            case "03n": bgNumber = 3; break;
            case "04d": bgNumber = 2; break;
            case "04n": bgNumber = 3; break;
            case "09d": bgNumber = 4; break;
            case "09n": bgNumber = 5; break;
            case "10d": bgNumber = 4; break;
            case "10n": bgNumber = 5; break;
            case "11d": bgNumber = 6; break;
            case "11n": bgNumber = 7; break;
            case "13d": bgNumber = 8; break;
            case "13n": bgNumber = 9; break;
            case "50d": bgNumber = 10; break;
            case "50n": bgNumber = 11; break;
            default: bgNumber = 0; break;
        }
        return bgNumber;
    }
    
    private static String formatTimeFull(int i) { //format time to "12:34"
        return new SimpleDateFormat("K:mm a").format(new Date((long) i * 1000)).toLowerCase();
    }
    
    private static String formatTimeDay(int i) { //format time to "12:34"
        return new SimpleDateFormat("EEEEEEEE, d/M").format(new Date((long) i * 1000));
    }
    
    private static String formatTimeShort(int i) {
        return new SimpleDateFormat("K a").format(new Date((long) i * 1000)).toLowerCase();
    }
    
    //String sunriseTime = new SimpleDateFormat("HH:mm").format(weatherData.getSunrise());
    
    private static JSONObject parse(String responseBody) {
        JSONObject myjson = new JSONObject(responseBody);
        return myjson;
    }
}
